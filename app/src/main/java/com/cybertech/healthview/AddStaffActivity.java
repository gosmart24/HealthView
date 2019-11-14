package com.cybertech.healthview;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import java.util.Random;

public class AddStaffActivity extends AppCompatActivity {

    TinyDB tinyDB;
    private StaffsModel staffsModel;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    private EditText f_nameED, o_nameED, s_nameED, usernameED, PhoneNOED, passwordED, addressED;
    Spinner spinner_userType, spinner_gender;
    String message ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff);

        f_nameED = (EditText) findViewById(R.id.reg_firstname);
        o_nameED = (EditText) findViewById(R.id.reg_othername);
        s_nameED = (EditText) findViewById(R.id.reg_surname);
        PhoneNOED = (EditText) findViewById(R.id.reg_phoneNo);
        addressED = (EditText) findViewById(R.id.reg_address_staff);
        spinner_userType = (Spinner) findViewById(R.id.reg_spinner_usertype);
        spinner_gender = (Spinner) findViewById(R.id.reg_spinner_gender);
        tinyDB = new TinyDB(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("HealthView").child("staffs");

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddStaffActivity.this, AdminActivity.class));
        finish();
    }

    public void onCreateStaff(View view) {
        final String useType = spinner_userType.getSelectedItem().toString();
        final String gender = spinner_gender.getSelectedItem().toString();
        final String firstname = f_nameED.getText().toString();
        final String othername = o_nameED.getText().toString();
        final String surname = s_nameED.getText().toString();
        final String address = addressED.getText().toString();
        // final String username = usernameED.getText().toString();
        // final String pass = passwordED.getText().toString();
        String phone = PhoneNOED.getText().toString();
        staffsModel = new StaffsModel();

        if (TextUtils.isEmpty(firstname) || TextUtils.isEmpty(surname) || TextUtils.isEmpty(phone)) {
            Toast.makeText(getApplicationContext(), "Required fields cannot be empty!", Toast.LENGTH_LONG).show();

        } else {
            progressDialog = ProgressDialog.show(AddStaffActivity
                    .this, null, "Processing...", true, true);
            final DatabaseReference ref = databaseReference.push();
            staffsModel.setKey(ref.getKey());
            staffsModel.setFirstName(firstname);
            staffsModel.setOtherName(othername);
            staffsModel.setSurName(surname);
            staffsModel.setGender(gender);
            staffsModel.setAddress(address);
            staffsModel.setEmployment_ID(generateID());
            staffsModel.setPhone(phone);
            staffsModel.setUserRole(useType);
            staffsModel.setPassword("1234");
            String token = (tinyDB.getBoolean("isTokenReady")) ? tinyDB.getString("token") : "";
            staffsModel.setTokenKey(token);
            staffsModel.setUsername(staffsModel.getEmployment_ID());

            MyConnection connect;
            RequestQueue request = Volley.newRequestQueue(AddStaffActivity.this);

            // Success listener
            Response.Listener<String> listen = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Log.i("Thread", "response: " + response);
                    progressDialog.dismiss();
                    Log.i("Account ", response);
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
                        ref.setValue(staffsModel).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Error has occurred on uploading with Message : " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
//                        tinyDB.putString("userpass", "1234");
//                        tinyDB.putString("userids", staffsModel.getUserRole());
//                        tinyDB.putString("staffname", name);
//                        tinyDB.putString("staffuser", useType);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(AddStaffActivity.this);
                                    builder.setTitle("Account Alert")
                                            .setMessage(message)
                                            .setCancelable(false)
                                            .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    startActivity(new Intent(AddStaffActivity.this, AdminActivity.class));
                                                    finish();
                                                }
                                            })
                                            .show();

                                    Toast.makeText(getApplicationContext(), "Your details is successfully added to database!", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Error has occurred while uploading profile", Toast.LENGTH_LONG).show();

                                }
                            }
                        });
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddStaffActivity.this);
                        builder.setTitle("Account Alert")
                                .setMessage(message)
                                .setCancelable(false)
                                .setNegativeButton("Close",null)
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddStaffActivity.this);
                    builder.setTitle("Booking Error")
                            .setMessage(error.getMessage())
                            .setCancelable(false)
                            .setNegativeButton("Close", null)
                            .show();
                }
            };
            connect = new MyConnection(staffsModel, listen, errorListener);
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
            if (i > 3) {
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
