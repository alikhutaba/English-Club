package com.example.khuta.englishclub;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class HomeActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FusedLocationProviderClient fusedLocationClient;

    private Button login, registration;
    private EditText login_email, login_password, registration_name, regregistration_email, registration_password;

    //User
    private String name, email, password;
    private double latitude, longitude;
    private Player player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //getting user info from sharedPreferences
        sharedPreferences = getSharedPreferences("UserInfo", 0);

        //clear sharedPreferences
        //sharedPreferences.edit().clear().commit();

        //getting user info from sharedPreferences
        String json = sharedPreferences.getString("Player", "");

        //check user is exists
        if(!json.equals("null") && json.length() >0 && json != null){

            Intent LevelActivity_intent = new Intent(this, LevelActivity.class);
            startActivity(LevelActivity_intent);
        }

        //initializing components
        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        login = findViewById(R.id.login);

        regregistration_email = findViewById(R.id.registration_email);
        registration_password = findViewById(R.id.registration_password);
        registration_name = findViewById(R.id.registration_name);
        registration = findViewById(R.id.registration);

        firebaseAuth = FirebaseAuth.getInstance();

        //Get user location
        GetUserLocation();

        // LOGIN
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = login_email.getText().toString();
                password = login_password.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(HomeActivity.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
                } else {

                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(HomeActivity.this, "login Failed", Toast.LENGTH_SHORT).show();
                            } else {
                                savetheplayer();
                                firebaseAuth.signOut();
                            }

                        }
                    });
                }
            }
        });


        // Registration new player
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = regregistration_email.getText().toString();
                password = registration_password.getText().toString();
                name = registration_name.getText().toString();

                if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                    Toast.makeText(HomeActivity.this, "Not correct email or password or name", Toast.LENGTH_SHORT).show();
                } else {

                    Query emailToQuery = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("Email").equalTo(email);
                    emailToQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.getChildrenCount() > 0)
                                Toast.makeText(HomeActivity.this, "existing email", Toast.LENGTH_LONG).show();
                            else {

                                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(HomeActivity.this, "sign up error", Toast.LENGTH_SHORT).show();
                                        } else {
                                            sendUserData();
                                            savetheplayer();
                                        }
                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void GetUserLocation(){

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            longitude = location.getLongitude();
                            latitude = location.getLatitude();
                        }
                    }
                });
    }


    // save player in firebase
    private void sendUserData() {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        String city = addresses.get(0).getLocality();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid());

        String UID = firebaseAuth.getUid();
        Player player = new Player(UID , email , password , name , 0 , latitude , longitude , city);
        myRef.setValue(player);
    }

    // save player in shareprefernce
    private void savetheplayer(){

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Player p = dataSnapshot.getValue(Player.class);

                sharedPreferences = getSharedPreferences("UserInfo", 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                Gson gson = new Gson();
                String json = gson.toJson(p);
                editor.putString("Player", json);

                editor.commit();
                firebaseAuth.signOut();

                Intent LevelActivity_intent = new Intent(HomeActivity.this, LevelActivity.class);
                startActivity(LevelActivity_intent);
                finish();

                NotificationEventReceiver.setupAlarm(getApplicationContext());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}


