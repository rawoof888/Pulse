package com.hive3.pulse.Utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Random;

/**
 * Created by rawoof on 17/04/16.
 */
public class Utils {


    public static void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)MyApplication.getAppContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}

