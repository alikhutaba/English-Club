package com.example.khuta.englishclub;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

 class LeaderboardActivity extends FragmentActivity implements LeaderList.listInterface{

    private LeaderList leaderList;
    private LeaderMap leaderMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);


        leaderList = new LeaderList();
        //leaderList.onAttach(this);
        getSupportFragmentManager().beginTransaction().add(R.id.frame1 ,leaderList).commit();


        leaderMap = new LeaderMap();
        //leaderMap.onAttach(this);
        getSupportFragmentManager().beginTransaction().add(R.id.frame2 ,leaderMap).commit();


    }


    @Override
    public void ListClicked(Player player) {

        leaderMap.MoveMapCamera(player);
    }
}
