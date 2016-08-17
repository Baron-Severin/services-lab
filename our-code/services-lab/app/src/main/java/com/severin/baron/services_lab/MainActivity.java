package com.severin.baron.services_lab;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.BeaconStateResult;
import com.google.android.gms.awareness.snapshot.DetectedActivityResult;
import com.google.android.gms.awareness.state.BeaconState;
import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks {
    GoogleApiClient mGoogleApiClient;
    Button activityButton, weatherButton, nearbyButton;
    TextView activityTextView, weatherTextView, nearbyTextView;
    MessageListener mMessageListener;
    List<String> mNearbyMessages;

    public static final int ACTIVITY_CODE = 123;
    public static final int WEATHER_CODE = 128;
    public static final int NEARBY_CODE = 137;

    private String TAG = getClass().getCanonicalName();

    private static List BEACON_TYPE_FILTERS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNearbyMessages = new ArrayList<>();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Awareness.API)
                .addApi(Nearby.MESSAGES_API)
                .build();
        mGoogleApiClient.connect();

        mMessageListener = new MessageListener() {
            @Override
            public void onFound(Message message) {
                Log.i(TAG, "Found a message!");
                String messageAsString = new String(message.getContent());
                mNearbyMessages.add(messageAsString);
                if (mNearbyMessages.size() > 1) {
                    unsubscribe();
                    BEACON_TYPE_FILTERS = Arrays.asList(
                            BeaconState.TypeFilter.with(
                                    //TODO: Put in mNearbyMessage.get(0)
                                    "my.beacon.namespace",
                                    "my-attachment-type"),
                            BeaconState.TypeFilter.with(
                                    //TODO: Put in mNearbyMessage.get(1)
                                    "my.other.namespace",
                                    "my-attachment-type"));
                    //TODO: Uncomment out the below once I've figured out how to add the messages above!
//                    getNearbyPlaces();
                }
                Log.d(TAG, "Found message: " + messageAsString);
            }

            @Override
            public void onLost(Message message) {
                String messageAsString = new String(message.getContent());
                Log.d(TAG, "Lost sight of message: " + messageAsString);
            }
        };

        activityTextView = (TextView) findViewById(R.id.activity_text_view);
        weatherTextView = (TextView) findViewById(R.id.weather_text_view);
        nearbyTextView = (TextView) findViewById(R.id.nearby_text_view);

        activityButton = (Button) findViewById(R.id.activity_button);
        weatherButton = (Button) findViewById(R.id.weather_button);
        nearbyButton = (Button) findViewById(R.id.nearby_button);

        nearbyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Non-service method
                // Note that the getNearby method is called from the listener once two messages have been received
                subscribe();

                //Service method
//                Intent intent = new Intent(MainActivity.this, GoogleService.class);
//                intent.putExtra(GoogleService.EXTRA,GoogleService.NEARBY);
//                startService(intent);
                //TODO: Set nearbyTextView after service ends
            }
        });

        activityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Non-service method
                getActivityType();

                //Service method
//                Intent intent = new Intent(MainActivity.this, GoogleService.class);
//                intent.putExtra(GoogleService.EXTRA,GoogleService.ACTIVITY);
//                startService(intent);
                //TODO: Set activityTextView after service ends
            }
        });

        weatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Non-service method
                getWeather();

                //Service method
//                Intent intent = new Intent(MainActivity.this, GoogleService.class);
//                intent.putExtra(GoogleService.EXTRA,GoogleService.WEATHER);
//                startService(intent);
                //TODO: Set weatherTextView after service ends
            }
        });

    }

    private void subscribe() {
        Log.i(TAG, "Subscribing.");
        SubscribeOptions options = new SubscribeOptions.Builder()
                .build();
        Nearby.Messages.subscribe(mGoogleApiClient, mMessageListener, options);
    }

    private void unsubscribe() {
        Log.i(TAG, "Unsubscribing.");
        Nearby.Messages.unsubscribe(mGoogleApiClient, mMessageListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case NEARBY_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getNearbyPlaces();
                }
                break;
            case ACTIVITY_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getActivityType();
                }
                break;
            case WEATHER_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getWeather();
                }
                break;
        }
    }

    private void getNearbyPlaces(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, NEARBY_CODE);
        }
        Awareness.SnapshotApi.getBeaconState(mGoogleApiClient,BEACON_TYPE_FILTERS)
                .setResultCallback(new ResultCallback<BeaconStateResult>() {
                    @Override
                    public void onResult(@NonNull BeaconStateResult beaconStateResult) {
                        if (!beaconStateResult.getStatus().isSuccess()) {
                            Log.i("MATT-TEST ", "beacon state result unsuccessful");
                            return;
                        }
                        Log.i("MATT-TEST ", "beacon state result successful");
                        BeaconState beacy = beaconStateResult.getBeaconState();
                        List<BeaconState.BeaconInfo> beacyInfo = beacy.getBeaconInfo();
                        int i = beacyInfo.size();

                    }
                });
    }

    private void getActivityType(){
        Awareness.SnapshotApi.getDetectedActivity(mGoogleApiClient)
                .setResultCallback(new ResultCallback<DetectedActivityResult>() {
                    @Override
                    public void onResult(@NonNull DetectedActivityResult detectedActivityResult) {
                        if (!detectedActivityResult.getStatus().isSuccess()) {
                            Log.e("SEVTEST ", "Could not get the current activity.");
                            return;
                        }
                        ActivityRecognitionResult ar = detectedActivityResult.getActivityRecognitionResult();
                        DetectedActivity probableActivity = ar.getMostProbableActivity();
                        Log.i("SEVTEST ", probableActivity.toString());
                        activityTextView.setText(probableActivity.toString());
                    }
                });
    }

    private void getWeather(){
      if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, WEATHER_CODE);
      }else{
        Awareness.SnapshotApi.getWeather(mGoogleApiClient)
        .setResultCallback(new ResultCallback<WeatherResult>() {
          @Override
          public void onResult(@NonNull WeatherResult weatherResult) {
            if (weatherResult.getStatus().isSuccess()) {
              Weather weather = weatherResult.getWeather();

              int[] conditions = weather.getConditions();
              StringBuilder stringBuilder = new StringBuilder();
              if (conditions.length > 0) {
                for (int i = 0; i < conditions.length; i++) {
                  if (i > 0) {
                    stringBuilder.append(", ");
                  }
                  stringBuilder.append(retrieveConditionString(conditions[i]));
                }
              }
              weatherTextView.setText(getString(R.string.text_conditions,
                stringBuilder.toString()));
            }
          }
        });
      }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
  private String retrieveConditionString(int condition) {
    switch (condition) {
      case Weather.CONDITION_CLEAR:
        return getString(R.string.condition_clear);
      case Weather.CONDITION_CLOUDY:
        return getString(R.string.condition_cloudy);
      case Weather.CONDITION_FOGGY:
        return getString(R.string.condition_foggy);
      case Weather.CONDITION_HAZY:
        return getString(R.string.condition_hazy);
      case Weather.CONDITION_ICY:
        return getString(R.string.condition_icy);
      case Weather.CONDITION_RAINY:
        return getString(R.string.condition_rainy);
      case Weather.CONDITION_SNOWY:
        return getString(R.string.condition_snowy);
      case Weather.CONDITION_STORMY:
        return getString(R.string.condition_stormy);
      case Weather.CONDITION_WINDY:
        return getString(R.string.condition_windy);
      default:
        return getString(R.string.condition_unknown);
    }
  }
}
