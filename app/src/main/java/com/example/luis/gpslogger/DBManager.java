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

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

    public final class DBManager {

        private static final String MODULE = "Db Manager";

        private static final String DATABASE_NAME = "myDatabase.sqlite";
        private static final String TABLE_NAME = "myTable";
        private static final int DATABASE_Version = 1;
        private static final String LONGITUDE="longitude";
        private static final String LATITUDE="latitude";
        private static final String DATAEHORA="dataEhora";
        private static final String ID="ID";
        private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
                " ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+LONGITUDE+" VARCHAR(45) ,"+ LATITUDE+" VARCHAR(45) ," +
                DATAEHORA+" VARCHAR(45) "+");";

        private static SQLiteDatabase db;

        public long last_evt_Inserted;

        public DBManager() {

            File test  = new File(Environment.getExternalStorageDirectory() + "/documents/");
            String[] test2 = test.list();
            Log.i("zxc",test2.toString());
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
                File db_storage_file = new File(Environment.getExternalStorageDirectory(), "/GPSlogger/");
                if (!db_storage_file.exists())
                    if (!db_storage_file.mkdir()) {
                        //Log.e(MODULE,"error creating /touchCloud directory");
                        return;
                    }
                db_storage_file = new File(db_storage_file, DATABASE_NAME);

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
    }
