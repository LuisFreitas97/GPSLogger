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

        //Para calcular os km de distância entre o ponto de partida e o de chegada.
       /* double d=GpsService.getDistanceFromLatLonInKm("32.67731306","-17.06042571","32.66787549","-17.05150265");
        Toast.makeText(this,Double.toString(d)+"Km",Toast.LENGTH_LONG).show();*/
    }

    @Override
    protected void onDestroy()
    {
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
            //startActivity(new Intent(StartAndStopService.this, ShowLocationActivity.class));
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
            double kmViagem=0;
            //Calcula os kms da última viagem
            //Toast.makeText(this,Integer.toString(GpsService.getIdViagem()),Toast.LENGTH_LONG).show();
            int idViagem=GpsService.getIdViagem();
            kmViagem=DBManager.calculaKmViagem(idViagem);
            stopService(intent);

            //Começamos a nova actividade que irá mostrar os kms, bateria, carregar etc
            Intent intentPrincipal=new Intent(StartAndStopService.this,ShowLocationActivity.class);
            intentPrincipal.putExtra("distanciaKm", kmViagem);
            intentPrincipal.putExtra("idViagem",idViagem);
            startActivity(intentPrincipal);
        }
    }
}
