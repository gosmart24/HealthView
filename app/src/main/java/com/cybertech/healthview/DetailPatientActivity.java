package com.cybertech.healthview;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailPatientActivity extends AppCompatActivity {


    TinyDB tinyDB;
    RequestQueue queue;
    MyConnection connection;
    BookingModel model;
    Priscriptions priscriptions;
    PatientModel patient;

    StaffsModel staffsModel;
    private String message, msg;
    TextView name_ED, age_ED, gender_ED, phone_ED, message_ED;
    private ProgressDialog progressDialog;

    Spinner refer_Person;
    private String refer_Staff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_patient);

        tinyDB = new TinyDB(this);
        model = tinyDB.getObject("patient", BookingModel.class);
        staffsModel = tinyDB.getObject("user", StaffsModel.class);


        name_ED = (TextView) findViewById(R.id.p_user_name);
        age_ED = (TextView) findViewById(R.id.p_user_age);
        gender_ED = (TextView) findViewById(R.id.p_user_gender);
        phone_ED = (TextView) findViewById(R.id.p_user_phone);
        message_ED = (TextView) findViewById(R.id.p_user_message);

        msg = getIntent().getStringExtra("message");

        getUser();
    }

    public void getUser() {
        progressDialog = ProgressDialog.show(DetailPatientActivity.this, null, "Processing...", true, true);

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailPatientActivity.this);
                builder.setTitle("Account Alert")
                        .setMessage("Unable to get User please try again later")
                        .setCancelable(false)
                        .setNegativeButton("Close", null)
                        .show();
            }
        };

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    final JSONArray jsonArray = object.getJSONArray("user");
                    boolean success = object.getBoolean("success");
                    progressDialog.setMessage("Loading Health data...");

                    if (success) {

                        Response.Listener<String> listen = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();

                                try {
                                    JSONObject object = new JSONObject(response);
                                    JSONArray health = object.getJSONArray("health");
                                    boolean success = object.getBoolean("success");

                                    if (success) {

                                        patient = new PatientModel();
                                        priscriptions = new Priscriptions();
                                        JSONObject user = jsonArray.getJSONObject(0);

                                        Log.i("sam", user.toString());

                                        patient.setFirstname(user.getString("firstName"));
                                        patient.setSurname(user.getString("surName"));
                                        patient.setUsername(user.getString("userName"));
                                        patient.setGender(user.getString("gender"));
                                        patient.setAge(user.getString("age"));
                                        patient.setIdNo(user.getString("Deviceid"));
                                        patient.setPhone(user.getString("phone"));
                                        patient.setPassword(user.getString("password"));
                                        patient.setAddress(user.getString("address"));

                                        // display to TVs.
                                        name_ED.setText(String.format("%s %s", patient.getFirstname(), patient.getSurname()));
                                        phone_ED.setText(patient.getPhone());
                                        message_ED.setText(msg);

                                        JSONObject healthDetail = health.getJSONObject(0);
                                        Log.i("sam", healthDetail.toString());
                                        priscriptions.setHeartbeat(healthDetail.getString("Heartbeat"));
                                        priscriptions.setTemperature(healthDetail.getString("Temperature"));
                                        priscriptions.setDeviceid(healthDetail.getString("DeviceId"));

                                        age_ED.setText(priscriptions.getTemperature());
                                        gender_ED.setText(priscriptions.getHeartbeat());

                                    } else {
                                        message = object.getString("message");
                                        AlertDialog.Builder builder = new AlertDialog.Builder(DetailPatientActivity.this);
                                        builder.setTitle("Alert")
                                                .setMessage(message)
                                                .setCancelable(false)
                                                .setNegativeButton("Close", null)
                                                .show();

                                    }
                                } catch (JSONException e) {
                                    Log.i("Account", e.getMessage());
                                    e.printStackTrace();
                                }
                            }
                        };
                        Response.ErrorListener errorListen = new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(DetailPatientActivity.this);
                                builder.setTitle("Account Alert")
                                        .setMessage("Unable to get User please try again later")
                                        .setCancelable(false)
                                        .setNegativeButton("Close", null)
                                        .show();
                            }
                        };

                        MyConnection connect = new MyConnection(listen, errorListen, model.getKey(), 1);
                        RequestQueue queues = Volley.newRequestQueue(DetailPatientActivity.this);
                        queues.add(connection);

                    } else {
                        message = object.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(DetailPatientActivity.this);
                        builder.setTitle("Alert")
                                .setMessage(message)
                                .setCancelable(false)
                                .setNegativeButton("Close", null)
                                .show();

                    }
                } catch (JSONException e) {
                    Log.i("Account", e.getMessage());
                    e.printStackTrace();
                }
            }
        };
        connection = new MyConnection(listener, errorListener, model.getKey());
        queue = Volley.newRequestQueue(DetailPatientActivity.this);
        queue.add(connection);

    }

    public void onMakePrescription(View view) {
        EditText prescription_ED = (EditText) findViewById(R.id.p_user_prescription);
        progressDialog = ProgressDialog.show(DetailPatientActivity.this, null, "Processing...", true, true);

        priscriptions.setPrescription(prescription_ED.getText().toString());
        priscriptions.setEmployment_id(staffsModel.getEmployment_ID());

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailPatientActivity.this);
                builder.setTitle("Alert")
                        .setMessage("Unable to send prescription, please try again later")
                        .setCancelable(false)
                        .setNegativeButton("Close", null)
                        .show();
            }
        };

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                Log.i("Account", response);
                try {
                    JSONObject object = new JSONObject(response);
                    boolean success = object.getBoolean("success");
                    message = object.getString("message");
                    if (success) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(DetailPatientActivity.this);
                        builder.setTitle("Alert")
                                .setMessage(message)
                                .setCancelable(false)
                                .setPositiveButton("", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setNegativeButton("Close", null)
                                .show();

                    } else {
                        message = object.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(DetailPatientActivity.this);
                        builder.setTitle("Alert")
                                .setMessage(message)
                                .setCancelable(false)
                                .setNegativeButton("Close", null)
                                .show();

                    }
                } catch (JSONException e) {
                    Log.i("Account", e.getMessage());
                    e.printStackTrace();
                }
            }
        };
        //make prescription and refer a patient
        MyConnection connection = new MyConnection(listener, errorListener, priscriptions);
        RequestQueue queue = Volley.newRequestQueue(DetailPatientActivity.this);
        queue.add(connection);
    }

    @SuppressLint("NewApi")
    public void onRefer(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailPatientActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.refer_layout, null);
        //  dialogBuilder.setView(dialogView);

        refer_Person = dialogView.findViewById(R.id.spinner_refer_Staff);
        builder.setTitle("Refer Patient To")
                .setView(dialogView)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Refer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // save to database
                        refer_Staff = refer_Person.getSelectedItem().toString();
                        priscriptions.setRefer(refer_Staff);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
