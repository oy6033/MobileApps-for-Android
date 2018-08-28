package com.example.group4.group4;

import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.widget.FrameLayout;
import java.util.Random;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import android.widget.RadioButton;


public class MainActivity extends Activity implements View.OnClickListener{
    private float[] Value;
    private String[] horizontalmark;
    private String[] VerticalMark;
    private GraphView graphView;
    private final int RECORD = 200;
    private boolean running = false;
    private Handler handler = new Handler();
    private RunnableClass runnable;
    //private int one=2700,two=2750,three=2800,four=2850,five=2900,six=2950,seven=3000,egiht=3050,nine=3100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //contentView, GraphView, Layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Value = new float[RECORD];
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.framedraw);
        frameLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        horizontalmark = new String[]{"2700", "2750", "2800", "2850", "2900","2950","3000", "3050","3100"};
        VerticalMark = new String[]{"2000", "1500", "1000", "500", "0"};
        graphView = new GraphView(this, Value, "CSE535 Health Monitoring", horizontalmark, VerticalMark, true);
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
        //findViewById(R.id.).setOnClickListener(this);

    }

    private class RunnableClass implements Runnable{
        @Override
        public void run(){
            if(running){
                RondomData();
                //UpdateHorizaontalLabel();
                graphView.setValues(Value);

                //graphView.setMark(horizontalmark);
                //update view
                graphView.invalidate();
                //timer and delay
                handler.postDelayed(this,100);
            }
        }

    }

//    private void UpdateHorizaontalLabel(){
//        one = one + 10;
//
//        horizontalmark = new String[]{Integer.toString(one),"1","1","1","1"};
//    }


    //get or update the new random data
    private void RondomData(){
        Random random = new Random();
        float[] newValue = new float[RECORD];
        if(Value.length == 0 || Value==null){
            for(int i = 0 ; i<RECORD-1;i++){
                newValue[i] = random.nextInt(100);
            }

        }else{
            for(int i = 0; i < RECORD - 1; i++){
                newValue[i] = Value[i + 1];
            }
            //update the last data
            newValue[RECORD - 1] = random.nextInt(100);
        }
        Value = newValue;

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

        }
    }

    //Handle run events
    private void RUN(){

        //test button if work****************
        TextView mTextView = (TextView) findViewById(R.id.InputID);
        //mTextView.setText("");
        //***********************************

        if (running) {
            //stop the pending message in the message queue
            handler.removeCallbacks(runnable);
            Value = new float[0];
        }
        runnable = new RunnableClass();
        running = true;
        //talk to main thread
        handler.post(runnable);
        //***********************************
    }

    //Handle stop events
    private void STOP(){
        //test button if work****************
        TextView mTextView = (TextView) findViewById(R.id.InputID);
        //mTextView.setText("");
        //***********************************
        graphView.setValues(new float[0]);
        graphView.invalidate();
        running = false;

    }

    //this part code is only use to test radio button**************************
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        TextView mTextView = (TextView) findViewById(R.id.InputName);
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.female:
                if (checked)
                    //mTextView.setText("");
                break;
            case R.id.male:
                if (checked)
                    //mTextView.setText("");
                break;
        }
    }
    //************************************************************************

}

