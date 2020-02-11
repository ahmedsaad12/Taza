package com.elkhelj.karam.models;

import java.io.Serializable;

public class Cities_Model implements Serializable {

        private int id;
            private String name;
        private String created_at;
        private String updated_at;

    public Cities_Model(String name) {
        this.name = name;
    }

    public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

}

