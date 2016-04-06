package com.fourty_eight_dps.marclay.media;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.google.common.collect.Iterators;
import java.io.File;
import java.util.Iterator;

/**
 * A wrapper around shared preferences for persisting video locations
 */
public class VideoStorage {

  private SharedPreferences sharedPreferences;
  private Iterator<?> cycleIterator;

  public VideoStorage(Context context) {
    this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    updateCycleIterator();
  }

  public boolean hasVideo(String videoId) {
    return sharedPreferences.contains(videoId);
  }

  public void putVideo(String videoId, String fileUri) {
    sharedPreferences.edit()
        .putString(videoId, fileUri)
        .apply();
    updateCycleIterator();
  }

  public File next() {
    if (!cycleIterator.hasNext()) {
      return null;
    }
    return new File(cycleIterator.next().toString());
  }

  private void updateCycleIterator() {
    this.cycleIterator = Iterators.cycle(sharedPreferences.getAll().values());
  }
}
