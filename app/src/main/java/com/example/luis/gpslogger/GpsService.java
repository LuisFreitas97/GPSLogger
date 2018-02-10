package com.example.luis.gpslogger;

import android.*;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by luis on 05-02-2018.
 */

public class GpsService extends Service implements LocationListener{

    // Location Variables
    private LocationManager locationManager;
    private final static int DISTANCE_UPDATES = 1;
    private final static int TIME_UPDATES = 5;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private boolean LocationAvailable;

    //private DBSqlite db;
    private DBManager db;
    private static boolean servicoIniciado=false;

    //O serviço chama este método quando outro componente da aplicação inicia o serviço chamando o
    //método StartAndStopService() iniciando o serviço em segundo plano indefinidamente até ser chamado o
    //método stopService() (chamado por outro componente) ou stopSelf() (interrompido pelo próprio serviço)..
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Serviço iniciado.", Toast.LENGTH_LONG).show();
        servicoIniciado=true;
        return super.onStartCommand(intent, flags, startId);
    }

    //O sistema chama este método quando outro componete pretende vincular-se ao serviºo através do
    //método bindService().
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //O sistema chama este método antes de chamar onStartCommand ou onBind para operações de configuração
    //este método é chamado apenas uma vez antes do serviço se iniciar.
    @Override
    public void onCreate() {

        super.onCreate();
        //db=new DBSqlite(this);

        if(!DBManager.databaseExists())
        {
            DBManager.initDatabase();
        }
        else
        {
            Log.i("sdf","Database já existe");
        }
        db =DBManager.getDBManager();

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

    //Este método é usado para destruir o serviço.
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
        Toast.makeText(this,"Serviço parado", Toast.LENGTH_LONG).show();
        servicoIniciado=false;
        locationManager.removeUpdates(this);
    }

    public static boolean getServicoIniciado()
    {
        return servicoIniciado;
    }

    //Implementação da interface LocationService

    public void listar(View v)
    {
        //Toast.makeText(this, db.buscaDados(), Toast.LENGTH_LONG).show();
        //System.out.println(db.buscaDados());
    }
    /**
     * Monitor for location changes
     * @param location holds the new location
     */
    @Override
    public void onLocationChanged(Location location)
    {
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        //long verif=db.inserirDados(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()),date);
        boolean verif=db.insertData(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()),date);
        //location.getAltitude()
        if(verif)
        {
            Toast.makeText(this, "Dados guardados "+date, Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Dados não guardados", Toast.LENGTH_SHORT).show();
        }

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
            Toast.makeText(this, "Desligou GPS, ligue por favor", Toast.LENGTH_LONG).show();
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
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
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
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION))
        {
            Toast.makeText(this, "This app relies on location data for it's main functionality. Please enable GPS data to access all features.", Toast.LENGTH_LONG).show();
        }
        else
        {
            ActivityCompat.requestPermissions((Activity) getApplicationContext(),new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * Monitor for permission changes.
     *
     * @param requestCode passed via PERMISSION_REQUEST_CODE
     * @param permissions list of permissions requested
     * @param grantResults the result of the permissions requested
     */

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
