package com.example.fgw.tracker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = MapsActivity.class.getSimpleName();
    private HashMap<String, Marker> mMarkers = new HashMap<>();
    private GoogleMap mMap;
    private EditText locationsearch,bussnumbersearch;
    private ImageView map,search1,search;
    private Button button;
   public String keyl;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        locationsearch = findViewById(R.id.editText4);
        bussnumbersearch = findViewById(R.id.editText);
        search1 = findViewById(R.id.imageView6);
        search = findViewById(R.id.imageView7);
        mAuth=FirebaseAuth.getInstance();
//        button = findViewById(R.id.button);
        map =findViewById(R.id.imageView5);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                keyl = mAuth.getUid();
//                requestLocationUpdates();
//                subscribeToUpdates();
//            }
//        });
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyl = mAuth.getUid();
                requestLocationUpdates();
                subscribeToUpdates();
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Authenticate with Firebase when the Google map is loaded
        mMap = googleMap;
        try {
            // Customise map styling via JSON file
            boolean success = googleMap.setMapStyle( MapStyleOptions.loadRawResourceStyle( this, R.raw.mapstyles));

            if (!success) {
//                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
//            Log.e(TAG, "Can't find style. Error: ", e);
        }


        mMap.setMaxZoomPreference(16);
//        loginToFirebase();
//        subscribeToUpdates();

    }
    private void requestLocationUpdates(){
        LocationRequest request = new LocationRequest();
        request.setInterval(1000);
        request.setFastestInterval(50);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
//        final String path = getString(R.string.firebase_path) + "/" + keyl;
        final String path = "location" + "/" + keyl;

//        Toast.makeText(getBaseContext(),keyl,Toast.LENGTH_SHORT).show();
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
//                        Log.d(TAG, "location update " + location);
                        ref.setValue(location);
                    }
//                    longitudes.setText( location.getLongitude()+"");
//                    la.setText( location.getLatitude()+"");
                }
            }, null);
        }

    }

//    private void loginToFirebase() {
//        String email = getString(R.string.firebase_email);
//        String password = getString(R.string.firebase_password);
//        // Authenticate with Firebase and subscribe to updates
//        FirebaseAuth.getInstance().signInWithEmailAndPassword(
//                email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    subscribeToUpdates();
//                    Log.d(TAG, "firebase auth success :)");
//                } else {
//                    Log.d(TAG, "firebase auth failed");
//                }
//            }
//        });
//    }

    private void subscribeToUpdates() {
        // Functionality coming next step
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("location");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                setMarker(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                setMarker(dataSnapshot);
//                Toast.makeText(getBaseContext(),dataSnapshot.child("longitude").toString() + dataSnapshot.getKey() + dataSnapshot.child("latitude"),Toast.LENGTH_SHORT).show();
//                Toast.makeText(getBaseContext(),dataSnapshot.toString() ,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                setMarker(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void setMarker(DataSnapshot dataSnapshot) {
        // Functionality coming next step
        String key = dataSnapshot.getKey();
        HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
        double lat = Double.parseDouble(value.get("latitude").toString());
        double lng = Double.parseDouble(value.get("longitude").toString());
        LatLng location = new LatLng(lat, lng);
        if (!mMarkers.containsKey(key)) {
            mMarkers.put(key, mMap.addMarker(new MarkerOptions().title(key + ":" + lat + ":" + lng).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_name)).position(location)));
        } else {
            mMarkers.get(key).setPosition(location);
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : mMarkers.values()) {
            builder.include(marker.getPosition());
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 300));
    }

}