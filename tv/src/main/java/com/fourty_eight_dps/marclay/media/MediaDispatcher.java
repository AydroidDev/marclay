package com.fourty_eight_dps.marclay.media;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.fourty_eight_dps.marclay.core.firebase.FirebaseRefs;
import com.fourty_eight_dps.marclay.core.firebase.Video;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Handles Pinning and Sync folder contents from Google Drive
 */
public class MediaDispatcher implements ChildEventListener, ValueEventListener {

  private Firebase videos;
  private DownloadManager downloadManager;
  private VideoStorage videoStorage;
  private Context context;
  private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
    @Override public void onReceive(Context context, Intent intent) {
      long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
      if (keyToDownloadIdMap.containsKey(downloadId)) {
        String videoId = keyToDownloadIdMap.remove(downloadId);
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
          videoStorage.putVideo(videoId, fileName);
        }
      }
    }
  };

  /**
   * Maps an Android ID to a Video ID
   */
  private Map<Long, String> keyToDownloadIdMap = new HashMap<>();
  private Iterator<DataSnapshot> cycleIterator;

  public MediaDispatcher(Context context) {
    this.context = context;
    this.downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    this.videoStorage = new VideoStorage(context);
  }

  public void onStart() {
    IntentFilter downloadCompleteFilter =
        new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
    context.registerReceiver(downloadReceiver, downloadCompleteFilter);

    videos = FirebaseRefs.videos();
    videos.addChildEventListener(this);
    videos.addValueEventListener(this);
  }

  public void onStop() {
    context.unregisterReceiver(downloadReceiver);
    videos.removeEventListener((ChildEventListener) this);
    videos.removeEventListener((ValueEventListener) this);
  }

  public File nextVideo() {
    if (cycleIterator == null) {return null;}

    // Iterate to the next available stored video
    while (cycleIterator.hasNext()) {
      DataSnapshot snapshot = cycleIterator.next();
      if (videoStorage.hasVideo(snapshot.getKey())) {
        return videoStorage.getVideo(snapshot.getKey());
      }
    }

    return null;
  }

  @Override public void onChildAdded(DataSnapshot dataSnapshot, String s) {
    // Start Video download
    Video video = dataSnapshot.getValue(Video.class);
    String key = dataSnapshot.getKey();

    if (!videoStorage.hasVideo(key)) {
      DownloadManager.Request request = new DownloadManager.Request(Uri.parse(video.getUrl()));
      request.setVisibleInDownloadsUi(true);
      request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
      request.setDestinationInExternalFilesDir(context, null, dataSnapshot.getKey());
      keyToDownloadIdMap.put(downloadManager.enqueue(request), key);
    }
  }

  @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

  @Override public void onChildRemoved(DataSnapshot dataSnapshot) {
    // TODO Deleted the file
  }

  @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

  @Override public void onDataChange(DataSnapshot dataSnapshot) {
    List<DataSnapshot> snapshots = Lists.newArrayList(dataSnapshot.getChildren());
    cycleIterator = Iterators.cycle(snapshots);
  }

  @Override public void onCancelled(FirebaseError firebaseError) {}
}
