package com.example.shootaliens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;

    public static int sx = 0;
    public static int sy = 350;

    private SoundPlayer sound;

    long tstart, tend, time=0;
    int score, losescore;
    TextView scoreLabel, losescoreLabel;
    ImageView imageGun, imageGunShot, alien;

    private int alienX, alienY;
    private Handler handler = new Handler();
    private Timer timer = new Timer();

    private int frameHeight;
    private int screenWidth;
    private int screenHeight;
    private TextView startLabel;
    FrameLayout layout;
    private boolean action_flag = false;
    private boolean start_flag = false;
    boolean click = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // code for portrait mode
            setRequestedOrientation(ActivityInfo. SCREEN_ORIENTATION_PORTRAIT);
        } else {
            // code for landscape mode
            setRequestedOrientation(ActivityInfo. SCREEN_ORIENTATION_LANDSCAPE);
        }
        setContentView(R.layout.activity_main);

        imageGun = findViewById(R.id.iv_gun);
        imageGunShot = findViewById(R.id.iv_gunshot);
        layout = findViewById(R.id.frame);
        startLabel = findViewById(R.id.tv_start_label);
        scoreLabel = findViewById(R.id.tv_score_label);
        losescoreLabel = findViewById(R.id.tv_lose_score_label);
        sound = new SoundPlayer(this);
        score = getIntent().getIntExtra("GAME_SCORE", 0);
        losescore = getIntent().getIntExtra("GAME_LOSESCORE", 0);
        time = getIntent().getLongExtra("GAME_TIME", 0L);

        losescoreLabel.setText("You Ran Away: "+losescore+" aliens");
        scoreLabel.setText("SCORE: "+score+" score");


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // on making a gun shot
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                sound.playHitSound();
                com.example.shootaliens.AnimationShot.startAnimationFight(imageGun,imageGunShot);

                return false;
            }
        });

        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        alien=(ImageView)findViewById(R.id.iv_alien);
        alien.setX(-80);
        alien.setY(-80);
        System.out.println("The position x of Alien: "+alienX + " and Y : "+alienY);
    }

    @Override
    // play the activity in Full Screen Mode for better user experience
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    //Set motion sensor to move the gun inside the framelayout
    @Override
    protected void onResume() {
        super.onResume();
        // Register this class as a listener for the accelerometer sensor
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        // ...and the orientation sensor
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    // This method will update the UI on new sensor events
    public void onSensorChanged(SensorEvent event) {

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        int ix = imageGun.getWidth();
        int iy = imageGun.getHeight();

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            int orientation = this.getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                // code for portrait mode
                if (sx < screenWidth - ix / 2) {
                    sx -= (int) event.values[0];
                } else {
                    sx = screenWidth - ix / 2;
                }
                if (sx > -ix / 2) {
                    sx -= (int) event.values[0];
                } else {
                    sx = -ix / 2;
                }

                if (sy < screenHeight - iy / 2) {
                    sy += (int) event.values[1];
                } else {
                    sy = screenHeight - iy / 2;
                }
                if (sy > -iy / 2) {
                    sy += (int) event.values[1];
                } else {
                    sy = -iy / 2;
                }
            } else {
                // code for landscape mode
                if (sx < screenWidth - ix / 2) {
                    sx += (int) event.values[1];
                } else {
                    sx = screenWidth - ix / 2;
                }
                if (sx > -ix / 2) {
                    sx += (int) event.values[1];
                } else {
                    sx = -ix / 2;
                }

                if (sy < screenHeight - iy / 2) {
                    sy += (int) event.values[0];
                } else {
                    sy = screenHeight - iy / 2;
                }
                if (sy > -iy / 2) {
                    sy += (int) event.values[0];
                } else {
                    sy = -iy / 2;
                }
            }
            imageGun.setY(sy);
            imageGun.setX(sx);
            imageGunShot.setY(sy);
            imageGunShot.setX(sx);
        }

        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {

        }
    }

    @Override
    // I've chosen to not implement this method
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void onStop() {
        // Unregister the listener
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    // update score when an alien is shot and start position of next alien
    private void hitCheck() {
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sound.playHitSound();
                com.example.shootaliens.AnimationShot.startAnimationFight(imageGun,imageGunShot);

                int[] aLocation = new int[2];
                alien.getLocationOnScreen(aLocation);

                int[] gLocation = new int[2];
                imageGun.getLocationOnScreen(gLocation);

                if(gLocation[1] > (aLocation[1] - 100) && gLocation[1] < (aLocation[1] + 400)) {
                    score+=1;
                    scoreLabel.setText("You have already Killed: "+score+" Aliens");

                    // alien's new position is ScreenWidth+20 on
                    alienX= screenWidth +20;
                    alienY=(int)Math.floor(Math.random() *(frameHeight-alien.getHeight()));
                }
                return false;
            }
        });
    }

    // increase the speed of alien w.r.t the number of aliens killed
    public void changePos() {
        hitCheck();

        if(score<7) {
            alienX-=15;
        }
        else if(score>=7&&score<13) {
            alienX -= 25;
        }
        else if(score>=13&&score<=17) {
            alienX -= 35;
        }
        else if(score>17&&score<=22) {
            alienX-=45;
        }
        else if(score>22&&score<=25) {
            alienX-=55;
        }
        else if(score>25&&score<=29) {
            alienX-=70;
        }
        else if(score>29) {
            alienX-=85;
        }

        // update the number of aliens missed to shoot and start position af next alien
        if (alienX < 0-alien.getWidth()) {
            losescore+=1;
            losescoreLabel.setText("You ran away: "+losescore+" Aliens");
            alienX = screenWidth + 20;
            alienY = (int) Math.floor(Math.random() * (frameHeight - alien.getHeight()));
        }

        alien.setX(alienX);
        alien.setY(alienY);

        // stop the current activity when more than 5 aliens are missed to shoot
        if(losescore>=5) {

            sound.playOverSound();

            if(timer!=null) {
                timer.cancel();
                timer=null;

                // get the time when the activity is stopped
                tend = System.currentTimeMillis();
            }

            // calculate the play time duration
            time += tend - tstart;
            int millisElapsed = (int) time;

            Intent intent = new Intent(getApplicationContext(), GameOver.class);
            intent.putExtra("SCORE", score);
            intent.putExtra("SCORELOSE", losescore);
            intent.putExtra("TIME", millisElapsed);
            startActivity(intent);
            MainActivity.this.finish();
        }
    }

    // start the game animation when the gun is fired
    public boolean onTouchEvent(MotionEvent me) {
        if(start_flag==false) {
            start_flag=true;

            // get the time when game starts
            tstart = System.currentTimeMillis();

            // Remove the text which give hints to start the game
            startLabel.setVisibility(View.GONE);

            FrameLayout frame =(FrameLayout)findViewById(R.id.frame);
            frameHeight=frame.getHeight();

            // update speed of alien
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            },0,20);
        }
        else {
            if(me.getAction()==MotionEvent.ACTION_DOWN) {
                action_flag=true;
            }
            else if(me.getAction()==MotionEvent.ACTION_UP) {
                action_flag=false;
            }
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            tend = System.currentTimeMillis();
        }

        startLabel = findViewById(R.id.tv_start_label);
        startLabel.setText("Fire to Resume Game");
        startLabel.setVisibility(View.VISIBLE);

        if (start_flag==false) {
            Intent intent = new Intent(getApplicationContext(), MainMenu.class);
            startActivity(intent);
            MainActivity.this.finish();
        }
        else {
            FrameLayout frameLayout = findViewById(R.id.frame);

            // inflate the layout of the popup window
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup_window, null);

            // create the popup window
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true; // lets taps outside the popup also dismiss it
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

            if (click) {
                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window token
                popupWindow.showAtLocation(this.layout, Gravity.CENTER, 0, 10);
                click = false;
            } else {
                popupWindow.dismiss();
                popupResumeGame();
                click = true;
            }

            // dismiss the popup window when touched
            frameLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popupWindow.dismiss();
                    popupResumeGame();
                    click = true;
                    return true;
                }
            });
        }
    }

    private void popupResumeGame() {

        time += tend - tstart;

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("GAME_SCORE", score);
        intent.putExtra("GAME_LOSESCORE", losescore);
        intent.putExtra("GAME_TIME", time);
        startActivity(intent);
        MainActivity.this.finish();
    }

    public void resumeGameButton(View view) {

        time += tend - tstart;

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("GAME_SCORE", score);
        intent.putExtra("GAME_LOSESCORE", losescore);
        intent.putExtra("GAME_TIME", time);
        startActivity(intent);
        MainActivity.this.finish();
    }

    public void forceEndGame(View view) {

        sound.playOverSound();

        // calculate the play time duration
        time += tend - tstart;
        int millisElapsed = (int) time;

        Intent intent = new Intent(getApplicationContext(), GameOver.class);
        intent.putExtra("SCORE", score);
        intent.putExtra("SCORELOSE", losescore);
        intent.putExtra("TIME", millisElapsed);
        startActivity(intent);
        MainActivity.this.finish();
    }

    public void mainMenuButton(View view) {

        Intent intent = new Intent(getApplicationContext(), MainMenu.class);
        startActivity(intent);
        MainActivity.this.finish();
    }
}