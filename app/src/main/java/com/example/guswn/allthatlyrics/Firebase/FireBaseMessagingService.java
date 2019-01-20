package com.example.guswn.allthatlyrics.Firebase;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;

import android.app.NotificationManager;

import android.app.PendingIntent;

import android.content.Context;
import android.content.Intent;

import android.graphics.Color;
import android.media.RingtoneManager;

import android.net.Uri;

import android.os.Build;

import android.os.Vibrator;

import android.support.v4.app.NotificationCompat;

import android.util.Log;


import com.example.guswn.allthatlyrics.Home.Frag4_chat.Home_fragment4;
import com.example.guswn.allthatlyrics.Home.Home;
import com.example.guswn.allthatlyrics.MainActivity;
import com.example.guswn.allthatlyrics.R;
import com.google.firebase.messaging.FirebaseMessagingService;

import com.google.firebase.messaging.RemoteMessage;



public class FireBaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From : " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            handleNow();
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Msg Notify Body : " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getTitle());

        }
    }

    public void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    private void sendNotification(String messageBody,String title) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, Home_fragment4.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            //oreo 버전에서는 채널없이 알림을 추가 할 수 없으므로
            @SuppressLint("WrongConstant")
            NotificationChannel notificationChannel=new NotificationChannel("my_notification","n_channel",NotificationManager.IMPORTANCE_MAX);
            notificationChannel.setDescription("description");
            notificationChannel.setName("Channel Name");
            notificationManager.createNotificationChannel(notificationChannel);
        }

//        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, "my_notification")
                        .setSmallIcon(R.drawable.allthatlyrics_icon_1)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(NotificationManager.IMPORTANCE_MAX)
                        .setOnlyAlertOnce(true)
                        .setContentIntent(pendingIntent)
                        .setColor(Color.parseColor("#3F5996"));

        notificationManager.notify(0, notificationBuilder.build());

    }



}
