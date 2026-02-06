package com.goswift.service;

import com.goswift.dto.SignupRequest;
import com.goswift.dto.UserDTO;
import com.goswift.entity.Agency;
import com.goswift.entity.User;
import com.goswift.enums.UserRole;
import com.goswift.enums.UserStatus;
import com.goswift.repository.AgencyRepository;
import com.goswift.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final AgencyRepository agencyRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }

    @Transactional
    public UserDTO registerUser(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = mapper.map(request, User.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus(UserStatus.ACTIVE);
        
        if (user.getRole() == UserRole.ROLE_ADMIN) {
             throw new RuntimeException("Cannot register as Admin");
        }

        User savedUser = userRepository.save(user);

        if (user.getRole() == UserRole.ROLE_AGENT) {
            agencyRepository.save(Agency.builder()
                    .user(savedUser)
                    .agencyName(request.getAgencyName())
                    .build());
        }
        return mapper.map(savedUser, UserDTO.class);
    }

    public UserDTO login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (user.getStatus() == UserStatus.BLOCKED) {
            throw new RuntimeException("Account is Blocked");
        }
        return mapper.map(user, UserDTO.class);
    }

    public UserDTO updateProfile(Long userId, UserDTO dto) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhoneNumber(dto.getPhoneNumber());
        return mapper.map(userRepository.save(user), UserDTO.class);
    }

    public UserDTO getUserProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return mapper.map(user, UserDTO.class);
    }
}