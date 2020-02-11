package com.elkhelj.karam.models;

import java.io.Serializable;

public class App_Data_Model implements Serializable {

             private String ar_content;
             private String en_content;
             private String image;
             private int type;

    public String getAr_content() {
        return ar_content;
    }

    public String getEn_content() {
        return en_content;
    }

    public String getImage() {
        return image;
    }

    public int getType() {
        return type;
    }
}
