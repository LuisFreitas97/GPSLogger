package com.example.luis.gpslogger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by luis on 03-02-2018.
 */

public class DBSqlite extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "myDatabase";
    private static final String TABLE_NAME = "myTable";
    private static final int DATABASE_Version = 1;
    private static final String LONGITUDE="longitude";
    private static final String LATITUDE="latitude";
    private static final String DATAEHORA="dataEhora";
    private static final String ID="ID";
    private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
            " ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+LONGITUDE+" VARCHAR(45) ,"+ LATITUDE+" VARCHAR(45) ," +
            DATAEHORA+" VARCHAR(45) "+");";
    private static final String DROP_TABLE ="DROP TABLE IF EXISTS "+TABLE_NAME;
    private Context context;

    public DBSqlite(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_Version);
        this.context=context;
    }

    //Usado para criar a DB apenas na primeira vez
    public void onCreate(SQLiteDatabase db)
    {
            db.execSQL(CREATE_TABLE);
    }

    //Usado para alterar a estrutura da DB, por exemplo adicionar uma tabela.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

    public long inserirDados(String longitude,String latitude,String dataEhora)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(LONGITUDE, longitude);
        contentValues.put(LATITUDE, latitude);
        contentValues.put(DATAEHORA,dataEhora);
        long id = getWritableDatabase().insert(TABLE_NAME, null , contentValues);
        return id;
    }

    public String buscaDados()
    {
        String[] columns = {ID,LONGITUDE,LATITUDE,DATAEHORA};
        Cursor cursor =getReadableDatabase().query(TABLE_NAME,columns,null,null,null,null,null);

        String buffer="";
        while (cursor.moveToNext())
        {
            int cid =cursor.getInt(cursor.getColumnIndex(ID));
            String longitude =cursor.getString(cursor.getColumnIndex(LONGITUDE));
            String  latitude =cursor.getString(cursor.getColumnIndex(LATITUDE));
            String  dataEhora =cursor.getString(cursor.getColumnIndex(DATAEHORA));
            buffer+=dataEhora;
        }
        return buffer;
    }
}
