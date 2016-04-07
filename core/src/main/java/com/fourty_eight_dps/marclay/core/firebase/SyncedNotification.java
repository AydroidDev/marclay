package com.fourty_eight_dps.marclay.core.firebase;

import android.content.Context;
import android.graphics.Bitmap;
import android.service.notification.StatusBarNotification;
import com.firebase.client.DataSnapshot;

import static com.fourty_eight_dps.marclay.core.util.BitmapUtil.encodeToString;
import static com.fourty_eight_dps.marclay.core.util.NotificationUtil.getAppplicationName;
import static com.fourty_eight_dps.marclay.core.util.NotificationUtil.getLargestImage;
import static com.fourty_eight_dps.marclay.core.util.NotificationUtil.getLauncherIcon;
import static com.fourty_eight_dps.marclay.core.util.StringUtil.nullToEmpty;

public class SyncedNotification implements Comparable<SyncedNotification> {

  private String key;
  private String message;
  private String icon;
  private String applicationIcon;
  private String applicationName;

  public SyncedNotification() {}

  public SyncedNotification(String key, String message, String icon, String applicationIcon,
      String applicationName) {
    this.key = key.replace('.', '_');
    this.message = message;
    this.icon = icon;
    this.applicationIcon = applicationIcon;
    this.applicationName = applicationName;
  }

  public static SyncedNotification create(Context context, StatusBarNotification sbn) {
    Bitmap image = getLargestImage(context, sbn);
    Bitmap launcherImage = getLauncherIcon(context, sbn);
    String applicationName = getAppplicationName(context, sbn);
    return new SyncedNotification(
        sbn.getKey(),
        nullToEmpty(sbn.getNotification().tickerText),
        encodeToString(image),
        encodeToString(launcherImage),
        applicationName);
  }

  public static SyncedNotification create(DataSnapshot snapshot) {
    return snapshot.getValue(SyncedNotification.class);
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

  public String getApplicationIcon() {
    return applicationIcon;
  }

  public String getApplicationName() {
    return applicationName;
  }

  @Override public String toString() {
    return "SyncedNotification{" +
        "key='" + key + '\'' +
        ", message='" + message + '\'' +
        ", icon='" + icon + '\'' +
        ", applicationIcon='" + applicationIcon + '\'' +
        '}';
  }

  @Override public int compareTo(SyncedNotification syncedNotification) {
    return message.compareTo(syncedNotification.getMessage());
  }
}
