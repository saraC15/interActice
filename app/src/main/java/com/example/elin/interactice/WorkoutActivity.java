package com.example.elin.interactice;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class WorkoutActivity extends AppCompatActivity {
    public String level;
    public long workoutTime;
    public final int REQUEST_CODE = 1;
    public boolean timeToRun = true;
    private int nbrOfReps = 10;
    private int activityIndex = 0;
    private MediaPlayer doubletapstart;
    private long totalWorkoutTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        /*final Button button = (Button) findViewById(R.id.startButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                workoutHandler(workoutTime);
            }
        });*/

        doubletapstart=MediaPlayer.create(this, R.raw.startversiontwo);


        Intent intent = getIntent();
        level = intent.getStringExtra("LEVEL");
        workoutTime = Long.valueOf(intent.getStringExtra("TIME"));


        TextView levelField =  (TextView)findViewById(R.id.level);
        TextView timeField =  (TextView)findViewById(R.id.time);
        //final TextView counterField =  (TextView)findViewById(R.id.countdown);

        levelField.setText("Level: " + level);
        timeField.setText("Time: " + workoutTime + " min");

        doubletapstart.start();

        //converting to millisec from min
        //workoutTime = workoutTime*1000*60;
        workoutTime = 2*1000*60;
        totalWorkoutTime = workoutTime;
        /*
        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                counterField.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                counterField.setText("Starting!");
                workoutHandler(workoutTime);
            }
        }.start();
        */


        final GestureDetector gd = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                doubletapstart.release();
                doubletapstart = null;
                workoutHandler(workoutTime);
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);

            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }
        });

        findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return gd.onTouchEvent(event);
            }
        });

    }

    public void workoutHandler(long time){
        if (time > 0) {
            if (timeToRun) {
                timeToRun = false;
                Intent intent = new Intent(this, DistanceActivity.class);

                int distance = 20;
                //int distance = (int) (30 + Math.random() * 30);
                intent.putExtra("DISTANCE", distance);

                startActivityForResult(intent, REQUEST_CODE);
            } else {
                timeToRun = true;

                if(activityIndex == 0){
                    activityIndex = 1;
                    Intent intent = new Intent(this, JumpActivity.class);
                    intent.putExtra("JUMPS", nbrOfReps);
                    startActivityForResult(intent, REQUEST_CODE);
                } else {
                    activityIndex = 0;
                    Intent intent = new Intent(this, PushUpActivity.class);
                    intent.putExtra("REPS", nbrOfReps);
                    startActivityForResult(intent, REQUEST_CODE);
                }
            }
        }
        else {
            Intent intent = new Intent(this, FinishActivity.class);
            intent.putExtra("LEVEL", level);
            intent.putExtra("TIME", totalWorkoutTime);
            startActivity(intent);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                workoutTime -= data.getLongExtra("TIMELEFT", 0);
                Log.wtf("print",Long.toString(workoutTime));
            }
        }
        workoutHandler(workoutTime);
    }
}
