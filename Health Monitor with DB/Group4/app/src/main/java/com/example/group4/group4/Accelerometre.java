package com.example.group4.group4;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

public class Accelerometre extends Service implements SensorEventListener{

    public float x;
    public float y;
    public float z;
    public long timeStamp;

    public static final String SQL_TABLE_NAME = "tName";
    private SensorManager sensorManager;
    private static final int sec = 1000000;
    private static final String TAG = "Accelerometre";
    private SQLiteDatabase sqLiteDatabase;
    private String mName;

    @Override
    public void onCreate(){
        Log.d(TAG, "onCreate");
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        Log.d(TAG,String.format("%s",SQL_TABLE_NAME));
        mName = intent.getStringExtra(SQL_TABLE_NAME);
        SQLconection sddbHelper = new SQLconection();
        sqLiteDatabase = sddbHelper.getSqLiteDatabase();
        registerSensorListener();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }

    public void registerSensorListener(){
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),sec);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //SensorDate data = new SensorDate();
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            addDBRtask adrcd = new addDBRtask();
            timeStamp = System.currentTimeMillis();
            x = sensorEvent.values[0];
            y = sensorEvent.values[1];
            z = sensorEvent.values[2];
            Log.d(TAG, String.format("New sensor event: %d %f %f %f",
                    System.currentTimeMillis(),
                    sensorEvent.values[0],
                    sensorEvent.values[1],
                    sensorEvent.values[2]));
            adrcd.execute();
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private class addDBRtask extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void...params){
            ContentValues cv = new ContentValues();
            cv.put(SQLconection.SQLSchema.TIME_STAMP, timeStamp);
            cv.put(SQLconection.SQLSchema.X_AXIS, x);
            cv.put(SQLconection.SQLSchema.Y_AXIS, y);
            cv.put(SQLconection.SQLSchema.Z_AXIS, z);
            Log.d(TAG, String.format("Adding record into database: %d %f %f %f", timeStamp, x, y, z));
            sqLiteDatabase.beginTransaction();
            sqLiteDatabase.insert(mName, null, cv);
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }
}
