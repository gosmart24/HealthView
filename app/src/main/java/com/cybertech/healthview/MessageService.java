package com.cybertech.healthview;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class MessageService extends FirebaseMessagingService {
    private final String TAG = "TAG";
    private notificationutil notificationUtils;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...
        if (remoteMessage == null)
            return;

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e("TAG", "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e("TAG", "Exception: " + e.getMessage());
            }
        }

        // Check if message contains a notification payload.
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {

            if (!notificationutil.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", remoteMessage.getNotification().getBody());
                pushNotification.putExtra("title", remoteMessage.getNotification().getTitle());
                pushNotification.putExtra("sendtime", remoteMessage.getSentTime());
                pushNotification.putExtra("to", remoteMessage.getTo());
                pushNotification.putExtra("from", remoteMessage.getFrom());
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                notificationutil notificationUtils = new notificationutil(getApplicationContext());
                notificationUtils.playNotificationSound();

            } else {
                // If the app is in background, firebase itself handles the notification\
                Intent resultIntent = new Intent(getApplicationContext(), DoctorsActivity.class);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //showNotificationMessage(getApplicationContext(), remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), String.valueOf(remoteMessage.getSentTime()), resultIntent);
                showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
            }
            // handleNotification(remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);


            if (!notificationutil.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("title", title);
                pushNotification.putExtra("message", message);
                pushNotification.putExtra("timestamp", timestamp);
                pushNotification.putExtra("from", data.getString("from"));
                pushNotification.putExtra("to", data.getString("to"));
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                notificationutil notificationUtils = new notificationutil(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), DoctorsActivity.class);
                resultIntent.putExtra("message", message);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    private void showNotification(String title, String message) {

        Intent i = new Intent(this, DoctorsActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        // Uri ringTone = FirebaseMessaging.
        final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/raw/notification");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message)
                .addAction(R.drawable.futminna,"TP:96.FR",null)
                .addAction(R.drawable.futminna,"HB:108.5pmt",null)
                .addAction(R.drawable.futminna,"CD:normal",null)
                .setSound(alarmSound)
                .setSmallIcon(R.drawable.futminna)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0, builder.build());
    }

    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new notificationutil(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new notificationutil(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

}
