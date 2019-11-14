package com.cybertech.healthview;

import java.util.ArrayList;

public class PatientModel {
    String firstname;
    String surname;
    String gender;
    String address;
    String age;
    String idNo;
    String key;
    String phone;
    String tokenKey;
    String password;
    String username;
    HealthModel healthModel;
   ArrayList<TimelineModel>  timelineModel;

    public PatientModel() {
    }

    public PatientModel(String firstname, String surname, String gender, String address, String age, String idNo, String key, String phone, String tokenKey, String password, String username, HealthModel healthModel, ArrayList<TimelineModel> timelineModel) {
        this.firstname = firstname;
        this.surname = surname;
        this.gender = gender;
        this.address = address;
        this.age = age;
        this.idNo = idNo;
        this.key = key;
        this.phone = phone;
        this.tokenKey = tokenKey;
        this.password = password;
        this.username = username;
        this.healthModel = healthModel;
        this.timelineModel = timelineModel;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public HealthModel getHealthModel() {
        return healthModel;
    }

    public void setHealthModel(HealthModel healthModel) {
        this.healthModel = healthModel;
    }

    public ArrayList<TimelineModel> getTimelineModel() {
        return timelineModel;
    }

    public void setTimelineModel(ArrayList<TimelineModel> timelineModel) {
        this.timelineModel = timelineModel;
    }
}
