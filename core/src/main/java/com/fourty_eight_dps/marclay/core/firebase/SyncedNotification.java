package com.fourty_eight_dps.marclay.core.firebase;

import android.service.notification.StatusBarNotification;
import com.firebase.client.DataSnapshot;

public class SyncedNotification implements Comparable<SyncedNotification> {

  private final String key;
  private final String message;

  public SyncedNotification(String key, String message) {
    this.key = key.replace('.', '_');
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

  public static SyncedNotification create(DataSnapshot snapshot) {
    return new SyncedNotification(snapshot.getKey(), snapshot.getValue().toString());
  }

  @Override public String toString() {
    return "SyncedNotification{" +
        "key='" + key + '\'' +
        ", message='" + message + '\'' +
        '}';
  }

  @Override public int compareTo(SyncedNotification syncedNotification) {
    return message.compareTo(syncedNotification.getMessage());
  }
}
