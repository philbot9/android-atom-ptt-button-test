package ca.klostermann.pttbuttontest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;



/**
 * CURRENTLY DEACTIVATED!
 *
 * This class was used to test the PTT Button with a Manifest-declared BroadcastReceiver.
 * To enable th BroadcastReceiver uncomment the <receiver> declaration in AndroidManifest.xml:26-31
 * and prevent the MainActivity from starting the PTTButtonService (MainActivity.java:37).
 */

public class PTTButtonReceiver extends BroadcastReceiver {
  public static final String TAG = PTTButtonReceiver.class.getCanonicalName();

  private static final String PTT_DOWN_INTENT_ACTION = "android.intent.action.PTT.down";
  private static final String PTT_UP_INTENT_ACTION = "android.intent.action.PTT.up";

  @Override
  public void onReceive(Context context, Intent intent) {
    String action = intent.getAction();

    Log.d(TAG, "Received PTT Button action: " + action);

    playButtonActionSound(context, action);

    broadcastPttAction(context, action);
  }

  private void playButtonActionSound(Context context, String action) {
    Sounds sounds = new Sounds(context);
    if (action.equals(PTT_DOWN_INTENT_ACTION)) {
      sounds.playPttPressed();
    } else if (action.equals(PTT_UP_INTENT_ACTION)) {
      sounds.playPttReleased();
    }
  }

  private void broadcastPttAction(Context context, String action) {
    Intent pttActionIntent = new Intent();
    String timestamp = Timestamp.now();

    pttActionIntent.setAction(PTTButtonReceiverService.PTT_ACTION_EVENT);
    pttActionIntent.putExtra("timestamp", timestamp);
    pttActionIntent.putExtra("action", action);

    LocalBroadcastManager.getInstance(context).sendBroadcast(pttActionIntent);
  }
}

