package com.example.fgw.tracker;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TrckerService extends Service {

    private static final String TAG = TrckerService.class.getSimpleName();

    @Override
    public IBinder onBind(Intent intent) {return null;}

    @Override
    public void onCreate() {
        super.onCreate();
//        buildNotification();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//        {createNotificationChannel();
//            startActivity(new Intent(this, MapsActivity.class));}
//
//        else {
//            buildNotification();
//            startActivity(new Intent(this, MapsActivity.class));
//                }

//        loginToFirebase();

    }

    private void buildNotification() {
        String stop = "stop";
        registerReceiver(stopReceiver, new IntentFilter(stop));
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);

        // Create the persistent notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notification_text))
                .setOngoing(true)
                .setContentIntent(broadcastIntent)
                .setSmallIcon(R.drawable.ic_stat_name);
        startForeground(1, builder.build());
    }





    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        String stop = "stop";
        registerReceiver(stopReceiver, new IntentFilter(stop));
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        final NotificationManager mNotific=
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        CharSequence name="Tracker";
        String desc=getString(R.string.notification_text);
        int imp=NotificationManager.IMPORTANCE_HIGH;
        final String ChannelID="my_channel_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel mChannel = new NotificationChannel(ChannelID, name,
                    imp);
            mChannel.setDescription(desc);
            mChannel.setLightColor(Color.CYAN);
            mChannel.canShowBadge();
            mChannel.setShowBadge(true);
            mChannel.setName("Tracker");
            mChannel.setLockscreenVisibility(1);


            mNotific.createNotificationChannel(mChannel);
        }

        final int ncode=101;

        String Body="This is testing notific";

        Notification n= new Notification.Builder(this,ChannelID)
                .setContentTitle("Tracker")
                .setContentText(desc)
                .setBadgeIconType(R.mipmap.ic_launcher)
                .setNumber(5)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setAutoCancel(true)
                .build();
        mNotific.notify(ncode, n);


    }


    protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "received stop broadcast");
            // Stop the service when the notification is tapped
            unregisterReceiver(stopReceiver);
            stopSelf();
        }
    };

    private void loginToFirebase() {
        // Functionality coming next step
        String email = getString(R.string.firebase_email);
        String password = getString(R.string.firebase_password);
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
                email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "firebase auth success");
                    requestLocationUpdates();
                } else {
                    Log.d(TAG, "firebase auth failed");
                }
            }
        });
    }

    private void requestLocationUpdates() {
        // Functionality coming next step
        LocationRequest request = new LocationRequest();
        request.setInterval(10000);
        request.setFastestInterval(5000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        final String path = getString(R.string.firebase_path) + "/" + getString(R.string.transport_id);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            // Request location updates and when an update is
            // received, store the location in Firebase
            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        Log.d(TAG, "location update " + location);
                        ref.setValue(location);
                    }
                }
            }, null);
        }
    }

}