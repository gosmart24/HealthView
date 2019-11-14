package com.cybertech.healthview;

public class HealthModel {
    // String stname;
    String temperature;
    String heartbeat;
    String condition;
    double latitude;
    double longitude;

    public HealthModel() {
    }

    public HealthModel(String temperature, String heartbeat, String condition, double latitude, double longitude) {
        this.temperature = temperature;
        this.heartbeat = heartbeat;
        this.condition = condition;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

}
