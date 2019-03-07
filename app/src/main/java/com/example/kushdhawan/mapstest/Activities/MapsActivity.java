package com.example.kushdhawan.mapstest.Activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import com.example.kushdhawan.mapstest.R;
import com.google.android.gms.location.LocationListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.kushdhawan.mapstest.Abstract.PermissionUtils;
import com.example.kushdhawan.mapstest.Generic.Custom_Dialog;
import com.example.kushdhawan.mapstest.Utilities.helper_Functions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;

import java.net.URL;

public class MapsActivity extends AppCompatActivity implements

        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        LocationListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener

{


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private boolean mPermissionDenied = false;
    private int Zoom_Value_Camera = 13;
    private float Straight_Distance = 0;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    LatLng latLng;
    Marker currLocationMarker;
    private GoogleMap mMap;


    ProgressBar pb;
    URL url_;

    float distance =0;

    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int COLOR_WHITE_ARGB = 0xffffffff;
    private static final int COLOR_GREEN_ARGB = 0xff388E3C;
    private static final int COLOR_PURPLE_ARGB = 0xff81C784;
    private static final int COLOR_ORANGE_ARGB = 0xffF57F17;
    private static final int COLOR_BLUE_ARGB = 0xffF9A825;

    private static final int POLYLINE_STROKE_WIDTH_PX = 12;
    private static final int POLYGON_STROKE_WIDTH_PX = 8;
    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final int PATTERN_GAP_LENGTH_PX = 20;
//    private static final PatternItem DOT = new Dot();
//    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
//    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);

    // Create a stroke pattern of a gap followed by a dot.
   // private static final List<PatternItem> PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP, DOT);

    // Create a stroke pattern of a gap followed by a dash.
    //private static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);

    // Create a stroke pattern of a dot followed by a gap, a dash, and another gap.
   // private static final List<PatternItem> PATTERN_POLYGON_BETA =
     //       Arrays.asList(DOT, GAP, DASH, GAP);




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }










    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        mMap.setOnMyLocationButtonClickListener(this);

        // mMap.setOnInfoWindowLongClickListener(this);
        try {
            mMap.setMyLocationEnabled(true);


            mMap.setTrafficEnabled(true);
            // Enable / Disable zooming controls
            mMap.getUiSettings().setZoomControlsEnabled(true);


            // Enable / Disable my location button
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            // Enable / Disable Compass icon
            mMap.getUiSettings().setCompassEnabled(true);

            // Enable / Disable Rotate gesture
            mMap.getUiSettings().setRotateGesturesEnabled(true);

            // Enable / Disable zooming functionality
            mMap.getUiSettings().setZoomGesturesEnabled(true);


            // Add polygons to indicate areas on the map.
            Polygon polygon1 = mMap.addPolygon(new PolygonOptions()
                    .clickable(true)
                    .add(
                            new LatLng(-27.457, 153.040),
                            new LatLng(-33.852, 151.211),
                            new LatLng(-37.813, 144.962),
                            new LatLng(-34.928, 138.599)));
            // Store a data object with the polygon, used here to indicate an arbitrary type.
           // polygon1.setTag("alpha");
            // Style the polygon.
            stylePolygon(polygon1);


            // Set listeners for click events.
            mMap.setOnPolylineClickListener(this);
            mMap.setOnPolygonClickListener(this);


        }catch(SecurityException s){
            // Toast.makeText(getApplicationContext(),"Not Good",Toast.LENGTH_SHORT).show();
        }

        buildGoogleApiClient();

        mGoogleApiClient.connect();

        enableMyLocation();

