package com.message.gomessaging.flows;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.message.gomessaging.R;
import com.message.gomessaging.dbmodels.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akanksha on 27/4/16.
 * A fragment representing a single Message detail screen.
 * This fragment is contained in {@link MessagingActivity}
 */
public class MessagingListFragment extends Fragment implements RecyclerClickCallback {

    private final static String MESSAGE_LIST_ARGS = "MESSAGE_LIST_ARGS";
    private MessagingActivityFragmentCallback messageActivityCallback;
    private List<Message> messagesList = new ArrayList<>();

    public MessagingListFragment() {
    }

    public static MessagingListFragment getInstance(ArrayList<Message> messages) {
        MessagingListFragment messagingListFragment = new MessagingListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(MESSAGE_LIST_ARGS, messages);
        messagingListFragment.setArguments(args);
        return messagingListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(MESSAGE_LIST_ARGS)) {
            messagesList = getArguments().getParcelableArrayList(MESSAGE_LIST_ARGS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_messaging_list, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View rootView) {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.message_list);
        TextView noMessagesPlaceholder = (TextView) rootView.findViewById(R.id.no_messages_placeholder);
        if (messagesList != null && messagesList.size() > 0) {
            noMessagesPlaceholder.setVisibility(View.GONE);
            if (recyclerView != null) {
                recyclerView.setAdapter(new MessagesRecyclerAdapter(messagesList, this));
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MessagingActivity) {
            messageActivityCallback = (MessagingActivity) context;
        } else if (context instanceof MessagingSearchActivity) {
            messageActivityCallback = (MessagingSearchActivity) context;
        } else {
            throw new IllegalArgumentException("This fragment must belong to " +
                    MessagingActivity.class.getSimpleName() + " or " +
                    MessagingSearchActivity.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        messageActivityCallback = null;
    }

    @Override
    public void onItemClicked(int position) {
        messageActivityCallback.messageListDetailCall(messagesList.get(position));
    }
}
