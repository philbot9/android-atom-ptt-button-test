package ca.klostermann.pttbuttontest;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class PTTButtonReceiverService extends Service {
  public static final String TAG = PTTButtonReceiverService.class.getCanonicalName();
  public static final String PTT_ACTION_EVENT = PTTButtonReceiverService.class.getSimpleName() + "/PTT_ACTION_EVENT";

  private final String PTT_DOWN_INTENT_ACTION = "android.intent.action.PTT.down";
  private final String PTT_UP_INTENT_ACTION = "android.intent.action.PTT.up";

  Sounds mSounds;

  @Override
  public void onCreate() {
    super.onCreate();

    startForeground(AppNotification.getNotificationId(),
      AppNotification.getNotification(this));

    mSounds = new Sounds(this);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    super.onStartCommand(intent, flags, startId);

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
    pttActionIntentFilter.addAction(PTT_DOWN_INTENT_ACTION);
    pttActionIntentFilter.addAction(PTT_UP_INTENT_ACTION);
    pttActionIntentFilter.setPriority(99999999);

    registerReceiver(pttActionReceiver, pttActionIntentFilter);
  }

  private void unregisterPttActionReceiver() {
    unregisterReceiver(pttActionReceiver);
  }

  private BroadcastReceiver pttActionReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      onPttActionReceived(intent);
    }
  };

  protected void onPttActionReceived (Intent intent) {
    String action = intent.getAction();

    Log.d(TAG, "Received PTT Button action: " + action);

    playButtonActionSound(action);

    broadcastPttAction(action);
  }

  private void playButtonActionSound(String action) {
    if (action.equals(PTT_DOWN_INTENT_ACTION)) {
      mSounds.playPttPressed();
    } else if (action.equals(PTT_UP_INTENT_ACTION)) {
      mSounds.playPttReleased();
    }
  }

  private void broadcastPttAction(String action) {
    Intent pttActionIntent = new Intent();
    String timestamp = Timestamp.now();

    pttActionIntent.setAction(PTT_ACTION_EVENT);
    pttActionIntent.putExtra("timestamp", timestamp);
    pttActionIntent.putExtra("action", action);

    LocalBroadcastManager.getInstance(this).sendBroadcast(pttActionIntent);
  }
}
