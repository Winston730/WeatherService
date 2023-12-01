package com.example.weatherservice.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor

public class Forecast {

    @Column(name = "temp")
    private Double temp;
    @Column(name = "startDate")
    private String startDate;
    @Column(name = "endDate")
    private String endDate;
}