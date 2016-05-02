package com.message.gomessaging.helpers;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsManager;

import com.message.gomessaging.dbmodels.Message;
import com.message.gomessaging.utils.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Akanksha on 26/4/16.
 * Helper for all sms features.
 */
public class SmsHelper {

    public static ArrayList<Message> fetchAllSms(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        HashMap<Long, Message> smsMap = new HashMap<>();

        smsMap.putAll(fetchAllSms(contentResolver, smsMap));

        return new ArrayList<>(smsMap.values());
    }

    private static HashMap<Long, Message> fetchAllSms(ContentResolver contentResolver, HashMap<Long, Message> smsMap) {
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/"), null, null, null, null);
        if (smsInboxCursor != null) {
            //COLUMN NAMES
            //_id || thread_id || address || person || date || date_sent || protocol ||
            // read || status || type || reply_path_present || subject || body || service_center ||
            // locked || error_code || sub_id || creator || seen || deletable || sim_slot || sim_imsi ||
            // hidden || group_id || group_type || delivery_date || app_id || msg_id || callback_number ||
            // reserved || pri || teleservice_id || link_url || svc_cmd || svc_cmd_content || roam_pending ||
            // spam_report || safe_message ||

            int index = smsInboxCursor.getColumnIndex("_id");
            int bodyIndex = smsInboxCursor.getColumnIndex("body");
            int fromIndex = smsInboxCursor.getColumnIndex("address");
            int typeIndex = smsInboxCursor.getColumnIndex("type");
            int dateIndex = smsInboxCursor.getColumnIndex("date");

            long id;
            String body, user;
            int type;
            Date date;
            if (index < 0 || !smsInboxCursor.moveToFirst()) return new HashMap<>();
            do {
                id = smsInboxCursor.getLong(index);
                body = smsInboxCursor.getString(bodyIndex);
                user = smsInboxCursor.getString(fromIndex);
                type = smsInboxCursor.getInt(typeIndex);
                date = new Date(smsInboxCursor.getLong(dateIndex));

                Message message = new Message(id, date, user, body, type);
                smsMap.put(id, message);
            } while (smsInboxCursor.moveToNext());
            smsInboxCursor.close();
        }
        return smsMap;
    }

    public static boolean sendSMSMessage(List<String> phoneNos, String message, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            for (String phoneNo : phoneNos) {
                smsManager.sendTextMessage(phoneNo, null, message, sentIntent, deliveryIntent);
            }
            return true;
        } catch (Exception e) {
            Logger.e(e.getMessage());
            return false;
        }
    }
}
