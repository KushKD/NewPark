package com.example.kushdhawan.mapstest.Activities;

//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.TextView;

//import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kushdhawan.mapstest.Abstract.PermissionUtils;
import com.example.kushdhawan.mapstest.Generic.Custom_Dialog;
import com.example.kushdhawan.mapstest.Model.LatLng_;
import com.example.kushdhawan.mapstest.R;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedTransferQueue;

public class AddParking extends AppCompatActivity implements
        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        LocationListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnInfoWindowLongClickListener,
        GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener{

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private boolean mPermissionDenied = false;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    LatLng latLng;
    Marker currLocationMarker;
    private GoogleMap mMap;
    public TextView tv_latitude,tv_longitude;
    Button addparking_bt , clear;

    /**
     * ListView Data
     */
    private Button btn;
    private ListView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;

    private List<LatLng_> list_latLng = new ArrayList<LatLng_>();
    private List<LatLng> latlongList  = new ArrayList<LatLng>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parking);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        tv_latitude = (TextView)findViewById(R.id.latitudetv);
        tv_longitude = (TextView)findViewById(R.id.longitudetv);
        addparking_bt = (Button)findViewById(R.id.addparking);

        btn = (Button) findViewById(R.id.addBtn);
        clear = (Button) findViewById(R.id.clear);
        list = (ListView) findViewById(R.id.list);
        arrayList = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);
        // Here, you set the data in your ListView
        list.setAdapter(adapter);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clear();
                adapter.notifyDataSetChanged();

                list_latLng.clear();
                mMap.clear();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StringBuilder SB = new StringBuilder();
                if(tv_latitude.getText().length()!= 0 && tv_longitude.getText().length()!=0){
                   SB.append(tv_latitude.getText().toString());
                   SB.append(tv_longitude.getText().toString());

                    arrayList.add(SB.toString());
                            adapter.notifyDataSetChanged();

                            //Adding data to Dynamic List
                    LatLng_ longlat = new LatLng_();
                    longlat.setLatitude(Double.parseDouble(tv_latitude.getText().toString()));
                    longlat.setLongitude(Double.parseDouble(tv_longitude.getText().toString()));

                    list_latLng.add(longlat);
                }else{

                }
                // this line adds the data of your EditText and puts in your array
               // arrayList.add(editTxt.getText().toString());
                // next thing you have to do is check if your adapter has changed

            }
        });
        addparking_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(tv_latitude.getText().length()!= 0 && tv_longitude.getText().length()!=0){

                   // Toast.makeText(AddParking.this,longlat.toString(),Toast.LENGTH_LONG).show();
                    Toast.makeText(AddParking.this,"Successfully saved location for polyline. Size of List is:-  " + list_latLng.size() ,Toast.LENGTH_LONG).show();
//                    Intent i = new Intent(AddParking.this,Add_Parking_Here.class);
//                    i.putExtra("LATITUDE",tv_latitude.getText().toString());
//                    i.putExtra("LONGITUDE",tv_longitude.getText().toString());
//                    startActivity(i);

                    //Draw Polygon TODO
                    for (int i=0; i<list_latLng.size();i++){
                        list_latLng.get(i).getLatitude();
                        list_latLng.get(i).getLongitude();
                        latlongList.add(new LatLng(list_latLng.get(i).getLatitude(),list_latLng.get(i).getLongitude()));

                    }
                    drawPolyLineOnMap(latlongList);
                   // PolygonOptions opts=new PolygonOptions();

//                    for (LatLng location : list) {
//                        opts.add(location);
//                    }



//                                       for (LatLng location : latlongList) {
//                        opts.add(location);
//                    }


                   // Polygon polygon = mMap.addPolygon(opts.strokeColor(Color.RED).fillColor(Color.BLUE));
                    // Add polygons to indicate areas on the map.
//                    Polygon polygon1 = mMap.addPolygon(new PolygonOptions()
//                            .clickable(true)
//                            .add(
//                                    new LatLng(-27.457, 153.040),
//                                    new LatLng(-33.852, 151.211),
//                                    new LatLng(-37.813, 144.962),
//                                    new LatLng(-34.928, 138.599)));
                    // Store a data object with the polygon, used here to indicate an arbitrary type.
                    // polygon.setTag("alpha");
                    // Style the polygon.
                    //stylePolygon(polygon1);


                    // Set listeners for click events.
                   // mMap.setOnPolylineClickListener(this);
                   // mMap.setOnPolygonClickListener(this);


                }else{

                    Custom_Dialog CD  = new Custom_Dialog();
                    CD.showDialog(AddParking.this,"Please go to the settings and  enable your GPS Location.");
                }
            }
        });
    }


    public void drawPolyLineOnMap(List<LatLng> list) {
        Log.e("Draw Line", "Are We here");
//        PolylineOptions polyOptions = new PolylineOptions();
//        polyOptions.color(Color.RED);
//        polyOptions.width(5);
//        polyOptions.addAll(list);

       // mMap.clear();
        Polygon polygon1 = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .addAll(list));
       // mMap.addPolyline(polyOptions);

