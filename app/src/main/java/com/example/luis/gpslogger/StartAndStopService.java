package com.example.luis.gpslogger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class StartAndStopService extends AppCompatActivity {

    private Intent intent;
    //private boolean serviceIniciado=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_service);

        intent = new Intent(this, GpsService.class);
        Log.i("sdf",intent.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(intent!=null) //Apenas paramos o serviço se este estiver a ser executado.
        {
            stopService(intent);
        }
    }

    public void iniciarServico(View v)
    {
        if(!GpsService.getServicoIniciado())//Para iniciar só o serviço quando este ainda não foi iniciado.
        {
            startService(intent);
            startActivity(new Intent(StartAndStopService.this, ShowLocationActivity.class));
        }
        else
        {
            Toast.makeText(this,"Serviço já foi iniciado anteriormente",Toast.LENGTH_SHORT).show();
        }
    }

    public void pararServico(View v)
    {
        if(intent!=null)
        {
            stopService(intent);
        }
    }
}
