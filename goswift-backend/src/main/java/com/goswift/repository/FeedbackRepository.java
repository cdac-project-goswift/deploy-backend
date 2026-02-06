package com.goswift.repository;

import com.goswift.entity.Booking;
import com.goswift.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    boolean existsByBooking(Booking booking);
}