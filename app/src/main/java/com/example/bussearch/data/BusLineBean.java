package com.example.bussearch.data;

public class BusLineBean {
    private int position;
    private String stop;
    private boolean isArrived;

    public BusLineBean(int position, String stop, boolean isArrived) {
        this.position = position;
        this.stop = stop;
        this.isArrived = isArrived;
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
