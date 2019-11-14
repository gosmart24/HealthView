package com.cybertech.healthview;

public class TimelineModel {
    String title;
    String diagnosis;
    Priscriptions priscriptions;
    String testResult;
    int referTo;

    public TimelineModel() {
    }

    public TimelineModel(String title, String diagnosis, Priscriptions priscriptions, String testResult, int referTo) {
        this.title = title;
        this.diagnosis = diagnosis;
        this.priscriptions = priscriptions;
        this.testResult = testResult;
        this.referTo = referTo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public Priscriptions getPriscriptions() {
        return priscriptions;
    }

    public void setPriscriptions(Priscriptions priscriptions) {
        this.priscriptions = priscriptions;
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

    public int getReferTo() {
        return referTo;
    }

    public void setReferTo(int referTo) {
        this.referTo = referTo;
    }
}
