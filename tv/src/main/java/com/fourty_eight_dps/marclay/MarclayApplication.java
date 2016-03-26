package com.fourty_eight_dps.marclay;

import android.app.Application;
import com.fourty_eight_dps.marclay.core.firebase.RemoteNotificationManager;

public class MarclayApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();
    RemoteNotificationManager.setAndroidContext(this);
  }
}
