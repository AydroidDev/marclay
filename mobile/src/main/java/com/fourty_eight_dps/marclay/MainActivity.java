package com.fourty_eight_dps.marclay;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Switch;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

  @Bind(R.id.switch_notification)
  Switch notificationSwitch;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
  }

  @Override protected void onResume() {
    super.onResume();
    if (hasNotificationsEnabled()) {
      notificationSwitch.setChecked(true);
    }
  }

  private boolean hasNotificationsEnabled() {
    String notificationSetting =
        Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
    return !TextUtils.isEmpty(notificationSetting) && notificationSetting.contains(
        getApplicationContext().getPackageName());
  }

  @OnClick(R.id.switch_notification)
  public void onNotificationSwitchClicked(View view) {
    startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
  }
}
