package com.fourty_eight_dps.marclay.core.firebase;

public class Video {

  String url;
  String location;
  int hour;

  public Video() {}

  public Video(int hour) {
    this.hour = hour;
  }

  public String getUrl() {
    return url;
  }

  public String getLocation() {
    return location;
  }

  public int getHour() {
    return hour;
  }
}
