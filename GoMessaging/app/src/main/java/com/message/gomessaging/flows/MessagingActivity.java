package com.message.gomessaging.flows;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.message.gomessaging.R;
import com.message.gomessaging.dbmodels.FragmentType;
import com.message.gomessaging.dbmodels.Message;
import com.message.gomessaging.helpers.MessageModelHelper;

/**
 * Created by Akanksha on 27/4/16.
 * An activity representing Messages.
 */
public class MessagingActivity extends AppCompatActivity implements MessagingActivityFragmentCallback {

    public static final String FRAGMENT_ID_TYPE = "FRAGMENT_ID_TYPE";
    private final Object objectLock = new Object();
    private String selectedUserAtPosition = "";
    private int chooserType = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        initValues();
        initToolbar();
        initViews();
    }

    private void initValues() {
        if (getIntent().hasExtra(FRAGMENT_ID_TYPE)) {
            chooserType = getIntent().getExtras().getInt(FRAGMENT_ID_TYPE);
        } else {
            chooserType = FragmentType.MESSAGE_LIST.ordinal();
        }

        if ((chooserType == FragmentType.MESSAGE_DETAIL.ordinal() ||
                chooserType == FragmentType.MESSAGE_NOTIFICATION.ordinal())
                && getIntent().hasExtra(MessagesDetailFragment.MESSAGE_USER)) {
            selectedUserAtPosition = getIntent().getExtras().getString(MessagesDetailFragment.MESSAGE_USER);
        } else {
            selectedUserAtPosition = "";
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initViews() {
        boolean isNotification = (chooserType == FragmentType.MESSAGE_NOTIFICATION.ordinal());
        if (isNotification) {
            chooserType = FragmentType.MESSAGE_LIST.ordinal();
        }
        substituteFragment();
        substituteToolbar();
        if (isNotification) {
            replaceFragment(FragmentType.MESSAGE_DETAIL.ordinal());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (chooserType == FragmentType.MESSAGE_LIST.ordinal()) {
            getMenuInflater().inflate(R.menu.menu_messaging, menu);

            // Retrieve the SearchView and plug it into SearchManager
            final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
            SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_compose:
                replaceFragment(FragmentType.MESSAGE_COMPOSE.ordinal());
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void substituteFragment() {
        synchronized (objectLock) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (chooserType == FragmentType.MESSAGE_LIST.ordinal()) {
                ft.add(R.id.frag_container, MessagingListFragment.getInstance(
                        MessageModelHelper.getUniqueUserLatestMessages())).commit();
            } else if (chooserType == FragmentType.MESSAGE_DETAIL.ordinal()) {
                if (!selectedUserAtPosition.equals("")) {
                    ft.replace(R.id.frag_container, MessagesDetailFragment.getInstance(selectedUserAtPosition)).commit();
                }
            } else if (chooserType == FragmentType.MESSAGE_COMPOSE.ordinal()) {
                ft.replace(R.id.frag_container, MessagesDetailFragment.getInstance(null)).commit();
            } else {
                throw new IllegalArgumentException("Invalid type");
            }
        }
    }

    private void substituteToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (chooserType == FragmentType.MESSAGE_LIST.ordinal()) {
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setTitle(getString(R.string.app_name));
            } else if (chooserType == FragmentType.MESSAGE_DETAIL.ordinal()) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle(selectedUserAtPosition);
            } else if (chooserType == FragmentType.MESSAGE_COMPOSE.ordinal()) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle(getString(R.string.new_message));
            }
        }
    }

    private void replaceFragment(int ordinal) {
        synchronized (objectLock) {
            Intent intent = null;
            if (ordinal == FragmentType.MESSAGE_LIST.ordinal()) {
                intent = new Intent(this, MessagingActivity.class);
                intent.putExtra(FRAGMENT_ID_TYPE, FragmentType.MESSAGE_LIST.ordinal());
            } else if (ordinal == FragmentType.MESSAGE_DETAIL.ordinal()) {
                if (!TextUtils.isEmpty(selectedUserAtPosition)) {
                    intent = new Intent(this, MessagingActivity.class);
                    intent.putExtra(MessagesDetailFragment.MESSAGE_USER, selectedUserAtPosition);
                    intent.putExtra(FRAGMENT_ID_TYPE, FragmentType.MESSAGE_DETAIL.ordinal());
                }
            } else if (ordinal == FragmentType.MESSAGE_COMPOSE.ordinal()) {
                intent = new Intent(this, MessagingActivity.class);
                intent.putExtra(FRAGMENT_ID_TYPE, FragmentType.MESSAGE_COMPOSE.ordinal());
            } else {
                throw new IllegalArgumentException("Invalid type");
            }
            if (intent != null) {
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (chooserType != FragmentType.MESSAGE_LIST.ordinal()) {
            overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        }
    }

    /**
     * Message List -> Detail
     */
    @Override
    public void messageListDetailCall(Message message) {
        selectedUserAtPosition = message.getFromTo();
        replaceFragment(FragmentType.MESSAGE_DETAIL.ordinal());
    }
}