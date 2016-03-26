package com.fourty_eight_dps.marclay;

import android.app.Application;
import com.firebase.client.Firebase;

public class MarclayApplication extends Application {
  @Override public void onCreate() {
    super.onCreate();
    Firebase.setAndroidContext(this);
  }
}
