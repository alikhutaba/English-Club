package com.example.khuta.englishclub;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;


public class LeaderMap extends ABaseFragment implements OnMapReadyCallback{

    final static int REQUEST_CODE_FINE_GPS = 1;

    DatabaseReference databaseReference;
    Context mContext;

    private MapView mapView;
    private GoogleMap googleMap;
    private List<Player> plist;



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = (MapView)view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leader_map, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        plist = new ArrayList<Player>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot playersnapshot : dataSnapshot.getChildren()){

                    Player player = playersnapshot.getValue(Player.class);
                    plist.add(player);

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        getAvailableActivity(new IActivityEnabledListener() {
            @Override
            public void onActivityEnabled(FragmentActivity activity) {

                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_FINE_GPS);
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }


    @Override
    public void onStart() {
        super.onStart();

    }


    @Override
    public void onMapReady(GoogleMap map) {

        googleMap = map;
        Player p;

        for(int i=0 ; i < plist.size() ; i++){

            p = (Player)plist.get(i);

            LatLng location = new LatLng(p.getLatitude(),p.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(location)
                    .title(p.getName()));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));

        }
    }

    public void MoveMapCamera(Player player) {

        LatLng latLng = new LatLng(player.getLatitude() , player.getLongitude());
        float zoom = 100;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

    }

}

