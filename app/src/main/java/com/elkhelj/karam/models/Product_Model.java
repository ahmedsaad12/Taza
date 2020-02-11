package com.elkhelj.karam.models;

import java.io.Serializable;

public class Product_Model implements Serializable {
  private int id;
    private String ar_title;
    private String en_title;

    private String image;
          private double price;
         private double descount_per;
          private int stock;
          private String main_image;
          private int category_id;
    private int  user_id;
    private String  description;
          private String weight;
          private String unit;
private int amount;
private int type;
         private String category_ar_title;

    public int getId() {
        return id;
    }

    public String getAr_title() {
        return ar_title;
    }

    public String getEn_title() {
        return en_title;
    }

    public String getImage() {
        return image;
    }

    public double getPrice() {
        return price;
    }

    public double getDescount_per() {
        return descount_per;
    }

    public int getStock() {
        return stock;
    }

    public String getMain_image() {
        return main_image;
    }

    public int getCategory_id() {
        return category_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getDescription() {
        return description;
    }

    public String getWeight() {
        return weight;
    }

    public String getUnit() {
        return unit;
    }

    public int getAmount() {
        return amount;
    }

    public String getCategory_ar_title() {
        return category_ar_title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
