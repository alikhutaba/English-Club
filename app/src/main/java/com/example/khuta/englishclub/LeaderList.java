package com.example.khuta.englishclub;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class LeaderList extends ABaseFragment {

    DatabaseReference databaseReference;

    private listInterface listInterface;
    private ListView listView;
    private List<Player> plist;
    private int[] index = {1,2,3,4,5,6,7,8,9,10};

    Context mContext;


    public interface listInterface {

        void ListClicked(Player player);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leader_list, container, false);

        listView = (ListView) view.findViewById(R.id.leaderList);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        plist = new ArrayList<Player>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                listInterface.ListClicked(plist.get(position));

            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

        if(context instanceof listInterface)
            listInterface = (listInterface) context;
        else
            throw new RuntimeException(context.toString() +" must implement listInterface");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
        listInterface = null;
    }

    @Override
    public void onStart() {
        super.onStart();

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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do this after 1000ms=1sec

                Collections.sort(plist, new Comparator<Player>() {
                    @Override
                    public int compare(Player o1, Player o2) {
                        return o1.compareTo(o2);
                    }
                });

                playerAdapter playerAdapter = new playerAdapter();
                listView.setAdapter(playerAdapter);

            }
        }, 100);

    }

    class playerAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            if(plist.size() > index.length)
                return index.length;
            return plist.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {

            view = getLayoutInflater().inflate(R.layout.playerlayout,null);
            TextView idnex = (TextView)view.findViewById(R.id.index);
            TextView name = (TextView)view.findViewById(R.id.name);
            TextView points = (TextView)view.findViewById(R.id.points);
            TextView address = (TextView)view.findViewById(R.id.address);

            idnex.setText(String.valueOf(index[position]));
            name.setText(plist.get(position).getName());
            points.setText(String.valueOf(plist.get(position).getPoints()));
            address.setText(plist.get(position).getCity());

            return view;
        }
    }

}
