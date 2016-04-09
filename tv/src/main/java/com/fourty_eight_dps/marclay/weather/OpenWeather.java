package com.fourty_eight_dps.marclay.weather;

import com.fourty_eight_dps.marclay.BuildConfig;
import com.fourty_eight_dps.marclay.weather.model.Response;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenWeather {
  public static String ENDPOINT = "http://api.openweathermap.org/data/2.5/";

  public static void getWeather(Callback<Response.Weather> callback) {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(ENDPOINT)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    OpenWeatherService openWeatherService = retrofit.create(OpenWeatherService.class);
    openWeatherService.weather("94040,us", BuildConfig.OPEN_WEATHER_API_KEY).enqueue(callback);
  }
}
