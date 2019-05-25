package com.example.khuta.englishclub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

public class GrammarActivity extends AppCompatActivity {


    SharedPreferences sharedPreferences;

    private Button button_grammar1;
    private Button button_grammar2;
    private Button button_grammar3;

    private String button_grammar1_text;
    private String button_grammar2_text;
    private String button_grammar3_text;

    private String uri_grammar1;
    private String uri_grammar2;
    private String uri_grammar3;

    private TextView username_textView;
    private TextView points_textView;

    private Player player;
    private String level_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar);

        //getting extras
        Bundle extras = getIntent().getExtras();
        level_name = extras.getString("EXTRA_LEVEL");

        //getting user info from sharedPreferences
        sharedPreferences = getSharedPreferences("UserInfo", 0);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Player", "");
        player = gson.fromJson(json , Player.class);

        //initializing components
        username_textView = findViewById(R.id.username_gram_textview);
        points_textView = findViewById(R.id.points_gram_textView);
        username_textView.setText(player.getName());
        points_textView.setText(String.valueOf(player.getPoints()));
        button_grammar1 = findViewById(R.id.grammar1_button);
        button_grammar2 = findViewById(R.id.grammar2_button);
        button_grammar3 = findViewById(R.id.grammar3_button);

        //change components as level game
        switch_level_game();

        //initializing components
        button_grammar1.setText(button_grammar1_text);
        button_grammar2.setText(button_grammar2_text);
        button_grammar3.setText(button_grammar3_text);


        //buttons clicked
        button_grammar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri_grammar1));
                startActivity(browserIntent);
            }
        });

        button_grammar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri_grammar2));
                startActivity(browserIntent);
            }
        });

        button_grammar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri_grammar3));
                startActivity(browserIntent);
            }
        });
    }

    //change components as level game
    private void switch_level_game(){
        switch (level_name){
            case "Beginners":
                uri_grammar1 = "https://www.talkenglish.com/grammar/grammar.aspx";
                uri_grammar2 = "https://www.englisch-hilfen.de/en/exercises_list/alle_grammar.htm";
                uri_grammar3 = "https://learnenglish.britishcouncil.org/en/english-grammar";
                button_grammar1_text = "בסיס הדקדוק האנגלי";
                button_grammar2_text = "תרגילי דקדוק באנגלית";
                button_grammar3_text = "לימוד אנגלית";
                break;

            case "Basic":
                uri_grammar1 = "https://www.learnamericanenglishonline.com/Links.html";
                uri_grammar2 = "http://www.english-test.net/toefl/";
                uri_grammar3 = "https://learnenglish.britishcouncil.org/en/english-grammar";
                button_grammar1_text = "בסיס הדקדוק האנגלי";
                button_grammar2_text = "מבחן דקדוק באנגלית";
                button_grammar3_text = "לימוד אנגלית";
                break;

            case "Advanced":
                uri_grammar1 = "http://www.advanced-english-grammar.com/";
                uri_grammar2 = "http://www.bbc.co.uk/learningenglish/english/course/towards-advanced/unit-6/session-1";
                uri_grammar3 = "https://learnenglish.britishcouncil.org/en/english-grammar";
                button_grammar1_text = "דקדוק אנגלית למתקדמים";
                button_grammar2_text = "תרגילי דקדוק באנגלית";
                button_grammar3_text = "לימוד אנגלית";
                break;
        }
    }

}
