package com.diverapp.diverapp;

import android.app.Notification;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.diverapp.diverapp.Config.App;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import Modle.Notifi;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    private NotificationManagerCompat notificationManager;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
//        Log.e(TAG, "onMessageReceived: " );
//        Log.d(TAG, "From: " + remoteMessage.getFrom());
////        Toast.makeText(this, "onMessageReceived "+remoteMessage.getNotification().getBody(), Toast.LENGTH_SHORT).show();
//        Log.d(TAG, "Notification Message Body: " +remoteMessage.getSentTime());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
                Map<String, String> data = remoteMessage.getData();
                notificationManager = NotificationManagerCompat.from(this);
                Notification notification = new Notification();
                switch (data.get("id")){
                    case "0":
                        Log.e(TAG, "onMessageReceived: ID 0"  );

                        // new Trip Add need to activit
                        notification = new NotificationCompat.Builder(this, App.channelId)
                                .setSmallIcon(R.drawable.ic_assetlogo).setContentText(data.get("firstName")+ "add new trip ("+data.get("secondName")+")").setContentTitle("New Trip added ")
                                .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH).setCategory(NotificationCompat.CATEGORY_MESSAGE).build();
                        break;
                    case "1":
                        Log.e(TAG, "onMessageReceived: ID 1"  );
                        // Trip Activit
                        notification = new NotificationCompat.Builder(this, App.channelId)
                                .setSmallIcon(R.drawable.ic_assetlogo).setContentText(data.get("firstName")+ "now ready").setContentTitle("Trip review")
                                .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH).setCategory(NotificationCompat.CATEGORY_MESSAGE).build();
                        Log.e(TAG, "onMessageReceived---- id == 1 - secondName: "+ data.get("secondName") );
                        break;
                    case "2":
                        Log.e(TAG, "onMessageReceived: ID 2"  );
                        // salse
                        notification = new NotificationCompat.Builder(this, App.channelId)
                                .setSmallIcon(R.drawable.ic_assetlogo).setContentText(data.get("bauerName")+ "He bought your trip ("+data.get("secondName")+")").setContentTitle("New Selse ")
                                .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH).setCategory(NotificationCompat.CATEGORY_MESSAGE).build();
                        break;
                }
                notificationManager.notify(1,notification);
                String reseverUID = data.get("secondName");
                Notifi notifi= new Notifi(data.get("id"),data.get("firstName"),data.get("secondName"));
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Notification").child(reseverUID).push();
                ref.setValue(notifi);
                Log.e(TAG, "onMessageReceived-----: "+ reseverUID );
                Log.e(TAG, "onMessageReceived: "+ data.get("id") );
                Log.e(TAG, "onMessageReceived: "+ data.get("body") );
                Log.e(TAG, "onMessageReceived: "+ data.get("total") );

            } else {
                // Handle message within 10 seconds
//                handleNow();
            }

        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }
    public void setupNotifiction(String firstName,String secondName){

    }
}
