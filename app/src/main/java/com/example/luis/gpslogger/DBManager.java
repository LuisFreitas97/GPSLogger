package com.example.luis.gpslogger;

/**
 * Created by luis on 09-02-2018.
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.games.GamesMetadata;

import java.io.File;
import java.util.ArrayList;

public final class DBManager {

        private static final String MODULE = "Db Manager";

        private static final String DATABASE_NAME = "myDatabase.sqlite";
        private static final String TABLE_NAME = "myTable";
        private static final int DATABASE_Version = 1;
        private static final String LONGITUDE="longitude";
        private static final String LATITUDE="latitude";
        private static final String ALTITUDE="altitude";
        private static final String DATAEHORA="dataEhora";
        private static final String ID="ID";
        private static final String VIAGEMID="viagemId";
        private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
                " ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+LONGITUDE+" VARCHAR(45) ,"+
                LATITUDE+" VARCHAR(45) ," +ALTITUDE+" VARCHAR(45) ,"+
                DATAEHORA+" VARCHAR(45) ,"+ VIAGEMID+" VARCHAR(45) "+");";

        private static SQLiteDatabase db;

        public long last_evt_Inserted;

        public DBManager() {

            //File test  = new File(Environment.getExternalStorageDirectory() + "/documents/");
            //String[] test2 = test.list();

            try {
                if (db == null)
                    db = SQLiteDatabase.openDatabase(
                            Environment.getExternalStorageDirectory() + "/GPSlogger/" + DATABASE_NAME,
                            null,
                            0);
                //Log.i(MODULE,"Db openned");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static class DBHolder {
            public static final DBManager INSTANCE = new DBManager();
        }

        public static DBManager getDBManager() {

            return DBHolder.INSTANCE;
        }

        public static boolean databaseExists() {

            File storage_file = new File(Environment.getExternalStorageDirectory(), "/GPSlogger/" + DATABASE_NAME);

            return storage_file.exists();
        }

        public static void initDatabase() {
            try {
                File db_storage_file = new File(Environment.getExternalStorageDirectory(), "/GPSlogger");
                if (!db_storage_file.exists()) {
                    if (!db_storage_file.mkdir()) {
                        Log.e(MODULE, "error creating /GPSLogger directory");
                        return;
                    }
                    db_storage_file = new File(db_storage_file, DATABASE_NAME);
                }

                if (!db_storage_file.exists()) {
                    db_storage_file.createNewFile();
                    createDatabase(db_storage_file.getAbsolutePath());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private static void createDatabase(String path) {
            //Log.i(MODULE, "Creating new database");
            db = SQLiteDatabase.openDatabase(
                    path,
                    null,
                    SQLiteDatabase.CREATE_IF_NECESSARY);
            // create anchor events table
            db.execSQL(CREATE_TABLE);
        }

        public synchronized boolean insertData(String longitude, String latitude,String altitude, String dataEhora,String viagemId)
        {

            SQLiteStatement stm = null;
            boolean res=false;
            db.beginTransaction();

            try{

            String sql="";
                sql="Insert Into myTable('"+LONGITUDE+"','"+LATITUDE+"','"+ALTITUDE+"','"+DATAEHORA+"','"+VIAGEMID+"') values('"+longitude+"','"+latitude+"','"+altitude+"','"+dataEhora+"','"+viagemId+"')";
                Log.i("sdf",sql);
                stm = db.compileStatement(sql);
                Log.i("sdf",sql);
                if (stm.executeInsert() <= 0)
                {
                    Log.i(MODULE, "Failed insertion of appliance into database");
                }
                res = true;

            db.setTransactionSuccessful();
        } catch (Exception e) {
            res=false;
            e.printStackTrace();
        } finally	{
            stm.close();
            db.endTransaction();
            Log.d(MODULE, "new appliance data inserted");

        }

            return true;
        }

        public static synchronized int getIdViagemAnterior()
        {
            Cursor c;

            c =  db.rawQuery("SELECT max(viagemId) from myTable group by viagemId", null);
            String idViagemAnterior="";
            while(c.moveToNext())
            {
                idViagemAnterior = c.getString(0); //0 é o índice da coluna
            }
            return Integer.parseInt(idViagemAnterior);
        }

        public static synchronized  double calculaKmViagem(int idViagem)
        {
            Cursor c;

            c=db.rawQuery("Select longitude,latitude from myTable where viagemId='"+idViagem+"'",null);

            String longAnterior="",latAnterior="", longSeguinte="",latSeguinte="";
            double kmTotalViagem=0;
            int i=0;

            while(c.moveToNext())
            {
                if(i==0)
                {
                    longAnterior = c.getString(0);
                    latAnterior=c.getString(1);
                    i++;
                }
                else
                {
                    longSeguinte=c.getString(0);
                    latSeguinte=c.getString(1);
                    kmTotalViagem+=GpsService.getDistanceFromLatLonInKm(latAnterior,longAnterior,latSeguinte,longSeguinte);
                    //Log.i("dsf",longAnterior+","+latAnterior+","+longSeguinte+","+latSeguinte);
                    longAnterior=longSeguinte;
                    latAnterior=latSeguinte;
                }
            }
            return kmTotalViagem;
        }
    }
