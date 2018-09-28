package com.example.fgw.tracker;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TrackerActivity extends Activity {

    private static final int PERMISSIONS_REQUEST = 1;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    public String UserID;
    public String UserIDs;


    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        databaseReference = mDatabase.getReference("users");
        UserID = mAuth.getCurrentUser().getUid();
        databaseReference.child(UserID).child("email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot!=null){
                    UserIDs = dataSnapshot.getValue().toString();
                    Toast.makeText(TrackerActivity.this,UserIDs,Toast.LENGTH_SHORT).show();
                }}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//        Toast.makeText(TrackerActivity.this,UserID,Toast.LENGTH_SHORT).show();
        // Check GPS is enabled
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Check location permission is granted - if it is, start
        // the service, otherwise request the permission
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            startTrackerService();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
        }
    }

    private void startTrackerService() {
        startService(new Intent(this, TrckerService.class));

        Toast.makeText(TrackerActivity.this,UserIDs,Toast.LENGTH_SHORT).show();
//        if(UserIDs=="true" || UserIDs=="false")
//        {
//            startActivity(new Intent(this, MapsForEmergencyVehicles.class));
//            Toast.makeText(TrackerActivity.this,"yeh",Toast.LENGTH_SHORT).show();
//
//            finish();
//        }else {
            startActivity(new Intent(this, MapsActivity.class));
            finish();
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Start the service when the permission is granted
            startTrackerService();
        } else {
            finish();
        }
    }
}
