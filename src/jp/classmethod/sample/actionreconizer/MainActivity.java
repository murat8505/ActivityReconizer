package jp.classmethod.sample.actionreconizer;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.ActivityRecognitionClient;

// http://dev.classmethod.jp/smartphone/android/google-play-services-android-location-api-activity-recognition/
public class MainActivity extends FragmentActivity {
  static TextView tv;
  private final MainActivity self = this;
  private ActivityRecognitionClient mClient;
  private ToggleButton toggleButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mClient = new ActivityRecognitionClient(self, mConnectionCallbacks, mOnConnectionFailedListener);
    toggleButton = new ToggleButton(this);
    setContentView(toggleButton);
    toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
          mClient.connect();
        } else {
          mClient.disconnect();
        }
      }
    });

  }

  private GooglePlayServicesClient.ConnectionCallbacks mConnectionCallbacks = new GooglePlayServicesClient.ConnectionCallbacks() {
    @Override
    public void onConnected(Bundle bundle) {
      Toast.makeText(self, "onConnected", Toast.LENGTH_LONG).show();
      Intent intent = new Intent(self, RecognitionIntentService.class);
      PendingIntent pendingIntent = PendingIntent.getService(self, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
      mClient.requestActivityUpdates(1000, pendingIntent);
    }

    @Override
    public void onDisconnected() {
      Toast.makeText(self, "onDisconnected", Toast.LENGTH_LONG).show();
    }
  };

  private GooglePlayServicesClient.OnConnectionFailedListener mOnConnectionFailedListener = new GooglePlayServicesClient.OnConnectionFailedListener() {
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
      Toast.makeText(self, "onConnectionFailed", Toast.LENGTH_LONG).show();
    }
  };
}
