package com.fourty_eight_dps.marclay.media;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.io.File;

/**
 * A wrapper around shared preferences for persisting video locations
 */
public class VideoStorage {

  private SharedPreferences sharedPreferences;

  public VideoStorage(Context context) {
    this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
  }

  public boolean hasVideo(String videoId) {
    return sharedPreferences.contains(videoId);
  }

  public void putVideo(String videoId, String fileUri) {
    sharedPreferences.edit()
        .putString(videoId, fileUri)
        .apply();
  }

  public File getVideo(String key) {
    return new File(sharedPreferences.getString(key, ""));
  }
}
