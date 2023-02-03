package com.example.busreachouttracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.busreachouttracking.ReCyclerData.KmsList;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.busreachouttracking.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Collections;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ActivityMapsBinding binding;
    Context context;
    LatLng latLng;
    private GoogleMap googleMap;
    ArrayList<LatLng> latitudePoly = new ArrayList<LatLng>();
    ArrayList<LatLng> arrayListStopsCord = new ArrayList<>();  // Add All Stop Marker.
    Polyline polyline;
    float distMetre = 0, distAverage;
    int busSpeedCount = 0, busSpeed = 0, sumOfSpeed = 0;
    RecyclerView listDistKM;
    int maxSpeed = 0, avgSpeed = 0;
    Marker marker, currentMarker;  // Polyline Marker and second one Current User Location Marker.
    ImageButton btnNext;
    ArrayList<Double> listKM = new ArrayList<>();  // Update KM on ListView.
    ArrayList<Double> busArrivalTime = new ArrayList<>(); // Update arrival bus time.
    ArrayList<Integer> speed = new ArrayList<>();
    String KmUpdate;
    Button btnInOut, btnMaxSpeed, btnDistanceFromBusStop;
    TextView txtSpeed;
    private boolean speedAvgStatus = true, speedStatusForClear = false;
    // Stop Latitude and Longitude.
    double[] stopLat = {18.46766, 18.472547, 18.481437, 18.492358, 18.500799, 18.512430, 18.489592},
            stopLng = {73.86433, 73.863015, 73.861022, 73.857634, 73.856640, 73.843757, 73.810778};
//    double[] stopLat = {18.46766, 18.472547, 18.481437, 18.492358, 18.500799, 18.512430},
//            stopLng = {73.86433, 73.863015, 73.861022, 73.857634, 73.856640, 73.843757};

    ArrayList<Float> totalKmSequence = new ArrayList<>();
    // Polyline Latitude.
    private double lat[] = {18.46766, 18.468148, 18.468965, 18.470715, 18.471750, 18.472547,
            18.472936, 18.474117, 18.475366, 18.476625, 18.477403, 18.478594, 18.479318, 18.480304, 18.481437, 18.482594,
            18.484018, 18.484820, 18.485544, 18.486448, 18.487104, 18.488538, 18.489801, 18.490948, 18.492358, 18.493466,
            18.494530, 18.496197, 18.496634, 18.497709, 18.498841, 18.499604, 18.500100, 18.500381, 18.500576, 18.500799,
            18.501269, 18.501746, 18.502810, 18.504423, 18.505699, 18.506072, 18.507078, 18.507706, 18.508536, 18.509250,
            18.509789, 18.510552, 18.511269, 18.511560, 18.511947, 18.512430, 18.489592};
    // Polyline Longitude.
    private double lng[] = {73.86433, 73.864243, 73.864088, 73.863712, 73.863410, 73.863015,
            73.862831, 73.862569, 73.862405, 73.862277, 73.861985, 73.861580, 73.861488, 73.861304, 73.861022, 73.860402,
            73.859679, 73.859392, 73.858844, 73.858116, 73.857465, 73.857373, 73.857470, 73.857583, 73.857634, 73.857747,
            73.857901, 73.857937, 73.857952, 73.858116, 73.858290, 73.858372, 73.858424, 73.858280, 73.857880, 73.856640,
            73.854289, 73.853871, 73.853769, 73.853607, 73.853590, 73.852666, 73.851375, 73.850517, 73.849303, 73.848025,
            73.847081, 73.846046, 73.845289, 73.844840, 73.844052, 73.843757, 73.810778};
    //    private double lat[] = {18.46766, 18.468148, 18.468965, 18.470715, 18.471750, 18.472547,
