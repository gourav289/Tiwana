package com.gk.myapp.pushnotifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.gk.myapp.R;
import com.gk.myapp.activities.LoginActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gk on 17-02-2018.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
private static String TAG="notificationtest";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        sendNotification(remoteMessage.getData().get("message").toString());
        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
//        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
//            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            Intent pushNotification = new Intent(getApplicationContext(), LoginActivity.class);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
//        }else{
//            // If the app is in background, firebase itself handles the notification
//        }
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


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), LoginActivity.class);
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

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        NotificationUtils notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        NotificationUtils notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }


    //my Code
        private void sendNotification(String msg/*, String collapse_key, String notifykey, Bundle data*/) {
            Log.d(TAG, "Preparing to send notification...: " + msg);

            NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
//		//particular notification
//		if (notifykey != null && notifykey.equalsIgnoreCase(C.CHAT)) {
//			try {
//				if (ChatActivity.isOnline && ChatActivity.sendTo.equalsIgnoreCase(data.getString("send_by"))) {
//					Intent mIntent = new Intent(C.CHAT_BROADCAST);
//					mIntent.putExtra("msg", data.getString("text"));
//					mIntent.putExtra("sendTo", data.getString("send_to"));
//					mIntent.putExtra("sendFrom", data.getString("send_by"));
//					mIntent.putExtra("date", data.getString("date"));
//					LocalBroadcastManager.getInstance(this).sendBroadcast(mIntent);
//				} else {
//					Bundle bundle = new Bundle();
//					bundle.putString("from", C.CHAT);
//					bundle.putString("sendTo", data.getString("send_by"));
//					bundle.putString("userName", data.getString("sender_name"));
					Intent mIntent = new Intent(getApplicationContext(), LoginActivity.class);
//					mIntent.putExtras(bundle);
					mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

					PendingIntent contentIntent = PendingIntent.getActivity(this, 0, mIntent, PendingIntent.FLAG_CANCEL_CURRENT);

					NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
							this).setSmallIcon(R.mipmap.ic_launcher)
							.setContentTitle(getApplicationContext().getString(R.string.app_name))
							.setAutoCancel(true)
							.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
							.setContentText(msg)
							.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);

					mBuilder.setContentIntent(contentIntent);
					mNotificationManager.notify(12345, mBuilder.build());
					Log.d(TAG, "Notification sent successfully.");
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}else if (notifykey != null && notifykey.equalsIgnoreCase(C.SHIPMENT_ACCEPT)) {
//			try {
//				if (MyShipmentFragment.isOnline ||((OfferDetailsActivity.isOnline || BidDetailsActivity.isOnline) && User.shipmentId!=null && User.shipmentId.equalsIgnoreCase(data.getString("shipment_id")))) {
//					Intent mIntent = new Intent(C.CHAT_BROADCAST);
//					mIntent.putExtra("msg",msg);
//					LocalBroadcastManager.getInstance(this).sendBroadcast(mIntent);
//				} else {
//					if(NotificationFragment.isOnline) {
//						Intent mIntent = new Intent(C.CHAT_BROADCAST);
//						LocalBroadcastManager.getInstance(this).sendBroadcast(mIntent);
//					}
//					Bundle bundle = new Bundle();
//					bundle.putString("from", C.SHIPMENT_ACCEPT);
//					bundle.putString("shipment_id", data.getString("shipment_id"));
//					Intent mIntent = new Intent(getApplicationContext(), SplashActivity.class);
//					mIntent.putExtras(bundle);
//					mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//					PendingIntent contentIntent = PendingIntent.getActivity(this, 0, mIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//
//					NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
//							this).setSmallIcon(R.mipmap.app_icon)
//							.setContentTitle(appName)
//							.setAutoCancel(true)
//							.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
//							.setContentText(msg)
//							.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);
//
//					mBuilder.setContentIntent(contentIntent);
//					mNotificationManager.notify(C.CHAT_NOTIF_ID, mBuilder.build());
//					Log.d(TAG, "Notification sent successfully.");
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		else {
//			if(NotificationFragment.isOnline|| TransNotificationFragment.isOnline) {
//				Intent mIntent = new Intent(C.CHAT_BROADCAST);
//				LocalBroadcastManager.getInstance(this).sendBroadcast(mIntent);
//			}
//			Intent mIntent = new Intent(getApplicationContext(), SplashActivity.class);
//			mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//
//			PendingIntent contentIntent = PendingIntent.getActivity(this, 0, mIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//
//			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
//					this).setSmallIcon(R.mipmap.app_icon)
//					.setContentTitle(appName)
//					.setAutoCancel(true)
//					.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
//					.setContentText(msg)
//					.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);
//
//			mBuilder.setContentIntent(contentIntent);
//			mNotificationManager.notify(C.CHAT_NOTIF_ID, mBuilder.build());
//			Log.d(TAG, "Notification sent successfully.");
//		}
        }
}
