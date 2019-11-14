package com.cybertech.healthview;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PatientActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private EditText message_ED;
    private DatabaseReference mReference;
    private TinyDB tinyDB;
    private BookingModel bookingModel;
    private PatientModel patient;
    private ProgressDialog dialog;
    private ProgressDialog progressDialog;
    private String username;
    private String pass;
    private MyConnection request;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        message_ED = (EditText) findViewById(R.id.booking_MSG);

        tinyDB = new TinyDB(this);
        bookingModel = new BookingModel();

        mReference = FirebaseDatabase.getInstance().getReference("HealthView").child("books");

        patient = tinyDB.getObject("user", PatientModel.class);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(PatientActivity.this, LoginActivity.class));
                    finish();
                }
            });
            builder.setNegativeButton("No", null);
            builder.setMessage("Do you want to really Logout?");
            builder.setTitle("Exit");
            builder.show();
            // super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_manage) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(PatientActivity.this, LoginActivity.class));
                    finish();
                }
            });
            builder.setNegativeButton("No", null);
            builder.setMessage("Do you want to really Logout?");
            builder.setTitle("Exit");
            builder.show();
            // super.onBackPressed();
        } else if (id == R.id.nav_pass) {
            startActivity(new Intent(PatientActivity.this, PasswordActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onCreateBooking(View view) {

        String current_time = new SimpleDateFormat("hh:mm:ss a dd-MM-yyyy").format(new Date());

        if (TextUtils.isEmpty(message_ED.getText().toString())) {
            Toast.makeText(PatientActivity.this, "Sorry required field cannot be empty!", Toast.LENGTH_SHORT).show();

        } else {
            String msg = message_ED.getText().toString();
            DatabaseReference reference = mReference.push();
            dialog = ProgressDialog.show(PatientActivity.this, null, "Inserting record...", true, true);
            bookingModel.setMessage(msg);
            bookingModel.setName(patient.getFirstname() + " " + patient.getSurname());
            bookingModel.setTime(current_time);
            bookingModel.setResponse("0");
            bookingModel.setResponsetime("0");
            bookingModel.setPatient(patient);
            bookingModel.setFlag("0");
            bookingModel.setConfirmation("0");
            bookingModel.setKey(patient.getIdNo());
            MyConnection connect;
            RequestQueue request = Volley.newRequestQueue(getApplicationContext());

            // Success listener
            Response.Listener<String> listen = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.i("Bookingsa", response);
                    JSONObject jsonObject = null;
                    boolean status = false;

                    try {
                        jsonObject = new JSONObject(response);
                        status = jsonObject.getBoolean("success");
                        message = jsonObject.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                    if (status) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PatientActivity.this);
                        builder.setTitle("Book Alert")
                                .setMessage(message)
                                .setCancelable(false)
                                .setNegativeButton("Close", null)
                                .show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PatientActivity.this);
                        builder.setTitle("Book Alert")
                                .setMessage(message)
                                .setCancelable(false)
                                .setNegativeButton("Close", null)
                                .show();
                    }


                }
            };
            // error listener
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PatientActivity.this);
                    builder.setTitle("Booking Error")
                            .setMessage(error.getMessage())
                            .setCancelable(false)
                            .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(PatientActivity.this, PatientActivity.class));
                                    finish();
                                }
                            })
                            .show();
                }
            };
           connect = new MyConnection(bookingModel, listen, errorListener);
           request.add(connect);
            reference.setValue(bookingModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    dialog.dismiss();
                    if (task.isSuccessful()) {

                        RemoteMessage message = new RemoteMessage.Builder("doctor")
                                .addData("hb", "87")
                                .addData("tp", "53")
                                .addData("latitude", "9.4")
                                .addData("longitude", "9.12")
                                .build();
                        FirebaseMessaging.getInstance().send(message);
//                        // send appointment push notifications to doctors from here
//                        Notification.MessagingStyle.Message message = new Notification.MessagingStyle.Message()
//                                .putData("score", "850")
//                                .putData("time", "2:45")
//                                .setTopic(topic)
//                                .build();
//
//                        //Send a message to the devices subscribed to the provided topic.
//                        String response = FirebaseMessaging.getInstance().send(message);
//                        //Response is a message ID string.
//                        AlertDialog.Builder builder = new AlertDialog.Builder(PatientActivity.this);
//                        builder.setTitle("Book Appointment")
//                                .setMessage("You have successfully booked an  Appointment!")
//                                .setCancelable(true)
//                                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        PatientActivity.this.startActivity(new Intent(PatientActivity.this, PatientActivity.class));
//                                        finish();
//                                    }
//                                })
//                                .show();
                    } else {
                        dialog.dismiss();
//                        AlertDialog.Builder builder = new AlertDialog.Builder(PatientActivity.this);
//                        builder.setTitle("Appointment")
//                                .setMessage("Unable to book appointment now please try again later!")
//                                .setCancelable(true)
//                                .setNegativeButton("Close", null)
//                                .show();
                    }

                }

            });
        }
    }
}