package com.example.weatherservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="currentweather")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class CurrentWeather {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "temp")
    private String temp;
    @Column(name = "windSpeed")
    private double windSpeed;
    @JsonSetter("pressure_pa")
    @Column(name = "pressure")
    private String pressure;
    @JsonSetter("humidity")
    @Column(name = "humidity")
    private String humidity;
    @JsonSetter("condition")
    @Column(name = "cond")
    private String condition;

    @Column(name = "region")
    private String region;
    @Column(name = "city")
    private String city;

}