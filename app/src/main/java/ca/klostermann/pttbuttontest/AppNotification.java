package ca.klostermann.pttbuttontest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class AppNotification {
  public static final String TAG = AppNotification.class.getCanonicalName();

  private static final int NOTIFICATION_ID = 32165;
  private static final String NOTIFICATION_CHANNEL_ID = "ptt-foreground-service";
  private static final String NOTIFICATION_TITLE = "PTT Button Test";
  private static final String NOTIFICATION_CONTENT = "App is running in Background";

  private static Notification mNotification;

  public static Notification getNotification(Context context) {
    if(mNotification == null) {
      createNotificationChannel(context);
      mNotification = createNotification(context);
    }

    return mNotification;
  }

  private static void createNotificationChannel (Context context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel serviceChannel = new NotificationChannel(
        NOTIFICATION_CHANNEL_ID,
        "PTTButtonReceiverService Notification Channel",
        NotificationManager.IMPORTANCE_LOW
      );

      serviceChannel.enableVibration(false);

      NotificationManager manager = context.getSystemService(NotificationManager.class);
      manager.createNotificationChannel(serviceChannel);
    }
  }

  private static Notification createNotification (Context context) {
    return new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(NOTIFICATION_CONTENT)
            .setSmallIcon(R.mipmap.ic_launcher)
            .build();
  }

  public static int getNotificationId() {
    return NOTIFICATION_ID;
  }
}
