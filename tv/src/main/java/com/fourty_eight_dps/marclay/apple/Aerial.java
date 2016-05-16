package com.fourty_eight_dps.marclay.apple;

import com.fourty_eight_dps.marclay.apple.model.Entry;
import java.util.List;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Aerial {
  public static final String ENDPOINT = "http://a1.phobos.apple.com/";

  public static void getAerial(Callback<List<Entry>> callback) {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(ENDPOINT)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    AerialApi aerialApi = retrofit.create(AerialApi.class);
    aerialApi.listEntries().enqueue(callback);
  }
}
