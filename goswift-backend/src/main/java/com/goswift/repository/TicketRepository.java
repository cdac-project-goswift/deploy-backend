package com.goswift.repository;

import com.goswift.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    // Used in CustomerService (BookingForm.jsx) to gray out occupied seats
    @Query("SELECT t.seatNo FROM Ticket t JOIN t.booking b WHERE b.schedule.scheduleId = :scheduleId AND b.journeyDate = :date AND b.status = 'Confirmed'")
    List<String> findOccupiedSeats(@Param("scheduleId") Long scheduleId, @Param("date") LocalDate date);
}