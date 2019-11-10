package ca.klostermann.pttbuttontest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
  public static final String TAG = MainActivity.class.getCanonicalName();

  List<String> mPTTActions = new ArrayList<>();
  ArrayAdapter<String> mPttActionsAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mPttActionsAdapter = new ArrayAdapter<>(this,
      R.layout.activity_listview, mPTTActions);

    ListView listView = findViewById(R.id.list);
    listView.setAdapter(mPttActionsAdapter);

    IntentFilter filter = new IntentFilter();
    filter.addAction(PTTButtonService.PTT_RX_ACTION);
    LocalBroadcastManager.getInstance(this).registerReceiver(pttActionReceiver, filter);
  }

  @Override
  protected void onStart() {
    super.onStart();
    startPTTButtonService();
  }

  private void startPTTButtonService () {
    Intent serviceIntent = new Intent(this, PTTButtonService.class);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      this.startForegroundService(serviceIntent);
    } else {
      this.startService(serviceIntent);
    }
  }

  private BroadcastReceiver pttActionReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      String action = intent.getAction();

      if (action.equals(PTTButtonService.PTT_RX_ACTION)) {
        String timestamp = intent.getStringExtra("timestamp");
        String pttAction = intent.getStringExtra("action");
        recordPTTAction(timestamp, pttAction);
      }
    }
  };

  protected void recordPTTAction(String timestamp, String action) {
    mPTTActions.add(0,timestamp + "\n" + action);
    mPttActionsAdapter.notifyDataSetChanged();

  }
}
