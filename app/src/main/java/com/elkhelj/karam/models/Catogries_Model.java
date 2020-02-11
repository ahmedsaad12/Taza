package com.elkhelj.karam.models;

import java.io.Serializable;

public class Catogries_Model implements Serializable {
     private int id;
             private String ar_title;
    private String en_title;

    private String image;


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

}
