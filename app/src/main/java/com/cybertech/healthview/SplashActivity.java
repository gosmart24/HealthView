package com.cybertech.healthview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

public class SplashActivity extends AppCompatActivity {

    ProgressBar loadingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        loadingProgress = (ProgressBar) findViewById(R.id.progressBar);
        loadingProgress.setIndeterminate(true);
       final boolean conn = Utility.checkConnection(SplashActivity.this);
        Thread welcomethread = new Thread() {
            public void run() {
                try {
                    sleep(5000);
                    startActivity(new Intent(SplashActivity.this, CheckActivity.class));
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    finish();
                }
            }
        };
        welcomethread.start();

    }
}


// if (conn) {
//
//            Utility.showmsg(SplashActivity.this, "Connectivity Alert!", "No Network Connection! this App require a Network to Function. Do you want to Continue?",
//                    new OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            // continue btn pressed.
//                            // start welcome Activity and finish(); the splash screen.
//                            startActivity(new Intent(SplashActivity.this, CheckActivity.class));
//                            finish();
//                        }
//                    }, new OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            // cancel button press
//                            System.exit(0);
//                        }
//                    });
//        } else {
//
//            Utility.showmsg(SplashActivity.this, "Connectivity Alert!", "No Network Connection! this App require a Network to Function. Do you want to Continue?",
//                    new OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            // continue btn pressed.
//                            // start welcome Activity and finish(); the splash screen.
//                            startActivity(new Intent(SplashActivity.this, CheckActivity.class));
//                            finish();
//                        }
//                    }, new OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            // cancel button press
//                            System.exit(0);
//                        }
//                    });
//        }
//