package com.elkhelj.taza.models;

import java.io.Serializable;

public class App_Data_Model implements Serializable {
    private Data data;

    public Data getData() {
        return data;
    }

    public class Data implements Serializable{

        private String ar_content;
        private String en_content;

        public String getAr_content() {
            return ar_content;
        }

        public String getEn_content() {
            return en_content;
        }
    }
}
