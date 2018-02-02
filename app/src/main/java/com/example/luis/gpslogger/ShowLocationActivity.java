package com.example.luis.gpslogger;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ShowLocationActivity extends AppCompatActivity implements LocationListener {

    // Location Variables
    private LocationManager locationManager;
    private final static int DISTANCE_UPDATES = 1;
    private final static int TIME_UPDATES = 5;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private boolean LocationAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_location_activity);

        LocationAvailable = false;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (checkPermission())
            {
               // Toast.makeText(this, "GPS ligado, permissão concedida", Toast.LENGTH_LONG).show();
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_UPDATES, DISTANCE_UPDATES, this);
            }
            else
            {
               // Toast.makeText(this, "GPS ligado, permissão não concedida", Toast.LENGTH_LONG).show();
                requestPermission();
            }
    }
    /**
     * Monitor for location changes
     * @param location holds the new location
     */
    @Override
    public void onLocationChanged(Location location)
    {
        TextView latitudeText = (TextView)findViewById(R.id.latitudeText);
        TextView longitudeText = (TextView)findViewById(R.id.longitudeText);
        TextView dateTimeText = (TextView)findViewById(R.id.dateTimeText);

        latitudeText.setText(String.valueOf(location.getLatitude()));
        longitudeText.setText(String.valueOf(location.getLongitude()));
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        dateTimeText.setText(date);
    }

    /**
     * GPS turned off, stop watching for updates.
     * @param provider contains data on which provider was disabled
     */
    @Override
    public void onProviderDisabled(String provider)
    {
        if (checkPermission())
        {
            Toast.makeText(this, "Desligou GPS", Toast.LENGTH_LONG).show();
           // locationManager.removeUpdates(this);
        }
        else
        {
            requestPermission();
        }
    }

    /**
     * GPS turned back on, re-enable monitoring
     * @param provider contains data on which provider was enabled
     */
    @Override
    public void onProviderEnabled(String provider)
    {
        Toast.makeText(this, "Ligou GPS", Toast.LENGTH_LONG).show();
        if (checkPermission())
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_UPDATES, DISTANCE_UPDATES, this);
        }
        else
        {
            requestPermission();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    /**
     * See if we have permission for locations
     *
     * @return boolean, true for good permissions, false means no permission
     */
    private boolean checkPermission()
    {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED)
        {
            LocationAvailable = true;
            return true;
        }
        else
        {
            LocationAvailable = false;
            return false;
        }
    }

    /**
     * Request permissions from the user
     */
    private void requestPermission()
    {

        /**
         * Previous denials will warrant a rationale for the user to help convince them.
         */
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
        {
            Toast.makeText(this, "This app relies on location data for it's main functionality. Please enable GPS data to access all features.", Toast.LENGTH_LONG).show();
        }
        else
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * Monitor for permission changes.
     *
     * @param requestCode passed via PERMISSION_REQUEST_CODE
     * @param permissions list of permissions requested
     * @param grantResults the result of the permissions requested
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    /**
                     * We are good, turn on monitoring
                     */
                    if (checkPermission())
                    {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_UPDATES, DISTANCE_UPDATES, this);
                    }
                    else
                    {
                        requestPermission();
                    }
                }
                else
                {
                    /**
                     * No permissions, block out all activities that require a location to function
                     */
                    Toast.makeText(this, "Permission Not Granted.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
