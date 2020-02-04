package com.elkhelj.taza.models;

import java.io.Serializable;

public class NotificationDataModel implements Serializable {

private long notification_date;
    private String from_user_name;

        public long getNotification_date() {
            return notification_date;
        }

    public String getFrom_user_name() {
        return from_user_name;
    }
}
