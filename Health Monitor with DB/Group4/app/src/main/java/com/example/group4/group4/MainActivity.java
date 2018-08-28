package com.example.group4.group4;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
//import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends Activity implements View.OnClickListener{
    private float[] Value;
    private String[] horizontalmark;
    private String[] VerticalMark;
    private GraphView graphView;
    private final int RECORD = 200;
    private boolean running = false;
    private Handler handler = new Handler();
    private RunnableClass runnable;
    private static final String TAG = "MainActivity";
    private boolean Uploading = false;
    private boolean Downloading = false;
    private SQLconection SQLCONNECTION;
    private EditText text_id;
    private EditText text_age;
    private EditText text_name;
    private String mPatientID;
    private String mPatientAge;
    private String mPatientName;
    private String text_sex;
    private String mTableName= "";
    private RadioGroup text_group;
    private SQLiteDatabase sqLiteDatabase;
    private SQLiteDatabase download_sqLiteDatabase;
    private String sensor_X;
    private String sensor_Y;
    private String sensor_Z;
    private String currentTime;
    private int one=0,two=1,three=2,four=3,five=4,six=5,seven=6,egiht=7,
            nine=8,ten=9;
    private String SEVER_URL = "http://impact.asu.edu/CSE535Spring18Folder/";
    private String UPLOAD_URL = SEVER_URL + "UploadToServer.php";
    private String DOWNLOAD_URL = "http://impact.asu.edu/CSE535Spring18Folder/Group4.db";
    private static final String DOWNLOAD_FILENAME = "Android/Data/CSE535_ASSIGNMENT2_DOWN/Group4.db";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //contentView, GraphView, Layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Value = new float[RECORD];
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.framedraw);
        frameLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        horizontalmark = new String[]{"0", "1", "2", "3", "4","5","6","7","8","9"};
        VerticalMark = new String[]{"10", "5", "0", "-5", "-10"};

       // newGraphView.getViewport().setScalable(true);
        graphView = new GraphView(this, Value, "Group4_Assignment2", horizontalmark, VerticalMark, true);
        frameLayout.addView(graphView);

        //landspace********
        if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
        //*****************

        // Connect listeners
        findViewById(R.id.run).setOnClickListener(this);
        findViewById(R.id.stop).setOnClickListener(this);
        findViewById(R.id.upload).setOnClickListener(this);
        findViewById(R.id.download).setOnClickListener(this);
        //findViewById(R.id.radiogroup).setOnClickListener(this);
        text_id = findViewById(R.id.InputID);
        text_age = findViewById(R.id.InputAge);
        text_name = findViewById(R.id.InputName);
        text_group = findViewById(R.id.radiogroup);
        int radioButtonID = text_group.getCheckedRadioButtonId();
        View radioButton = text_group.findViewById(radioButtonID);
        int idx = text_group.indexOfChild(radioButton);
        RadioButton r = (RadioButton) text_group.getChildAt(idx);
        String selectedtext = r.getText().toString();
        text_sex = selectedtext;
        // databaseContext = new DatabaseContext(this);
        //sqLiteDatabase =
        //SQLconection.
        SQLCONNECTION = new SQLconection();
    }

    private void stopService(){

    }



    @Override
    protected void onDestroy(){
        super.onDestroy();
        stopService();
    }

    private class RunnableClass implements Runnable{
        @Override
        public void run(){
            if(running) new ReadData().execute();
        }

    }

    private static class SensorDate{
        public float x;
        public float y;
        public float z;
    }

    //get or update the new random data
    private void updateAccelerometreData(ArrayList<SensorDate> AccelerometreData) {
        Log.d(TAG,"UpdateAccelerometreData");
        one++;two++;three++;four++;five++;six++;seven++;egiht++;nine++;
        ten++;
        int len = AccelerometreData.size();
        float[] values = new float[len * 3];
        for(int i = 0; i < len; i++){
            SensorDate tmp = AccelerometreData.get(i);
            values[i * 3 ] = (float) tmp.x;
            values[i * 3 + 1] = (float) tmp.y;
            values[i * 3 + 2] = (float) tmp.z;
        }
        Value = values;
    }

    //Handle click events
    //Called when a view has been clicked.
    @Override
    public void onClick(View v){
//        int runID = R.id.run;
//        int stopID = R.id.stop;
        switch (v.getId()){
            case R.id.run:
                RUN();
                break;
            case R.id.stop:
                STOP();
                break;
            case R.id.upload:
                UPLOAD();
                break;
            case R.id.download:
                DOWNLOAD();
                break;

        }
    }

    //Handle run events
    private void RUN(){
        Log.d(TAG,"RUN");
        Log.d(TAG,String.format("Sex: %s",text_sex));
        //test button if work****************
        //TextView mTextView = (TextView) findViewById(R.id.InputID);
        //mTextView.setText("");
        //***********************************
        mPatientID = text_id.getText().toString();
        mPatientAge = text_age.getText().toString();
        mPatientName = text_name.getText().toString();
        String mPatientSex = text_sex==null?"":text_sex;
        if(mPatientID.equals("")){
            Toast.makeText(MainActivity.this,"Please fill patient's ID", Toast.LENGTH_SHORT).show();
            return;
        }else if(mPatientAge.equals("")){
            Toast.makeText(MainActivity.this,"Please fill patient's age", Toast.LENGTH_SHORT).show();
            return;
        }else if(mPatientName.equals("")){
            Toast.makeText(MainActivity.this,"Please fill patient's name", Toast.LENGTH_SHORT).show();
            return;
        }else if(mPatientSex.equals("")){
            Toast.makeText(MainActivity.this,"Please select patient's sex", Toast.LENGTH_SHORT).show();
            return;
        }

        if (running) {
            //stop the pending message in the message queue
//            handler.removeCallbacks(runnable);
//            Value = new float[0];
//            startDataService();
            return;
        }
        PatientInfo patientInfo = new PatientInfo(mPatientName,mPatientSex,mPatientID,mPatientAge);
        String patientTableName = mPatientName+"_"+mPatientID+"_"+mPatientAge+"_"+text_sex;
        SQLCONNECTION.createTables(patientTableName);
        sqLiteDatabase = SQLCONNECTION.getSqLiteDatabase();
        mTableName = patientTableName;
        startAccelerometre();
        runnable = new RunnableClass();
        running = true;
        //talk to main thread
        handler.post(runnable);
        //***********************************
    }

    public String getmTableName(){
        return mPatientName+"_"+mPatientID+"_"+mPatientAge+"_"+text_sex;
    }

    // Handle implements accelerometre sensor
    private void startAccelerometre(){
        Log.d(TAG,"startAccelerometre");
        stoptAccelerometre();
        Intent intent = new Intent(this, Accelerometre.class);
        intent.putExtra(Accelerometre.SQL_TABLE_NAME, mTableName);
        startService(intent);
    }


    //  Handle implements accelerometre sensor
    private void stoptAccelerometre(){
        Intent intent = new Intent(this, Accelerometre.class);
        stopService(intent);
    }


    //Handle stop events
    private void STOP(){
        //test button if work****************
        //TextView mTextView = (TextView) findViewById(R.id.InputID);
        //mTextView.setText("");
        //***********************************
        graphView.setValues(new float[0]);
        graphView.invalidate();
        running = false;

        stopService();

    }


    //this part code is only use to test radio button**************************
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        TextView mTextView = (TextView) findViewById(R.id.InputAge);
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.female:
                if (checked)
                    //mTextView.setText("");
                    text_sex = "Female";
                break;
            case R.id.male:
                if (checked)
                    //mTextView.setText("");
                     text_sex = "Male";
                break;
        }

    }
    //************************************************************************

    public void getSensor_XYZ(){

        sensor_X = SQLconection.SQLSchema.X_AXIS;
        sensor_Y = SQLconection.SQLSchema.Y_AXIS;
        sensor_Z = SQLconection.SQLSchema.Z_AXIS;
        currentTime = SQLconection.SQLSchema.TIME_STAMP;
    }

    //Handle upload data event
    private void UPLOAD() {
        Log.d(TAG, "UPLOAD");

        //if is uploading skip step
        if(Uploading) {
            return;
        }
        mPatientID = text_id.getText().toString();
        mPatientAge = text_age.getText().toString();
        mPatientName = text_name.getText().toString();
        String mPatientSex = text_sex==null?"":text_sex;
        if(mPatientID.equals("")){
            Toast.makeText(MainActivity.this,"Please fill patient's ID", Toast.LENGTH_SHORT).show();
            return;
        }else if(mPatientAge.equals("")){
            Toast.makeText(MainActivity.this,"Please fill patient's age", Toast.LENGTH_SHORT).show();
            return;
        }else if(mPatientName.equals("")){
            Toast.makeText(MainActivity.this,"Please fill patient's name", Toast.LENGTH_SHORT).show();
            return;
        }else if(mPatientSex.equals("")){
            Toast.makeText(MainActivity.this,"Please select patient's sex", Toast.LENGTH_SHORT).show();
            return;
        }

        //Checking if network error
        if(NetWorkConnection.NetWrokConnection(this)==false){
            Log.d(TAG, "Network error");
            Toast.makeText(getApplicationContext(),
                    "Error: Network error", Toast.LENGTH_SHORT).show();
            Uploading = false;
            return;
        }

        // if database hasn't been created yet
        if(!(new File(SQLCONNECTION.getPath())).exists()) {
            Log.e(TAG, "Can't find database");
            Toast.makeText(getApplicationContext(),
                    "Error: Please check if database exist!", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Database has been created");

        //start to upload data
        Toast.makeText(getApplicationContext(), "Starting Upload to Database", Toast.LENGTH_SHORT).show();
        Uploading = true;
        new UploadDatabase().execute(UPLOAD_URL);
    }

    //handle download data event
    private void DOWNLOAD(){
        Log.d(TAG, "DOWNLOAD");
        STOP();
        if(Downloading){
            return;
        }

        //Checking if network error
        if(NetWorkConnection.NetWrokConnection(this)==false){
            Log.d(TAG, "Network error");
            Toast.makeText(getApplicationContext(),
                    "Error: Network error", Toast.LENGTH_SHORT).show();
            return;
        }
        Downloading = true;
        Toast.makeText(getApplicationContext(),"Starting Download from Database",Toast.LENGTH_SHORT).show();
        new DownloadDBTask().execute();
    }

    private Cursor queryDatabase(Cursor cursor){
        cursor =  sqLiteDatabase.query(
                mTableName,
                new String[]{SQLconection.SQLSchema.X_AXIS,
                        SQLconection.SQLSchema.Y_AXIS,
                        SQLconection.SQLSchema.Z_AXIS,
                        SQLconection.SQLSchema.TIME_STAMP},
                null,
                null,
                null,
                null,
                SQLconection.SQLSchema.TIME_STAMP + " DESC",
                "10"
        );
        return cursor;
    }

    // Background task to download the database from server
    private class DownloadDBTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {

        }

        //copy the file from downloaded to local
        public void copy(String src, String dst) throws IOException {
            InputStream in = new FileInputStream(src);
            FileOutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[4096];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }

        }

        @Override
        protected void onPostExecute(Boolean res) {
            if (res) {
                Toast.makeText(getApplicationContext(), "Download compeleted!\n"
                        +Calendar.getInstance().getTime(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Download failed!", Toast.LENGTH_SHORT).show();
            }
            Downloading = false;
            SQLCONNECTION.close();
            //copy to target location
            String downloaded_path = Environment.getExternalStorageDirectory().getPath()+"/"+DOWNLOAD_FILENAME;
            String target_path = SQLCONNECTION.getPath();
            try {
                copy(downloaded_path, target_path);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //re-read updated database
            SQLCONNECTION = new SQLconection();
            mTableName = getmTableName();
            SQLCONNECTION.createTables(mTableName);
            sqLiteDatabase = SQLCONNECTION.getSqLiteDatabase();

            //draw it agian
            new re_updateDatabase().execute();
        }


        // Background task to read and draw the last ten sec
        private class re_updateDatabase extends AsyncTask<Void, Void, ArrayList<SensorDate>> {
            @Override
            protected ArrayList<SensorDate> doInBackground(Void... params) {
                return readRecords();
            }

            //re-set graph
            @Override
            protected void onPostExecute(ArrayList<SensorDate> sensorDatas) {
                updateAccelerometreData(sensorDatas);
                graphView.setValues(Value);
                graphView.invalidate();
                handler.postDelayed(runnable, 1000);
            }

            //read last ten sec from database
            private ArrayList<SensorDate> readRecords() {
                ArrayList<SensorDate> dataRecord = new ArrayList<>(0);
                Cursor cursor = null;
                sqLiteDatabase.beginTransaction();
                cursor = queryDatabase(cursor);
                sqLiteDatabase.setTransactionSuccessful();
                sqLiteDatabase.endTransaction();
                if(cursor == null){
                    return new ArrayList<>(0);
                }
                if(cursor.moveToFirst()){
                    do{
                        SensorDate data = readRecord(cursor);
                        dataRecord.add(0, data);
                    }while (cursor.moveToNext());
                }
                cursor.close();
                return dataRecord;
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {

            boolean result = false;
            try {
                result = doDownloadDB();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        //Handle and HTTP connection
        private HttpURLConnection HTTPresponse () throws Exception {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }});
            return (HttpURLConnection) new URL(DOWNLOAD_URL).openConnection();
        }

        private Boolean doDownloadDB() throws Exception {
            HttpURLConnection connection = null;
            InputStream inputStream = null;
            FileOutputStream fileOutputStream = null;
            try {
                //Open HTTPS connection
                connection = HTTPresponse();
                connection.connect();
                Log.d(TAG, "HTTP Connected!");

                // Check response code from server
                if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                    return false;
                }

                // Download the data
                inputStream = connection.getInputStream();
                fileOutputStream = new FileOutputStream(Environment.getExternalStorageDirectory()+
                        "/"+DOWNLOAD_FILENAME);
                long total = 0;
                byte data[] = new byte[4096];
                int count;
                while ((count = inputStream.read(data)) != -1) {
                    if (isCancelled()) {
                        inputStream.close();
                        return null;
                    }
                    total = total + count;
                    fileOutputStream.write(data, 0, count);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                    if (inputStream != null) inputStream.close();
                    if (fileOutputStream != null) fileOutputStream.close();
                if (connection != null)
                    connection.disconnect();
            }
            return true;
        }
    }

    private InputStream Input(HttpURLConnection connection) throws IOException {
        // Download the file
        return connection.getInputStream();
    }




    //Upload to database
    private class UploadDatabase extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
        }


        @Override
        protected void onPostExecute(Boolean Result) {
            if (Result) {
                Toast.makeText(getApplicationContext(),
                        "Upload data successfully!\n"+ Calendar.getInstance().getTime(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Upload data failed!", Toast.LENGTH_SHORT).show();
            }
            Uploading = false;
        }

        @Override
        protected Boolean doInBackground(String... params) {

            boolean result = false;
            try {
                result = uploadDatabase(UPLOAD_URL);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        //upload local database to server
        //https://developer.android.com/reference/java/io/DataOutputStream
        private boolean uploadDatabase(String urlString) throws IOException {
            final String boundary = "**************";
            FileInputStream fis = new FileInputStream(SQLCONNECTION.getPath());
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            HTTPConnection(connection, boundary);
            DataOutputStream dataOutputStream = new DataOutputStream(
                    connection.getOutputStream());
            writeStart(dataOutputStream, boundary);
            byte[] buffer = new byte[4096];
            int count = 0;
            while ((count = fis.read(buffer)) > 0) {
                dataOutputStream.write(buffer, 0, count);
            }
            writeEnd(dataOutputStream, boundary);
            dataOutputStream.flush();
            dataOutputStream.close();
            final int status = connection.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                return false;
            } else {
                return true;
            }
        }

        //HTTP Connection
        //https://stackoverflow.com/questions/7715224/httpurlconnectionsetreadtimeout-has-no-effect-when-posting-large-messages
        private void HTTPConnection(HttpURLConnection conn, String boundary) throws ProtocolException {
            final int connectTimeout = 50000;
            final int readTimeout = 50000;
            conn.setUseCaches(false);
            conn.setConnectTimeout(connectTimeout);
            conn.setReadTimeout(readTimeout);
            conn.setRequestProperty(
                    "Content-Type",
                    "multipart/form-data;boundary="
                            + boundary);
            conn.setDoInput(true);
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
        }

        private void writeStart(DataOutputStream dataOutputStream, String boundary) throws IOException {
            final String attachmentName = "uploaded_file";
            final String attachmentFileName = SQLCONNECTION.getDB_NAME();
            dataOutputStream.writeBytes("--" + boundary + "\r\n");
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" +
                    attachmentName + "\";filename=\"" +
                    attachmentFileName + "\"\r\n");

            dataOutputStream.writeBytes("\r\n");
        }

        private void writeEnd(DataOutputStream upStream, String boundary) throws IOException {
            upStream.writeBytes("\r\n");
            upStream.writeBytes("--" + boundary + "--\r\n");
            ;
        }
    }



    // Background task to fetch and draw the last ten seconds of samples
    private class ReadData extends AsyncTask<Void, Void, ArrayList<SensorDate>> {
        @Override
        protected ArrayList<SensorDate> doInBackground(Void... params) {
            return readRecords();
        }

        // Redraw
        @Override
        protected void onPostExecute(ArrayList<SensorDate> sensorDatas) {
            updateAccelerometreData(sensorDatas);
            horizontalmark = new String[]{Integer.toString(one), Integer.toString(two),
                    Integer.toString(three), Integer.toString(four), Integer.toString(five),
                    Integer.toString(six),Integer.toString(seven), Integer.toString(egiht),
                    Integer.toString(nine),Integer.toString(ten)};
            graphView.setX_AXIS(horizontalmark);
            graphView.setValues(Value);
            graphView.invalidate();
            handler.postDelayed(runnable, 1000);
        }

        private ArrayList<SensorDate> readRecords() {
            // get the last 10 sec
            sqLiteDatabase.beginTransaction();
            final int FIRST_TEN = 10;
            //long currentTime =  System.currentTimeMillis();
            //final long begin = currentTime - 10 * 100;
            getSensor_XYZ();
            Cursor cursor =  sqLiteDatabase.query(
                    mTableName,
                    new String[]{sensor_X,
                            sensor_Y,
                            sensor_Z,
                            currentTime},
                    null,
                    null,
                    null,
                    null,
                    currentTime+ " DESC",
                    "10"
            );
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();

            if(cursor == null) return new ArrayList<>(0);

            ArrayList<SensorDate> dataRecord = new ArrayList<>(0);
            if(cursor.moveToFirst()){
                do{
                    SensorDate data = readRecord(cursor);
                    dataRecord.add(0, data);
                }while (cursor.moveToNext());
            }
            cursor.close();
            return dataRecord;
        }

    }

    public static SensorDate readRecord(Cursor cursor){
        float x = cursor.getFloat(cursor.getColumnIndex(SQLconection.SQLSchema.X_AXIS));
        float y = cursor.getFloat(cursor.getColumnIndex(SQLconection.SQLSchema.Y_AXIS));
        float z = cursor.getFloat(cursor.getColumnIndex(SQLconection.SQLSchema.Z_AXIS));
        SensorDate data = new SensorDate();
        data.x = x;
        data.y = y;
        data.z = z;
        return data;
    }


}

