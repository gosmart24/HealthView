package com.cybertech.healthview;

public class BookingModel {
    String message;
    String name;
    PatientModel patient;
    String time;
    String response;
    String responsetime;
    String confirmation;
    String flag;
    String key;

    public BookingModel() {
    }


    public BookingModel(String message, String name, PatientModel patient, String time, String response, String responsetime, String confirmation, String flag, String key) {
        this.message = message;
        this.name = name;
        this.patient = patient;
        this.time = time;
        this.response = response;
        this.responsetime = responsetime;
        this.confirmation = confirmation;
        this.flag = flag;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PatientModel getPatient() {
        return patient;
    }

    public void setPatient(PatientModel patient) {
        this.patient = patient;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResponsetime() {
        return responsetime;
    }

    public void setResponsetime(String responsetime) {
        this.responsetime = responsetime;
    }

    public String getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(String confirmation) {
        this.confirmation = confirmation;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
