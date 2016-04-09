package com.fourty_eight_dps.marclay;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
  }

  @OnClick(R.id.switch_notification)
  public void onNotificationSwitchClicked(View view) {
    startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
  }
}
