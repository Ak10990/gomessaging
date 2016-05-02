package com.message.gomessaging.flows;

import android.provider.Telephony;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.message.gomessaging.R;
import com.message.gomessaging.dbmodels.Message;

import java.util.List;

/**
 * Created by Akanksha on 27/4/16.
 * MessagesRecyclerAdapter for List
 */
public class MessagesDetailRecyclerAdapter
        extends RecyclerView.Adapter<MessagesDetailRecyclerAdapter.ViewHolder> {

    private final List<Message> mValues;
    private final RecyclerClickCallback clickCallback;

    public MessagesDetailRecyclerAdapter(List<Message> items, RecyclerClickCallback clickCallback) {
        mValues = items;
        this.clickCallback = clickCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Message message = mValues.get(position);

        switch (message.getType()) {
            case Telephony.TextBasedSmsColumns.MESSAGE_TYPE_INBOX:
                holder.youView.setText(message.getBody());
                holder.meView.setVisibility(View.GONE);
                holder.youView.setVisibility(View.VISIBLE);
                break;
            case Telephony.TextBasedSmsColumns.MESSAGE_TYPE_SENT:
            case Telephony.TextBasedSmsColumns.MESSAGE_TYPE_DRAFT:
            case Telephony.TextBasedSmsColumns.MESSAGE_TYPE_OUTBOX:
            case Telephony.TextBasedSmsColumns.MESSAGE_TYPE_FAILED:
            case Telephony.TextBasedSmsColumns.MESSAGE_TYPE_QUEUED:
                holder.meView.setText(message.getBody());
                holder.youView.setVisibility(View.GONE);
                holder.meView.setVisibility(View.VISIBLE);
                break;
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickCallback != null)
                    clickCallback.onItemClicked(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView youView;
        public final TextView meView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            youView = (TextView) view.findViewById(R.id.you_message);
            meView = (TextView) view.findViewById(R.id.me_message);
        }
    }
}