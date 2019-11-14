package com.cybertech.healthview;

public class Priscriptions {
    String deviceid;
    String employment_id;
    String temperature;
    String heartbeat;
    String refer;
    String health_condition;
    String type_of_diagnosis;
    String prescription;
    String prescription_date;


    public Priscriptions() {
    }

    public Priscriptions(String deviceid, String employment_id, String temperature, String heartbeat, String refer, String health_condition, String type_of_diagnosis, String prescription, String prescription_date) {
        this.deviceid = deviceid;
        this.employment_id = employment_id;
        this.temperature = temperature;
        this.heartbeat = heartbeat;
        this.refer = refer;
        this.health_condition = health_condition;
        this.type_of_diagnosis = type_of_diagnosis;
        this.prescription = prescription;
        this.prescription_date = prescription_date;
    }

    @Override
    public String toString() {
        return "Priscriptions{" +
                "deviceid='" + deviceid + '\'' +
                ", employment_id='" + employment_id + '\'' +
                ", temperature='" + temperature + '\'' +
                ", heartbeat='" + heartbeat + '\'' +
                ", refer='" + refer + '\'' +
                ", health_condition='" + health_condition + '\'' +
                ", type_of_diagnosis='" + type_of_diagnosis + '\'' +
                ", prescription='" + prescription + '\'' +
                ", prescription_date='" + prescription_date + '\'' +
                '}';
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getEmployment_id() {
        return employment_id;
    }

    public void setEmployment_id(String employment_id) {
        this.employment_id = employment_id;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(String heartbeat) {
        this.heartbeat = heartbeat;
    }

    public String getRefer() {
        return refer;
    }

    public void setRefer(String refer) {
        this.refer = refer;
    }

    public String getHealth_condition() {
        return health_condition;
    }

    public void setHealth_condition(String health_condition) {
        this.health_condition = health_condition;
    }

    public String getType_of_diagnosis() {
        return type_of_diagnosis;
    }

    public void setType_of_diagnosis(String type_of_diagnosis) {
        this.type_of_diagnosis = type_of_diagnosis;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public String getPrescription_date() {
        return prescription_date;
    }

    public void setPrescription_date(String prescription_date) {
        this.prescription_date = prescription_date;
    }
}
