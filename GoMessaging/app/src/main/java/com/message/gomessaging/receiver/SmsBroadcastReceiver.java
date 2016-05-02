package com.message.gomessaging.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.NotificationCompat.Builder;
import android.telephony.SmsMessage;

import com.message.gomessaging.R;
import com.message.gomessaging.dbmodels.FragmentType;
import com.message.gomessaging.flows.MessagesDetailFragment;
import com.message.gomessaging.flows.MessagingActivity;

/**
 * Created by Akanksha on 30/4/16.
 * A BroadcastReceiver for Sms Notification.
 */
public class SmsBroadcastReceiver extends BroadcastReceiver {

    public static final String SMS_BUNDLE = "pdus";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            //Create SmsMessage[]
            SmsMessage[] msgs = null;
            if (Build.VERSION.SDK_INT >= 19) {
                msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            } else {
                Object[] smses = (Object[]) intentExtras.get(SMS_BUNDLE);
                if (smses != null) {
                    msgs = new SmsMessage[smses.length];
                    for (int i = 0; i < smses.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) smses[i]);
                    }
                }
            }
            String smsMessageStr = "";
            if (msgs != null && msgs.length > 0) {
                String smsFrom = "";

                //Create notification display string
                for (SmsMessage smsMessage : msgs) {
                    String smsBody = smsMessage.getMessageBody();
                    String address = smsMessage.getOriginatingAddress();
                    smsFrom = address;
                    smsMessageStr += address + " : ";
                    smsMessageStr += smsBody + "\n";
                }

                //Build notification
                Builder mBuilder = new Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(msgs.length + " New Message")
                        .setContentText(smsMessageStr)
                        .setAutoCancel(true);

                //Add click intent
                Intent resultIntent = new Intent(context, MessagingActivity.class);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                resultIntent.putExtra(MessagingActivity.FRAGMENT_ID_TYPE, FragmentType.MESSAGE_NOTIFICATION.ordinal());
                resultIntent.putExtra(MessagesDetailFragment.MESSAGE_USER, smsFrom);
                PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(resultPendingIntent);

                //NotificationManager
                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(0, mBuilder.build());
            }
        }
    }
}