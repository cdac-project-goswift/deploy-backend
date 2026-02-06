package com.goswift.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Table(name = "schedules")
@Data
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    @ManyToOne
    @JoinColumn(name = "bus_id", nullable = false)
    private Bus bus;

    @ManyToOne
    @JoinColumn(name = "source_city_id", nullable = false)
    private City sourceCity;

    @ManyToOne
    @JoinColumn(name = "dest_city_id", nullable = false)
    private City destCity;

    @Column(nullable = false)
    private LocalTime departureTime;

    @Column(nullable = false)
    private LocalTime arrivalTime;

    @Column(nullable = false)
    private BigDecimal baseFare;
}