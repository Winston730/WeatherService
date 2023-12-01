package com.example.weatherservice.controllers;

import com.example.weatherservice.models.CurrentWeather;
import com.example.weatherservice.models.Forecast;
import com.example.weatherservice.services.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.text.ParseException;
import java.util.NoSuchElementException;

@Controller
public class WeatherController {
    @Autowired
    private WeatherService weatherService;

    @GetMapping("/")
    public String getCurrentWeatherPage(Model model) {
        try {
            CurrentWeather weather = weatherService.getCurrentWeather();
            model.addAttribute("currnetDate",weatherService.getCurrentDate());
            model.addAttribute("inSevenDaysDate",weatherService.getInSevenDaysDate());
            model.addAttribute("weather", weather);
            weatherService.saveCurrentWeather(weather);
            return "current_weather";
        } catch (NoSuchElementException e) {
            return "error";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/forecast")
    public String getForecastPage(@RequestParam(name = "startDate") String startDate,
                                  @RequestParam(name = "endDate") String endDate,
                                  Model model) throws IOException, ParseException {

        Forecast forecast = weatherService.getForecast(startDate,endDate);
        model.addAttribute("forecast", forecast);
        return "forecast";
    }


}
