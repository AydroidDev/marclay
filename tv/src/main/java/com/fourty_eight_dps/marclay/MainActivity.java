package com.fourty_eight_dps.marclay;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.fourty_eight_dps.marclay.core.firebase.RemoteNotificationManager;
import com.fourty_eight_dps.marclay.core.firebase.SyncedNotification;

public class MainActivity extends Activity
    implements RemoteNotificationManager.NotificationListener {

  RemoteNotificationManager remoteNotificationManager;
  TextView textView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    remoteNotificationManager = new RemoteNotificationManager();
    setContentView(R.layout.activity_main);
    textView = (TextView) findViewById(R.id.notification);
  }

  @Override protected void onStart() {
    super.onStart();
    remoteNotificationManager.registerNotificationListener(this);
  }

  @Override protected void onStop() {
    super.onStop();
    remoteNotificationManager.unregisterNotificationListener();
  }

  @Override public void onNotificationPosted(SyncedNotification syncedNotification) {
    textView.setText("Added: " + syncedNotification.toString());
  }

  @Override public void onNotificationRemoved(SyncedNotification syncedNotification) {
    textView.setText("Removed: " + syncedNotification.toString());
  }
}
