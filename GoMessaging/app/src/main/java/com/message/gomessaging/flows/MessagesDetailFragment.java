package com.message.gomessaging.flows;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.message.gomessaging.R;
import com.message.gomessaging.dbmodels.Message;
import com.message.gomessaging.helpers.MessageModelHelper;
import com.message.gomessaging.helpers.SmsHelper;
import com.message.gomessaging.utils.AlertToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akanksha on 27/4/16.
 * A fragment representing a single Message detail screen.
 * This fragment is contained in {@link MessagingActivity}
 */
public class MessagesDetailFragment extends Fragment implements RecyclerClickCallback, View.OnClickListener {

    public static final String MESSAGE_USER = "message_user";
    private EditText enterRecipientsEt, etEnterMessages;
    private List<Message> messagesDetailList;

    public MessagesDetailFragment() {
    }

    public static MessagesDetailFragment getInstance(String user) {
        MessagesDetailFragment fragment = new MessagesDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putString(MessagesDetailFragment.MESSAGE_USER, user);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(MESSAGE_USER)) {
            String args;
            if (Build.VERSION.SDK_INT >= 12) {
                args = getArguments().getString(MESSAGE_USER, null);
            } else {
                args = (String) getArguments().get(MESSAGE_USER);
            }
            messagesDetailList = MessageModelHelper.getMessagesByUser(args);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_messaging_detail, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View rootView) {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.message_list);
        enterRecipientsEt = (EditText) rootView.findViewById(R.id.et_enter_recipients);
        etEnterMessages = (EditText) rootView.findViewById(R.id.et_enter_messages);
        if (messagesDetailList != null && messagesDetailList.size() > 0) {
            enterRecipientsEt.setVisibility(View.GONE);
            if (recyclerView != null) {
                recyclerView.setAdapter(new MessagesDetailRecyclerAdapter(messagesDetailList, this));
            }
        } else {
            enterRecipientsEt.setVisibility(View.VISIBLE);
        }
        FloatingActionButton sendFabBtn = (FloatingActionButton) rootView.findViewById(R.id.send_fab_btn);
        sendFabBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_fab_btn:
                String recipient = enterRecipientsEt.getText().toString();
                String message = etEnterMessages.getText().toString();
                if (TextUtils.isEmpty(recipient) && (messagesDetailList == null || messagesDetailList.size() == 0)) {
                    AlertToastUtils.createToast(getActivity(), getString(R.string.err_no_recepient));
                } else if (TextUtils.isEmpty(message)) {
                    AlertToastUtils.createToast(getActivity(), getString(R.string.err_no_message));
                } else {
                    ArrayList<String> phoneList = new ArrayList<>();
                    if (TextUtils.isEmpty(recipient)) {
                        recipient = messagesDetailList.get(0).getFromTo();
                    }
                    phoneList.add(recipient);
                    composeMessage(phoneList, message);
                }
                break;
        }
    }

    protected void composeMessage(ArrayList<String> phoneList, String message) {
        if (!SmsHelper.sendSMSMessage(phoneList, message, getSentIntent(), getDeliveryIntent())) {
            AlertToastUtils.createToast(getActivity(), getString(R.string.sms_failed));
        }
    }

    private PendingIntent getSentIntent() {
        String SENT = "sent";

        Intent sentIntent = new Intent(SENT);
        PendingIntent sentPI = PendingIntent.getBroadcast(getActivity(), 0, sentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        getActivity().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String result = "";
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        AlertToastUtils.createToast(getActivity(), getString(R.string.sms_sent));
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        result = " Transmission failed";
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        result = " Radio off";
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        result = " No PDU defined";
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        result = " No service";
                        break;
                }
                if (!TextUtils.isEmpty(result)) {
                    AlertToastUtils.createToast(getActivity(), getString(R.string.sms_failed, result));
                }
            }
        }, new IntentFilter(SENT));
        return sentPI;
    }

    private PendingIntent getDeliveryIntent() {
        String DELIVERED = "delivered";
        Intent deliveryIntent = new Intent(DELIVERED);
        PendingIntent deliverPI = PendingIntent.getBroadcast(getActivity(), 0, deliveryIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        getActivity().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        //      "Delivered"
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));
        return deliverPI;
    }

    @Override
    public void onItemClicked(int position) {
    }
}
