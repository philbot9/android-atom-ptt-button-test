package ca.klostermann.pttbuttontest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class PTTButtonReceiver extends BroadcastReceiver {
  public static final String TAG = PTTButtonReceiver.class.getCanonicalName();

  private static final String PTT_DOWN_INTENT = "android.intent.action.PTT.down";
  private static final String PTT_UP_INTENT = "android.intent.action.PTT.up";

  @Override
  public void onReceive(Context context, Intent intent) {
    String action = intent.getAction();

    Log.d(TAG, "Received PTT Button action: " + action);

    Intent pttActionIntent = new Intent();
    pttActionIntent.setAction(PTTButtonService.PTT_RX_ACTION);
    pttActionIntent.putExtra("timestamp", getCurrentTimestamp());
    pttActionIntent.putExtra("action", action);

    LocalBroadcastManager.getInstance(context).sendBroadcast(pttActionIntent);


    Sounds sounds = new Sounds(context);
    if (action.equals(PTT_DOWN_INTENT)) {
      sounds.playPttPressed();
    } else if (action.equals(PTT_UP_INTENT)) {
      sounds.playPttReleased();
    }
  }


  private String getCurrentTimestamp() {
    TimeZone tz = TimeZone.getTimeZone("UTC");
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");
    df.setTimeZone(tz);

    return df.format(new Date());
  }
}

