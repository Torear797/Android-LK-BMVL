package com.bmvl.lk.push;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.bmvl.lk.R;
import com.bmvl.lk.ui.MenuActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

public class BMVLFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        //  super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null) {
            Log.d("TAG", "Message Notification Body: " + remoteMessage.getNotification().getBody());

            sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());
        }
       // Log.d("TAG", "null ");
    }
    private static final String CHANNEL_ID = "CHANNEL_BMVL";
    private static int counter = 0;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    private void sendNotification(String messageBody, String Title) {
//        Intent intent = new Intent(this, MenuActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
      //  intent.putExtra("GoToNotify", true);

//        PendingIntent contentIntent = PendingIntent.getActivity(this,
//                0, intent,
//                PendingIntent.FLAG_CANCEL_CURRENT);


       // Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            assert notificationManager != null;
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(CHANNEL_ID);
            if (notificationChannel == null) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(CHANNEL_ID, "Канал БМВЛ", importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(false);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                //  .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.logo))
                .setContentTitle(Title)
                .setContentText(messageBody)
                .setAutoCancel(true)
              //  .setContentIntent(contentIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        notificationBuilder.build().flags |= Notification.FLAG_AUTO_CANCEL;
        Objects.requireNonNull(notificationManager).notify(counter++, notificationBuilder.build());
    }
}
