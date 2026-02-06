package com.goswift.repository;

import com.goswift.entity.User;
import com.goswift.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);

    // Fixed: Use this method in AdminService instead of countByRole_RoleName
    long countByRole(UserRole role);
}