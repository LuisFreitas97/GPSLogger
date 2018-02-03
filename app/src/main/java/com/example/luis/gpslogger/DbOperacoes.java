package com.example.luis.gpslogger;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by luis on 03-02-2018.
 */

public class DbOperacoes {

    private DBSqlite dbSqlite;

    public DbOperacoes(Context context)
    {
        dbSqlite=new DBSqlite(context);
    }

    public long insertData(double longitude,double latitude,String dataEhora)
    {
        SQLiteDatabase dbb = dbSqlite.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(dbSqlite.getLongitude(), longitude);
        contentValues.put(dbSqlite.getLatitude(), latitude);
        contentValues.put(dbSqlite.getDataEhora(),dataEhora);
        long id = dbb.insert(dbSqlite.getTABLE_NAME(), null , contentValues);
        return id;
    }
}
