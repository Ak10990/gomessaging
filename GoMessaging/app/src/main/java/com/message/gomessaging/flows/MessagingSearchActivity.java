package com.message.gomessaging.flows;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;

import com.message.gomessaging.R;
import com.message.gomessaging.dbmodels.FragmentType;
import com.message.gomessaging.dbmodels.Message;
import com.message.gomessaging.helpers.MessageModelHelper;

import java.util.ArrayList;

/**
 * Created by Akanksha on 01/5/16.
 * An activity representing Messages Search by query.
 */
public class MessagingSearchActivity extends AppCompatActivity implements MessagingActivityFragmentCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        setContentView(R.layout.activity_messaging);
        initToolbar();
        handleIntent(getIntent());
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    private void doMySearch(String query) {
        ArrayList<Message> messagesList = MessageModelHelper.getMessagesBySearchQuery(query);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (messagesList != null)
                actionBar.setTitle(messagesList.size() + " results found for \"" + query + "\"");
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frag_container, MessagingListFragment.getInstance(messagesList)).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }

    @Override
    public void messageListDetailCall(Message message) {
        String selectedUserAtPosition = message.getFromTo();
        if (!TextUtils.isEmpty(selectedUserAtPosition)) {
            Intent intent = new Intent(this, MessagingActivity.class);
            intent.putExtra(MessagesDetailFragment.MESSAGE_USER, selectedUserAtPosition);
            intent.putExtra(MessagingActivity.FRAGMENT_ID_TYPE, FragmentType.MESSAGE_DETAIL.ordinal());
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        }
    }
}