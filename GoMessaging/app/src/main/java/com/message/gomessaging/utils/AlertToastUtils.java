package com.message.gomessaging.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

/**
 * AlertToastUtils
 * Created by Akanksha on 24/4/16.
 */
public class AlertToastUtils {

    public static void createSnackbar(View view, String text) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    public static void createToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
