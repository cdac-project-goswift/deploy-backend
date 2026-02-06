package com.goswift.repository;

import com.goswift.entity.Schedule;
import com.goswift.entity.Bus;
import com.goswift.entity.Agency;
import com.goswift.enums.BusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    
    @Query("SELECT COUNT(s) > 0 FROM Schedule s WHERE s.bus = :bus AND " +
           "((s.departureTime < :arrival AND s.arrivalTime > :depart))")
    boolean existsOverlappingSchedule(@Param("bus") Bus bus, 
                                      @Param("depart") LocalTime depart, 
                                      @Param("arrival") LocalTime arrival);

    @Query("SELECT s FROM Schedule s " +
           "JOIN s.bus b " +
           "JOIN b.agency a " +
           "JOIN a.user u " +
           "WHERE s.sourceCity.cityName = :source " +
           "AND s.destCity.cityName = :dest " +
           "AND u.status = 'ACTIVE' " + 
           "AND (:busType IS NULL OR b.busType = :busType)")
    List<Schedule> searchSchedules(@Param("source") String source, 
                                   @Param("dest") String dest,
                                   @Param("busType") BusType busType);
    
    // Fixed: Declared missing method
    List<Schedule> findByBus_Agency(Agency agency);
}