package com.fourty_eight_dps.marclay.service;

import android.service.notification.StatusBarNotification;
import com.fourty_eight_dps.marclay.firebase.RemoteNotificationManager;
import com.fourty_eight_dps.marclay.firebase.SyncedNotification;

public class NotificationListenerService
    extends android.service.notification.NotificationListenerService {

  RemoteNotificationManager remoteNotificationManager;

  @Override public void onCreate() {
    super.onCreate();
    remoteNotificationManager = new RemoteNotificationManager();
  }

  @Override public void onNotificationPosted(StatusBarNotification sbn) {
    super.onNotificationPosted(sbn);
    SyncedNotification syncedNotification = SyncedNotification.create(sbn);
    remoteNotificationManager.postNotification(syncedNotification);
  }

  @Override public void onNotificationRemoved(StatusBarNotification sbn) {
    super.onNotificationRemoved(sbn);
    SyncedNotification syncedNotification = SyncedNotification.create(sbn);
    remoteNotificationManager.removeNotification(syncedNotification);
  }
}
