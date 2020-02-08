package com.elkhelj.taza.models;

import java.io.Serializable;
import java.util.List;

public class Add_Order_Model implements Serializable {


    private int user_id;

private String name;
private String address;
private double latitude=0.0;
private double longitude=0.0;
  private List<Orders_Cart_Model> orderDetalis;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public void setOrderDetalis(List<Orders_Cart_Model> orderDetalis) {
        this.orderDetalis = orderDetalis;
    }

    public List<Orders_Cart_Model> getOrderDetalis() {
        return orderDetalis;
    }

    public void setOrder_detials(List<Orders_Cart_Model> order_detials) {
        this.orderDetalis = order_detials;
    }
}
