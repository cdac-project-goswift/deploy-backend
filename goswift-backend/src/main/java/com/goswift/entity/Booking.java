package com.goswift.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bookings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @Column(nullable = false, updatable = false)
    private LocalDateTime bookingDate;

    @PrePersist
    protected void onCreate() {
        bookingDate = LocalDateTime.now();
    }

    @Column(nullable = false)
    private LocalDate journeyDate;

    @Column(nullable = false)
    private BigDecimal totalFare;

    @Column(nullable = false)
    private String status;

    @Column(unique = true)
    private String bookingRefNo;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    private List<Ticket> tickets = new ArrayList<>();
}