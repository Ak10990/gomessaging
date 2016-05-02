package com.message.gomessaging;

import android.app.Application;

import com.message.gomessaging.dbmodels.Message;
import com.message.gomessaging.helpers.SmsHelper;
import com.message.gomessaging.utils.Logger;

import java.util.ArrayList;

public class GoMessagingApp extends Application {

    public static GoMessagingApp mInstance;
    private ArrayList<Message> messagesList;

    public static GoMessagingApp getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initLogger();
        initMessages();
    }

    private void initMessages() {
        messagesList = SmsHelper.fetchAllSms(this);
    }

    private void initLogger() {
        Logger.enableLogging(BuildConfig.DEBUG);
    }

    public ArrayList<Message> getMessagesList() {
        return messagesList;
    }
}
