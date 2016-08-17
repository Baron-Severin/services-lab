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

  @Override
  public int onStartCommand(final Intent intent,int flags, int startId){
    Thread customThread = new Thread(new Runnable() {
      @Override
      public void run() {
        Log.i(TAG,"Service Started");
        // this is where the real work happens
        try {
            Thread.sleep(1000);
            Log.i(TAG, "Service Running");
        } catch (InterruptedException e){
          e.printStackTrace();
          Thread.currentThread().interrupt();
        }
      }
    });
    customThread.start();
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
