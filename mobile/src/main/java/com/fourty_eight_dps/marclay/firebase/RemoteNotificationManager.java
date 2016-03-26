package com.fourty_eight_dps.marclay.firebase;

import com.firebase.client.Firebase;

public class RemoteNotificationManager {

  private static final String CHILD_NOTIFICATIONS = "notifications";
  private static final String FIREBASE_APP_URL = "https://marclay.firebaseio.com";

  private final Firebase firebase;

  public RemoteNotificationManager() {
    this.firebase = new Firebase(FIREBASE_APP_URL).child(CHILD_NOTIFICATIONS);
  }

  public void postNotification(SyncedNotification syncedNotification) {
    firebase.child(syncedNotification.getKey())
        .setValue(syncedNotification.getMessage());
  }

  public void removeNotification(SyncedNotification syncedNotification) {
    firebase.child(syncedNotification.getKey())
        .removeValue();
  }
}
