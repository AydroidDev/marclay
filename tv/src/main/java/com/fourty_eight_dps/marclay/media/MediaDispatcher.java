package com.fourty_eight_dps.marclay.media;

import android.content.Intent;
import android.content.IntentSender;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;

import static android.app.Activity.RESULT_OK;

/**
 * Handles Pinning and Sync folder contents from Google Drive
 */
public class MediaDispatcher implements GoogleApiClient.OnConnectionFailedListener {

  private static final int RESOLVE_CONNECTION_REQUEST_CODE = 1000;
  private FragmentActivity activity;
  GoogleApiClient googleApiClient;

  public MediaDispatcher(FragmentActivity activity) {
    this.activity = activity;
  }

  public void onCreate() {
    googleApiClient = new GoogleApiClient.Builder(activity)
        .enableAutoManage(activity, this)
        .addApi(Drive.API)
        .addScope(Drive.SCOPE_FILE)
        .build();
  }

  public void onStart() {
    googleApiClient.connect();
  }

  public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
    switch (requestCode) {
      case RESOLVE_CONNECTION_REQUEST_CODE:
        if (resultCode == RESULT_OK) {
          googleApiClient.connect();
        }
        break;
    }
  }

  @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    if (connectionResult.hasResolution()) {
      try {
        connectionResult.startResolutionForResult(activity, RESOLVE_CONNECTION_REQUEST_CODE);
      } catch (IntentSender.SendIntentException e) {
        // Unable to resolve, message user appropriately
      }
    } else {
      GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), activity, 0).show();
    }
  }
}
