package com.example.luis.gpslogger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartAndStopService extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_service);
        intent= new Intent(this,GpsService.class);
    }

    public void iniciarServico(View v)
    {
        //Intent intent = new Intent(this, GpsService.class);
        startService(intent);
        startActivity(new Intent(StartAndStopService.this, ShowLocationActivity.class));
    }

    public void pararServico(View v)
    {
        //Intent intent = new Intent(this, GpsService.class);
        stopService(intent);
    }
}
