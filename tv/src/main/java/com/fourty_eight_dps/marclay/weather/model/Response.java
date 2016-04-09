package com.fourty_eight_dps.marclay.weather.model;

import com.google.gson.annotations.SerializedName;

public class Response {

  public static class Weather {
    public Info main;
  }

  public static class Info {
    public float temp;

    @SerializedName("temp_min")
    public String minTemp;

    @SerializedName("temp_max")
    public String maxTemp;
  }
}
