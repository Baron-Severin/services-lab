package com.severin.baron.services_lab;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by mgkan on 2016-08-16.
 */
public class GoogleService extends Service {

  private String TAG = getClass().getCanonicalName();
  public static String EXTRA = "extra";
  public static String ACTIVITY = "activity";
  public static String WEATHER = "weather";
  public static String NEARBY = "nearby";

  Thread weatherThread = new Thread(new Runnable() {
    @Override
    public void run() {

    }
  });

  Thread nearbyThread = new Thread(new Runnable() {
    @Override
    public void run() {

    }
  });

  Thread activityThread = new Thread(new Runnable() {
    @Override
    public void run() {

    }
  });

  @Override
  public int onStartCommand(final Intent intent,int flags, int startId){
    //TODO: I think that I should probably be doing this in bind as I need to notify the main activity when certain things have finished so it can update the textViews
    String s = intent.getStringExtra(EXTRA);
    if (s.equals(WEATHER)) {
      weatherThread.start();
    } else if (s.equals(NEARBY)) {
      nearbyThread.start();
    } else if (s.equals(ACTIVITY)) {
      activityThread.start();
    }

    return 0;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Log.i(TAG, "Service Destroyed");
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
}
