package com.fourty_eight_dps.marclay;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.SurfaceTexture;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.fourty_eight_dps.marclay.core.firebase.RemoteNotificationManager;
import com.fourty_eight_dps.marclay.core.firebase.SyncedNotification;
import com.fourty_eight_dps.marclay.media.MediaDispatcher;
import com.fourty_eight_dps.marclay.playback.MoviePlayer;
import com.fourty_eight_dps.marclay.playback.SpeedControlCallback;
import com.fourty_eight_dps.marclay.weather.OpenWeather;
import com.fourty_eight_dps.marclay.weather.model.Response;
import java.io.File;
import java.io.IOException;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import retrofit2.Call;
import retrofit2.Callback;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

public class MainActivity extends AppCompatActivity
    implements RemoteNotificationManager.NotificationListener, TextureView.SurfaceTextureListener,
    MoviePlayer.PlayerFeedback {

  public static final long DELAY_TEN_SECONDS = SECONDS.toMillis(10);
  public static final long DELAY_ONE_MINUTE = MINUTES.toMillis(1);

  MoviePlayer.PlayTask playTask;
  RemoteNotificationManager remoteNotificationManager;
  NotificationAdapter notificationAdapter;
  MediaDispatcher mediaDispatcher;
  Surface surface;

  @Bind(R.id.texture) TextureView textureView;
  @Bind(android.R.id.list) RecyclerView recyclerView;
  @Bind(android.R.id.progress) View progress;
  @Bind(R.id.weather) TextView weather;
  @Bind(R.id.mask) View mask;

  Handler handler;

  private boolean surefaceTextureReady = false;

  private Runnable updateWeather = new Runnable() {
    @Override public void run() {
      OpenWeather.getWeather(new Callback<Response.Weather>() {
        @Override public void onResponse(Call<Response.Weather> call,
            retrofit2.Response<Response.Weather> response) {
          if (response.isSuccessful()) {
            Response.Weather currentWeather = response.body();
            weather.setText(
                String.format(getString(R.string.title_weather_format),
                    Math.round(currentWeather.main.temp)));
          }
        }

        @Override public void onFailure(Call<Response.Weather> call, Throwable t) {}
      });
      handler.postDelayed(this, DELAY_ONE_MINUTE);
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    handler = new Handler(Looper.getMainLooper());
    mediaDispatcher = new MediaDispatcher(this);

    remoteNotificationManager = new RemoteNotificationManager();
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    textureView.setSurfaceTextureListener(this);

    notificationAdapter = new NotificationAdapter();
    recyclerView.setItemAnimator(new SlideInLeftAnimator(new OvershootInterpolator(.8f)));
    recyclerView.getItemAnimator().setAddDuration(600);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    linearLayoutManager.setAutoMeasureEnabled(true);
    recyclerView.setLayoutManager(linearLayoutManager);
    recyclerView.setAdapter(notificationAdapter);
  }

  @Override protected void onStart() {
    super.onStart();
    mediaDispatcher.onStart();
    remoteNotificationManager.registerNotificationListener(this);
    handler.post(updateWeather);
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
    mediaDispatcher.onStop();
    remoteNotificationManager.unregisterNotificationListener();
    handler.removeCallbacks(updateWeather);
  }

  @Override public void onNotificationPosted(SyncedNotification syncedNotification) {
    notificationAdapter.add(syncedNotification);
    handler.postDelayed(new Runnable() {
      @Override public void run() {
        playNotificationSound();
      }
    }, 200);
  }

  private void playNotificationSound() {
    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
    r.play();
  }

  @Override public void onNotificationRemoved(SyncedNotification syncedNotification) {
    notificationAdapter.remove(syncedNotification);
  }

  @Override public void onNotificationUpdate(SyncedNotification syncedNotification) {
    notificationAdapter.update(syncedNotification);
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
        endProgressLoading();
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

  private void endProgressLoading() {
    if (progress.getVisibility() == View.GONE) {return;}

    progress.animate()
        .alpha(0)
        .setStartDelay(2000)
        .setDuration(700)
        .setListener(new Animator.AnimatorListener() {
          @Override public void onAnimationStart(Animator animator) {}

          @Override public void onAnimationEnd(Animator animator) {
            removeMask();
          }

          @Override public void onAnimationCancel(Animator animator) {}

          @Override public void onAnimationRepeat(Animator animator) {}
        })
        .start();
  }

  private void removeMask() {
    // get the center for the clipping circle
    // create the animation (the final radius is zero)
    Animator anim =
        ViewAnimationUtils.createCircularReveal(mask, 0, 0, mask.getWidth(), 0);

    // make the view invisible when the animation is done
    anim.setDuration(400);
    anim.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        mask.setVisibility(View.INVISIBLE);
      }
    });

    // start the animation
    anim.start();
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
