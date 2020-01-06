package com.egorovsoft.mysun.services.connections;

import android.util.Log;

import com.egorovsoft.mysun.MainPresenter;
import com.egorovsoft.mysun.services.api.WeatherRequest;

import java.io.IOException;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConnectionToWheatherServer {
    private static final String TAG = "ConnectionServer";

    private static final String API_KEY = "37a0b47b7c853559bc683f29de620736";
    private static final String SERVER_URL = "https://api.openweathermap.org/data/2.5/weather?";
    private static final String SERVER_URI = "https://api.openweathermap.org/";

    private String city_name;
    private String country_code;

    private float temperature;
    private int pressure;
    private int humidity;
    private float windSpeed;
    private HttpsURLConnection urlConnection;
    private int error_message;

    private Retrofit retrofit;
    private OpenWheatherMapFactory weatherRequestCall;

    public ConnectionToWheatherServer(){
        Log.d(TAG, "ConnectionToWheatherServer: ");
        
        temperature = 0;
        pressure = 0;
        humidity = 0;
        windSpeed = 0;
        error_message = 200;

        retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_URI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        weatherRequestCall = retrofit.create(OpenWheatherMapFactory.class);
    }

    public void refreshDataRetrofitLocation(double latitude, double longitude){

        //api.openweathermap.org/data/2.5/weather?lat=35&lon=139
        Log.d(TAG, "refreshDataRetrofitLocation: ");

        Call<WeatherRequest> call = weatherRequestCall.refreshDataRetrofitLocation(Double.toString(latitude), Double.toString(longitude), "metric", API_KEY);
        try {
            Response<WeatherRequest> weatherRequest = call.execute();
            int error = weatherRequest.code();

            if (weatherRequest.isSuccessful()) {
                displayWeather(weatherRequest.body(), error);
            }
            else{
                SetError(error);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshDataRetrofit(String city, String country){
        Log.d(TAG, "refreshDataRetrofit: ");

        Call<WeatherRequest> call = weatherRequestCall.refreshDataRetrofit(city + "," + country, "metric", API_KEY);
        try {
            Response<WeatherRequest> weatherRequest = call.execute();
            int error = weatherRequest.code();

            if (weatherRequest.isSuccessful()) {
                displayWeather(weatherRequest.body(), error);
            }
            else{
                SetError(error);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void SetError(int error_code) {
        Log.d(TAG, "SetError: " + error_code);

        temperature = 0;
        pressure = 0;
        humidity = 0;
        windSpeed = 0;
        error_message = error_code;

        updateActivity();
    }

    private void displayWeather(WeatherRequest weatherRequest, int error_code) {
        Log.d(TAG, "displayWeather: ");

        country_code = weatherRequest.getSys().getCountry();
        city_name = weatherRequest.getName();
        temperature = weatherRequest.getMain().getTemp();
        pressure = weatherRequest.getMain().getPressure();
        humidity = weatherRequest.getMain().getHumidity();
        windSpeed = weatherRequest.getWind().getSpeed();
        error_message = error_code;

        updateActivity();
    }

    private void updateActivity(){
        Log.d(TAG, "updateActivity: ");

        MainPresenter.getInstance().setCurrentCountry(country_code);
        MainPresenter.getInstance().setCurrentCity(city_name);
        MainPresenter.getInstance().setTemperature(temperature);
        MainPresenter.getInstance().setHumidity(humidity);
        MainPresenter.getInstance().setWind(windSpeed);
        MainPresenter.getInstance().setError(error_message);
    }

    public void close() {
        Log.d(TAG, "close: ");

        if (urlConnection != null){
            urlConnection.disconnect();
        }
    }
}
