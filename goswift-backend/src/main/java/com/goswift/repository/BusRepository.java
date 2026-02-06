package com.goswift.repository;

import com.goswift.entity.Bus;
import com.goswift.entity.Agency;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BusRepository extends JpaRepository<Bus, Long> {
    List<Bus> findByAgency(Agency agency);
    List<Bus> findByAgency_AgencyId(Long agencyId);
}