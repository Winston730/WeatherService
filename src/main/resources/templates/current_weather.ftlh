<!DOCTYPE html>
<html>
<head>
    <title> CURRENT WEATHER</title>
</head>
<body>
CURRENT WEATHER

<h3> Температура: ${weather.temp} °C </h3> <br />
<h3> Скорость ветра: ${weather.windSpeed} м/ч </h3> <br />
<h3> Давление: ${weather.pressure} гПа </h3> <br />
<h3> Влажность воздуха: ${weather.humidity} % </h3> <br />
<h3> Погодные условия: ${weather.condition} </h3> <br />
<h3> Локация: ${weather.city} ${weather.region} ${weather.location}   </h3> <br />


<h2> Рассчитать среднесуточную за период </h2>

<form action="/forecast" method="get">

    <h2><div class="mt-4">
        C:
        <input type="date" name="startDate" value="2023-12-01"
               max="2023-12-25" min="2023-12-01">
    </div>
    <div class="mt-4">
        До:
        <input type="date" name="endDate" value="2023-12-01"
               max="2023-12-25" min="2023-12-01">
    </div>
    <p>
        <button type="submit">Отправить</button>
    </p>
    </h2>
</form>

</body>
</html>