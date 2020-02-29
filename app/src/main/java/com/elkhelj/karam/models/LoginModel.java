package com.elkhelj.karam.models;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.elkhelj.karam.BR;
import com.elkhelj.karam.R;


public class LoginModel extends BaseObservable {


    private String email;
    private String password;



    public ObservableField<String> error_email = new ObservableField<>();

    public ObservableField<String> error_password = new ObservableField<>();


    public LoginModel() {

        this.password = "";
        this.email = "";
    }




    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);


    }

    @Bindable

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);

    }



    public boolean isDataValid(Context context) {
        if ( !TextUtils.isEmpty(password)&&
                (password.length() >= 6) &&
                ! TextUtils.isEmpty(email))
         {

            error_email.set(null);
            error_password.set(null);
            return true;
        } else {






            if (email.isEmpty()) {
                error_email.set(context.getString(R.string.field_req));
            }   else {
                error_email.set(null);

            }


            if (password.isEmpty()) {
                error_password.set(context.getString(R.string.field_req));
            } else if (password.length() < 6) {
                error_password.set(context.getString(R.string.pass_short));
            }  else {
                error_password.set(null);

            }



            return false;
        }
    }
}
