package com.example.khuta.englishclub;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

public class AssemblingActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    DatabaseReference mDatabase;

    private ArrayList<String> words_list = new ArrayList<>();
    private ArrayList<String> meanings_list = new ArrayList<>();

    private TextView word_Heb_textView;
    private TextView word_Eng_textView;
    private TextView points_textView;
    private TextView timer_display;

    private CountDownTimer countDownTimer;
    private Player player;
    private Random rand = new Random();

    private String level_name;
    private String word_in_hebrew;
    private String meaning_in_english;
    private String word_answer;

    private int second_per_ques;
    private int length_of_meaning;
    private int current_letter = 0;
    private int current_question_index = 0;

    private char[] alphabet = {'a','b','c','d','e','f','g','h','i','j','k','l','m',
            'n','o','p','q','r','s','t','u','v','w','x','y','z'};

    private char button_1, button_2, button_3, button_4, button_5, button_6, button_7, button_8;
    private char button_9, button_10, button_11, button_12, button_13, button_14, button_15;

    private Button A_button1, A_button2, A_button3, A_button4, A_button5, A_button6, A_button7, A_button8, A_button9, A_button10;
    private Button A_button11, A_button12, A_button13, A_button14, A_button15;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assembling);

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
        points_textView = findViewById(R.id.points_Ass_textView);
        points_textView.setText(String.valueOf(player.getPoints()));
        timer_display = findViewById(R.id.timer_Ass_textView);
        word_Heb_textView = findViewById(R.id.Aword_hebrew_txt);
        word_Eng_textView = findViewById(R.id.Aword_english_txt);

        //initializing components
        A_button1 = findViewById(R.id.button1_A);
        A_button2 = findViewById(R.id.button2_A);
        A_button3 = findViewById(R.id.button3_A);
        A_button4 = findViewById(R.id.button4_A);
        A_button5 = findViewById(R.id.button5_A);
        A_button6 = findViewById(R.id.button6_A);
        A_button7 = findViewById(R.id.button7_A);
        A_button8 = findViewById(R.id.button8_A);
        A_button9 = findViewById(R.id.button9_A);
        A_button10 = findViewById(R.id.button10_A);
        A_button11 = findViewById(R.id.button11_A);
        A_button12 = findViewById(R.id.button12_A);
        A_button13 = findViewById(R.id.button13_A);
        A_button14 = findViewById(R.id.button14_A);
        A_button15 = findViewById(R.id.button15_A);


        //get all words from firebase server by level name
        get_words_from_server();


        //buttons clicked
        A_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_1);
            }
        });

        A_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_2);
            }
        });

        A_button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_3);
            }
        });

        A_button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_4);
            }
        });

        A_button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_5);
            }
        });

        A_button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_6);
            }
        });

        A_button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_7);
            }
        });

        A_button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_8);
            }
        });

        A_button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_9);
            }
        });

        A_button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_10);
            }
        });

        A_button11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_11);
            }
        });

        A_button12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_12);
            }
        });

        A_button13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_13);
            }
        });

        A_button14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_14);
            }
        });

        A_button15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_letter_correct(button_15);
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
        int[] leters_Button = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
        int index;

        for(int i=0; i<length_of_meaning; i++) {
            index = rand.nextInt(15);
            while (leters_Button[index] != -1)
                index = rand.nextInt(15);

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


        A_button1.setText(button_1 + "");
        A_button2.setText(button_2 + "");
        A_button3.setText(button_3 + "");
        A_button4.setText(button_4 + "");
        A_button5.setText(button_5 + "");
        A_button6.setText(button_6 + "");
        A_button7.setText(button_7 + "");
        A_button8.setText(button_8 + "");
        A_button9.setText(button_9 + "");
        A_button10.setText(button_10 + "");
        A_button11.setText(button_11 + "");
        A_button12.setText(button_12 + "");
        A_button13.setText(button_13 + "");
        A_button14.setText(button_14 + "");
        A_button15.setText(button_15 + "");
    }


    //return index of char in alphabet
    private int index_of_char(char ch){
        for (int i=0; i<alphabet.length; i++)
            if(ch == alphabet[i])
                return i;
        return -1;
    }


    //check current letter clicked is correct
    private void check_letter_correct(char letter){
        if(letter == meaning_in_english.charAt(current_letter)){
            word_answer = word_answer.substring(0,current_letter*2) + letter +
                    word_answer.substring(current_letter*2+1);
            word_Eng_textView.setText(word_answer);
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
        word_in_hebrew = meanings_list.get(current_question_index);
        word_Heb_textView.setText(word_in_hebrew);

        meaning_in_english = words_list.get(current_question_index);
        meaning_in_english = meaning_in_english.toLowerCase();
        length_of_meaning = meaning_in_english.length();

        word_answer = "";
        for (int i=0; i<length_of_meaning; i++){
            word_answer += "_ ";
        }
        word_Eng_textView.setText(word_answer);
        fill_letters_in_Buttons();
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


    //get current index question from shared preferance
    private void current_question_index_sh(){
        switch (level_name){
            case "Beginners":
                current_question_index = sharedPreferences.getInt("Assembling1",0);
                break;
            case "Basic":
                current_question_index = sharedPreferences.getInt("Assembling2",0);
                break;
            case "Advanced":
                current_question_index = sharedPreferences.getInt("Assembling3",0);
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
                editor.putInt("Assembling1", current_question_index);
                break;
            case "Basic":
                editor.putInt("Assembling2", current_question_index);
                break;
            case "Advanced":
                editor.putInt("Assembling3", current_question_index);
                break;
        }
        editor.commit();
    }
}
