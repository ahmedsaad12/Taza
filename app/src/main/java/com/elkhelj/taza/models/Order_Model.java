package com.elkhelj.taza.models;

import java.io.Serializable;

public class Order_Model implements Serializable {

        private int id;
        private String address;
        private String seller_name;
        private String created_at;
private String seller_image;
private String seller_phone;
    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public String getSeller_image() {
        return seller_image;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getSeller_phone() {
        return seller_phone;
    }
}
