package com.example.weatherservice.controllers;

import com.example.weatherservice.models.CurrentWeather;
import com.example.weatherservice.models.Forecast;
import com.example.weatherservice.services.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.io.IOException;
import java.text.ParseException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WeatherControllerTest {

    @InjectMocks
    private WeatherController weatherController;
    @Mock
    private WeatherService weatherService;
    @Mock
    private Model model;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCurrentWeatherPage() throws IOException {

        CurrentWeather currentWeather = new CurrentWeather();
        when(weatherService.getCurrentWeather()).thenReturn(currentWeather);

        String viewName = weatherController.getCurrentWeatherPage(model);

        assertEquals("current_weather", viewName);
        verify(model).addAttribute(eq("currnetDate"), any());
        verify(model).addAttribute(eq("inSevenDaysDate"), any());
        verify(model).addAttribute("weather", currentWeather);
        verify(weatherService).saveCurrentWeather(currentWeather);

    }

    @Test
    void testGetCurrentWeatherPageWithException() throws IOException {

        when(weatherService.getCurrentWeather()).thenThrow(NoSuchElementException.class);

        String viewName = weatherController.getCurrentWeatherPage(model);

        assertEquals("error", viewName);
    }

    @Test
    void testGetCurrentWeatherPageWithIOException() throws IOException {

        when(weatherService.getCurrentWeather()).thenThrow(IOException.class);

        assertThrows(RuntimeException.class, () -> weatherController.getCurrentWeatherPage(model));
    }

    @Test
    void testGetForecastPage() throws IOException, ParseException {

        String startDate = "2023-01-01";
        String endDate = "2023-01-07";
        Forecast forecast = new Forecast();
        when(weatherService.getForecast(startDate, endDate)).thenReturn(forecast);

        String viewName = weatherController.getForecastPage(startDate, endDate, model);

        assertEquals("forecast", viewName);
        verify(model).addAttribute("forecast", forecast);
    }
}