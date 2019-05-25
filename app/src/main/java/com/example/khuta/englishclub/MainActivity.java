package com.example.khuta.englishclub;


import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private ArrayList<String> names_of_pictures = new ArrayList<>();
    private ArrayList<String> url_of_pictures = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get URL for all pictures from firebase
        get_pictures_from_server();

        //show main activity 5 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do this after 1000ms=1sec
                Intent HomeActivity_intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(HomeActivity_intent);
                finish();
            }
        }, 5000);


    }

    //get URL for all pictures from firebase
    private void get_pictures_from_server(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("pictures").child("Beginners");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot wordSnapshot : dataSnapshot.getChildren()){
                    String word = wordSnapshot.getKey();
                    String meaning = wordSnapshot.getValue(String.class);
                    names_of_pictures.add(word);
                    url_of_pictures.add(meaning);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
