package com.example.weatherservice.repositories;

import com.example.weatherservice.models.CurrentWeather;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRepository extends JpaRepository<CurrentWeather, Long> {


}
