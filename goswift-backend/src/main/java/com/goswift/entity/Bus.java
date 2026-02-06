package com.goswift.entity;

import com.goswift.enums.BusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "buses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long busId;

    @ManyToOne
    @JoinColumn(name = "agency_id", nullable = false)
    private Agency agency;

    @Column(name = "registration_no", nullable = false, unique = true)
    private String registrationNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "bus_type", nullable = false)
    private BusType busType;

    @Column(nullable = false)
    private int capacity;
}