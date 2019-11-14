package com.cybertech.healthview;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MyConnection extends StringRequest {
//    private static final String PATIENT_REG_URL = "https://outpatient.000webhostapp.com/patient_register.php";
//    private static final String BOOKING_URL = "https://outpatient.000webhostapp.com/makebooking.php";
//    private static final String STAFF_REG_URL = "https://outpatient.000webhostapp.com/staff_register.php";
//    private static final String LOGIN_URL = "https://outpatient.000webhostapp.com/login.php";
//    private static final String PASS_URL = "https://outpatient.000webhostapp.com/password.php";
//    private static final String STAFF_EDIT_URL = "https://outpatient.000webhostapp.com/editStaff.php";
//    private static final String PASS_URL_EDIT = "https://outpatient.000webhostapp.com/updateStaffPassword.php";
//    private static final String STAFF_URL_LOGIN = "https://outpatient.000webhostapp.com/staffLogin.php";
//    private static final String Get_STAFF_USER_URL = "https://outpatient.000webhostapp.com/getStaffUser.php";
//    private static final String GETBOOKINGS_URL_EDIT = "https://outpatient.000webhostapp.com/getBookings.php";
//    private static final String GETALL_PATIENT_URL_EDIT = "https://outpatient.000webhostapp.com/getUserDetails.php";
//    private static final String PRESCRIPTION_URL_EDIT = "https://outpatient.000webhostapp.com/prescription.php";
//    private static final String GET_Health_URL_EDIT = "https://outpatient.000webhostapp.com/getHealth.php";

    private static final String PATIENT_REG = "http://10.0.2.2/Apis/patient_register.php";
    private static final String BOOKING = "http://10.0.2.2/Apis/makebooking.php";
    private static final String STAFF_REG = "http://10.0.2.2/Apis/staff_register.php";
    private static final String LOGIN = "http://10.0.2.2/Apis/login.php";
    private static final String PASS = "http://10.0.2.2/Apis/password.php";
    private static final String STAFF_EDIT = "http://10.0.2.2/Apis/editStaff.php";
    private static final String PASS_EDIT = "http://10.0.2.2/Apis/updateStaffPassword.php";
    private static final String STAFF_LOGIN = "http://10.0.2.2/Apis/staffLogin.php";
    private static final String Get_STAFF_USER = "http://10.0.2.2/Apis/getStaffUser.php";
    private static final String GETBOOKINGS = "http://10.0.2.2/Apis/getBookings.php";
    private static final String GETALL_PATIENT = "http://10.0.2.2/Apis/getUserDetails.php";
    private static final String PRESCRIPTION = "http://10.0.2.2/Apis/prescription.php";
    private static final String GET_Health = "http://10.0.2.2/Apis/getHealth.php";


    Map<String, String> params;
    BookingModel model;

    //staff registration
    public MyConnection(StaffsModel model, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, STAFF_REG, listener, errorListener);
        params = new HashMap<>();
        params.put("employmentID", model.getEmployment_ID());
        params.put("surname", model.getSurName());
        params.put("otherName", model.getOtherName());
        params.put("firstname", model.getFirstName());
        params.put("username", model.getUsername());
        params.put("gender", model.getGender());
        params.put("phone", model.getPhone());
        params.put("userRole", model.getUserRole());
        params.put("address", model.getAddress());
        params.put("pass", model.getPassword());

    }

    //staff Record editing
    public MyConnection(StaffsModel model, int flag, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, STAFF_EDIT, listener, errorListener);
        params = new HashMap<>();
        params.put("employmentID", model.getEmployment_ID());
        params.put("surname", model.getSurName());
        params.put("otherName", model.getOtherName());
        params.put("firstname", model.getFirstName());
        params.put("username", model.getUsername());
        params.put("gender", model.getGender());
        params.put("phone", model.getPhone());
        params.put("userRole", model.getUserRole());
        params.put("address", model.getAddress());
        params.put("pass", model.getPassword());

    }

    // patient registration
    public MyConnection(PatientModel model, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, PATIENT_REG, listener, null);
        params = new HashMap<>();
        params.put("deviceid", model.getIdNo());
        params.put("surname", model.getSurname());
        params.put("firstName", model.getFirstname());
        params.put("phoneno", model.getPhone());
        params.put("gender", model.getGender());
        params.put("address", model.getAddress());
        params.put("userName", model.getUsername());
        params.put("pass", model.getPassword());
        params.put("age", model.getAge());

    }

    // New Booking  connection
    public MyConnection(BookingModel model, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, BOOKING, listener, errorListener);
        params = new HashMap<>();
        params.put("deviceid", model.getPatient().getIdNo());
        params.put("name", model.getName());
        params.put("message", model.getMessage());
        params.put("response", model.getResponse());
        params.put("responsetime", model.getResponsetime());

    }

    // Staff Login  connection
    public MyConnection(String username, String pass, StaffsModel model, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, STAFF_LOGIN, listener, errorListener);
        params = new HashMap<>();
        params.put("userName", username);
        params.put("pass", pass);

    }

    // Patent Login  connection
    public MyConnection(String username, String pass, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, LOGIN, listener, errorListener);
        params = new HashMap<>();
        params.put("userName", username);
        params.put("pass", pass);

    }

    // Get staff User before editing record  connection
    public MyConnection(StaffsModel model, String employment_ID, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, Get_STAFF_USER, listener, errorListener);
        params = new HashMap<>();
        params.put("userName", employment_ID);

    }

    //Patent Password Change  connection
    public MyConnection(String username, String pass, int flag, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, PASS, listener, errorListener);
        params = new HashMap<>();
        params.put("userName", username);
        params.put("pass", pass);
    }

    //Staff Password Change  connection
    public MyConnection(String username, String pass, Response.Listener<String> listener, int flag, Response.ErrorListener errorListener) {
        super(Method.POST, PASS_EDIT, listener, errorListener);
        params = new HashMap<>();
        params.put("user", username);
        params.put("pass", pass);
    }

    //get all bookings  connection
    public MyConnection(Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, GETBOOKINGS, listener, errorListener);
    }

    // get patient details
    public MyConnection(Response.Listener<String> listener, Response.ErrorListener errorListener, String model) {
        super(Method.POST, GETALL_PATIENT, listener, errorListener);
        params = new HashMap<>();
        params.put("deviceid", model);
    }

    // get patient details
    public MyConnection(Response.Listener<String> listener, Response.ErrorListener errorListener, String model, int id) {
        super(Method.POST, GET_Health, listener, errorListener);
        params = new HashMap<>();
        params.put("deviceid", model);
    }


    // make prescription details
    public MyConnection(Response.Listener<String> listener, Response.ErrorListener errorListener, Priscriptions priscriptions) {
        super(Method.POST, PRESCRIPTION, listener, errorListener);
        params = new HashMap<>();
        params.put("deviceid", priscriptions.getDeviceid());
        params.put("employment_id", priscriptions.getEmployment_id());
        params.put("temperature", priscriptions.getTemperature());
        params.put("heartbeat", priscriptions.getHeartbeat());
        params.put("refer", priscriptions.getRefer());
        params.put("prescription", priscriptions.getPrescription());
    }


    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
