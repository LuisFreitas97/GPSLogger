package com.example.luis.gpslogger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ShowLocationActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_location_activity);
    }

    public void voltar(View v)
    {
        startActivity(new Intent(ShowLocationActivity.this, StartAndStopService.class));
    }
}
