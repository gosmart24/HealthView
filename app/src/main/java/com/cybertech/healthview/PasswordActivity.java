package com.cybertech.healthview;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class PasswordActivity extends AppCompatActivity {


    EditText pass1_ED, confirm_ED;
    private String message;
    private ProgressDialog dialog;
    PatientModel model;
    TinyDB tinyDB;
    private StaffsModel staffmodel;
    Response.Listener<String> listen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        pass1_ED = (EditText) findViewById(R.id.ed_pass_1);
        confirm_ED = (EditText) findViewById(R.id.ed_pass_con);
        tinyDB = new TinyDB(this);

        if (tinyDB.getString("account").equals("patient")){
            model = tinyDB.getObject("user", PatientModel.class);
        }else {
            staffmodel = tinyDB.getObject("user", StaffsModel.class);
        }

    }

    public void onChangePassword(View view) {
        final String pass = pass1_ED.getText().toString();
        String confirm_Pass = pass1_ED.getText().toString();

        if (TextUtils.isEmpty(pass) || TextUtils.isEmpty(pass)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PasswordActivity.this);
            builder.setTitle("Alert")
                    .setMessage("Password cannot be Empty!")
                    .setCancelable(false)
                    .setNegativeButton("Close", null)
                    .show();
        } else {
            if (pass.equals(confirm_Pass)) {
                dialog = ProgressDialog.show(PasswordActivity.this, null, "Processing...", true, true);

                MyConnection connect;
                RequestQueue request = Volley.newRequestQueue(PasswordActivity.this);

                if (tinyDB.getString("account").equals("patient")){
                    listen = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Log.i("Thread", "response: " + response);
                            dialog.dismiss();

                            JSONObject jsonObject = null;
                            boolean status = false;

                            try {
                                jsonObject = new JSONObject(response);
                                status = jsonObject.getBoolean("success");
                                message = jsonObject.getString("message");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (status) {
                                model.setPassword(pass);
                                tinyDB.remove("user");
                                tinyDB.putObject("user", model);
                                tinyDB.putString("password", pass);
                                AlertDialog.Builder builder = new AlertDialog.Builder(PasswordActivity.this);
                                builder.setTitle(" Alert")
                                        .setMessage(message)
                                        .setCancelable(false)
                                        .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startActivity(new Intent(PasswordActivity.this, LoginActivity.class));
                                                finish();
                                            }
                                        })
                                        .show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(PasswordActivity.this);
                                builder.setTitle("Alert")
                                        .setMessage(message)
                                        .setCancelable(false)
                                        .setNegativeButton("Close", null)
                                        .show();
                            }


                        }
                    };

                }else {
                    listen = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                             Log.i("PPP", "response: " + response);
                            dialog.dismiss();

                            JSONObject jsonObject = null;
                            boolean status = false;

                            try {
                                jsonObject = new JSONObject(response);
                                status = jsonObject.getBoolean("success");
                                message = jsonObject.getString("message");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (status) {
                                staffmodel.setPassword(pass);
                                tinyDB.remove("user");
                                tinyDB.putObject("user", staffmodel);
                                tinyDB.putString("password", pass);
                                AlertDialog.Builder builder = new AlertDialog.Builder(PasswordActivity.this);
                                builder.setTitle(" Alert")
                                        .setMessage(message)
                                        .setCancelable(false)
                                        .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startActivity(new Intent(PasswordActivity.this, LoginActivity.class));
                                                finish();
                                            }
                                        })
                                        .show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(PasswordActivity.this);
                                builder.setTitle("Alert")
                                        .setMessage(message)
                                        .setCancelable(false)
                                        .setNegativeButton("Close", null)
                                        .show();
                            }


                        }
                    };

                }

                // error listener
                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PasswordActivity.this);
                        builder.setTitle(" Error")
                                .setMessage(error.getMessage())
                                .setCancelable(false)
                                .setNegativeButton("Close", null)
                                .show();
                    }
                };

                if (tinyDB.getString("account").equals("patient")){
                    connect = new MyConnection(model.getUsername(), model.getPassword(), 1, listen, errorListener);
                    request.add(connect);

                }else {
                    connect = new MyConnection(staffmodel.getUsername(), staffmodel.getPassword(),  listen, 1,errorListener);
                    request.add(connect);

                }


            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(PasswordActivity.this);
                builder.setTitle("Alert")
                        .setMessage("Password mismatch!")
                        .setCancelable(false)
                        .setNegativeButton("Close", null)
                        .show();
            }
        }
    }
}
