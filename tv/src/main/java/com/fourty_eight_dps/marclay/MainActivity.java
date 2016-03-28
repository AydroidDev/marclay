package com.fourty_eight_dps.marclay;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.widget.TextView;
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
  TextView textView;
  TextureView textureView;

  private boolean surefaceTextureReady = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    remoteNotificationManager = new RemoteNotificationManager();
    setContentView(R.layout.activity_main);

    textView = (TextView) findViewById(R.id.notification);
    textureView = (TextureView) findViewById(R.id.texture);
    textureView.setSurfaceTextureListener(this);
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
    textView.setText("Added: " + syncedNotification.toString());
  }

  @Override public void onNotificationRemoved(SyncedNotification syncedNotification) {
    textView.setText("Removed: " + syncedNotification.toString());
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

  /**
   * Sets the TextureView transform to preserve the aspect ratio of the video.
   */
  private void adjustAspectRatio(int videoWidth, int videoHeight) {
    int viewWidth = textureView.getWidth();
    int viewHeight = textureView.getHeight();
    double aspectRatio = (double) videoHeight / videoWidth;

    int newWidth, newHeight;
    if (viewHeight > (int) (viewWidth * aspectRatio)) {
      // limited by narrow width; restrict height
      newWidth = viewWidth;
      newHeight = (int) (viewWidth * aspectRatio);
    } else {
      // limited by short height; restrict width
      newWidth = (int) (viewHeight / aspectRatio);
      newHeight = viewHeight;
    }
    int xoff = (viewWidth - newWidth) / 2;
    int yoff = (viewHeight - newHeight) / 2;

    Matrix txform = new Matrix();
    textureView.getTransform(txform);
    txform.setScale((float) newWidth / viewWidth, (float) newHeight / viewHeight);
    txform.postTranslate(xoff, yoff);
    textureView.setTransform(txform);
  }

  @Override public void playbackStopped() {}

}
