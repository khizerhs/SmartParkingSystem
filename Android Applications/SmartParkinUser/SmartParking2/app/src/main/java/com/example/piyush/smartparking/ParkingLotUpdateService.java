package com.example.piyush.smartparking;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by ChenYu Wu on 5/6/2016.
 */
public class ParkingLotUpdateService extends Service {
    private boolean isStopped = false;
    private Looper serviceLooper;
    private ServiceHandler serviceHandler;

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            String url = "http://54.68.124.173:8080/SmartParking/api/user/getAllSensorsByRange";
            String latitude = msg.getData().getString(getString(R.string.bundle_search_latitude));
            String longitude = msg.getData().getString(getString(R.string.bundle_search_longitude));
            String range = msg.getData().getString(getString(R.string.bundle_search_range));

            while (true) {
                try {
                    if (isStopped) {
                        break;
                    }

                    HttpConnectionHelper connectionHelper;
                    connectionHelper = new HttpConnectionHelper(url, "POST", HttpConnectionHelper.DEFAULT_CONNECT_TIME_OUT);
                    connectionHelper.setRequestProperty("Content-type", "application/json");

                    JSONObjectHelper jsonObjectHelper = new JSONObjectHelper();
                    jsonObjectHelper.add(getString(R.string.user_data_location), latitude + "," + longitude);
                    jsonObjectHelper.add(getString(R.string.user_data_range), range);

                    int returnCode = connectionHelper.request_InOutput(HttpConnectionHelper.DEFAULT_READ_TIME_OUT, jsonObjectHelper.getResult());

                    if (HttpURLConnection.HTTP_OK == returnCode) {
                        String parkingLots = connectionHelper.getResponseString();

                        if (!parkingLots.isEmpty()) {
                            Intent intent = new Intent(getString(R.string.bundle_parking_lots_info));
                            intent.putExtra(getString(R.string.bundle_parking_lots_info), parkingLots);
                            LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);
                        }
                    }

                    Thread.sleep(2000);
                } catch (IOException e) {
                    break;
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        this.serviceLooper = thread.getLooper();
        this.serviceHandler = new ServiceHandler(this.serviceLooper);

        Message msg = this.serviceHandler.obtainMessage();
        msg.setData(intent.getExtras());
        this.serviceHandler.sendMessage(msg);

        return START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        isStopped = true;

        super.onDestroy();
    }
}
