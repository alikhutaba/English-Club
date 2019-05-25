package com.example.khuta.englishclub;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Random;


public class VocabularyActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    DatabaseReference mDatabase;
    final Handler handler_e = new Handler();

    private ArrayList<String> words_list = new ArrayList<>();
    private ArrayList<String> meanings_list = new ArrayList<>();

    private Random rand = new Random();
    private int current_question_index = 0;

    private String word;
    private String option_1;
    private String option_2;
    private String option_3;
    private String option_4;
    private Button option1_button;
    private Button option2_button;
    private Button option3_button;
    private Button option4_button;

    private ImageView true_imageView;
    private ImageView false_imageView;

    private TextView word_textView;
    private TextView points_textView;
    private TextView username_display;

    private int correct_answer_mum;
    private Player player;
    private String level_name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);

        //getting extras
        Bundle extras = getIntent().getExtras();
        level_name = extras.getString("EXTRA_LEVEL");

        //getting user info from sharedPreferences
        sharedPreferences = getSharedPreferences("UserInfo", 0);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Player", "");
        player = gson.fromJson(json , Player.class);

        current_question_index_sh();

        //initializing components
        option1_button = findViewById(R.id.button_option1);
        option2_button = findViewById(R.id.button_option2);
        option3_button = findViewById(R.id.button_option3);
        option4_button = findViewById(R.id.button_option4);

        //initializing components
        true_imageView = findViewById(R.id.true_imageView);
        false_imageView = findViewById(R.id.false_imageView2);
        word_textView = findViewById(R.id.word_textView);
        points_textView = findViewById(R.id.points_voc_textView);
        username_display = findViewById(R.id.username_voc_textview);
        username_display.setText(player.getName());
        points_textView.setText(String.valueOf(player.getPoints()));

        //hiding answer message
        true_imageView.setVisibility(View.INVISIBLE);
        false_imageView.setVisibility(View.INVISIBLE);

        //get all words from firebase by name of level
        get_words_from_server();


        //buttons clicked
        option1_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_answer(1);
            }
        });

        option2_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_answer(2);
            }
        });

        option3_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_answer(3);
            }
        });

        option4_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_answer(4);
            }
        });
    }


    //event stop activity
    @Override
    protected void onStop(){
        super.onStop();
        commit_user_info();
    }


    //event back clicked
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        commit_user_info();
    }


    //create new question
    private void new_question(){
        if(words_list.size() == 0)
            return;

        word = words_list.get(current_question_index);

        correct_answer_mum = rand.nextInt(4) + 1;

        fill_answers_in_options();

        option1_button.setText(option_1);
        option2_button.setText(option_2);
        option3_button.setText(option_3);
        option4_button.setText(option_4);
        word_textView.setText(word);
    }


    //fill answers in all options buttons
    private void fill_answers_in_options(){
        int index1, index2, index3;
        int num_of_words = meanings_list.size();
        if(num_of_words < 5)
            return;

        index1 = rand.nextInt(num_of_words);
        while (index1 == current_question_index)
            index1 = rand.nextInt(num_of_words);

        index2 = rand.nextInt(num_of_words);
        while (index1 == index2 || index2 == current_question_index)
            index2 = rand.nextInt(num_of_words);

        index3 = rand.nextInt(num_of_words);
        while (index1 == index3 || index2 == index3 || index3 == current_question_index)
            index3 = rand.nextInt(num_of_words);

        switch (correct_answer_mum){
            case 1:
                option_1 = meanings_list.get(current_question_index);
                option_2 = meanings_list.get(index1);
                option_3 = meanings_list.get(index2);
                option_4 = meanings_list.get(index3);
                break;
            case 2:
                option_1 = meanings_list.get(index1);
                option_2 = meanings_list.get(current_question_index);
                option_3 = meanings_list.get(index2);
                option_4 = meanings_list.get(index3);
                break;
            case 3:
                option_1 = meanings_list.get(index1);
                option_2 = meanings_list.get(index2);
                option_3 = meanings_list.get(current_question_index);
                option_4 = meanings_list.get(index3);
                break;
            case 4:
                option_1 = meanings_list.get(index1);
                option_2 = meanings_list.get(index2);
                option_3 = meanings_list.get(index3);
                option_4 = meanings_list.get(current_question_index);
                break;
        }
    }


    //check answer is correct
    private void check_answer(int num_answer_selected){
        if(num_answer_selected == correct_answer_mum) {
            player.setPoints(player.getPoints()+1);
            points_textView.setText(String.valueOf(player.getPoints()));
            true_imageView.setVisibility(View.VISIBLE);
            handler_e.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do this after 1000ms=1sec
                    true_imageView.setVisibility(View.INVISIBLE);
                    current_question_index++;
                    if(current_question_index >= words_list.size())
                        current_question_index = 0;

                    new_question();
                }
            }, 1000);
        }
        else
        {
            rotate_true_answer();
            false_imageView.setVisibility(View.VISIBLE);
            handler_e.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do this after 1000ms=1sec
                    false_imageView.setVisibility(View.INVISIBLE);
                    current_question_index++;
                    if(current_question_index >= words_list.size())
                        current_question_index = 0;

                    new_question();
                }
            }, 2000);
        }
    }


    //get all words from firebase server by level name
    private void get_words_from_server(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("words").child(level_name);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot wordSnapshot : dataSnapshot.getChildren()){
                    String word = wordSnapshot.getKey();
                    String meaning = wordSnapshot.getValue(String.class);
                    words_list.add(word);
                    meanings_list.add(meaning);
                }
                new_question();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //rotate the correct answer
    private void rotate_true_answer(){
        switch (correct_answer_mum){
            case 1:
                option1_button.animate().rotation(option1_button.getRotation()-360).start();
                break;
            case 2:
                option2_button.animate().rotation(option2_button.getRotation()-360).start();
                break;
            case 3:
                option3_button.animate().rotation(option3_button.getRotation()-360).start();
                break;
            case 4:
                option4_button.animate().rotation(option4_button.getRotation()-360).start();
                break;
        }

    }


    //get current index question from shared preferance
    private void current_question_index_sh(){
        switch (level_name){
            case "Beginners":
                current_question_index = sharedPreferences.getInt("VOCABULARY1",0);
                break;
            case "Basic":
                current_question_index = sharedPreferences.getInt("VOCABULARY2",0);
                break;
            case "Advanced":
                current_question_index = sharedPreferences.getInt("VOCABULARY3",0);
                break;
        }

    }


    //commit user info
    private void commit_user_info(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(player);
        editor.putString("Player", json);
        switch (level_name){
            case "Beginners":
                editor.putInt("VOCABULARY1", current_question_index);
                break;
            case "Basic":
                editor.putInt("VOCABULARY2", current_question_index);
                break;
            case "Advanced":
                editor.putInt("VOCABULARY3", current_question_index);
                break;
        }
        editor.commit();
    }
}
