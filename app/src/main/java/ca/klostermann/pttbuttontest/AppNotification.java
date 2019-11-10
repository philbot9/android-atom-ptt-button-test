package ca.klostermann.pttbuttontest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class AppNotification {
  public static final String TAG = AppNotification.class.getCanonicalName();

  private static final int NOTIFICATION_ID = 32165;
  private static final String CHANNEL_ID = "ptt-foreground-service";

  private static Notification notification;

  public static Notification getNotification(Context context) {
    final String title = "PTT Button Test";
    final String content = "App is running in Background";


    if(notification == null) {
      createNotificationChannel(context);

      notification = new NotificationCompat.Builder(context, CHANNEL_ID)
        .setContentTitle(title)
        .setContentText(content)
        .setSmallIcon(R.mipmap.ic_launcher)
        .build();
    }

    return notification;
  }

  private static void createNotificationChannel (Context context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel serviceChannel = new NotificationChannel(
        CHANNEL_ID,
        "PTT Service Channel",
        NotificationManager.IMPORTANCE_LOW
      );

      serviceChannel.enableVibration(false);

      NotificationManager manager = context.getSystemService(NotificationManager.class);
      manager.createNotificationChannel(serviceChannel);
    }
  }

  public static int getNotificationId() {
    return NOTIFICATION_ID;
  }
}
