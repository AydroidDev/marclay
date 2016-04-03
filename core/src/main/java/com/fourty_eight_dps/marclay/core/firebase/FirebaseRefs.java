package com.fourty_eight_dps.marclay.core.firebase;

import com.firebase.client.Firebase;

public class FirebaseRefs {

  private static final String CHILD_NOTIFICATIONS = "notifications";
  private static final String CHILD_VIDEOS = "videos";
  private static final String FIREBASE_APP_URL = "https://marclay.firebaseio.com";

  public static Firebase notifications() {
    return new Firebase(FIREBASE_APP_URL).child(CHILD_NOTIFICATIONS);
  }

  public static Firebase videos() {
    return new Firebase(FIREBASE_APP_URL).child(CHILD_VIDEOS);
  }
}
