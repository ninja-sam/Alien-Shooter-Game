package com.example.shootaliens;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenu extends AppCompatActivity {

    boolean click = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void startGame(View view) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("new Message", "Play Game");
        startActivity(intent);
        MainMenu.this.finish();
    }

    public void ranking(View view) {

        Intent intent = new Intent(this, GameOver.class);
        intent.putExtra("new Message", "High Score");
        intent.putExtra("VISIBILITY", false);
        startActivity(intent);
        MainMenu.this.finish();
    }

    public void playerData(View view) {

        Intent intent = new Intent(this, PlayerData.class);
        intent.putExtra("new Message", "Player Data");
        startActivity(intent);
        MainMenu.this.finish();
    }

    public void endGame(View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Do you want to exit Game?");
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(click==false) {
                Toast.makeText(this, "Press again to Exit Game", Toast.LENGTH_SHORT).show();
                click = true;
            }
            else {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}