//        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//        for (LatLng latLng : list) {
//            builder.include(latLng);
//        }
//
//        final LatLngBounds bounds = builder.build();



    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        // mMap.setOnInfoWindowLongClickListener(this);
        try {
            mMap.setMyLocationEnabled(true);
        }catch(SecurityException s){
            // Toast.makeText(getApplicationContext(),"Not Good",Toast.LENGTH_SHORT).show();
        }

        buildGoogleApiClient();

        mGoogleApiClient.connect();

        enableMyLocation();

//        if(latlongList.size()!=0){
//            PolygonOptions polOpt =
//                    new PolygonOptions().addAll(latlongList).strokeColor(Color.RED).fillColor(Color.BLUE);
//
//            Polygon polygon = mMap.addPolygon(polOpt);
//
//
//        }


    }



    protected synchronized void buildGoogleApiClient() {
        // Toast.makeText(this,"buildGoogleApiClient",Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,  android.Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Location mLastLocation = null;
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }catch(SecurityException r){
            //
            //  Toast.makeText(getApplicationContext(),"There is a problem with the GPS device.",Toast.LENGTH_SHORT).show();
        }
        if (mLastLocation != null) {
            //place marker at current position
            //mGoogleMap.clear();
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            // markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            currLocationMarker = mMap.addMarker(markerOptions);
        }
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(150000); //5 seconds
        mLocationRequest.setFastestInterval(100000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }catch(SecurityException s){
            //  Toast.makeText(getApplicationContext(),"Something's not Good.",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this,"onConnectionSuspended",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
//place marker at current position
        //mGoogleMap.clear();
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        //MarkerOptions markerOptions = new MarkerOptions();
        // markerOptions.position(latLng);
        if(latLng!=null){

            //   tv_latitude.setText(Double.toString((location.getLatitude())));
            //  tv_longitude.setText(Double.toString((location.getLongitude())));

            Log.e("Latitude",Double.toString(location.getLatitude()));
            Log.e("Longitude",Double.toString(location.getLongitude()));
            // markerOptions.title("Latitude: \t"+Double.toString(location.getLatitude())+"\n Longitude:-"+ Double.toString(location.getLongitude()));

            //Update TextView
            new update_View_GPS().execute(Double.toString(location.getLatitude()),Double.toString(location.getLongitude()));


        }else{
            tv_latitude.setText("N/A");
            tv_longitude.setText("N/A");
        }
        //
        // markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        //currLocationMarker = mMap.addMarker(markerOptions);


        Location CurrentLocation = new Location("Current Location");
        CurrentLocation.setLatitude(latLng.latitude);
        CurrentLocation.setLongitude(latLng.longitude);


        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(18).build();  //default was 14

        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,"onConnectionFailed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onInfoWindowLongClick(Marker marker) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        if((latLng==null)) {
            Custom_Dialog CD = new Custom_Dialog();
            CD.showDialog(AddParking.this,"Please go to the settings and  enable your GPS Location.");

        }
        return false;
    }

    @Override
    public void onPolygonClick(Polygon polygon) {

    }



    @Override
    public void onPolylineClick(Polyline polyline) {

    }


    public class update_View_GPS extends AsyncTask<String,String,String[]>{

        String[] GPS_ = new String[2]; // <--initialized statement



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tv_latitude.setText("");
            tv_longitude.setText("");
        }

        @Override
        protected String[] doInBackground(String... params) {


            GPS_[0] = params[0];
            GPS_[1] = params[1];

            Log.e("Latitude Async", GPS_[0] );
            Log.e("Longitude Async", GPS_[1] );

            return GPS_;
        }

        @Override
        protected void onPostExecute(String[] s) {
            // super.onPreExecute(s);
            Log.e("Latitude Async Post", s[0] );
            Log.e("Longitude Async Post", s[1] );
            tv_latitude.setText(s[0]);
            tv_longitude.setText(s[1]);

        }
    }
}

