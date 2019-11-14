package com.cybertech.healthview;

public class StaffsModel {
    String surName;
    String firstName;
    String otherName;
    String gender;
    String address;
    String employment_ID;
    String phone;
    String key;
    String tokenKey;
    String userRole;
    String password;
    String username;

    public StaffsModel(String surName, String firstName, String otherName, String gender, String address, String employment_ID, String phone, String key, String tokenKey, String userRole, String password, String username) {
        this.surName = surName;
        this.firstName = firstName;
        this.otherName = otherName;
        this.gender = gender;
        this.address = address;
        this.employment_ID = employment_ID;
        this.phone = phone;
        this.key = key;
        this.tokenKey = tokenKey;
        this.userRole = userRole;
        this.password = password;
        this.username = username;
    }

    public StaffsModel() {
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getEmployment_ID() {
        return employment_ID;
    }

    public void setEmployment_ID(String employment_ID) {
        this.employment_ID = employment_ID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
