package com.fourty_eight_dps.marclay.core.firebase;

import android.content.Context;
import android.graphics.Bitmap;
import android.service.notification.StatusBarNotification;
import com.firebase.client.DataSnapshot;
import com.fourty_eight_dps.marclay.core.util.BitmapUtil;
import com.fourty_eight_dps.marclay.core.util.NotificationUtil;

import static com.fourty_eight_dps.marclay.core.util.StringUtil.nullToEmpty;

public class SyncedNotification implements Comparable<SyncedNotification> {

  private final String key;
  private final String message;
  private final String icon;

  public SyncedNotification(String key, String message, Bitmap icon) {
    this.key = key.replace('.', '_');
    this.message = message;
    this.icon = BitmapUtil.encodeToString(icon);
  }

  public String getKey() {
    return key;
  }

  public String getMessage() {
    return message;
  }

  public String getIcon() {
    return icon;
  }

  public static SyncedNotification create(Context context, StatusBarNotification sbn) {
    Bitmap image = NotificationUtil.getLargestImage(context, sbn);
    return new SyncedNotification(sbn.getKey(), nullToEmpty(sbn.getNotification().tickerText),
        image);
  }

  public static SyncedNotification create(DataSnapshot snapshot) {
    return new SyncedNotification(snapshot.getKey(), snapshot.getValue().toString(), null);
  }

  @Override public String toString() {
    return "SyncedNotification{" +
        "key='" + key + '\'' +
        ", message='" + message + '\'' +
        ", icon='" + icon + '\'' +
        '}';
  }

  @Override public int compareTo(SyncedNotification syncedNotification) {
    return message.compareTo(syncedNotification.getMessage());
  }
}
