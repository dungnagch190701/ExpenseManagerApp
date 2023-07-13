package com.example.myapplication;

public class ExpenseEntity {
    private int id;
    private String type;
    private String amount;
    private String time;
    private String address;
    private String cmt;
    private int trip_id;

    public ExpenseEntity(int id, String type, String amount, String time,String address, String cmt, int trip_id) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.time = time;
        this.address = address;
        this.cmt = cmt;
        this.trip_id = trip_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getAmount() {
        return amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCmt() {
        return cmt;
    }

    public void setCmt(String cmt) {
        this.cmt = cmt;
    }

    public int getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(int trip_id) {
        this.trip_id = trip_id;
    }


}
