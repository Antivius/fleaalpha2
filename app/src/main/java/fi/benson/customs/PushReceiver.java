package fi.benson.customs;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import fi.benson.R;
import fi.benson.views.ChatListActivity;
import fi.benson.views.LoginActivity;

/**
 * Created by bkamau on 4/19/16.
 */
public class PushReceiver extends ParsePushBroadcastReceiver {
    private final String TAG = "PUSH_NOTIFY";

    long[] v = {500,1000};
    Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    String senderName = null;
    String senderId = null;
    String message = null;
    String action = null;
    String recieverId = null;



    @Override
    public void onPushOpen(Context context, Intent intent) {
        Log.i(TAG, "onPushOpen triggered!");
        Intent i = new Intent(context, ChatListActivity.class);
        i.putExtras(intent.getExtras());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    @Override
    public void onPushReceive(Context context, Intent intent) {
        Log.i(TAG, "onPushReceive triggered!");

        JSONObject pushData;

        try {
            pushData = new JSONObject(intent.getStringExtra(PushReceiver.KEY_PUSH_DATA));

            senderName = pushData.getString("senderName");
            senderId = pushData.getString("senderId");
            message = pushData.getString("message");
            action = pushData.getString("action");
            recieverId = pushData.getString("receiverId");



        } catch (JSONException e) {}


        ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        String packageName = am.getRunningTasks(1).get(0).topActivity.getPackageName();

        if (action != null && action.equals("launch")){
            Intent i = new Intent(context, LoginActivity.class);
            i.putExtra("name", senderName);
            i.putExtras(intent.getExtras());
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //           context.startActivity(i);
        }

        if (!packageName.equals("fi.benson")){

            Intent cIntent = new Intent(PushReceiver.ACTION_PUSH_OPEN);
            cIntent.putExtras(intent.getExtras());
            cIntent.setPackage(context.getPackageName());

            // WE SHOULD HANDLE DELETE AS WELL
            // BUT IT'S NOT HERE TO SIMPLIFY THINGS!

            PendingIntent pContentIntent =
                    PendingIntent.getBroadcast(context, 0 /*just for testing*/, cIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(senderName)
                    .setContentText(message)
                    .setContentIntent(pContentIntent)
                    .setVibrate(v)
                    .setSound(uri)
                    .setAutoCancel(true);


            NotificationManager myNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            myNotificationManager.notify(1, builder.build());

        }
    }

    public void showDialog(Context context){
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("This will make your item invisible")
                .setCancelText("No")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        // reuse previous dialog instance, keep widget user state, reset them if you need
                        sDialog.setTitleText("Cancelled!")
                                .setContentText("Your item is safe :)")
                                .setConfirmText("OK")
                                .showCancelButton(false)
                                .setCancelClickListener(null)
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.setTitleText("Marked as Sold!")
                                .setContentText("Lets sell more ...")
                                .setConfirmText("OK")
                                .showCancelButton(false)
                                .setCancelClickListener(null)
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    }
                })
                .show();
    }



}