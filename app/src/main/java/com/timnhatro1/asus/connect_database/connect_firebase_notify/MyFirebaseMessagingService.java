package com.timnhatro1.asus.connect_database.connect_firebase_notify;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.timnhatro1.asus.MyApplication;
import com.timnhatro1.asus.view.activity.MainActivity;
import com.timnhatro1.asus.view.activity.R;
import com.timnhatro1.asus.model.notify.NotifyFirebaseModel;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.net.URLDecoder;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            NotifyFirebaseModel notifyFirebaseModel = convertDataNotify(remoteMessage.getData());
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            if (!MyApplication.isActivityVisible()) {
                showNotification(notifyFirebaseModel);
            } else {
                showHeadUpNotification(notifyFirebaseModel);
            }
        }
        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            LogUtils.dTag(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }

    }

    private NotifyFirebaseModel convertDataNotify(Map<String, String> data) {
        NotifyFirebaseModel notifyFirebaseModel = new NotifyFirebaseModel();
        notifyFirebaseModel.setTitle(URLDecoder.decode(data.get("title")));
        notifyFirebaseModel.setBody(URLDecoder.decode(data.get("body")));
        notifyFirebaseModel.setLat(data.get("lat"));
        notifyFirebaseModel.setLng(data.get("long"));
        return notifyFirebaseModel;
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        FirebaseUtils.updateTokenToServer(token);
    }

    private void showNotification(NotifyFirebaseModel notifyFirebaseModel) {
        String channelId = getString(R.string.app_name);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .setContentTitle(notifyFirebaseModel.getTitle())
                        .setContentText(notifyFirebaseModel.getBody())
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle(notifyFirebaseModel.getTitle()).bigText(notifyFirebaseModel.getBody()))
                        .setContentIntent(getPendingIntent(notifyFirebaseModel));

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    getApplication().getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        if (notificationManager != null) {
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }
    }


    private void showHeadUpNotification(NotifyFirebaseModel notifyFirebaseModel) {


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.app_name));
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(notifyFirebaseModel.getTitle())
                .setContentText(notifyFirebaseModel.getBody())
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentIntent(getPendingIntent(notifyFirebaseModel))
                .setPriority(NotificationCompat.PRIORITY_MAX);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(getString(R.string.app_name),
                    getApplication().getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_HIGH);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        if (notificationManager != null) {
            notificationManager.notify(0 /* ID of notification */, builder.build());
        }
    }

    private PendingIntent getPendingIntent(NotifyFirebaseModel notifyFirebaseModel) {
//        int action = Integer.parseInt(payload.get("click_action"));
        Intent intent;
        intent = new Intent(this, MainActivity.class);
        intent.setAction("click_action");
        intent.putExtra("lat", notifyFirebaseModel.getLat());
        intent.putExtra("lng", notifyFirebaseModel.getLng());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return  PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
//        switch (action) {
//            default:
//                intent = new Intent(this, MainActivity.class);
//                intent.setAction(payload.get("click_action"));
//                intent.putExtra("data",payload.get("data_id"));
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                return  PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                        PendingIntent.FLAG_UPDATE_CURRENT);
//        }
    }


}
