package com.example.shootaliens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class PlayerData extends AppCompatActivity {

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_data);

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        final ArrayList<ListItem> listItems = new ArrayList<ListItem>();

        RealmResults<Player> results = realm.where(Player.class).findAll();
        for (Player player : results){
            listItems.add(new ListItem(player.getPlayer_name(), player.getPlayer_score()));
        }

        ListItemAdapter adapter = new ListItemAdapter(this, listItems);
        ListView listView = (ListView) findViewById(R.id.player_list);
        listView.setAdapter(adapter);
    }

    public void backToMainMenu(View view) {
        Intent intent = new Intent(this, MainMenu.class);
        intent.putExtra("new Message", "Main Menu");
        startActivity(intent);
        PlayerData.this.finish();
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
        PlayerData.this.finish();
    }
}