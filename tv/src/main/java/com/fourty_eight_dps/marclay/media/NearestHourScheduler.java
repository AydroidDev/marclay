package com.fourty_eight_dps.marclay.media;

import com.firebase.client.DataSnapshot;
import com.fourty_eight_dps.marclay.core.firebase.Video;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class NearestHourScheduler {

  private final TreeMap<Video, DataSnapshot> videoDataSnapshotMap;

  public NearestHourScheduler(Iterable<DataSnapshot> items) {
    this.videoDataSnapshotMap = new TreeMap<>(new Comparator<Video>() {
      @Override public int compare(Video video, Video that) {
        return Integer.compare(video.getHour(), that.getHour());
      }
    });

    for (DataSnapshot snapshot : items) {
      Video video = snapshot.getValue(Video.class);
      videoDataSnapshotMap.put(video, snapshot);
    }
  }

  public DataSnapshot closestToCurrentHour() {
    int hour = getHour();
    Video currentTime = new Video(hour);
    Map.Entry<Video, DataSnapshot> floor = videoDataSnapshotMap.floorEntry(currentTime);
    Map.Entry<Video, DataSnapshot> ceiling = videoDataSnapshotMap.ceilingEntry(currentTime);

    if (floor != null) {
      return floor.getValue();
    } else if (ceiling != null) {
      return ceiling.getValue();
    } else {
      return null;
    }
  }

  private int getHour() {
    Calendar calendar = Calendar.getInstance();
    return calendar.get(Calendar.HOUR_OF_DAY);
  }
}
