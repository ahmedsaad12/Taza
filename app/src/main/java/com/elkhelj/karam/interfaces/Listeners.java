package com.elkhelj.karam.interfaces;


import com.elkhelj.karam.models.ContactUsModel;

public interface Listeners {

    interface LoginListener {
        void checkDataLogin(String email, String password);
    }
    interface SkipListener
    {
        void skip();
    }
    interface EditprofileListener
    {
        void Editprofile(String name, String phone, String email);

    }
    interface ForgetpasswordListner {
        void checkDataForget(String emial);
    }
    interface PasswordListner {
        void checkDatapass(String pass);
    }

    interface ForgetListner
    {
        void forget();
    }
    interface CreateAccountListener
    {
        void createNewAccount();
    }

    interface ShowCountryDialogListener
    {
        void showDialog();
    }

    interface SignUpListener
    {
        void checkDataSignUp(String name, String phone_code, String phone, String email, String password, String confirmpassword,String shop_name);

    }


    interface RatingListener
    {
        void checkDataRating(String desc);

    }
    interface BackListener
    {
        void back();
    }





    interface UpdateProfileListener
    {
        void updateProfile();
    }

    interface ContactListener
    {
        void sendContact(ContactUsModel contactUsModel);
    }

}
