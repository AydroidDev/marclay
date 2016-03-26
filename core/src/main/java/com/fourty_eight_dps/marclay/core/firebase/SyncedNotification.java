package com.fourty_eight_dps.marclay.core.firebase;

import android.service.notification.StatusBarNotification;

public class SyncedNotification {

  private final String key;
  private final String message;

  public SyncedNotification(String key, String message) {
    this.key = key.replace('.','_');
    this.message = message;
  }

  public String getKey() {
    return key;
  }

  public String getMessage() {
    return message;
  }

  public static SyncedNotification create(StatusBarNotification sbn) {
    return new SyncedNotification(sbn.getKey(), sbn.getNotification().tickerText.toString());
  }
}
