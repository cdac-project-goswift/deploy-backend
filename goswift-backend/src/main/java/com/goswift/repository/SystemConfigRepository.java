package com.goswift.repository;

import com.goswift.entity.SystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SystemConfigRepository extends JpaRepository<SystemConfig, Integer> {
    // Used for tax calculation. ID is always 1.
    @Query("SELECT s FROM SystemConfig s WHERE s.configId = 1")
    SystemConfig getGlobalConfig();
}