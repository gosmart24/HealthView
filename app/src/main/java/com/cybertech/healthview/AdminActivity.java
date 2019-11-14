package com.cybertech.healthview;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

public class AdminActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private TinyDB tinyDB;
    PatientModel model;
    EditText reg_first_name_ed, reg_surname_ed, address_ed, deviceID_ed, age_ed, reg_phoneNo_ed, reg_temp_ed, reg_E_hbeat_ed, reg_bpressure_ed;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    private Spinner spinner_gender;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tinyDB = new TinyDB(this);
        model = new PatientModel();
        Log.i("TOKEN", tinyDB.getString("token"));
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        age_ed = (EditText) findViewById(R.id.reg_age_pa);
        deviceID_ed = (EditText) findViewById(R.id.reg_deviceID_pa);
        reg_first_name_ed = (EditText) findViewById(R.id.reg_firstname_pa);
        reg_surname_ed = (EditText) findViewById(R.id.reg_surname_pa);
        address_ed = (EditText) findViewById(R.id.reg_address_pa);
        reg_phoneNo_ed = (EditText) findViewById(R.id.reg_phoneNo_pa);
        spinner_gender = (Spinner) findViewById(R.id.reg_spinner_gender_pa);

        databaseReference = FirebaseDatabase.getInstance().getReference("HealthView").child("patients");

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
                    startActivity(new Intent(AdminActivity.this, LoginActivity.class));
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

        if (id == R.id.nav_add_staff) {
            // navigate to add staff page.
            startActivity(new Intent(AdminActivity.this, AddStaffActivity.class));
            finish();
        } else if (id == R.id.nav_edit_staff) {
            // navigate to edit staff page.
            startActivity(new Intent(AdminActivity.this, EditActivity.class));
            finish();
        } else if (id == R.id.nav_add_patient) {
            // navigate to add patient page.
            startActivity(new Intent(AdminActivity.this, AdminActivity.class));
            finish();
        } else if (id == R.id.nav_logOut) {
            // logout from here.
            System.exit(0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onCreatePatient(View view) {
        String firstname = reg_first_name_ed.getText().toString();
        String surname = reg_surname_ed.getText().toString();
        String device_ID = deviceID_ed.getText().toString();
        String address = address_ed.getText().toString();
        String age = age_ed.getText().toString();
        String phone = reg_phoneNo_ed.getText().toString();
        final String gender = spinner_gender.getSelectedItem().toString();

        if (TextUtils.isEmpty(firstname) || TextUtils.isEmpty(surname) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(phone)
                || TextUtils.isEmpty(device_ID) || TextUtils.isEmpty(address)  || TextUtils.isEmpty(age)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setNegativeButton("Close", null);
            builder.setMessage("Required fields cannot be empty!");
            builder.setTitle("Error! Empty field Alert");
            builder.show();
        } else {
            progressDialog = ProgressDialog.show(AdminActivity.this, null, "Processing...", true, true);
            final DatabaseReference ref = databaseReference.push();

            model.setFirstname(firstname);
            model.setSurname(surname);
            model.setPhone(phone);
            model.setAddress(address);
            model.setAge(age);
            model.setGender(gender);
            model.setIdNo(device_ID);
            model.setPassword("0000");
            model.setTimelineModel(new ArrayList<TimelineModel>());
            model.setHealthModel(new HealthModel("32", "87", "severe", 00.0, 00.0));
            model.setKey(ref.getKey());
            String token = (tinyDB.getBoolean("isTokenReady")) ? tinyDB.getString("token") : "";
            model.setTokenKey(token);
            model.setUsername(model.getIdNo());

            MyConnection connect;
            RequestQueue request = Volley.newRequestQueue(AdminActivity.this);

            // Success listener
            Response.Listener<String> listen = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Log.i("Thread", "response: " + response);
                    progressDialog.dismiss();
                    Log.e("Account", response);
                    JSONObject jsonObject = null;
                    boolean status = false;

                    try {
                        jsonObject = new JSONObject(response);
                        status = jsonObject.getBoolean("success");
                        message = jsonObject.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (status){
                        ref.setValue(model).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Error has occurred on uploading details with this Message : " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
                                    builder.setTitle("Account Alert")
                                            .setMessage(message)
                                            .setCancelable(false)
                                            .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    startActivity(new Intent(AdminActivity.this, AdminActivity.class));
                                                    finish();
                                                }
                                            })
                                            .show();

                                    Toast.makeText(getApplicationContext(), "Patient details has been successfully added to database!", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Error has occurred while registering patient profile", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
                        builder.setTitle("Account Alert")
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
                    progressDialog.dismiss();
                    Log.i("Account ", error.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
                    builder.setTitle("Account Error")
                            .setMessage(error.getMessage())
                            .setCancelable(false)
                            .setNegativeButton("Close", null)
                            .show();


                }
            };
            connect = new MyConnection(model, listen, errorListener);
            request.add(connect);


        }

    }




    public static String generateID() {
        String numbers = "0123456789";
        String aTOz = "ABCDEFGHIJKLMNOPQRSTVWXYZ";
        int len = 5;
        // Using random method
        Random rndm_method = new SecureRandom();
        char[] otp = new char[len];
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            if (i < 2) {
                otp[i] = aTOz.charAt(rndm_method.nextInt(numbers.length()));
            } else if (i <= 0) {
                int rand = 0;
                while (rand <= 0) {
                    int temp = rndm_method.nextInt(numbers.length());
                    rand = (temp == 0) ? rndm_method.nextInt(numbers.length()) : temp;
                }
                otp[i] = numbers.charAt(rand);
            } else {
                otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
            }
            stringBuilder.append(otp[i]);
        }
        return stringBuilder.toString();

    }
}
