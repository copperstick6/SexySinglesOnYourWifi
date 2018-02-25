package com.example.ty.sexywifi;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import static java.lang.System.out;

public class MatchActivity extends AppCompatActivity {

    final String TAG = "MATCH";
    RelativeLayout mRelativeLayout;
    LocationManager locationManager;
    CallAPI apicaller;
    String name;
    String sex;
    Location loc;
    String pref;
    String ssid;

    String mMatch = "";
    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        getSupportActionBar().hide();

        //add RelativeLayout
        mRelativeLayout = findViewById(R.id.activity_main_relative_layout);

        //add LayoutParams
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        MatchView match = new MatchView(this);
        match.setLayoutParams(params);
        mRelativeLayout.addView(match);

        name = "Don Degrees"; sex = "M"; pref = "M";

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService (Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo ();
        ssid  = info.getSSID();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                loc = location;
                makeUseOfNewLocation(name, sex, location, pref);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        // Register the listener with the Location Manager to receive location updates
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
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        apicaller = new CallAPI(this);
        if(loc == null)
            apicaller.execute("add_user", name, sex, "0.0", "0.0", ssid, pref);
        else
            apicaller.execute("add_user", name, sex, loc.getLatitude() + "", loc.getLongitude() + "", ssid, pref);
    }

    boolean sexUpdated = false;
    boolean prefUpdated = false;


    public void setSexUpdated(boolean state) {
        sexUpdated = state;
    }

    public void setPrefUpdated(boolean state) {
        prefUpdated = state;
    }

    public void updateDetails() {
        // Send details to server
        Log.d(TAG, "updateDetails()");

        apicaller = new CallAPI(this);
        apicaller.execute("update_sex", name, sex);
        apicaller = new CallAPI(this);
        apicaller.execute("update_preference", name, pref);
    }

    // After information sent to server, get the match
    public void getMatch() {
        if (!sexUpdated || !prefUpdated) return;
        Log.d(TAG, "getMatch()");
        apicaller = new CallAPI(this);
        apicaller.execute("get_match", ssid, name);
    }

    public void setMatchName(String name) {
        if (mMatch.equals("")) {
            System.out.println( " HEY PAPPY UR LVOER IS : " + name);
            mMatch = name;

            getPosition();
        }
    }

    public void setPosition(String res) {
        System.out.println("setPosition: res"  + res);
    }

    // Get your matches position
    public void getPosition() {
        Log.d(TAG, "getPosition() TRACK THAT LADY YOU HOUND DOG");
        apicaller = new CallAPI(this);
        apicaller.execute("get_position", name);
    }

    //post location to backend
    public void makeUseOfNewLocation(String name, String sex, Location location, String preference) {
        // Update location
        Log.d(TAG, "makeUseOfNewLocation()");
        apicaller = new CallAPI(this);
        apicaller.execute("update_position", name, location.getLatitude()+"", location.getLongitude()+"");
        // Update SSID if changed, not gonna be relevant lol
//        apicaller = new CallAPI();
//        apicaller.execute("update_ssid", name, ssid);
    }
}
