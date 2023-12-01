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

    private final WeatherApiService api = new WeatherApiService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CurrentWeather getCurrentWeather() throws IOException {
        String jsonString = api.getResponse();
        JsonNode weatherNode = objectMapper.readTree(jsonString.getBytes());
        CurrentWeather weather = new CurrentWeather();

        if (weatherNode.get("now") != null) {
            populateCurrentWeather(weather, weatherNode);
        } else {
            System.out.println("Null");
        }

        return weather;
    }

    private void populateCurrentWeather(CurrentWeather weather, JsonNode weatherNode) {
        JsonNode factNode = weatherNode.get("fact");
        weather.setTemp(factNode.get("temp").toString());
        weather.setPressure(factNode.get("pressure_pa").toString());
        weather.setHumidity(factNode.get("humidity").toString());
        weather.setCondition(factNode.get("condition").toString());
        weather.setWindSpeed(Double.parseDouble(factNode.get("wind_speed").toString()) * 3600);

        JsonNode geoObjectNode = weatherNode.get("geo_object");
        weather.setRegion(geoObjectNode.get("province").get("name").toString());

        JsonNode infoNode = weatherNode.get("info");
        weather.setCity(infoNode.get("tzinfo").get("name").toString());
    }

    public Forecast getForecast(String startDate, String endDate) throws IOException, ParseException {
        String json = api.getResponse();
        JsonNode rootNode = objectMapper.readTree(json);

        Forecast forecast = new Forecast();
        ArrayList<Double> temperatures = extractTemperatures(rootNode, startDate, endDate);

        forecast.setStartDate(startDate);
        forecast.setTemp(average(temperatures));
        forecast.setEndDate(endDate);

        return forecast;
    }

    private ArrayList<Double> extractTemperatures(JsonNode rootNode, String startDate, String endDate)
            throws ParseException {
        JsonNode forecastsNode = rootNode.get("forecasts");
        ArrayList<Double> temperatures = new ArrayList<>();

        if (forecastsNode.isArray()) {
            for (JsonNode forecasts : forecastsNode) {
                processForecastNode(startDate, endDate, temperatures, forecasts);
            }
        }
        return temperatures;
    }

    private void processForecastNode(String startDate, String endDate, ArrayList<Double> temperatures, JsonNode forecasts)
            throws ParseException {
        JsonNode dateNode = forecasts.get("date");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date docDate = format.parse(dateNode.asText());
        JsonNode tempNode = forecasts.get("parts").get("day").get("temp_avg");

        if (!docDate.before(format.parse(startDate)) && !docDate.after(format.parse(endDate))) {
            String tempValue = tempNode.asText();
            temperatures.add(Double.parseDouble(tempValue));
        }
    }

    public void saveCurrentWeather(CurrentWeather currentWeather) {
        weatherRepository.save(currentWeather);
    }

    @Value("${weather.api.interval}")
    @Scheduled(fixedRateString = "${weather.api.interval}")
    public void fetchWeatherPeriodically() {
        try {
            saveCurrentWeather(getCurrentWeather());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Double average(ArrayList<Double> list) {
        return list.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    private static String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }

    public String getCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        System.out.println("Текущая дата: " + formatDate(currentDate));
        return formatDate(currentDate);
    }

    public String getInSevenDaysDate() {
        LocalDate currentDate = LocalDate.now();
        LocalDate dateInSevenDays = currentDate.plusDays(7);
        System.out.println("Дата через 7 дней: " + formatDate(dateInSevenDays));

        return formatDate(dateInSevenDays);
    }
}