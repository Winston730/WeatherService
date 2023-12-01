package com.example.weatherservice.services;

import com.example.weatherservice.models.CurrentWeather;
import com.example.weatherservice.models.Forecast;
import com.example.weatherservice.repositories.WeatherRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

@Service
public class WeatherService {
    @Autowired
    private WeatherRepository weatherRepository;



    public CurrentWeather getCurrentWeather() throws IOException {


        WeatherApiService api = new WeatherApiService();
        String jsonString = api.getResponse();

        JsonNode weatherNode = new ObjectMapper().readTree(jsonString.getBytes());
        CurrentWeather weather = new CurrentWeather();

        if(weatherNode.get("now")!=null)
        {
            weather.setTemp(weatherNode.get("fact").get("temp").toString());
            weather.setPressure(weatherNode.get("fact").get("pressure_pa").toString());
            weather.setHumidity(weatherNode.get("fact").get("humidity").toString());
            weather.setCondition(weatherNode.get("fact").get("condition").toString());
            weather.setWindSpeed(Double.parseDouble(weatherNode.get("fact").get("wind_speed").toString())*3600);

            weather.setRegion(weatherNode.get("geo_object").get("province").get("name").toString());
            weather.setCity(weatherNode.get("info").get("tzinfo").get("name").toString());
        }
        else    System.out.println("Null");

        return weather;
    }
    public Forecast getForecast(String startDate, String endDate) throws IOException, ParseException {

        WeatherApiService api = new WeatherApiService();
        String json = api.getResponse();


        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(json);

        Forecast forecast = new Forecast();

        JsonNode forecastsNode = rootNode.get("forecasts");
        ArrayList<Double> temperatures = new ArrayList<>();

        if (forecastsNode.isArray()) {
            for (JsonNode forecasts : forecastsNode) {
                JsonNode dateNode = forecasts.get("date");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date docDate = format.parse(dateNode.asText());
                JsonNode tempNode = forecasts.get("parts").get("day").get("temp_avg");
                //Проверка на нахождение в заданном диапазоне
                if (!docDate.before(format.parse(startDate)) && !docDate.after(format.parse(endDate))) {
                    String tempValue = tempNode.asText();
                    temperatures.add(Double.parseDouble(tempValue));
                }
            }
        }
        forecast.setStartDate(startDate);
        forecast.setTemp(average(temperatures));
        forecast.setEndDate(endDate);

        return forecast;
    }

    public void saveCurrentWeather(CurrentWeather currentWeather)
    {
        weatherRepository.save(currentWeather);
    }
    @Value("${weather.api.interval}")
    @Scheduled(fixedRateString = "${weather.api.interval}") // Запрос (в миллисекундах)
    public void fetchWeatherPeriodically() {
        try {
           saveCurrentWeather(getCurrentWeather());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Double average (ArrayList<Double> list)
    {
        double sum = 0;
        for(int i=0;i<list.size();i++) {
            sum+=list.get(i);
        }
        return sum/list.size();
    }
    private static String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }
    public String getCurrentDate ()
    {
        LocalDate currentDate = LocalDate.now();
        System.out.println("Текущая дата: " + formatDate(currentDate));
        return formatDate(currentDate);
    }
    public String getInSevenDaysDate ()
    {
        LocalDate currentDate = LocalDate.now();
        LocalDate dateInSevenDays = currentDate.plusDays(7);
        System.out.println("Дата через 7 дней: " + formatDate(dateInSevenDays));

        return formatDate(dateInSevenDays);
    }
    }


