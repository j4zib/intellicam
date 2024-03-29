package com.example.j4zib.intellicam;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public MyFirebaseMessagingService() {
    }

    private static final String TAG = "FirebaseMessagingServce";
    String CHANNEL_ID="myChannel";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        createNotificationChannel();
        String notificationTitle = null, notificationBody = null;
        String id= null;


        // Check if message contains a data payload.
        if (remoteMessage.getData().size()>0) {
            notificationTitle = remoteMessage.getData().get("name");
            notificationBody = remoteMessage.getData().get("spam");
            id = remoteMessage.getData().get("id");

        }
        Log.d("tag",id);
        if(notificationTitle.equals("unknown")){
            sendNotification(notificationTitle, notificationBody,id);
        }else{
            customNotification(notificationTitle,notificationBody);
        }

    }

    private void customNotification(String notificationTitle, String notificationBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        if(Integer.parseInt(notificationBody)>0) {
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setAutoCancel(true)   //Automatically delete the notification
                    .setSmallIcon(R.drawable.ic_error_black_24dp) //Notification icon
                    .setContentIntent(pendingIntent)
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationTitle + " has been detected")
                    .setSound(defaultSoundUri);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());

        }
        else{
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setAutoCancel(true)   //Automatically delete the notification
                    .setSmallIcon(R.mipmap.ic_launcher) //Notification icon
                    .setContentIntent(pendingIntent)
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationTitle + " has been detected")
                    .setSound(defaultSoundUri);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
        }
    }
    private void sendNotification(String notificationTitle, String notificationBody,String id) {
        Intent notifyIntent = new Intent(this,Dialog.class);
        notifyIntent.putExtra("id",id);
        notifyIntent.putExtra("spam",notificationBody);
        notifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent intent = PendingIntent.getActivity(this, 0,
                notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if(Integer.parseInt(notificationBody)>0) {
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setAutoCancel(true)   //Automatically delete the notification
                    .setSmallIcon(R.drawable.ic_error_black_24dp) //Notification icon
                    .setContentIntent(intent)
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationTitle + " Person")
                    .setSound(defaultSoundUri);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());

        }
        else{
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setAutoCancel(true)   //Automatically delete the notification
                    .setSmallIcon(R.mipmap.ic_launcher) //Notification icon
                    .setContentIntent(intent)
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationTitle + " Person")
                    .setSound(defaultSoundUri);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
        }
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "intelliCam";
            String description = "intelliCam is enabled";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}