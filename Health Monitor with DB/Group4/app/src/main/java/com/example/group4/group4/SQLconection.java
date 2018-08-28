package com.example.group4.group4;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class SQLconection {

    private String nameOfTable;
    private String path;
    private SQLiteDatabase sqLiteDatabase;
    private String DB_NAME = "Group4.db";
    private static final String TAG  = "SQLconection";

    public void close(){
        sqLiteDatabase.close();
    }

    public String getPath() {
        return path;
    }

    public String getDB_NAME(){
        return DB_NAME;
    }


    public static class SQLSchema{

        public static final String Auto_Increase_Key = "Key";
        public static final String X_AXIS = "X";
        public static final String Y_AXIS = "Y";
        public static final String Z_AXIS = "Z";
        public static final String TIME_STAMP = "TIMESTAMP";

    }

    public void createTables(String tableName)
    {
        nameOfTable = tableName;
        Log.d(TAG,String.format("Table name: %s",nameOfTable));
        String CMD = "CREATE TABLE IF NOT EXISTS " + tableName + " ( "
                + SQLSchema.Auto_Increase_Key + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SQLSchema.TIME_STAMP + " INTEGER NOT NULL, "
                + SQLSchema.X_AXIS + " REAL NOT NULL, "
                + SQLSchema.Y_AXIS + " REAL NOT NULL, "
                + SQLSchema.Z_AXIS + " REAL NOT NULL " + "); ";

        Log.e(TAG, "Creating Table in datbase " + sqLiteDatabase.toString());
        Log.e(TAG, "SQL Query: " + CMD);
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.execSQL(CMD);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        Log.e(TAG, "Success");
    }

    public SQLconection(){

        String[] sdCardPath=new String[2];
        sdCardPath[0] = "";
        File sdFile=Environment.getExternalStorageDirectory();
        File[] files=sdFile.listFiles();
            for(File file : files){
                Log.e(TAG,String.format("%s",file.getAbsolutePath()));
                if(file.getAbsolutePath().equals(sdFile.getAbsolutePath())){
                    sdCardPath[1]=sdFile.getAbsolutePath();
                }else if(file.getAbsolutePath().contains("sdcard")){
                    sdCardPath[0]=file.getAbsolutePath();
                }
            }
//        Log.e(TAG,String.format("%s",Environment.getRootDirectory()));
//        String x = Environment.getExternalStorageState();
//       Log.e(TAG,String.format("%s",sdCardPath[0]));
        File DB_FILE = Environment.getExternalStorageDirectory();
        String DB_FILE_PATH = DB_FILE+ "/Android/Data/CSE535_ASSIGNMENT2";

        //Android/Data/CSE535_ASSIGNMENT2_DOWN
        ///sdcard/Android/data
        String DB_DOWNLOAD_FILE_PATH = DB_FILE+"/Android/Data/CSE535_ASSIGNMENT2_DOWN";
        path = DB_FILE_PATH +"/" + DB_NAME;

        try
        {
            // Open the database if not create
            Log.e(TAG,"DB Path: " + path);
            new File(DB_FILE_PATH).mkdirs();
            new File(DB_DOWNLOAD_FILE_PATH).mkdir();
            sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(path, null);
            Log.e(TAG, "Successful DB: " + sqLiteDatabase.toString());
        }
        catch (SQLiteException e)
        {
            Log.e(TAG, "error to create table or open database " + e.getMessage(), e);
        }
        finally
        {
        }
    }

    public SQLiteDatabase getSqLiteDatabase() {
        return sqLiteDatabase;
    }
}
