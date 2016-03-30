package com.fourty_eight_dps.marclay.core.firebase;

import android.content.Context;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import java.lang.ref.WeakReference;

public class RemoteNotificationManager {

  public interface NotificationListener {
    void onNotificationPosted(SyncedNotification syncedNotification);

    void onNotificationRemoved(SyncedNotification syncedNotification);
  }

  private static final String CHILD_NOTIFICATIONS = "notifications";
  private static final String FIREBASE_APP_URL = "https://marclay.firebaseio.com";

  private final Firebase firebase;
  private WeakReference<NotificationListener> notificationListenerWeakReference;
  private final ChildEventListener notificationListener = new ChildEventListener() {
    @Override public void onChildAdded(DataSnapshot dataSnapshot, String s) {
      if (notificationListenerWeakReference != null) {
        notificationListenerWeakReference
            .get()
            .onNotificationPosted(SyncedNotification.create(dataSnapshot));
      }
    }

    @Override public void onChildRemoved(DataSnapshot dataSnapshot) {
      if (notificationListenerWeakReference != null) {
        notificationListenerWeakReference
            .get()
            .onNotificationRemoved(SyncedNotification.create(dataSnapshot));
      }
    }

    @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

    @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

    @Override public void onCancelled(FirebaseError firebaseError) {}
  };

  public RemoteNotificationManager() {
    this.firebase = new Firebase(FIREBASE_APP_URL).child(CHILD_NOTIFICATIONS);
    this.firebase.addChildEventListener(notificationListener);
  }

  public void postNotification(SyncedNotification syncedNotification) {
    firebase.child(syncedNotification.getKey())
        .setValue(syncedNotification);
  }

  public void removeNotification(SyncedNotification syncedNotification) {
    firebase.child(syncedNotification.getKey())
        .removeValue();
  }

  public void registerNotificationListener(NotificationListener notificationListener) {
    notificationListenerWeakReference = new WeakReference<>(notificationListener);
  }

  public void unregisterNotificationListener() {
    notificationListenerWeakReference.clear();
    notificationListenerWeakReference = null;
  }

  public static void setAndroidContext(Context context) {
    Firebase.setAndroidContext(context);
  }
}
