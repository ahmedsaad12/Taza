package com.elkhelj.karam.models;

import android.content.Context;
import android.text.TextUtils;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.elkhelj.karam.BR;
import com.elkhelj.karam.R;

import java.io.Serializable;


public class Order_Upload_Model extends BaseObservable implements Serializable {

private String name;

    private String address;


    public ObservableField<String> address_error = new ObservableField<>();
    public ObservableField<String> name_error = new ObservableField<>();




    public boolean isDataValidStep1(Context context)
    {
        if (


!TextUtils.isEmpty(name)
                        &&!TextUtils.isEmpty(address)


        )
        {
            address_error.set(null);
name_error.set(null);
            return true;
        }else
            {




                if (TextUtils.isEmpty(address))
                {
                    address_error.set(context.getString(R.string.field_req));
                }else
                {
                    address_error.set(null);

                }



                if (TextUtils.isEmpty(name))
                {
                    name_error.set(context.getString(R.string.field_req));
                }else
                {
                    name_error.set(null);

                }



                return false;
            }
    }









    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);

    }








    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);

    }



}
