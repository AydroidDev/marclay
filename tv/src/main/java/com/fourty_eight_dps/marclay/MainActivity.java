package com.fourty_eight_dps.marclay;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import com.fourty_eight_dps.marclay.core.firebase.RemoteNotificationManager;
import com.fourty_eight_dps.marclay.core.firebase.SyncedNotification;
import com.fourty_eight_dps.marclay.video.MoviePlayer;
import com.fourty_eight_dps.marclay.video.SpeedControlCallback;
import java.io.File;
import java.io.IOException;

public class MainActivity extends Activity
    implements RemoteNotificationManager.NotificationListener, TextureView.SurfaceTextureListener,
    MoviePlayer.PlayerFeedback {

  MoviePlayer.PlayTask playTask;
  RemoteNotificationManager remoteNotificationManager;

  TextureView textureView;
  RecyclerView recyclerView;
  NotificationAdapter notificationAdapter;

  private boolean surefaceTextureReady = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
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
    try {
      Surface surface = new Surface(surfaceTexture);
      File file = new File(Environment.getExternalStorageDirectory(), "example.mp4");
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

  @Override public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {}

  @Override public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
    surefaceTextureReady = false;
    return true;
  }

  @Override public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {}

  @Override public void playbackStopped() {}
}
