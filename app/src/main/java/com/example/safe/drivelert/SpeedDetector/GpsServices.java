package com.example.safe.drivelert.SpeedDetector;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.safe.drivelert.FaceTrackerActivity;
import com.example.safe.drivelert.R;
import com.example.safe.drivelert.Utility.Const;
import com.example.safe.drivelert.Utility.TinyDB;

import java.util.Locale;

public class GpsServices extends Service implements LocationListener, GpsStatus.Listener {
    Location lastlocation = new Location("last");
    ContactsContract.Data data;
    double currentLon = 0;
    double currentLat = 0;
    double lastLon = 0;
    double lastLat = 0;
    Notification.Builder builder;
    String currentSpeed;
    PendingIntent contentIntent;
    private NotificationManager manager;
    private LocationManager mLocationManager;

    TinyDB tinyDB;

    @Override
    public void onCreate() {

        tinyDB = new TinyDB(this);
        Intent notificationIntent = new Intent(this, FaceTrackerActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        createNotification(this);
        Toast.makeText(this, "Service is runing", Toast.LENGTH_SHORT).show();

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (mLocationManager.getAllProviders().indexOf(LocationManager.GPS_PROVIDER) >= 0) {
                mLocationManager.addGpsStatusListener(this);
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, this);
            } else {
                Log.w("MainActivity", "No GPS location provider found. GPS data display will not be available.");
            }
        }
    }


    private void createNotification(Context context) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = initChannels(this);
            builder = new Notification.Builder(this, channel.getId());
        } else {
            builder = new Notification.Builder(context);
        }


        builder.setContentTitle(getString(R.string.app_name))
                .setSmallIcon(R.drawable.ic_action_logout)
                .setContentText("Current Speed : " + currentSpeed)
                .setAutoCancel(true)
                .setContentIntent(contentIntent);

        startForeground(R.string.speed_gps, builder.build());

        /*assert getManager() != null;
        getManager().notify(1, builder.build());*/
    }

    public NotificationChannel initChannels(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            return null;
        }
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("default",
                "Channel name",
                NotificationManager.IMPORTANCE_MIN);
        channel.setShowBadge(false);
        channel.setSound(null, null);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        channel.setImportance(NotificationManager.IMPORTANCE_MIN);
        channel.setDescription("Channel description");
        assert notificationManager != null;
        notificationManager.createNotificationChannel(channel);
        return channel;
    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    /* Remove the locationlistener updates when Services is stopped */
    @Override
    public void onDestroy() {
        mLocationManager.removeUpdates(this);
        mLocationManager.removeGpsStatusListener(this);
        stopForeground(true);
    }

    @Override
    public void onGpsStatusChanged(int event) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location.hasSpeed()) {

            currentSpeed = String.format(Locale.ENGLISH, "%.0f", location.getSpeed() * 3.6) /*+ "km/h"*/;

//            if (SharedPreferenceHelper.getInstance().getStringValue(AppConstant.METER_TYPE, "km/h").equals("km/h")) {
//                currentSpeed = String.format(Locale.ENGLISH, "%.0f", location.getSpeed() * 3.6) /*+ "km/h"*/;
//            } else if (SharedPreferenceHelper.getInstance().getStringValue(AppConstant.METER_TYPE, "km/h").equals("mph")) {
//                currentSpeed = String.format(Locale.ENGLISH, "%.0f", location.getSpeed() * 3.6 * 0.62137119)/* + "mi/h"*/;
//            } else {
//                currentSpeed = String.format(Locale.ENGLISH, "%.0f", location.getSpeed() * 3.6 * 0.5399568) /*+ "kn"*/;
//            }


        }
        if (tinyDB.getBoolean(Const.NOTIFICATION_KEY)) {
            createNotification(this);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

}
