package com.message.gomessaging.helpers;

import android.text.TextUtils;

import com.message.gomessaging.GoMessagingApp;
import com.message.gomessaging.dbmodels.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Akanksha on 29/4/16.
 * Helper to query from messages List
 */
public class MessageModelHelper {

    public static ArrayList<Message> getUniqueUserLatestMessages() {
        ArrayList<Message> messagesList = new ArrayList<>();
        Set<String> hashMessages = new HashSet<>();
        ArrayList<Message> initList = GoMessagingApp.getInstance().getMessagesList();
        Collections.reverse(initList);
        for (Message message : initList) {
            if (hashMessages.add(message.getFromTo())) {
                messagesList.add(message);
            }
        }
        //Sort by date
        Collections.sort(messagesList, new Comparator<Message>() {
            @Override
            public int compare(final Message lhs, final Message rhs) {
                if (lhs.getDate().after(rhs.getDate()))
                    return -1;
                else if (lhs.getDate().before(rhs.getDate()))
                    return 1;
                else
                    return lhs.getFromTo().compareTo(rhs.getFromTo());
            }
        });
        return messagesList;
    }

    public static List<Message> getMessagesByUser(String user) {
        List<Message> messages = new ArrayList<>();
        if (TextUtils.isEmpty(user)) {
            return messages;
        }
        for (Message message : GoMessagingApp.getInstance().getMessagesList()) {
            if (user.equals(message.getFromTo())) {
                messages.add(0, message);
            }
        }
        return messages;
    }

    public static ArrayList<Message> getMessagesBySearchQuery(String query) {
        ArrayList<Message> messages = new ArrayList<>();
        if (TextUtils.isEmpty(query)) {
            return null;
        }
        for (Message message : GoMessagingApp.getInstance().getMessagesList()) {
            if (message.getBody().toLowerCase().contains(query.toLowerCase())) {
                messages.add(0, message);
            }
        }
        return messages;
    }

}
