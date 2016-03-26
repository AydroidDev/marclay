package com.fourty_eight_dps.marclay;

import android.app.Activity;
import android.os.Bundle;
import com.fourty_eight_dps.marclay.core.firebase.RemoteNotificationManager;

public class MainActivity extends Activity {

  RemoteNotificationManager remoteNotificationManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    remoteNotificationManager = new RemoteNotificationManager();
    setContentView(R.layout.activity_main);
  }
}
