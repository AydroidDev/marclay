package com.fourty_eight_dps.marclay;

import android.content.SharedPreferences;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import com.fourty_eight_dps.marclay.core.firebase.RemoteNotificationManager;
import com.fourty_eight_dps.marclay.core.firebase.SyncedNotification;
import com.fourty_eight_dps.marclay.media.MediaDispatcher;
import com.fourty_eight_dps.marclay.playback.MoviePlayer;
import com.fourty_eight_dps.marclay.playback.SpeedControlCallback;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
    implements RemoteNotificationManager.NotificationListener, TextureView.SurfaceTextureListener,
    MoviePlayer.PlayerFeedback {

  MoviePlayer.PlayTask playTask;
  RemoteNotificationManager remoteNotificationManager;

  TextureView textureView;
  RecyclerView recyclerView;
  NotificationAdapter notificationAdapter;

  MediaDispatcher mediaDispatcher;
  SharedPreferences sharedPreferences;

  private boolean surefaceTextureReady = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    mediaDispatcher = new MediaDispatcher(this);
    mediaDispatcher.onCreate();

    remoteNotificationManager = new RemoteNotificationManager();
    setContentView(R.layout.activity_main);

    textureView = (TextureView) findViewById(R.id.texture);
    textureView.setSurfaceTextureListener(this);

    notificationAdapter = new NotificationAdapter();
    recyclerView = (RecyclerView) findViewById(android.R.id.list);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    linearLayoutManager.setAutoMeasureEnabled(true);
    recyclerView.setLayoutManager(linearLayoutManager);
    recyclerView.setAdapter(notificationAdapter);
  }

  @Override protected void onStart() {
    super.onStart();
    remoteNotificationManager.registerNotificationListener(this);
  }

  @Override protected void onPause() {
    super.onPause();
    if (playTask != null) {
      stopPlayback();
      playTask.waitForStop();
    }
  }

  @Override protected void onStop() {
    super.onStop();
    remoteNotificationManager.unregisterNotificationListener();
  }

  @Override public void onNotificationPosted(SyncedNotification syncedNotification) {
    notificationAdapter.add(syncedNotification);
  }

  @Override public void onNotificationRemoved(SyncedNotification syncedNotification) {
    notificationAdapter.remove(syncedNotification);
  }

  @Override public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
    surefaceTextureReady = true;
    List<Object> files = new ArrayList<>(sharedPreferences.getAll().values());
    if (!files.isEmpty()) {
      try {
        Surface surface = new Surface(surfaceTexture);
        File file = new File(files.get(0).toString());
        Log.d("FILE", file.getAbsolutePath());
        MoviePlayer player = null;
        SpeedControlCallback callback = new SpeedControlCallback();
        player = new MoviePlayer(file, surface, callback);
        playTask = new MoviePlayer.PlayTask(player, this);
        playTask.setLoopMode(true);
        playTask.execute();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void stopPlayback() {
    if (playTask != null) {
      playTask.requestStop();
    }
  }

  @Override public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {}

  @Override public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
    surefaceTextureReady = false;
    return true;
  }

  @Override public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {}

  @Override public void playbackStopped() {}
}