//        if (isOnline()) {
//            try {
//                Get_Parking_Details GPD = new Get_Parking_Details();
//                GPD.execute(Econstants.URL_GENERIC);
//            }catch(Exception e){
//                Log.e("CAUGHT",e.getMessage().toString());
//            }
//
//        } else {
//            Toast.makeText(this,"Unable to Connect to Internet. Please check your Network connection.", Toast.LENGTH_LONG).show();
//        }
        // Async Task Starts Here


       /* if(mMyMarkersArray.size() > 0) {
            plotMarkers(mMyMarkersArray);
        }else{
            Log.d("List is","Empty");
        }*/



    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        if((latLng==null)) {
            Custom_Dialog CD = new Custom_Dialog();
            CD.showDialog(MapsActivity.this,"Please go to the settings and  enable your GPS Location.");

        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
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
        Toast.makeText(this,"onConnected",Toast.LENGTH_SHORT).show();
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
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,this);
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
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        // markerOptions.title("Current Position");
        // markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        //  currLocationMarker = mMap.addMarker(markerOptions);

        // Toast.makeText(this,"Location Changed",Toast.LENGTH_SHORT).show();

        //Get Distance Grom Delhi
        float Distance_From_Delhi = helper_Functions.distFromDelhi((float)28.6457559,(float)76.8105573,(float)31.0699863,(float)77.1867547);
        Log.e("Distance from Delhi",Float.toString(Distance_From_Delhi/40));
        //Get Distance from Changigarh
        float Distance_From_Changigarh = helper_Functions.distFromChandigarh((float)30.726525,(float)76.6963736,(float)31.0699863,(float)77.1867547);
        Log.e("Distance from Chandi",Float.toString(Distance_From_Changigarh/40));




        //Straight_Distance
        Straight_Distance = helper_Functions.distGeneric((float)31.099992,(float)71.174456,(float)latLng.latitude,(float)latLng.longitude);
        Log.e("Distance from ChotaS",Float.toString(Straight_Distance));

        //  Zoom_Value_Camera = Math.round((600+Straight_Distance)/Straight_Distance);
        //  Log.e("Zoom Value",Integer.toString(Zoom_Value_Camera));

        Location CurrentLocation = new Location("Current Location");
        CurrentLocation.setLatitude(latLng.latitude);
        CurrentLocation.setLongitude(latLng.longitude);

        Location Shimla_Location = new Location("Shimla Location");
        Shimla_Location.setLatitude(31.0782882);
        Shimla_Location.setLongitude(77.1240016);

        distance = CurrentLocation.distanceTo(Shimla_Location) / 1000; // in km

        Log.e("DistanceKM",Float.toString(distance));

        if(distance<=15){
            Zoom_Value_Camera = 12;
        }else if(distance >15 && distance <=60){
            Zoom_Value_Camera =12;
        }else if(distance>60 && distance<=300){
            Zoom_Value_Camera = 12;
        }else{
            Zoom_Value_Camera=2;
        }


        //zoom to current position:
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(Zoom_Value_Camera).build();  //default was 14

        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        //If you only need one location, unregister the listener
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,"onConnectionFailed",Toast.LENGTH_SHORT).show();
    }






    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // MArkerDetails = null;
        // mMarkersHashMap = null;
        // mMyMarkersArray = null;
        // My_Marker_Pojo = null;
        MapsActivity.this.finish();
    }

    @Override
    public void onPolygonClick(Polygon polygon) {
        // Flip the values of the red, green, and blue components of the polygon's color.
        int color = polygon.getStrokeColor() ^ 0x00ffffff;
        polygon.setStrokeColor(color);
        color = polygon.getFillColor() ^ 0x00ffffff;
        polygon.setFillColor(color);

        Toast.makeText(this, "Area type " , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPolylineClick(Polyline polyline) {

    }

    /**
     * Styles the polygon, based on type.
     * @param polygon The polygon object that needs styling.
     */
    private void stylePolygon(Polygon polygon) {
//        String type = "";
//        // Get the data object stored with the polygon.
//        if (polygon.getTag() != null) {
//            type = polygon.getTag().toString();
//        }

       // List<PatternItem> pattern = null;
        int strokeColor = COLOR_BLACK_ARGB;
        int fillColor = COLOR_WHITE_ARGB;

       // switch (type) {
            // If no type is given, allow the API to use the default.
         //   case "alpha":
                // Apply a stroke pattern to render a dashed line, and define colors.
               // pattern = PATTERN_POLYGON_ALPHA;
                strokeColor = COLOR_GREEN_ARGB;
                fillColor = COLOR_PURPLE_ARGB;
           //     break;
           // case "beta":
                // Apply a stroke pattern to render a line of dots and dashes, and define colors.
             //   pattern = PATTERN_POLYGON_BETA;
              //  strokeColor = COLOR_ORANGE_ARGB;
               // fillColor = COLOR_BLUE_ARGB;
              //  break;
        //}

        //polygon.setStrokePattern(pattern);
        polygon.setStrokeWidth(POLYGON_STROKE_WIDTH_PX);
        polygon.setStrokeColor(strokeColor);
        polygon.setFillColor(fillColor);
    }
}
