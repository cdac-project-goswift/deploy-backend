package com.goswift.repository;

import com.goswift.entity.Booking;
import com.goswift.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);
    
    // Fixed: countConfirmedSeats signature matches usage
    @Query("SELECT COUNT(t) FROM Ticket t JOIN t.booking b WHERE b.schedule.scheduleId = :scheduleId AND b.journeyDate = :date AND b.status = 'Confirmed'")
    Integer countConfirmedSeats(@Param("scheduleId") Long scheduleId, @Param("date") LocalDate date);

    // Fixed: findByAgencyId uses Long
    @Query("SELECT b FROM Booking b WHERE b.schedule.bus.agency.agencyId = :agencyId")
    List<Booking> findByAgencyId(@Param("agencyId") Long agencyId);
    
    // Fixed: Added method for Admin Search
    List<Booking> findBySchedule_Bus_BusId(Long busId);

    @Query("SELECT COALESCE(SUM(b.totalFare), 0) FROM Booking b WHERE b.status = 'Confirmed'")
    BigDecimal getTotalRevenue();
}