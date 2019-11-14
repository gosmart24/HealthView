package com.cybertech.healthview;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EditActivity extends AppCompatActivity {

    EditText employ_ID_ED;
    private ProgressDialog progressDialog;
    StaffsModel model;
    private TinyDB tinyDB;
    private MyConnection request;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        employ_ID_ED = (EditText) findViewById(R.id.edit_ID_employee);

        tinyDB = new TinyDB(this);
    }


    public void onGetUser(View view) {
        String employee = employ_ID_ED.getText().toString();


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
                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                builder.setTitle("Error")
                        .setMessage(error.getMessage())
                        .setCancelable(false)
                        .setNegativeButton("Close", null)
                        .show();

            }
        };
        progressDialog = ProgressDialog.show(EditActivity.this, null, "Retrieving User please wait...", true, true, oncancel);
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray jsonArray = object.getJSONArray("user");
                    boolean success = object.getBoolean("success");
                    if (success) {
                        model = new StaffsModel();
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
                        Intent intent = new Intent(EditActivity.this, EditStaffActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        message = object.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
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
        // request.setRetryPolicy(new DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request = new MyConnection(model, employee, listener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(EditActivity.this);
        queue.add(request);


    }
}
