package com.message.gomessaging.flows;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.message.gomessaging.R;
import com.message.gomessaging.dbmodels.Message;
import com.message.gomessaging.utils.DateUtils;

import java.util.List;

/**
 * Created by Akanksha on 27/4/16.
 * MessagesRecyclerAdapter for List
 */
public class MessagesRecyclerAdapter
        extends RecyclerView.Adapter<MessagesRecyclerAdapter.ViewHolder> {

    private final List<Message> mValues;
    private final RecyclerClickCallback clickCallback;

    public MessagesRecyclerAdapter(List<Message> items, RecyclerClickCallback clickCallback) {
        mValues = items;
        this.clickCallback = clickCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Message message = mValues.get(position);
        holder.mIdView.setText(message.getFromTo());
        holder.mDateView.setText(DateUtils.getDisplayDate(message.getDate()));
        holder.mContentView.setText(message.getBody());
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
        public final TextView mIdView;
        public final TextView mDateView;
        public final TextView mContentView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mDateView = (TextView) view.findViewById(R.id.date);
            mContentView = (TextView) view.findViewById(R.id.content);
        }
    }
}