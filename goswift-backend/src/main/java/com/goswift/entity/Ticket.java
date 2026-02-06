package com.goswift.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tickets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Integer ticketId;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    @JsonBackReference
    private Booking booking;

    @Column(name = "passenger_name", nullable = false)
    private String passengerName;

    @Column(name = "passenger_age")
    private Integer passengerAge;

    @Column(name = "passenger_gender")
    private String passengerGender;

    @Column(name = "seat_no")
    private String seatNo;

    @Column(name = "ticket_no", unique = true)
    private String ticketNo;
}