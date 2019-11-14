package com.cybertech.healthview;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DoctorsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private TinyDB tinyDB;
    private ProgressDialog progressDialog;
    private ArrayList<BookingModel> bookinList;
    private ListView lsView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tinyDB = new TinyDB(this);
        final String token = tinyDB.getString("token");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        lsView = (ListView) findViewById(R.id.listview);

        setup();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logOut) {
            // Handle the Logout action
            System.exit(0);

        }else if (id == R.id.nav_pass){
            startActivity(new Intent(DoctorsActivity.this, PasswordActivity.class));
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void setup() {

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                Log.i("Booking", response);
                try {
                    JSONObject object = new JSONObject(response);
                    boolean success = object.getBoolean("success");
                    if (success) {

                        JSONArray jsonArray = object.getJSONArray("booking");
                        bookinList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            BookingModel model = new BookingModel();
                            model.setMessage(jsonObject.getString("messages"));
                            model.setTime(jsonObject.getString("created_at"));
                            model.setKey(jsonObject.getString("Deviceid"));
                            model.setName(jsonObject.getString("patient"));
                            bookinList.add(model);
                        }
                        lsView.setAdapter(new BookingAdapter(DoctorsActivity.this, bookinList));
                    } else {
                        String msg = object.getString("messages");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DoctorsActivity.this);
                builder.setTitle(" Alert")
                        .setMessage(error.getMessage())
                        .setCancelable(false)
                        .setNegativeButton("Close", null)
                        .show();
            }
        };
        progressDialog = ProgressDialog.show(DoctorsActivity.this, null, "Loading  Appointments please wait...", true, true);
        MyConnection connection = new MyConnection(listener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(DoctorsActivity.this);
        queue.add(connection);


    }
}
