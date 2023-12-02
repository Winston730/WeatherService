<!DOCTYPE html>
<html lang="ru" xmlns:th="http://www.thymeleaf.org" >
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Текущая погода</title>
    <style type="text/css">
    <#include "css/styles.css">
    </style>

</head>
<body>
<h2>Текущая погода</h2>

<h3>Температура: ${weather.temp} °C</h3> <br />
<h3>Скорость ветра: ${weather.windSpeed} м/ч</h3> <br />
<h3>Давление: ${weather.pressure} гПа</h3> <br />
<h3>Влажность воздуха: ${weather.humidity} %</h3> <br />
<h3>Погодные условия: ${weather.condition}</h3> <br />
<h3>Локация: ${weather.city} ${weather.region} </h3> <br />

<h2>Рассчитать среднесуточную за период</h2>

<form action="/forecast" method="get">
    <div class="mt-4">
        C:
        <input type="date" name="startDate" value="${currentDate}"
               max="${inSevenDaysDate}" min="${currentDate}">
    </div>
    <div class="mt-4">
        До:
        <input type="date" name="endDate" value="${currentDate}"
               max="${inSevenDaysDate}" min="${currentDate}">
    </div>
    <p>
        <button type="submit">Отправить</button>
    </p>
</form>
</body>
</html>