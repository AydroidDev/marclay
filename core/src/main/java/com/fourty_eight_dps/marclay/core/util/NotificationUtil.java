package com.fourty_eight_dps.marclay.core.util;

import android.app.Notification;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.service.notification.StatusBarNotification;

public final class NotificationUtil {

  /**
   * List of preferred icons from top to bottom.
   * <ul>
   * <li>Remote View Bitmap</li>
   * <li>Large Icon</li>
   * <li>Application Icon</li>
   * </ul>
   */
  public static Bitmap getLargestImage(Context context, StatusBarNotification sbn) {
    //Bitmap bigPicture = (Bitmap) notification.extras.get(Notification.EXTRA_PICTURE);
    // Example 192 x 192
    Notification notification = sbn.getNotification();
    Bitmap largeIcon = notification.largeIcon;

    if (largeIcon != null) {
      return largeIcon;
    } else {
      return getNotificationIcon(context, sbn);
    }
  }

  public static Bitmap getLauncherIcon(Context context, StatusBarNotification sbn) {
    try {
      String packageName = sbn.getPackageName();
      PackageManager packageManager = context.getPackageManager();
      Drawable iconDrawable = packageManager.getApplicationIcon(packageName);
      if (iconDrawable instanceof BitmapDrawable) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) iconDrawable;
        return bitmapDrawable.getBitmap();
      }
    } catch (Exception ignored) {}
    return null;
  }

  public static String getAppplicationName(Context context, StatusBarNotification sbn) {
    try {
      PackageManager packageManager = context.getPackageManager();
      ApplicationInfo applicationInfo = packageManager.getApplicationInfo(sbn.getPackageName(), 0);
      return packageManager.getApplicationLabel(applicationInfo).toString();
    } catch (PackageManager.NameNotFoundException ignored) {}
    return "";
  }

  private static Bitmap getNotificationIcon(Context context, StatusBarNotification sbn) {
    Resources resources;
    try {
      resources = context.getPackageManager().getResourcesForApplication(sbn.getPackageName());
      return BitmapFactory.decodeResource(resources, sbn.getNotification().icon);
    } catch (PackageManager.NameNotFoundException ignored) {}
    return null;
  }
}
