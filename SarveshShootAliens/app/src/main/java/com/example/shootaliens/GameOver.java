package com.example.shootaliens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import io.realm.Realm;

public class GameOver extends AppCompatActivity {

    Realm realm;
    TextView totalTime;
    EditText playerName;
    Button button1, button2;
    int score, elapsedMillis;
    boolean visibility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        TextView activityLabel = findViewById(R.id.tv_game_over);
        TextView scoreLabel =(TextView)findViewById(R.id.tv_score_label);
        TextView loseScoreLabel =(TextView)findViewById(R.id.tv_lose_score_label);
        TextView highScoreLabel =(TextView)findViewById(R.id.tv_high_score);
        totalTime = (TextView) findViewById(R.id.tv_time_label);
        playerName = (EditText) findViewById(R.id.ed_player_name);
        button1 = findViewById(R.id.button1);

        // get lose score i.e number of aliens missed to kill and update the lose score label
        int losescore = getIntent().getIntExtra("SCORELOSE",0);
        loseScoreLabel.setText("You let "+losescore+" aliens go!");

        // get score i.e. number of aliens killed and update the score label
        score = getIntent().getIntExtra("SCORE",0);
        scoreLabel.setText("You Killed: "+score+" Aliens");

        visibility = getIntent().getBooleanExtra("VISIBILITY",true);
        if (visibility == false) {
            activityLabel.setText("High Score");
            scoreLabel.setVisibility(View.INVISIBLE);
            loseScoreLabel.setVisibility(View.INVISIBLE);
            totalTime.setVisibility(View.INVISIBLE);
            playerName.setVisibility(View.INVISIBLE);
            button1.setVisibility(View.GONE);
            visibility = true;
        }

        // get total time elapsed and update the total play duration
        elapsedMillis = getIntent().getIntExtra("TIME", 0);
        int hours = (int) (elapsedMillis / 3600000);
        int minutes = (int) (elapsedMillis - hours * 3600000) / 60000;
        int seconds = (int) (elapsedMillis - hours * 3600000 - minutes * 60000) / 1000;
        totalTime.setText(hours+":"+minutes+":"+seconds);

        // Update the High Score in system using Shared Preferences
        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        int highScore=settings.getInt("HIGH_SCORE",0);

        if(score>highScore) {
            highScoreLabel.setText("High Score: "+score);

            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE",score);

            editor.commit();
        }
        else {
            highScoreLabel.setText("High Score : "+highScore);
        }

        Realm.init(this);
        realm = Realm.getDefaultInstance();
    }

    public void newGame(View view) {
        saveRecord();

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("new Message", "Play Game");
        startActivity(intent);
        GameOver.this.finish();
    }

    public void mainActivity(View view) {
        saveRecord();
        Intent intent = new Intent(this, MainMenu.class);
        intent.putExtra("new Message", "Main Activity");
        startActivity(intent);
        GameOver.this.finish();
    }

    private void saveRecord() {
        visibility = getIntent().getBooleanExtra("VISIBILITY",true);
        if (visibility == false) {
            visibility = true;
        }
        else {
            realm.beginTransaction();
            Player player = realm.createObject(Player.class);
            player.setPlayer_name(playerName.getText().toString());
            player.setPlayer_score(score);
            realm.commitTransaction();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            saveRecord();

            Intent intent = new Intent(this, MainMenu.class);
            intent.putExtra("new Message", "Main Activity");
            startActivity(intent);
            GameOver.this.finish();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}