package com.example.bussearch.data;

public class Test {
    private String title,fromTo,meter;

    public Test(String title, String fromTo, String meter) {
        this.title = title;
        this.fromTo = fromTo;
        this.meter = meter;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFromTo() {
        return fromTo;
    }

    public void setFromTo(String fromTo) {
        this.fromTo = fromTo;
    }

    public String getMeter() {
        return meter;
    }

    public void setMeter(String meter) {
        this.meter = meter;
    }
}
