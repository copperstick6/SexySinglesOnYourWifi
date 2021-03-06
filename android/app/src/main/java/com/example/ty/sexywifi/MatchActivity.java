package com.example.ty.sexywifi;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MatchActivity extends AppCompatActivity {

    static final double MEET_THRESHOLD = 10.0;
    final String TAG = "MATCH";
    RelativeLayout mRelativeLayout;
    LocationManager locationManager;
    CallAPI apicaller;
    String mName;
    String mSex;
    String mPref;
    MatchView match;
    Location loc;
    String ssid;

    String mMatch = "";

    boolean isStopped = false;

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
        initProfile();

        match = new MatchView(this);
        match.setLayoutParams(params);
        mRelativeLayout.addView(match);
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService (Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo ();
        ssid  = info.getSSID();

        Button testBtn = findViewById(R.id.testBtn);
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                useFakeDistance = !useFakeDistance;
            }
        });
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                loc = location;
                makeUseOfNewLocation(mName, location);
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
            apicaller.execute("add_user", mName, mSex, "0.0", "0.0", ssid, mPref);
        else
            apicaller.execute("add_user", mName, mSex, loc.getLatitude() + "", loc.getLongitude() + "", ssid, mPref);
    }

    boolean sexUpdated = false;
    boolean prefUpdated = false;

    public void initProfile() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mName = sp.getString("firstname", "NA") + " " + sp.getString("lastname", "NA");
        if (sp.getString("sex","NA").equals("Male"))
            mSex = "M";
        else
            mSex = "F";

        if (sp.getString("sex_preference", "NA").equals("Men"))
            mPref = "M";
        else
            mPref = "F";
    }

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
        apicaller.execute("update_sex", mName, mSex);
        apicaller = new CallAPI(this);
        apicaller.execute("update_preference", mName, mPref);
    }

    // After information sent to server, get the match
    public void getMatch() {
        if (!sexUpdated || !prefUpdated) return;
        Log.d(TAG, "getMatch()");
        apicaller = new CallAPI(this);
        apicaller.execute("get_match", ssid, mName);
    }

    public void setMatchName(String name) {
        if (name == null) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getMatch();
                }
            }, 2000);
        } else if (mMatch.equals("")) {
            mMatch = name;
            match.setMatchName(name.split(" ")[0]);
            getPosition();
        }
    }

    double mLatitude = 0.0;
    double mLongitude = 0.0;
    double matchLatitude = 0.0;
    double matchLongitude = 0.0;
    double fakeDistance = 50.0;

    boolean useFakeDistance = true;

    public void setPosition(String latitude, String longitude) {
        if (latitude != null && longitude != null) {
            matchLatitude = Double.parseDouble(latitude);
            matchLongitude = Double.parseDouble(longitude);

            float[] results = new float[2];
            Location.distanceBetween(mLatitude, mLongitude, matchLatitude, matchLongitude, results);

            System.out.println("latitude distance shit");
            System.out.println(mLatitude + " " + mLongitude);
            System.out.println(matchLatitude + " " + matchLongitude);
//            Toast.makeText(getApplicationContext(), "mLat: " + mLatitude + " mLong: " + mLongitude + " matchLatitude: " + matchLatitude + " matchLongitude: " + matchLongitude, Toast.LENGTH_LONG).show();
            double distance = results[0];
            System.out.println("distance: " + distance);
            if (useFakeDistance) {
                fakeDistance -= 10.0;
                distance = Math.max(fakeDistance, 0.0);
            }

            match.setDistance(distance);

            // distance = 10;
            if (distance <= MEET_THRESHOLD) {
                showMeetView();
            }
        }

        if (!isStopped) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getPosition();
                }
            }, 3000);
        }
    }

    // Get your matches position
    public void getPosition() {
        Log.d(TAG, "getPosition() TRACK THAT LADY YOU HOUND DOG");
        apicaller = new CallAPI(this);
        apicaller.execute("get_position", mMatch);
    }

    //post location to backend
    public void makeUseOfNewLocation(String name, Location location) {
        // Update location
        Log.d(TAG, "makeUseOfNewLocation()");
        apicaller = new CallAPI(this);
        apicaller.execute("update_position", name, location.getLatitude()+"", location.getLongitude()+"");

        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        Log.d(TAG, "mlAtitude: " + mLatitude + " mLongitude: " + mLongitude);
//        Toast.makeText(getApplicationContext(), "mAlatitude: " + mLatitude + " mlongitude: " + mLongitude, Toast.LENGTH_SHORT).show();
    }

    boolean isMeetViewVisible = false;
    private void showMeetView() {
        Log.d(TAG, "showMeetView()");
        if(!isMeetViewVisible && findViewById(android.R.id.content) != null) { // making sure background is displayed
            isMeetViewVisible = true;
            System.out.println( " doing it ");
            // Layouts
            final View root = getLayoutInflater().inflate(R.layout.meet_view, (ViewGroup) findViewById(android.R.id.content)
                    .getRootView(), true);
            final RelativeLayout container = root.findViewById(R.id.notify_layout);

            // Animations for satellite add and remove view
            final Animation alphaIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_in);
            final Animation alphaOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_out);

            TextView textViewName = container.findViewById(R.id.header);
            textViewName.setText(mMatch);
            container.startAnimation(alphaIn); // initial anim in

            // Button set up
            Button button = root.findViewById(R.id.okay_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    container.startAnimation(alphaOut);
                    container.setVisibility(View.GONE);
                    // isMeetViewVisible = false; -- REMOVE to allow prompt to appear again
                }
            });
        }
    }

}
