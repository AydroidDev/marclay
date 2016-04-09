package com.fourty_eight_dps.marclay.weather;

import com.fourty_eight_dps.marclay.weather.model.Response;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherService {

  @GET("weather?units=imperial") Call<Response.Weather> weather(@Query("zip") String zip,
      @Query("APPID") String apiKey);
}
