package com.example.luis.gpslogger;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by luis on 03-02-2018.
 */

public class DBSqlite extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "myDatabase";    // Database Name
    private static final String TABLE_NAME = "myTable";   // Table Name
    private static final int DATABASE_Version = 1;   // Database Version
    private static final String longitude="longitude";
    private static final String latitude="latitude";
    private static final String dataEhora="dataEhora";
    private static final String ID="ID";
    private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
            " ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+longitude+" DOUBLE ,"+ latitude+" DOUBLE ," +
            dataEhora+" VARCHAR(45) "+");";
    private static final String DROP_TABLE ="DROP TABLE IF EXISTS "+TABLE_NAME;
    private Context context;

    public DBSqlite(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_Version);
        this.context=context;
    }

    public void onCreate(SQLiteDatabase db)
    {
        try
        {
            db.execSQL(CREATE_TABLE);
        }
        catch (Exception e)
        {

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        try
        {
           // Message.message(context,"OnUpgrade");
            db.execSQL(DROP_TABLE);
            onCreate(db);
        }
        catch (Exception e)
        {
            //Message.message(context,""+e);
        }
    }

    public String getLongitude()
    {
        return longitude;
    }
    public String getLatitude()
    {
        return latitude;
    }
    public String getDataEhora()
    {
        return dataEhora;
    }
    public String getTABLE_NAME()
    {
        return TABLE_NAME;
    }
}
