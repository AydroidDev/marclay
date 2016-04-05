package com.fourty_eight_dps.marclay;

import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import com.fourty_eight_dps.marclay.core.firebase.RemoteNotificationManager;
import com.fourty_eight_dps.marclay.core.firebase.SyncedNotification;
import com.fourty_eight_dps.marclay.media.MediaDispatcher;
import com.fourty_eight_dps.marclay.playback.MoviePlayer;
import com.fourty_eight_dps.marclay.playback.SpeedControlCallback;
import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity
    implements RemoteNotificationManager.NotificationListener, TextureView.SurfaceTextureListener,
    MoviePlayer.PlayerFeedback {

  public static final int DELAY_TEN_SECONDS = 10000;

  MoviePlayer.PlayTask playTask;
  RemoteNotificationManager remoteNotificationManager;

  TextureView textureView;
  RecyclerView recyclerView;
  NotificationAdapter notificationAdapter;

  MediaDispatcher mediaDispatcher;
  Surface surface;

  View progress;
  Handler handler;

  private boolean surefaceTextureReady = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    handler = new Handler(Looper.getMainLooper());
    mediaDispatcher = new MediaDispatcher(this);
    mediaDispatcher.onCreate();

    remoteNotificationManager = new RemoteNotificationManager();
    setContentView(R.layout.activity_main);

    progress = findViewById(android.R.id.progress);

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
    surface = new Surface(surfaceTexture);
    enqueueNext();
  }

  private void enqueueNext() {
    File nextVideo = mediaDispatcher.nextVideo();
    if (nextVideo != null) {
      try {
        progress.setVisibility(View.GONE);
        MoviePlayer player = null;
        SpeedControlCallback callback = new SpeedControlCallback();
        player = new MoviePlayer(nextVideo, surface, callback);
        playTask = new MoviePlayer.PlayTask(player, this);
        playTask.execute();
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      // Schedule a delayed attempt to play
      handler.postDelayed(new Runnable() {
        @Override public void run() {
          enqueueNext();
        }
      }, DELAY_TEN_SECONDS);
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

  @Override public void playbackStopped() {
    enqueueNext();
  }
}
