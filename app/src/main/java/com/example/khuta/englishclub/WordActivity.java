package com.example.khuta.englishclub;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Random;

public class WordActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    DatabaseReference mDatabase;

    private ArrayList<String> words_list = new ArrayList<>();
    private ArrayList<String> meanings_list = new ArrayList<>();

    private TextView word_textView;
    private TextView points_textView;
    private TextView timer_display;

    private CountDownTimer countDownTimer;
    private int second_per_ques;
    private Player player;
    private String level_name;
    private Random rand = new Random();

    private char[] alphabet = {'a','b','c','d','e','f','g','h','i','j','k','l','m',
            'n','o','p','q','r','s','t','u','v','w','x','y','z'};

    private String word_in_hebrew;
    private String meaning_in_english;

    private int length_of_meaning;
    private int current_letter = 0;
    private int current_question_index = 0;

    private char button_1, button_2, button_3, button_4, button_5, button_6, button_7, button_8;
    private char button_9, button_10, button_11, button_12, button_13, button_14, button_15, button_16;
    private char button_17, button_18, button_19, button_20, button_21, button_22, button_23, button_24,button_25;

    private Button l_button1, l_button2, l_button3, l_button4, l_button5, l_button6, l_button7, l_button8, l_button9, l_button10;
    private Button l_button11, l_button12, l_button13, l_button14, l_button15, l_button16, l_button17, l_button18, l_button19;
    private Button l_button20, l_button21, l_button22, l_button23, l_button24, l_button25;

    private ImageView star1_imageView;
    private ImageView star2_imageView;
    private ImageView star3_imageView;
    private int stars = 3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);


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
        points_textView = findViewById(R.id.points_word_textView);
        points_textView.setText(String.valueOf(player.getPoints()));
        timer_display = findViewById(R.id.timer_word_textView);
        word_textView = findViewById(R.id.word_hebrew_txt);

        //initializing components
        l_button1 = findViewById(R.id.letter_button1);
        l_button2 = findViewById(R.id.letter_button2);
        l_button3 = findViewById(R.id.letter_button3);
        l_button4 = findViewById(R.id.letter_button4);
        l_button5 = findViewById(R.id.letter_button5);
        l_button6 = findViewById(R.id.letter_button6);
        l_button7 = findViewById(R.id.letter_button7);
        l_button8 = findViewById(R.id.letter_button8);
        l_button9 = findViewById(R.id.letter_button9);
        l_button10 = findViewById(R.id.letter_button10);
        l_button11 = findViewById(R.id.letter_button11);
        l_button12 = findViewById(R.id.letter_button12);
        l_button13 = findViewById(R.id.letter_button13);
        l_button14 = findViewById(R.id.letter_button14);
        l_button15 = findViewById(R.id.letter_button15);
        l_button16 = findViewById(R.id.letter_button16);
        l_button17 = findViewById(R.id.letter_button17);
        l_button18 = findViewById(R.id.letter_button18);
        l_button19 = findViewById(R.id.letter_button19);
        l_button20 = findViewById(R.id.letter_button20);
        l_button21 = findViewById(R.id.letter_button21);
        l_button22 = findViewById(R.id.letter_button22);
        l_button23 = findViewById(R.id.letter_button23);
        l_button24 = findViewById(R.id.letter_button24);
        l_button25 = findViewById(R.id.letter_button25);

        //initializing components
        star1_imageView = findViewById(R.id.star1_imageView);
        star2_imageView = findViewById(R.id.star2_imageView);
        star3_imageView = findViewById(R.id.star3_imageView);

        //check level name is not Basic
        if( ! level_name.equals("Basic")){
            star1_imageView.setVisibility(View.INVISIBLE);
            star2_imageView.setVisibility(View.INVISIBLE);
            star3_imageView.setVisibility(View.INVISIBLE);
        }


        //get all words from firebase server by level name
        get_words_from_server();


        //buttons clicked
        l_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_1, l_button1);
            }
        });

        l_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_2, l_button2);
            }
        });

        l_button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_3, l_button3);
            }
        });

        l_button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_4, l_button4);
            }
        });

        l_button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_5, l_button5);
            }
        });

        l_button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_6, l_button6);
            }
        });

        l_button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_7, l_button7);
            }
        });

        l_button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_8, l_button8);
            }
        });

        l_button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_9, l_button9);
            }
        });

        l_button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_10, l_button10);
            }
        });

        l_button11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_11, l_button11);
            }
        });

        l_button12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_12, l_button12);
            }
        });

        l_button13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_13, l_button13);
            }
        });

        l_button14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_14, l_button14);
            }
        });

        l_button15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_15, l_button15);
            }
        });

        l_button16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_16, l_button16);
            }
        });

        l_button17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_17, l_button17);
            }
        });

        l_button18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_18, l_button18);
            }
        });

        l_button19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_19, l_button19);
            }
        });

        l_button20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_20, l_button20);
            }
        });

        l_button21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_21, l_button21);
            }
        });

        l_button22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_22, l_button22);
            }
        });

        l_button23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_23, l_button23);
            }
        });

        l_button24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_24, l_button24);
            }
        });

        l_button25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_25, l_button25);
            }
        });
    }


    //event stop activity
    @Override
    protected void onStop(){
        super.onStop();
        countDownTimer.cancel();
        commit_user_info();
    }


    //event back clicked
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        countDownTimer.cancel();
        commit_user_info();
    }


    //set text to all buttons
    @SuppressLint("SetTextI18n")
    private void fill_letters_in_Buttons(){
        int[] leters_Button = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,
                -1,-1,-1,-1,-1, -1,-1,-1,-1,-1,};
        int index;

        for(int i=0; i<length_of_meaning; i++) {
            index = rand.nextInt(25);
            while (leters_Button[index] != -1)
                index = rand.nextInt(25);

            leters_Button[index] = index_of_char(meaning_in_english.charAt(i));
        }

        for(int i=0; i<leters_Button.length; i++)
            if(leters_Button[i] == -1)
                leters_Button[i] = rand.nextInt(26);

        button_1 = alphabet[leters_Button[0]];
        button_2 = alphabet[leters_Button[1]];
        button_3 = alphabet[leters_Button[2]];
        button_4 = alphabet[leters_Button[3]];
        button_5 = alphabet[leters_Button[4]];
        button_6 = alphabet[leters_Button[5]];
        button_7 = alphabet[leters_Button[6]];
        button_8 = alphabet[leters_Button[7]];
        button_9 = alphabet[leters_Button[8]];
        button_10 = alphabet[leters_Button[9]];
        button_11 = alphabet[leters_Button[10]];
        button_12 = alphabet[leters_Button[11]];
        button_13 = alphabet[leters_Button[12]];
        button_14 = alphabet[leters_Button[13]];
        button_15 = alphabet[leters_Button[14]];
        button_16 = alphabet[leters_Button[15]];
        button_17 = alphabet[leters_Button[16]];
        button_18 = alphabet[leters_Button[17]];
        button_19 = alphabet[leters_Button[18]];
        button_20 = alphabet[leters_Button[19]];
        button_21 = alphabet[leters_Button[20]];
        button_22 = alphabet[leters_Button[21]];
        button_23 = alphabet[leters_Button[22]];
        button_24 = alphabet[leters_Button[23]];
        button_25 = alphabet[leters_Button[24]];

        l_button1.setText(button_1 + "");
        l_button2.setText(button_2 + "");
        l_button3.setText(button_3 + "");
        l_button4.setText(button_4 + "");
        l_button5.setText(button_5 + "");
        l_button6.setText(button_6 + "");
        l_button7.setText(button_7 + "");
        l_button8.setText(button_8 + "");
        l_button9.setText(button_9 + "");
        l_button10.setText(button_10 + "");
        l_button11.setText(button_11 + "");
        l_button12.setText(button_12 + "");
        l_button13.setText(button_13 + "");
        l_button14.setText(button_14 + "");
        l_button15.setText(button_15 + "");
        l_button16.setText(button_16 + "");
        l_button17.setText(button_17 + "");
        l_button18.setText(button_18 + "");
        l_button19.setText(button_19 + "");
        l_button20.setText(button_20 + "");
        l_button21.setText(button_21 + "");
        l_button22.setText(button_22 + "");
        l_button23.setText(button_23 + "");
        l_button24.setText(button_24 + "");
        l_button25.setText(button_25 + "");
    }


    //return index of char in alphabet
    private int index_of_char(char ch){
        for (int i=0; i<alphabet.length; i++)
            if(ch == alphabet[i])
                return i;
        return -1;
    }


    //check current letter clicked is correct
    private void check_letter_correct(char letter, Button button){
        if(letter == meaning_in_english.charAt(current_letter)){
            button.setEnabled(false);
            current_letter++;
            if(current_letter == meaning_in_english.length()) {
                player.setPoints(player.getPoints()+10);
                points_textView.setText(String.valueOf(player.getPoints()));
                countDownTimer.cancel();
                current_question_index++;
                new_question();
            }
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


    //create new question
    private void new_question(){
        if(words_list.size() == 0)
            return;
        if(current_question_index >= words_list.size())
            current_question_index = 0;

        start_count_down();
        current_letter = 0;
        set_enable_buttons();
        word_in_hebrew = meanings_list.get(current_question_index);
        word_textView.setText(word_in_hebrew);

        meaning_in_english = words_list.get(current_question_index);
        meaning_in_english = meaning_in_english.toLowerCase();
        length_of_meaning = meaning_in_english.length();

        fill_letters_in_Buttons();
    }


    //set enable all buttons
    private void set_enable_buttons(){
        l_button1.setEnabled(true);
        l_button2.setEnabled(true);
        l_button3.setEnabled(true);
        l_button4.setEnabled(true);
        l_button5.setEnabled(true);
        l_button6.setEnabled(true);
        l_button7.setEnabled(true);
        l_button8.setEnabled(true);
        l_button9.setEnabled(true);
        l_button10.setEnabled(true);
        l_button11.setEnabled(true);
        l_button12.setEnabled(true);
        l_button13.setEnabled(true);
        l_button14.setEnabled(true);
        l_button15.setEnabled(true);
        l_button16.setEnabled(true);
        l_button17.setEnabled(true);
        l_button18.setEnabled(true);
        l_button19.setEnabled(true);
        l_button20.setEnabled(true);
        l_button21.setEnabled(true);
        l_button22.setEnabled(true);
        l_button23.setEnabled(true);
        l_button24.setEnabled(true);
        l_button25.setEnabled(true);
    }


    //time per question
    private void time_per_question(){
        switch (level_name){
            case "Beginners":
                second_per_ques = 30;
                break;
            case "Basic":
                second_per_ques = 20;
                break;
            case "Advanced":
                second_per_ques = 15;
                break;
        }
    }


    //countdown timer
    private void start_count_down(){
        time_per_question();

        countDownTimer = new CountDownTimer(1000 * second_per_ques, 1000) {
            public void onTick(long millisUntilFinished) {
                timer_display.setText("" + millisUntilFinished / 1000);
            }
            public void onFinish() {
                Toast.makeText(getApplicationContext(), "You did not make it in time", Toast.LENGTH_LONG).show();
                if(level_name.equals("Basic"))
                    stars_change();
                else
                    if(level_name.equals("Advanced"))
                        onBackPressed();
                     else
                         {
                             current_question_index++;
                             new_question();
                         }
            }
        }.start();
    }


    //starts = number of mistakes is 3
    private void stars_change(){
        stars--;
        if(stars == 0)
            onBackPressed();
        else {
            switch (stars) {
                case 1:
                    star2_imageView.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    star1_imageView.setVisibility(View.INVISIBLE);
                    break;
            }
            current_question_index++;
            new_question();
        }
    }


    //get current index question from shared preferance
    private void current_question_index_sh(){
        switch (level_name){
            case "Beginners":
                current_question_index = sharedPreferences.getInt("WORD1",0);
                break;
            case "Basic":
                current_question_index = sharedPreferences.getInt("WORD2",0);
            case "Advanced":
                current_question_index = sharedPreferences.getInt("WORD3",0);
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
                editor.putInt("WORD1", current_question_index);
                break;
            case "Basic":
                editor.putInt("WORD2", current_question_index);
                break;
            case "Advanced":
                editor.putInt("WORD3", current_question_index);
                break;
        }
        editor.commit();
    }
}
