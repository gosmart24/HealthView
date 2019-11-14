package com.cybertech.healthview;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private String accountType;
    private TinyDB tinyDB;
    TextView invalid_Display;
    EditText et_login_username, et_login_pass;
    Button SignIn_btn;
    private PatientModel patient;
    private ProgressDialog progressDialog;
    private String message;
    private MyConnection request;
    private String username;
    private String password;
    private StaffsModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tinyDB = new TinyDB(getApplicationContext());
        accountType = tinyDB.getString("account");

        invalid_Display = (TextView) findViewById(R.id.invalid_Display);
        et_login_username = (EditText) findViewById(R.id.ed_login_username);
        et_login_pass = (EditText) findViewById(R.id.ed_login_pass);
        SignIn_btn = (Button) findViewById(R.id.btn_SignIn);
        et_login_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invalid_Display.setText("");
            }
        });
        et_login_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invalid_Display.setText("");
            }
        });
    }

    public void getLoginDetails() {
        username = et_login_username.getText().toString();
        password = et_login_pass.getText().toString();

        if (accountType.equals("admin")) {
            // load doctor home page.

            if (username.equals("admin") && password.equals("MOSESOKPE19")) {
                startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                finish();
            } else {
                invalid_Display.setText(R.string.invalid_MSG);
                et_login_username.setText("");
                et_login_pass.setText("");
            }

        } else {
            if (tinyDB.getBoolean("reg")) {

                if (!username.isEmpty() && !password.isEmpty()) {
                    if (username.equals(tinyDB.getString("username"))) {
                        if (password.equals(tinyDB.getString("password"))) {

                            try {

                                if (accountType.equals("doctor")) {
                                    // load pharmacy home page.
                                    startActivity(new Intent(LoginActivity.this, DoctorsActivity.class));
                                    finish();
                                } else if (accountType.equals("nurse")) {
                                    // load default user home.
                                    startActivity(new Intent(LoginActivity.this, NurseActivity.class));
                                    finish();
                                } else if (accountType.equals("lab")) {
                                    // load default user home.
                                    startActivity(new Intent(LoginActivity.this, LabActivity.class));
                                    finish();
                                } else if (accountType.equals("patient")) {
                                    // load default user home.
                                    patient = tinyDB.getObject("user", PatientModel.class);
                                    Intent intent = new Intent(LoginActivity.this, PatientActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else if (accountType.equals("pharmacy")) {
                                    // load default user home.
                                    startActivity(new Intent(LoginActivity.this, PharmacyActivity.class));
                                    finish();

                                }

                            } catch (Exception e) {
                                Log.e("MY", "Could not get into Home Activity!");
                            }
                        } else {
                            invalid_Display.setText(R.string.invalid_MSG);
                            et_login_pass.setText("");
                            et_login_username.setText("");
                        }
                    } else {
                        invalid_Display.setText(R.string.invalid_MSG);
                        et_login_username.setText("");
                        et_login_pass.setText("");
                    }
                } else {
                    invalid_Display.setText(R.string.requiredfields);
                    et_login_username.setText("");
                    et_login_pass.setText("");
                }

            } else {

                if (accountType.equals("patient")) {
                    // get patients details from database.
                    DialogInterface.OnCancelListener oncancel = new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            // show cancel message

                        }
                    };
                    Response.ErrorListener errorListener = new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("Account ", error.getMessage());
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle("Booking Error")
                                    .setMessage(error.getMessage())
                                    .setCancelable(false)
                                    .setNegativeButton("Close", null)
                                    .show();

                        }
                    };
                    progressDialog = ProgressDialog.show(LoginActivity.this, null, "Retrieving User please wait...", true, true, oncancel);
                    Response.Listener<String> listener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();

                            try {
                                JSONObject object = new JSONObject(response);
                                JSONArray jsonArray = object.getJSONArray("user");
                                boolean success = object.getBoolean("success");
                                if (success) {
                                    patient = new PatientModel();
                                    JSONObject user = jsonArray.getJSONObject(0);
                                    patient.setFirstname(user.getString("firstName"));
                                    patient.setSurname(user.getString("surName"));
                                    patient.setUsername(user.getString("userName"));
                                    patient.setGender(user.getString("gender"));
                                    patient.setAge(user.getString("age"));
                                    patient.setIdNo(user.getString("Deviceid"));
                                    patient.setPhone(user.getString("phone"));
                                    patient.setPassword(user.getString("password"));
                                    patient.setAddress(user.getString("address"));

                                    tinyDB.putObject("user", patient);
                                    tinyDB.putString("username", patient.getUsername());
                                    tinyDB.putString("password", patient.getPassword());
                                    tinyDB.putBoolean("reg", true);
                                    Intent intent = new Intent(LoginActivity.this, PatientActivity.class);
                                    startActivity(intent);
                                    finish();


                                } else {
                                    message = object.getString("message");
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setTitle("Account Alert")
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

                    request = new MyConnection(username, password, listener, errorListener);
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    queue.add(request);
                } else {
                    //get staff details fro database.
                    // get patients details from database.
                    DialogInterface.OnCancelListener oncancel = new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            // show cancel message

                        }
                    };
                    Response.ErrorListener errorListener = new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Log.i("Account ", error.getMessage());
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle(" Error")
                                    .setMessage(error.getMessage())
                                    .setCancelable(false)
                                    .setNegativeButton("Close", null)
                                    .show();

                        }
                    };
                    progressDialog = ProgressDialog.show(LoginActivity.this, null, "Retrieving User please wait...", true, true, oncancel);
                    Response.Listener<String> listener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            Log.i("login", response);

                            try {
                                JSONObject object = new JSONObject(response);
                                JSONArray jsonArray = object.getJSONArray("user");
                                boolean success = object.getBoolean("success");
                                if (success) {
                                    model = new StaffsModel();
                                    Log.i("login", object.toString());
                                    JSONObject user = jsonArray.getJSONObject(0);
                                    model.setFirstName(user.getString("firstName"));
                                    model.setSurName(user.getString("surname"));
                                    model.setOtherName(user.getString("otherName"));
                                    model.setUsername(user.getString("userName"));
                                    model.setGender(user.getString("gender"));
                                    model.setUserRole(user.getString("userRole"));
                                    model.setEmployment_ID(user.getString("employment_id"));
                                    model.setPhone(user.getString("phone"));
                                    model.setPassword(user.getString("password"));
                                    model.setAddress(user.getString("address"));

                                    tinyDB.putObject("user", model);
                                    tinyDB.putString("username", model.getUsername());
                                    tinyDB.putString("password", model.getPassword());
                                    tinyDB.putBoolean("reg", true);

                                    try {

                                        if (accountType.equals("admin")) {
                                            // load doctor home page.
                                            startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                                            finish();
                                        } else if (accountType.equals("doctor")) {
                                            // load pharmacy home page.
                                            startActivity(new Intent(LoginActivity.this, DoctorsActivity.class));
                                            finish();
                                        } else if (accountType.equals("nurse")) {
                                            // load default user home.
                                            startActivity(new Intent(LoginActivity.this, NurseActivity.class));
                                            finish();
                                        } else if (accountType.equals("lab")) {
                                            // load default user home.
                                            startActivity(new Intent(LoginActivity.this, LabActivity.class));
                                            finish();
                                        } else if (accountType.equals("pharmacy")) {
                                            // load default user home.
                                            startActivity(new Intent(LoginActivity.this, PharmacyActivity.class));
                                            finish();

                                        }

                                    } catch (Exception e) {
                                        Log.e("MY", "Could not get into Home Activity!");
                                    }
                                } else {
                                    message = object.getString("message");
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setTitle("Account Alert")
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
                    request = new MyConnection(username, password, model, listener, errorListener);
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    queue.add(request);

                }

            }

        }

    }

    public void onLogin(View view) {
        getLoginDetails();

    }


}

