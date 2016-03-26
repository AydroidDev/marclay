package com.fourty_eight_dps.marclay.service;

import android.app.Notification;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NotificationListenerService
    extends android.service.notification.NotificationListenerService {
  @Override public void onNotificationPosted(StatusBarNotification sbn) {
    super.onNotificationPosted(sbn);
    Notification notification = sbn.getNotification();
    Log.d("NOTI", "Title: " + notification.tickerText);
  }
}
