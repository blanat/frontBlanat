package com.example.myapplication;


import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Handle FCM messages here
        Log.d("MyFirebaseMessaging", "From: " + remoteMessage.getFrom());

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("MyFirebaseMessaging", "Notification Message Body: " + remoteMessage.getNotification().getBody());
        }
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        // Get updated InstanceID token.
        Log.d("MyFirebaseMessaging", "Refreshed token: " + token);

        // TODO: Implement this method to send any registration to your app's servers.
        // If you are not using this token to send to your server, you should save it to
        // shared preferences to send later.
    }
}
