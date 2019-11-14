package com.cybertech.healthview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.Response;

/**
 * Created by Samuel Adakole King on 7/4/2019.
 */

public class Utility {

    public static boolean checkConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
        return isConnected;
    }


    public static void showmsg(Context context,String title, String Msg, DialogInterface.OnClickListener listener, DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(Msg);
        builder.setNegativeButton("Exit",cancelListener);
        builder.setPositiveButton("Continue",listener);
        builder.show();

    }

}