//            18.472936, 18.474117, 18.475366, 18.476625, 18.477403, 18.478594, 18.479318, 18.480304, 18.481437, 18.482594,
//            18.484018, 18.484820, 18.485544, 18.486448, 18.487104, 18.488538, 18.489801, 18.490948, 18.492358, 18.493466,
//            18.494530, 18.496197, 18.496634, 18.497709, 18.498841, 18.499604, 18.500100, 18.500381, 18.500576, 18.500799,
//            18.501269, 18.501746, 18.502810, 18.504423, 18.505699, 18.506072, 18.507078, 18.507706, 18.508536, 18.509250,
//            18.509789, 18.510552, 18.511269, 18.511560, 18.511947, 18.512430};
//    // Polyline Longitude.
//    private double lng[] = {73.86433, 73.864243, 73.864088, 73.863712, 73.863410, 73.863015,
//            73.862831, 73.862569, 73.862405, 73.862277, 73.861985, 73.861580, 73.861488, 73.861304, 73.861022, 73.860402,
//            73.859679, 73.859392, 73.858844, 73.858116, 73.857465, 73.857373, 73.857470, 73.857583, 73.857634, 73.857747,
//            73.857901, 73.857937, 73.857952, 73.858116, 73.858290, 73.858372, 73.858424, 73.858280, 73.857880, 73.856640,
//            73.854289, 73.853871, 73.853769, 73.853607, 73.853590, 73.852666, 73.851375, 73.850517, 73.849303, 73.848025,
//            73.847081, 73.846046, 73.845289, 73.844840, 73.844052, 73.843757};
    private boolean lctStatus = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        btnNext = binding.getRoot().findViewById(R.id.btnNext);
        listDistKM = binding.getRoot().findViewById(R.id.listDistKM);
        ImageButton btnNext = binding.getRoot().findViewById(R.id.btnNext);
        btnInOut = binding.getRoot().findViewById(R.id.btnInOut);
        btnMaxSpeed = binding.getRoot().findViewById(R.id.btnMaxSpeed);
        txtSpeed = binding.getRoot().findViewById(R.id.txtSpeed);
        btnDistanceFromBusStop = binding.getRoot().findViewById(R.id.btnDistanceFromBusStop);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsActivity.this, MainActivity.class));
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    private void getCurrentUserLocation() {
        // get the current status 1 location within 2000 milliSecond(2 Second) by this class.
        com.google.android.gms.location.LocationRequest locationRequest = new com.google.android.gms.location.LocationRequest()
                .setInterval(1000)
                .setFastestInterval(1000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null && locationResult.getLastLocation() != null) {
                    double currentLatitude = locationResult.getLastLocation().getLatitude();
                    double currentLongitude = locationResult.getLastLocation().getLongitude();
                    for (Location location : locationResult.getLocations()) {
                        // Show the BusSpeed.
                        busSpeedCount = (int) ((location.getSpeed() * 3600) / 1000);
                        txtSpeed.setText(busSpeedCount + " km/h");
                        speed.add(busSpeedCount);
                    }
                    avgSpeedOfBus(currentLatitude, currentLongitude);
                    currentLocationChange(currentLatitude, currentLongitude);
                    calculateSpeedAndDist(currentLatitude, currentLongitude);
//                    Toast.makeText(getApplicationContext(),currentLatitude+" : "+currentLongitude,Toast.LENGTH_SHORT).show();
                }
            }
        }, Looper.getMainLooper());
    }

    private void currentLocationChange(double currentLatitude, double currentLongitude) {
        latLng = new LatLng(currentLatitude, currentLongitude);
        if (currentMarker != null) {
            currentMarker.remove();
        }
        currentMarker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(currentLatitude, currentLongitude))
                .title("YOU ARE HERE!")
                .icon(bitmapDescriptorVector(getApplicationContext(), R.drawable.currentlocationbus)));
        if (lctStatus != false) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.5f));
            lctStatus = false;
        }

        // Add Stop marker.
        for (int i = 0; i < stopLat.length; i++) {
            arrayListStopsCord.add(i, new LatLng(stopLat[i], stopLng[i]));
            marker = googleMap.addMarker(new MarkerOptions()
                    .position(arrayListStopsCord.get(i))
//                    .title(arrayListStopsName.get(i))
                    .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.bus_route)));  //  R.drawable.busstopicon1_foreground

            //   calculateDist(calculateLat(i,Double.parseDouble(object.getString("latitude"))),Double.parseDouble(object.getString("longitude")));
        }
    }

    private void avgSpeedOfBus(double latitude1, double longitude1) {
        Log.d("speed === ", speed + "");
        aa:
        for (int dksLat = 0; dksLat < stopLat.length; dksLat++) {
            bb:
            for (int dksLng = 0; dksLng < stopLng.length; dksLng++) {

                float[] meterAverageSpeed = new float[1];
                Location.distanceBetween(latitude1, longitude1, stopLat[dksLat], stopLng[dksLng], meterAverageSpeed);
                distAverage = meterAverageSpeed[0];
                if (distAverage < 100) {
//                                Toast.makeText(context, "Less== "+distAverage, Toast.LENGTH_SHORT).show();
                    btnDistanceFromBusStop.setText("Distance_From =" + (int) distAverage);
                    btnDistanceFromBusStop.setBackgroundColor(Color.GREEN);
                    //   Log.d("distMetre =",stopLat[dksLat]+""+ stopLng[dksLng]+""+distAverage+"");
                    if (speedAvgStatus && !speed.isEmpty()) {

                        for (int avgsp = 0; avgsp < speed.size(); avgsp++) // avgp means AverageSpeed.
                        {
                            sumOfSpeed += speed.get(avgsp);
                            //    System.out.println("speed = "+speed.get(avgp));
                        }
                        avgSpeed = sumOfSpeed / speed.size();
//                        if(avgSpeed != 0)
//                        {
//                        }
//                        avgSpeed = Collections.max(speed);
                        speedAvgStatus = false;
                        Log.d("==avgSpeed=======", avgSpeed + " = " + distAverage);
                        //  btnMaxSpeed.setText(avgSpeed+"");
                        break aa;
                    } else {

                        Log.d("Clear = ", "clear");
                        speed.clear();
                        break aa;
                    }
                } else {
                    //  speedAvgStatus = true;
                    btnDistanceFromBusStop.setText("Distance_From =" + (int) distAverage);
                    btnDistanceFromBusStop.setBackgroundColor(Color.RED);
                    if (speed.size() > 10) {
                        speedAvgStatus = true;
                        Log.d("=========abc========", distAverage + "");
                    }
                }
            }
        }
    }

    // it will provide a Speed and Distance.
    public void calculateSpeedAndDist(double latitude, double longitude) {
        if (latitude != 0.0 && longitude != 0.0) {
            if (stopLat != null && stopLng != null) {
                for (int dc = 0; dc < lat.length; dc++) {  // distance.
                    float[] result = new float[1];
                    Location.distanceBetween(latitude, longitude, lat[dc], lng[dc], result);
                    distMetre = result[0];
                    //    System.out.println("Metre  ===== "+distMetre);+
                    double distKM = result[0] / 1000;
//                totalKM += distKM;
                    if (distKM != 0 && totalKmSequence.size() == lat.length) {
                        totalKmSequence.set(dc, distMetre);
                    } else {
                        totalKmSequence.add(dc, distMetre);
                    }

                    for (int dksLat = 0; dksLat < stopLat.length; dksLat++) {
                        for (int dksLng = 0; dksLng < stopLng.length; dksLng++) {
                            // Check bus stop coordinate mach.
                            if (lat[dc] == stopLat[dksLat] && lng[dc] == stopLng[dksLng]) {
                                if (!listKM.isEmpty() && listKM.size() == stopLng.length) {
                                    listKM.set(dksLng, distKM);
                                    //    Log.d("KmUpdate",lat[dc]+","+stopLat[dksLat]+"  :  "+lng[dc]+","+stopLng[dksLng]+" KmUpdate "+listKM.get(dksLng)+"");
                                } else {
                                    listKM.add(dksLng, distKM);
                                }
                            }
                        }
                    }
                }

                // Calculate reach out distance.
                reachOutAlert(totalKmSequence, latitude, longitude);

                for (int i = 0; i < listKM.size(); i++) {

                    if (avgSpeed != 0) {
                        busArrivalTime.set(i, listKM.get(i) / avgSpeed);
                    } else if (busSpeedCount != 0) {
                        busArrivalTime.add(i, listKM.get(i) / busSpeedCount);
                        Log.d("Add", "" + busArrivalTime.get(i));
                    }

                }
                if (avgSpeed != 0) {
                    busSpeed = avgSpeed;
                    btnMaxSpeed.setText(String.valueOf(avgSpeed));
                } else if (busSpeedCount != 0) {
                    busSpeed = busSpeedCount;
                    btnMaxSpeed.setText(String.valueOf(busSpeedCount));
                    Toast.makeText(context, "BusSpeed = " + busSpeedCount, Toast.LENGTH_SHORT).show();
                }


//                for (int i = 0; i < listKM.size(); i++) {
//                    if (!busArrivalTime.isEmpty() && busArrivalTime.size() == listKM.size()) {
//                        if (avgSpeed != 0) {
//                            busArrivalTime.set(i, listKM.get(i) / avgSpeed);
//                            busSpeed = avgSpeed;
//                            btnMaxSpeed.setText(String.valueOf(busSpeed));
//                        } else {
//                            busArrivalTime.set(i, listKM.get(i) / busSpeedCount);
//                            busSpeed = busSpeedCount;
//                            btnMaxSpeed.setText(String.valueOf(busSpeed));
//                        }
//                        //  Toast.makeText(context, " already inti"+time.get(i), Toast.LENGTH_SHORT).show();
//                    } else {
//                        if (busSpeedCount != 0) {
//                            busArrivalTime.add(i, listKM.get(i) / avgSpeed);
//                            busSpeed = busSpeedCount;
//                            btnMaxSpeed.setText(String.valueOf(busSpeedCount));
//                        } else {
//                            busArrivalTime.add(i, listKM.get(i) / busSpeed);
//                            busSpeed = busSpeedCount;
//                            btnMaxSpeed.setText(String.valueOf(busSpeedCount));
//                        }
//                    }
//                }

                listDistKM.setLayoutManager(new LinearLayoutManager(MapsActivity.this));
                listDistKM.setAdapter(new KmsList(context, busArrivalTime, listKM, busSpeed));//, busSpeed
            }
        }
    }

    private void reachOutAlert(ArrayList<Float> totalKmSequence, double latitude, double longitude) {
        Float meterDist = Collections.min(totalKmSequence);

        if (avgSpeed != 0) {
            alertTime(avgSpeed, meterDist);
            //  Log.d("maxSpeed",""+maxSpeed);
        } else if (busSpeedCount != 0) {

            alertTime(busSpeedCount, meterDist);
            //    Log.d("busSpeedCount",""+busSpeedCount);
        }

//        if (meterDist < 200) {
//        //    System.out.println("Meter = " + meterDist);
//            btnInOut.setText("IN");
//            btnMaxSpeed.setBackgroundColor(Color.WHITE);
//        } else {
//            // System.out.println("++++++++++++++++++" + (Collections.min(this.totalKmSequence)*18/5)/maxSpeed);
//            btnInOut.setText("OUT");
//            btnMaxSpeed.setBackgroundColor(Color.BLACK);
//        }
    }

    private void alertTime(int speed, Float meterDist) {
        String totalMeter = String.valueOf(((meterDist * 0.001) / speed) * 60 * 60);
        String[] abc = totalMeter.split("[\\.]");
//            Toast.makeText(context, ""+abc[0], Toast.LENGTH_SHORT).show();
        Log.d("", abc[0]);
        if (Integer.parseInt(abc[0]) < 120) {
//                Toast.makeText(context, "__"+abc[0], Toast.LENGTH_SHORT).show();
            btnInOut.setText("IN = " + (int) (Math.round(meterDist)));
            btnInOut.setBackgroundColor(Color.GREEN);
            //   btnMaxSpeed.setBackgroundColor(Color.WHITE);
        } else {
//                Toast.makeText(context, "+++"+abc[0], Toast.LENGTH_SHORT).show();
            btnInOut.setText("OUT = " + (int) (Math.round(meterDist)));
            //    btnMaxSpeed.setBackgroundColor(Color.BLACK);
            btnInOut.setBackgroundColor(Color.RED);
        }
    }

    // Set icon for current bus Location.
    public static BitmapDescriptor bitmapDescriptorVector(Context Context, int busmovingpoint) {
        Drawable drawable = ContextCompat.getDrawable(Context, busmovingpoint);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    // Set the icon for live location on google map.
    public static BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        Paint color = new Paint();
        color.setTextSize(20);
        color.setColor(Color.RED);
        vectorDrawable.draw(canvas);
        // canvas.drawText("Stop", 3, 40, color);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    private boolean locationPermission() {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION
                    , Manifest.permission.ACCESS_FINE_LOCATION}, 111);
            return true;
        } else {
            return true;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (locationPermission()) {
            getCurrentUserLocation();
            //   accPolylineLocationServer();
            drawPolyline();
        }
    }

    private void drawPolyline() {
        for (int i = 0; i < lng.length; i++) {
            latitudePoly.add(i, new LatLng(lat[i], lng[i]));
        }
        //   System.out.println("poly = " + latitudePoly);
        polyline = googleMap.addPolyline(new PolylineOptions()
//                .add(new LatLng(18.46766,73.86433),new LatLng(18.470715,73.863712))
                .addAll(latitudePoly)
                .color(Color.BLACK)
                .width(12));
    }
}