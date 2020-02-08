package com.elkhelj.taza.models;

import java.io.Serializable;
import java.util.List;

public class Add_Order_Model implements Serializable {


    private int form_id;

private String name;
private String address;
private String des;
  private List<Orders_Cart_Model> orderDetalis;

    public int getForm_id() {
        return form_id;
    }

    public void setUser_id(int user_id) {
        this.form_id = user_id;
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

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public List<Orders_Cart_Model> getOrderDetalis() {
        return orderDetalis;
    }

    public void setOrder_detials(List<Orders_Cart_Model> order_detials) {
        this.orderDetalis = order_detials;
    }
}
