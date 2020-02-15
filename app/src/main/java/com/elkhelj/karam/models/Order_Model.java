package com.elkhelj.karam.models;

import java.io.Serializable;

public class Order_Model implements Serializable {

         private int id;
                 private int amount;

                 private double total;
                 private String address;
                 private String name;
                 private String des;
                 private String status;
                 private String user_id;
                 private String representative_id;
private double latitude;
    private double longitude;
    private String representative_name;
    private String representative_phone;
    private String representative_image;
    private String representative_for;

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public double getTotal() {
        return total;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getDes() {
        return des;
    }

    public String getStatus() {
        return status;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getRepresentative_id() {
        return representative_id;
    }

    public String getRepresentative_name() {
        return representative_name;
    }

    public String getRepresentative_phone() {
        return representative_phone;
    }

    public String getRepresentative_image() {
        return representative_image;
    }

    public String getRepresentative_for() {
        return representative_for;
    }
}
