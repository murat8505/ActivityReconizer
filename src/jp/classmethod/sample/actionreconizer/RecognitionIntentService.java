package jp.classmethod.sample.actionreconizer;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import jp.classmethod.sample.actionreconizer.R;

public class RecognitionIntentService extends IntentService {
  
  private static final String TAG = RecognitionIntentService.class.getSimpleName();
  private final RecognitionIntentService self = this;

  public RecognitionIntentService() {
    super("RecognitionIntentService");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    if (ActivityRecognitionResult.hasResult(intent)) {
      ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
      DetectedActivity mostProbableActivity = result.getMostProbableActivity();
      int activityType = mostProbableActivity.getType();
      Log.i(TAG, "confidence   = " + mostProbableActivity.getConfidence());
      Log.i(TAG, "activityType = " + activityType);
      notification(getTypeName(activityType));
    }
  }

  private void notification(String message) {
    Intent intent = new Intent(self, MainActivity.class);
    PendingIntent contentIntent = PendingIntent.getActivity(self, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
    NotificationCompat.Builder builder = new NotificationCompat.Builder(self);
    builder.setContentIntent(contentIntent);
    builder.setTicker(message);
    builder.setSmallIcon(R.drawable.ic_launcher);
    builder.setContentTitle("ActivityRecognition");
    builder.setContentText(message);
    builder.setLargeIcon(largeIcon);
    builder.setWhen(System.currentTimeMillis());
    builder.setAutoCancel(true);
    NotificationManager manager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
    manager.notify(0, builder.build());
  }

  private String getTypeName(int activityType) {
    switch (activityType) {
      case DetectedActivity.IN_VEHICLE:
        return "車で移動中";
      case DetectedActivity.ON_BICYCLE:
        return "自転車で移動中";
      case DetectedActivity.ON_FOOT:
        return "徒歩で移動中";
      case DetectedActivity.STILL:
        return "待機中";
      case DetectedActivity.UNKNOWN:
        return "不明";
      case DetectedActivity.TILTING:
        return "デバイスが傾き中";
    }
    return null;
  }
}
