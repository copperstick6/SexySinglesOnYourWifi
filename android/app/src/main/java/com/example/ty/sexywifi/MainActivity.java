package com.example.ty.sexywifi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import static java.lang.System.out;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    CallAPI apicaller;
    String name;
    String sex;
    Location loc;
    String pref;

    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        Button infoBtn = findViewById(R.id.buttonSetInfo);
        Button findMatchBtn = findViewById(R.id.buttonFindMatch);

        findMatchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MatchActivity.class);
                startActivity(i);
            }
        });

        name = "Tin"; sex = "M"; pref = "F";

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

        apicaller = new CallAPI();
        if(loc == null)
            apicaller.execute("add_user", name, sex, "0.0", "0.0", "1000", pref);
        else
            apicaller.execute("add_user", name, sex, loc.getLatitude() + "", loc.getLongitude() + "", "1000", pref);

    }

    //post location to backend
    public void makeUseOfNewLocation(String name, String sex, Location location, String preference){
        out.println("fuck" + location.getLatitude() + ", " + location.getLongitude());
        apicaller = new CallAPI();
        apicaller.execute("update_position", name, location.getLatitude()+"", location.getLongitude()+"");

        out.println("CHANGIN' FUCKIN' SEX");
        apicaller = new CallAPI();
        apicaller.execute("update_sex", name, "F");

        out.println("CHANGIN' FUCKIN' SEX DRIVEEEEE");
        apicaller = new CallAPI();
        apicaller.execute("update_preference", name, "M");

        out.println("POOOPSITION");
        apicaller = new CallAPI();
        apicaller.execute("get_position", name);

        out.println("NEW SEXXXID");
        apicaller = new CallAPI();
        apicaller.execute("update_ssid", name, "69");
    }

}

