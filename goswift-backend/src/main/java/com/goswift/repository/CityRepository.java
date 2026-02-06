package com.goswift.repository;

import com.goswift.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {
    // Used to resolve City Name -> City Entity
    Optional<City> findByCityName(String cityName);
}