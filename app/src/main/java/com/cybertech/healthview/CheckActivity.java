package com.cybertech.healthview;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.messaging.FirebaseMessaging;

public class CheckActivity extends AppCompatActivity implements View.OnClickListener {

    TinyDB tinyDB;
    RadioGroup group;
    RadioButton radioButton;
    Button submit;
    boolean accountRegisted;
    // private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tinyDB = new TinyDB(this);

        try {
            //  accountRegisted = tinyDB.getBoolean("isRegisted");
            accountRegisted = tinyDB.getBoolean("isRegisted");
            if (accountRegisted){
                startActivity(new Intent(CheckActivity.this, LoginActivity.class));
                finish();
            } else {
                // load default user home.
                setContentView(R.layout.activity_check);
                group = (RadioGroup)findViewById(R.id.checkRadio_group_id);
                radioButton = (RadioButton)findViewById(R.id.admin_id);
                submit = (Button)findViewById(R.id.submit_id);
                submit.setOnClickListener(this);

            }
        }catch (Exception e){
            setContentView(R.layout.activity_check);
            group = (RadioGroup)findViewById(R.id.checkRadio_group_id);
            radioButton = (RadioButton)findViewById(R.id.admin_id);
            submit = (Button)findViewById(R.id.submit_id);
            submit.setOnClickListener(this);

            //startActivity(new Intent(CheckActivity.this, PatientActivity.class));
        }

    }

    @Override
    public void onClick(View v) {
        int id = group.getCheckedRadioButtonId();
        switch (id) {
            case R.id.admin_id:
                tinyDB.putString("account", "admin");
                tinyDB.putBoolean("isRegisted",true);
                FirebaseMessaging.getInstance().subscribeToTopic("admin");
                startActivity(new Intent(CheckActivity.this, LoginActivity.class));
                finish();
                break;

            case R.id.doctor_id:
                tinyDB.putString("account", "doctor");
                tinyDB.putBoolean("isRegisted",true);
                FirebaseMessaging.getInstance().subscribeToTopic("doctor");
                startActivity(new Intent(CheckActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.nurse_id:
                tinyDB.putString("account", "nurse");
                tinyDB.putBoolean("isRegisted",true);
                FirebaseMessaging.getInstance().subscribeToTopic("nurse");
                startActivity(new Intent(CheckActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.lab_id:
                tinyDB.putString("account", "lab");
                tinyDB.putBoolean("isRegisted",true);
                FirebaseMessaging.getInstance().subscribeToTopic("lab");
                startActivity(new Intent(CheckActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.patient_id:
                tinyDB.putString("account", "patient");
                tinyDB.putBoolean("isRegisted",true);
                FirebaseMessaging.getInstance().subscribeToTopic("patient");
                startActivity(new Intent(CheckActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.pharmacy_id:
                tinyDB.putString("account", "pharmacy");
                tinyDB.putBoolean("isRegisted",true);
                FirebaseMessaging.getInstance().subscribeToTopic("Pharmacist");
                startActivity(new Intent(CheckActivity.this, LoginActivity.class));
                finish();
                break;
        }
    }
}

