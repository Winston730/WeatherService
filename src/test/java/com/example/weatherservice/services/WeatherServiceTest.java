package com.example.weatherservice.services;

import com.example.weatherservice.models.Forecast;
import com.example.weatherservice.repositories.WeatherRepository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


class WeatherServiceTest {

    @Mock
    private WeatherRepository weatherRepository;

    @Mock
    private WeatherApiService weatherApiService;

    @InjectMocks
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetForecast() throws IOException, ParseException {
        // Arrange
        String jsonResponse = "{ \"forecasts\": [ { \"date\": \"2023-12-01\", \"parts\": { \"day\": { \"temp_avg\": 20.0 } } }, { \"date\": \"2023-12-02\", \"parts\": { \"day\": { \"temp_avg\": 25.0 } } }, { \"date\": \"2023-12-03\", \"parts\": { \"day\": { \"temp_avg\": 18.0 } } } ] }";
        JsonNode jsonNode = new ObjectMapper().readTree(jsonResponse);
        when(weatherApiService.getResponse()).thenReturn(jsonResponse);

        // Act
        Forecast forecast = weatherService.getForecast("2023-12-01", "2023-12-03");
        ArrayList<Double> temperatureList = new ArrayList<>(Arrays.asList(20.0, 25.0, 18.0));




        // Assert
        assertEquals("2023-12-01", forecast.getStartDate());
        assertEquals((20.0 + 25.0 + 18.0) / 3, weatherService.average(temperatureList));
        assertEquals("2023-12-03", forecast.getEndDate());
    }

}