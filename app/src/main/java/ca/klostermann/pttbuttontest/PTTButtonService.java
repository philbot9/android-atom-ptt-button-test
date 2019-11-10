package ca.klostermann.pttbuttontest;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class PTTButtonService extends Service {
  public static final String TAG = PTTButtonService.class.getCanonicalName();
  public static final String PTT_RX_ACTION = PTTButtonService.class.getSimpleName() + "/PTT_RX_ACTION";

  private static final String PTT_DOWN_INTENT = "android.intent.action.PTT.down";
  private static final String PTT_UP_INTENT = "android.intent.action.PTT.up";

  Sounds mSounds;

  @Override
  public void onCreate() {
    super.onCreate();
    Log.d(TAG, "Creating PTTButtonService");

    startForeground(AppNotification.getNotificationId(),
      AppNotification.getNotification(this));

    mSounds = new Sounds(this);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    super.onStartCommand(intent, flags, startId);

    Log.d(TAG, "Starting PTTButtonService");

    registerPttActionReceiver();

    return START_STICKY;
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();

    unregisterPttActionReceiver();
  }

  private void registerPttActionReceiver() {
    IntentFilter pttActionIntentFilter = new IntentFilter();
    pttActionIntentFilter.addAction(PTT_DOWN_INTENT);
    pttActionIntentFilter.addAction(PTT_UP_INTENT);
    pttActionIntentFilter.setPriority(99999999);

    registerReceiver(pttActionReceiver, pttActionIntentFilter);
  }

  private void unregisterPttActionReceiver() {
    unregisterReceiver(pttActionReceiver);
  }

  private BroadcastReceiver pttActionReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      onPttActionReceived(context, intent);
    }
  };

  protected void onPttActionReceived (Context _context, Intent intent) {
    String action = intent.getAction();

    Log.d(TAG, "Received PTT Button action: " + action);

    Intent pttActionIntent = new Intent();
    pttActionIntent.setAction(PTT_RX_ACTION);
    pttActionIntent.putExtra("timestamp", getCurrentTimestamp());
    pttActionIntent.putExtra("action", action);

    LocalBroadcastManager.getInstance(this).sendBroadcast(pttActionIntent);


    if (action.equals(PTT_DOWN_INTENT)) {
      mSounds.playPttPressed();
    } else if (action.equals(PTT_UP_INTENT)) {
      mSounds.playPttReleased();
    }
  }

  private String getCurrentTimestamp() {
    TimeZone tz = TimeZone.getTimeZone("UTC");
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");
    df.setTimeZone(tz);

    return df.format(new Date());
  }
}
