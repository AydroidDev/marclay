package com.fourty_eight_dps.marclay.apple;

import com.fourty_eight_dps.marclay.apple.model.Entry;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface AerialApi {

  @GET("us/r1000/000/Features/atv/AutumnResources/videos/entries.json")
  Call<List<Entry>> listEntries();
}
