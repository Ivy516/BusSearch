package com.example.bussearch.data;

public class BusLineBean {
    private int position;
    private String stop;
    private boolean isArrived;
    private double latitude,longitude;

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

    public BusLineBean(int position, String stop, boolean isArrived,
                       double latitude, double longitude) {
        this.position = position;
        this.stop = stop;
        this.isArrived = isArrived;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

    public boolean getIsArrived() {
        return isArrived;
    }

    public void setIsArrived(boolean arrived) {
        isArrived = arrived;
    }
}
