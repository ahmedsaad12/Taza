package com.elkhelj.karam.models;

import java.io.Serializable;

public class UserModel implements Serializable {

    private int id;
    private String type;
    private int user_id;

    private String name;
    private String phone_code;
    private String phone;
    private String email;
    private String full_name;
    private String membership_code;
    private String logo;
    private String image;
    private String banner;
    private String twitter;
    private String facebook;
    private String google;
    private String instagram;
    private String address;
    private String latitude;
    private String longitude;
    private String is_company;
    private String software_type;
    private String block;
    private String is_confirmed;
    private String confirmation_code;
    private String password_forget_code;
    private String balance;
    private String is_branch;
    private String branch_parent;
    private String subscription_end;
    private String commercial_registration_no;
    private String residency_number;
    private String No_civil_registry;
    private String rate;
    private String is_login;
    private String logout_time;
    private String city;
    private String email_verified_at;
    private String is_deleted;
    private String default_theme;
    private int is_following;
    private int is_like;
    private int can_rate;

    public String getCity() {
        return city;
    }

    public int getIs_following() {
        return is_following;
    }

    public int getIs_like() {
        return is_like;
    }

    public int getCan_rate() {
        return can_rate;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getMembership_code() {
        return membership_code;
    }

    public String getLogo() {
        return logo;
    }

    public String getImage() {
        return image;
    }

    public String getBanner() {
        return banner;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getGoogle() {
        return google;
    }

    public String getInstagram() {
        return instagram;
    }

    public String getAddress() {
        return address;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getIs_company() {
        return is_company;
    }

    public String getSoftware_type() {
        return software_type;
    }

    public String getBlock() {
        return block;
    }

    public String getIs_confirmed() {
        return is_confirmed;
    }

    public String getConfirmation_code() {
        return confirmation_code;
    }

    public String getPassword_forget_code() {
        return password_forget_code;
    }

    public String getBalance() {
        return balance;
    }

    public String getIs_branch() {
        return is_branch;
    }

    public String getBranch_parent() {
        return branch_parent;
    }

    public String getSubscription_end() {
        return subscription_end;
    }

    public String getCommercial_registration_no() {
        return commercial_registration_no;
    }

    public String getResidency_number() {
        return residency_number;
    }

    public String getNo_civil_registry() {
        return No_civil_registry;
    }

    public String getRate() {
        return rate;
    }

    public String getIs_login() {
        return is_login;
    }

    public String getLogout_time() {
        return logout_time;
    }



    public String getEmail_verified_at() {
        return email_verified_at;
    }

    public String getIs_deleted() {
        return is_deleted;
    }

    public String getDefault_theme() {
        return default_theme;
    }

    public void setIs_following(int is_following) {
        this.is_following = is_following;
    }

    public void setIs_like(int is_like) {
        this.is_like = is_like;
    }
}
