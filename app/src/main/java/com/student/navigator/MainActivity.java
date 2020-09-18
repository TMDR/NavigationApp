package com.student.navigator;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.hardware.SensorEvent;
import android.widget.TextView;
import android.hardware.SensorEventListener;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;


/**
 *
 *
 *
 * the 2 additional features, both in the other activity are:
 *
 * 1-share button, which lets you share certain coordinates, the importance of features is based on the interaction and communication between human beings,
 * as it always was a necessity for survival, being able to share is required everywhere, which led me to the idea of adding this feature
 *
 *
 *
 * 2- the ability to add a custom location you havent been to before, because well obviously, if someone sent you a location, you have to add it,
 * add to that that the idea behind this app is navigation and guidance, so you can open your Google maps, mark the location you want to go to,
 * and easily mount it in the application, that way you wont lose it, and you can always use it.
 *
 *
 *
 *
 */








public class MainActivity extends AppCompatActivity implements Orientation.Listener,SensorEventListener  {
    //get the sensor manager
    private SensorManager SensorManage;
    // define the compass picture that will be use
    private Compass compass;
    static float degree;
    // record the angle turned of the compass picture
    private float DegreeStart = 0f;
    private float DegreeStart0 = 0f;
    TextView compassheading;
    // here is the artificial horizon view
    private Orientation compassorientation;
    private AttitudeIndicator attitudeIndicatorview;
    //GPS which has been done once
    LocationManager locationManager;
    LocationListener locationListener;
    Location mlocation;
    Looper looper;
    Criteria criteria;
    //store and send data between activities
    static ArrayList<String> lattitude = new ArrayList<String>();
    static ArrayList<String> longitude = new ArrayList<String>();
    //used for the waypoint direction
    static boolean LocationSearch = false;
    static Float longtitudeToSearch;
    static Float latitudeToSearch;
    LocationListener locationListener1;
    private waypointind wpind;
    static float anglewp;
    static float anglewpbaring;
    TextView distance;

    /**
     * permissions request code
     */
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;

    /**
     * Permissions that need to be explicitly requested from end user.
     */
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION };
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // compass
        compass = findViewById(R.id.Compass1);
            // TextView that will display the degree
        compassheading =  findViewById(R.id.compassheading);
            // initialize your android device sensor capabilities
        SensorManage = (SensorManager) getSystemService(SENSOR_SERVICE);
        //artificial horizon
        compassorientation = new Orientation(this);
        attitudeIndicatorview = findViewById(R.id.attitude_indicator);
        wpind = findViewById(R.id.wpind);
        distance = findViewById(R.id.distance);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mlocation = location;
                lattitude.add(String.valueOf(location.getLatitude()));
                longitude.add(String.valueOf(location.getLongitude()));
                prefs.savearray("lattitude",lattitude,MainActivity.this);
                prefs.savearray("longitude",longitude,MainActivity.this);
                Toast.makeText(MainActivity.this,"location saved",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        //done to save battery
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
        // initiliazing things
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        looper = null;
        //gets the previously saved list
        lattitude = prefs.getarray("lattitude",this);
        longitude = prefs.getarray("longitude", this);
        if(MainActivity.LocationSearch){
            distance.setVisibility(View.VISIBLE);
        }
        else{
            distance.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        compassorientation.startListening( MainActivity.this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        // to stop the listener
        SensorManage.unregisterListener(this);
        compassorientation.stopListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        SensorManage.unregisterListener(this);
        compassorientation.stopListening();
        try {


            locationManager.removeUpdates(locationListener1);
        }
        catch (Exception e) {}
    }



    @Override
    protected void onResume() {
        super.onResume();
        SensorManage.registerListener(this, SensorManage.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
        //check for waypoint indicator to draw or not
        if (MainActivity.LocationSearch){
             locationListener1 = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                            anglewpbaring =  waypointmaths.bearing( (float) location.getLatitude(),
                            (float) location.getLongitude(),
                            MainActivity.latitudeToSearch,MainActivity.longtitudeToSearch);
                    distance.setText(getString(R.string.distance)+ "" +String.format("%.2f",waypointmaths.distance( (float) location.getLongitude(), (float) location.getLatitude(), MainActivity.longtitudeToSearch, MainActivity.latitudeToSearch)) +"Km");


                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };
             try {


                 locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener1);
             }
             catch (SecurityException e){}

        }
        else{
            distance.setVisibility(View.INVISIBLE);
            wpind.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public void onSensorChanged(SensorEvent sensdegree) {
        // get the heading
        degree = Math.round(sensdegree.values[0]);
       // String heading = getString(R.string.heading);
     //   String degrees = getString(R.string.degrees);
       compassheading.setText(getString(R.string.heading)+degree);
        RotateAnimation ra = new RotateAnimation(
                DegreeStart,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setFillAfter(true);
        ra.setDuration(210);
        // Start animation of compass image
        compass.startAnimation(ra);
        DegreeStart = -degree;

        if (MainActivity.LocationSearch){
            if (anglewpbaring <= degree){
                anglewp = ( -anglewpbaring+degree);
            }
            else
                anglewp = (degree+(360f-anglewpbaring));
            Toast.makeText(MainActivity.this,"result = "+anglewp+" and bearing = "+anglewpbaring,Toast.LENGTH_SHORT).show();
            RotateAnimation ra1 = new RotateAnimation(
                    DegreeStart0,
                    -anglewp,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            ra1.setFillAfter(true);
            ra1.setDuration(210);
            // Start animation of the waypoint direction image
            wpind.startAnimation(ra1);
            DegreeStart0 = -anglewp;
        }

    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }
    @Override
    public void onOrientationChanged(float pitch, float roll) {
        attitudeIndicatorview.setAttitude(pitch, roll);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        //menu for the settings
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()) {
            case R.id.GPSbutton:
                try {
                    locationManager.requestSingleUpdate(criteria, locationListener, looper);
                }

                catch (SecurityException e){}
                return true;
            case R.id.locbutton:
                startActivityForResult(new Intent(this, waypointlist.class),2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2){
            longtitudeToSearch = Float.valueOf(data.getStringExtra("long"));
            latitudeToSearch = Float.valueOf(data.getStringExtra("lat"));
            if(MainActivity.LocationSearch){
                distance.setVisibility(View.VISIBLE);
            }
            else{
                distance.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
