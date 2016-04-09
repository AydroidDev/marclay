package com.fourty_eight_dps.marclay.weather.model;

import com.google.gson.annotations.SerializedName;

public class Response {

  public static class Weather {
    Info main;
  }

  public static class Info {
    String temp;

    @SerializedName("temp_min")
    String minTemp;

    @SerializedName("temp_max")
    String maxTemp;
  }
}
