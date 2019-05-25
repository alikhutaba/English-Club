package com.example.khuta.englishclub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class OptionActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    // User
    private TextView username_textView;
    private TextView points_textView;
    private Player player;
    private String level_name;

    // Games Buttons
    private Button write_button;
    private Button vocabulary_button;
    private Button word_button;
    private Button grammar_button;
    private Button assembling_button;
    private Button translate_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);


        //getting extras
        Bundle extras = getIntent().getExtras();
        level_name = extras.getString("EXTRA_LEVEL");

        //getting user info from sharedPreferences
        sharedPreferences = getSharedPreferences("UserInfo", 0);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Player", "");
        player = gson.fromJson(json , Player.class);

        //initializing components
        username_textView = findViewById(R.id.username_opt_textview);
        points_textView = findViewById(R.id.points_opt_textView);
        username_textView.setText(player.getName());
        points_textView.setText(String.valueOf(player.getPoints()));


        //buttons clicked
        write_button = findViewById(R.id.picture_button);
        write_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent write_intent = new Intent(OptionActivity.this, PictureActivity.class);
                Bundle extras = new Bundle();
                extras.putString("EXTRA_LEVEL",level_name);
                write_intent.putExtras(extras);
                startActivity(write_intent);
            }
        });

        vocabulary_button = findViewById(R.id.vocabulary_button);
        vocabulary_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent vocablary_intent = new Intent(OptionActivity.this, VocabularyActivity.class);
                Bundle extras = new Bundle();
                extras.putString("EXTRA_LEVEL",level_name);
                vocablary_intent.putExtras(extras);
                startActivity(vocablary_intent);
            }
        });

        word_button = findViewById(R.id.word_button);
        word_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent word_intent = new Intent(OptionActivity.this, WordActivity.class);
                Bundle extras = new Bundle();
                extras.putString("EXTRA_LEVEL",level_name);
                word_intent.putExtras(extras);
                startActivity(word_intent);
            }
        });

        grammar_button = findViewById(R.id.grammar_button);
        grammar_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent grammar_intent = new Intent(OptionActivity.this, GrammarActivity.class);
                Bundle extras = new Bundle();
                extras.putString("EXTRA_LEVEL",level_name);
                grammar_intent.putExtras(extras);
                startActivity(grammar_intent);
            }
        });

        assembling_button = findViewById(R.id.Assembling_button);
        assembling_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent assembling_intent = new Intent(OptionActivity.this, AssemblingActivity.class);
                Bundle extras = new Bundle();
                extras.putString("EXTRA_LEVEL",level_name);
                assembling_intent.putExtras(extras);
                startActivity(assembling_intent);
            }
        });

        translate_button = findViewById(R.id.Translate_button);
        translate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent translate_intent = new Intent(OptionActivity.this , DictionaryActivity.class);
                startActivity(translate_intent);
            }
        });


    }


    @Override
    protected void onResume(){
        super.onResume();

        String gsonPlayer = sharedPreferences.getString("Player" , null);
        Gson gson = new Gson();
        player = gson.fromJson(gsonPlayer , Player.class);
        points_textView.setText(String.valueOf(player.getPoints()));

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference().child("Users").child(player.getUID());

        myRef.setValue(player);

    }

}
