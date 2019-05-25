package com.example.khuta.englishclub;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NotificationIntentService extends IntentService {

    private static final String ACTION_START = "ACTION_START";
    private static final String ACTION_DELETE = "ACTION_DELETE";
    private static final String CHANNEL_ID = "English_Club_Leader_List_001";
    private static final String CHANNEL_NAME = "English Club Leader List";

    private NotificationManager mNotificationManager;
    private static Context mContext;
    private List<Player> plist;
    private int playerIndex;
    private Player p;


    public NotificationIntentService() {
        super(NotificationIntentService.class.getSimpleName());
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = super.getApplicationContext();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = super.getApplicationContext();
        return super.onStartCommand(intent, flags, startId);
    }

    public static Intent createIntentStartNotificationService(Context context) {
         Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_START);
        return intent;
    }

    public static Intent createIntentDeleteNotification(Context context) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_DELETE);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            String action = intent.getAction();
            if (ACTION_START.equals(action)) {
                getIndexOfPlayer();
                processStartNotification();
            }
            if (ACTION_DELETE.equals(action)) {
                processDeleteNotification(intent);
            }
        } finally {
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }


    private void getIndexOfPlayer(){

        plist = new ArrayList<Player>();

        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", 0);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Player", "");
        p = gson.fromJson(json , Player.class);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

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

        Collections.sort(plist, new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                return o1.compareTo(o2);
            }
        });

        if(plist.indexOf(p) > 1)
            playerIndex = plist.indexOf(p);
        else
            playerIndex = 100;

    }

    private void processDeleteNotification(Intent intent) {
        // Log something?
    }

    private void processStartNotification() {
        // Do something. For example, fetch fresh data from backend to create a rich notification?
        Log.d(getClass().getSimpleName(), "English Club app test log processStartNotification, processStartNotification start push notification");

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this.getApplicationContext(), "notify_001");
        Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.ic_expand);
        mBuilder.setContentTitle("English Club");
        mBuilder.setContentText("you are now in rank "+playerIndex+" in the leader list\nclick here to play");
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setCategory(Notification.CATEGORY_SERVICE);

        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //String channelId = "YOUR_CHANNEL_ID";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(CHANNEL_ID);
        }

        mNotificationManager.notify(0, mBuilder.build());

    }
}