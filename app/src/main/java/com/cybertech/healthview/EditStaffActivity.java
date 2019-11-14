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
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

public class EditStaffActivity extends AppCompatActivity {


    private EditText f_nameED, s_nameED, o_nameED, adddress_Ed, Phone_ED;
    Spinner spinner_userType, spinner_gender;
    private TinyDB tinyDB;
    private DatabaseReference databaseReference;
    TextView employee_TV;
    StaffsModel staffsModel;
    private ProgressDialog progressDialog;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_staff);

        f_nameED = (EditText) findViewById(R.id.reg_firstname_edit);
        o_nameED = (EditText) findViewById(R.id.reg_othername_edit);
        s_nameED = (EditText) findViewById(R.id.reg_surname_edit);
        adddress_Ed = (EditText) findViewById(R.id.reg_address_edit);
        Phone_ED = (EditText) findViewById(R.id.reg_phoneNo_edit);
        employee_TV = (TextView) findViewById(R.id.reg_staff_Id_edit);

        spinner_userType = (Spinner) findViewById(R.id.reg_spinner_usertype_edit);
        spinner_gender = (Spinner) findViewById(R.id.reg_spinner_gender_edit);

        tinyDB = new TinyDB(this);
        staffsModel = tinyDB.getObject("user", StaffsModel.class);

        f_nameED.setText(staffsModel.getFirstName());
        o_nameED.setText(staffsModel.getOtherName());
        s_nameED.setText(staffsModel.getSurName());
        adddress_Ed.setText(staffsModel.getAddress());
        Phone_ED.setText(staffsModel.getPhone());
        employee_TV.setText(staffsModel.getEmployment_ID());
        if (staffsModel.getGender().equals("Male")) {
            spinner_gender.setSelection(0);
        } else {
            spinner_gender.setSelection(1);
        }

        if (staffsModel.getUserRole().equals("Doctor")) {
            spinner_gender.setSelection(0);
        } else if (staffsModel.getUserRole().equals("Lab Attendant")) {
            spinner_gender.setSelection(1);
        } else if (staffsModel.getUserRole().equals("Nurse")) {
            spinner_gender.setSelection(2);
        } else if (staffsModel.getUserRole().equals("Pharmacist")) {
            spinner_gender.setSelection(3);
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("HealthView").child("staffs");

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(EditStaffActivity.this, AdminActivity.class));
        finish();
    }


    public void onEditRecord(View view) {
        final String useType = spinner_userType.getSelectedItem().toString();
        final String gender = spinner_gender.getSelectedItem().toString();
        final String firstname = f_nameED.getText().toString();
        final String othername = o_nameED.getText().toString();
        final String surname = s_nameED.getText().toString();
        final String address = adddress_Ed.getText().toString();
        // final String username = usernameED.getText().toString();
        // final String pass = passwordED.getText().toString();
        String phone = Phone_ED.getText().toString();

        if (TextUtils.isEmpty(firstname) || TextUtils.isEmpty(surname) || TextUtils.isEmpty(phone)) {
            Toast.makeText(getApplicationContext(), "Required fields cannot be empty!", Toast.LENGTH_LONG).show();

        } else {
            progressDialog = ProgressDialog.show(EditStaffActivity.this, null, "Processing...", true, true);
            final DatabaseReference ref = databaseReference.push();
            staffsModel.setKey(ref.getKey());
            staffsModel.setFirstName(firstname);
            staffsModel.setOtherName(othername);
            staffsModel.setSurName(surname);
            staffsModel.setGender(gender);
            staffsModel.setAddress(address);
            staffsModel.setPhone(phone);
            staffsModel.setUserRole(useType);
            String token = (tinyDB.getBoolean("isTokenReady")) ? tinyDB.getString("token") : "";
            staffsModel.setTokenKey(token);

            MyConnection connect;
            RequestQueue request = Volley.newRequestQueue(EditStaffActivity.this);

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
                                    AlertDialog.Builder builder = new AlertDialog.Builder(EditStaffActivity.this);
                                    builder.setTitle("Alert")
                                            .setMessage(message)
                                            .setCancelable(false)
                                            .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    startActivity(new Intent(EditStaffActivity.this, AdminActivity.class));
                                                    finish();
                                                }
                                            })
                                            .show();

                                    Toast.makeText(getApplicationContext(), "Your staff record has been successfully updated!", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Error has occurred while updating profile", Toast.LENGTH_LONG).show();

                                }
                            }
                        });
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditStaffActivity.this);
                        builder.setTitle("Alert")
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditStaffActivity.this);
                    builder.setTitle("Booking Error")
                            .setMessage("Error Has occurred")
                            .setCancelable(false)
                            .setNegativeButton("Close", null)
                            .show();
                }
            };
            connect = new MyConnection(staffsModel, 1, listen, errorListener);
            request.add(connect);

        }

    }
}
