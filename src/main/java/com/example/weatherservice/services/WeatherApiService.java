package com.example.weatherservice.services;

import org.springframework.web.reactive.function.client.WebClient;

public class WeatherApiService {
    public String getResponse() {
        WebClient webClient = WebClient.create();
        String apiUrl = "https://api.weather.yandex.ru/v2/forecast?lat=53.53590&lon=27.34000&extra=true&lang=ru_RU";


        WebClient.RequestHeadersSpec<?> requestSpec = webClient.get()
                .uri(apiUrl)
                .header("X-Yandex-API-Key", "40ad899a-0141-430d-8e9d-57d6fb88ea3e");


        String response = requestSpec.retrieve()
                .bodyToMono(String.class)
                .block();

        //  System.out.println("Ответ: " + response);
        return response;
    }

}