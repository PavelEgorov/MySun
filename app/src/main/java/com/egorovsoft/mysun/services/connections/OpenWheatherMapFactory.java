package com.egorovsoft.mysun.services.connections;

import com.egorovsoft.mysun.services.api.WeatherRequest;
import com.egorovsoft.mysun.services.api.WeatherRequestFiveDay;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

//    q={название города}, {код страны}
//    Пример итогового запроса: api.openweathermap.org/data/2.5/weather?q=London,uk&units=metric&appid=37a0b47b7c853559bc683f29de620736
//    api.openweathermap.org/data/2.5/forecast для погоды на 5 дней

interface OpenWheatherMapFactory {
    @GET("data/2.5/weather")
    Call<WeatherRequest> refreshDataRetrofit(@Query("q") String q, @Query("units") String units, @Query("appid") String appid);

    @GET("data/2.5/weather")
    Call<WeatherRequest> refreshDataRetrofitLocation(@Query("lat") String lat, @Query("lon") String lon, @Query("units") String units, @Query("appid") String appid);

    @GET("data/2.5/forecast")
    Call<WeatherRequestFiveDay> refreshDataRetrofitForecast(@Query("q") String q, @Query("units") String units, @Query("appid") String appid);

    @GET("data/2.5/forecast")
    Call<WeatherRequestFiveDay> refreshDataRetrofitLocationForecast(@Query("lat") String lat, @Query("lon") String lon, @Query("units") String units, @Query("appid") String appid);
}
