package com.goswift.repository;

import com.goswift.entity.Agency;
import com.goswift.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AgencyRepository extends JpaRepository<Agency, Long> {
    // Used to link a logged-in User to their Agency details
    Optional<Agency> findByUser(User user);
}