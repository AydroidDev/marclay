package com.fourty_eight_dps.marclay.daydream;

import android.service.dreams.DreamService;
import com.fourty_eight_dps.marclay.R;

public class AerialDayDream extends DreamService {

  @Override public void onAttachedToWindow() {
    super.onAttachedToWindow();
    setInteractive(false);
    setFullscreen(true);
    setContentView(R.layout.dream_aerial);
  }
}
