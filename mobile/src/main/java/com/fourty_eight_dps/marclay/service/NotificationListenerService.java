package com.fourty_eight_dps.marclay.service;

import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NotificationListenerService
    extends android.service.notification.NotificationListenerService {
  @Override public void onNotificationPosted(StatusBarNotification sbn) {
    super.onNotificationPosted(sbn);
    Log.d("NOTI", sbn.getPackageName());
  }
}
