package com.example.khuta.englishclub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;


public class LevelActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    private TextView username_textView;
    private TextView points_textView;

    private Button button_basis;
    private Button button_beginners;
    private Button button_advanced;
    private Button  button_logout ,  button_leaderboard;

    private String level_name;
    private Player player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);


        //getting user info from sharedPreferences
        sharedPreferences = getSharedPreferences("UserInfo", 0);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Player", "");
        player = gson.fromJson(json , Player.class);

        //initializing components
        username_textView = findViewById(R.id.username_lev_textview);
        points_textView = findViewById(R.id.points_lev_textView);
        username_textView.setText(player.getName());
        points_textView.setText(String.valueOf(player.getPoints()));
        button_beginners = findViewById(R.id.button_beginners);
        button_basis = findViewById(R.id.button_basis);
        button_advanced = findViewById(R.id.button_advanced);
        button_logout = findViewById(R.id.logout);
        button_leaderboard = findViewById(R.id.leaderboard);

        //buttons clicked
        button_beginners.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                level_name = "Beginners";
                button_level_click(level_name);
            }
        });

        button_basis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                level_name = "Basic";
                button_level_click(level_name);
            }
        });

        button_advanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                level_name = "Advanced";
                button_level_click(level_name);
            }
        });

        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logout();
            }
        });

        button_leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaderboard();
            }
        });
    }


    @Override
    protected void onResume(){
        super.onResume();
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Player", "");
        player = gson.fromJson(json , Player.class);
        points_textView.setText(String.valueOf(player.getPoints()));
    }


    @Override
    public void onBackPressed() {

    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    private void logout(){

        sharedPreferences.edit().clear().commit();
        Intent intent = new Intent(LevelActivity.this , HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void leaderboard(){

        Intent intent = new Intent(LevelActivity.this , LeaderboardActivity.class);
        startActivity(intent);
    }

    //event level button clicked
    private void button_level_click(String level){
        Intent OptionActivity_intent = new Intent(LevelActivity.this , OptionActivity.class);

        Bundle extras = new Bundle();
        extras.putString("EXTRA_LEVEL",level);
        OptionActivity_intent.putExtras(extras);
        startActivity(OptionActivity_intent);
    }


}
