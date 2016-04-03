package com.fourty_eight_dps.marclay.media;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.fourty_eight_dps.marclay.core.firebase.FirebaseRefs;
import java.util.HashSet;
import java.util.Set;

/**
 * Handles Pinning and Sync folder contents from Google Drive
 */
public class MediaDispatcher implements ChildEventListener {

  private Firebase videos;
  private DownloadManager downloadManager;
  private SharedPreferences sharedPreferences;
  private Context context;
  private Set<Long> downloadIds = new HashSet<>();

  public MediaDispatcher(Context context) {
    this.context = context;
    this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

    IntentFilter downloadCompleteFilter =
        new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
    context.registerReceiver(new BroadcastReceiver() {
      @Override public void onReceive(Context context, Intent intent) {
        long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
        if (downloadIds.contains(downloadId)) {
          downloadIds.remove(downloadId);
          DownloadManager.Query query = new DownloadManager.Query();
          query.setFilterById(downloadId);
          Cursor cursor = downloadManager.query(query);
          cursor.moveToFirst();

          int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
          if (cursor.getInt(statusIndex) == DownloadManager.STATUS_SUCCESSFUL) {
            // Save File URI location.
            int uriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
            int fileNameIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
            String uriLocation = cursor.getString(uriIndex);
            String fileName = cursor.getString(fileNameIndex);
            sharedPreferences.edit()
                .putString(fileName, uriLocation)
                .apply();
          }
        }
      }
    }, downloadCompleteFilter);
  }

  public void onCreate() {
    videos = FirebaseRefs.videos();
    videos.addChildEventListener(this);
  }

  @Override public void onChildAdded(DataSnapshot dataSnapshot, String s) {
    // Start Video download
    Uri downloadUrl = Uri.parse(dataSnapshot.getValue().toString());
    DownloadManager.Request request = new DownloadManager.Request(downloadUrl);
    request.setVisibleInDownloadsUi(true);
    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
    request.setDestinationInExternalFilesDir(context, null, dataSnapshot.getKey());
    downloadIds.add(downloadManager.enqueue(request));
  }

  @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {

  }

  @Override public void onChildRemoved(DataSnapshot dataSnapshot) {
    // TODO Deleted the file

  }

  @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {

  }

  @Override public void onCancelled(FirebaseError firebaseError) {

  }
}
