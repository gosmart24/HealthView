package com.cybertech.healthview;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class HealthViewInstanceIDService extends FirebaseInstanceIdService {

    TinyDB tinyDB;
    String token;

    @Override
    public void onTokenRefresh() {
        token = FirebaseInstanceId.getInstance().getToken();
        tinyDB = new TinyDB(getApplicationContext());
        // saving token to share pref storage
       // tinyDB.putString("token", token);
        boolean checkToken = checkToken(token);
        tinyDB.putBoolean("isTokenReady", checkToken);

        Log.d("TAG", "Generated token : " + token);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
       // Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
       // registrationComplete.putExtra("token", token);
       // LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private boolean checkToken(String token) {
        boolean istokenReady;

        if (!TextUtils.isEmpty(token)) {
            tinyDB.putString("token", token);
            istokenReady = true;
        } else {
            istokenReady = false;
        }
        return istokenReady;
    }

}
