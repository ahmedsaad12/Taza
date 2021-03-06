package com.elkhelj.karam.activities_fragments.activity_sign_in.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.elkhelj.karam.activities_fragments.activity_sign_in.fragments.Fragment_Forgetpass;
import com.elkhelj.karam.activities_fragments.activity_sign_in.fragments.Fragment_Newpass;
import com.elkhelj.karam.activities_fragments.activity_sign_in.fragments.Fragment_SignUpCompany;
import com.elkhelj.karam.activities_fragments.activity_sign_in.fragments.Fragment_SignUpRepres;
import com.elkhelj.karam.models.UserModel;
import com.elkhelj.karam.R;
import com.elkhelj.karam.activities_fragments.activity_sign_in.fragments.Fragment_SignUp;
import com.elkhelj.karam.activities_fragments.activity_sign_in.fragments.Fragment_Sign_In;
import com.elkhelj.karam.databinding.ActivitySignInBinding;
import com.elkhelj.karam.language.LanguageHelper;
import com.elkhelj.karam.preferences.Preferences;

import java.util.Locale;

import io.paperdb.Paper;

public class SignInActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private ActivitySignInBinding binding;

    private int fragment_count = 0;
    private Fragment_Sign_In fragment_sign_in;
    private Fragment_SignUp fragment_sign_up;
    private String cuurent_language;
    private Preferences preferences;
    private Fragment_Forgetpass fragment_forgetpass;
    private Fragment_Newpass fragment_newpass;
    private Fragment_SignUpCompany fragment_sign_up_company;
    private Fragment_SignUpRepres fragment_sign_up_repress;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase,LanguageHelper.getLanguage(newBase)));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);
        Paper.init(this);


        initView();
        if (savedInstanceState == null) {

            DisplayFragmentSignIn();

        }


    }

    private void initView() {
        Paper.init(this);
        preferences = Preferences.newInstance();
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        fragmentManager = this.getSupportFragmentManager();

    }
    public void DisplayFragmentSignInSignup() {
        fragment_count += 1;
        fragment_sign_up = Fragment_SignUp.newInstance();
        if (fragment_sign_up.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_sign_up).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_sign_in_container, fragment_sign_up, "fragment_sign_up").addToBackStack("fragment_sign_up").commit();
        }
    }
    public void DisplayFragmentSignInSignupCompany() {
        fragment_count += 1;
        fragment_sign_up_company = Fragment_SignUpCompany.newInstance();
        if (fragment_sign_up_company.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_sign_up_company).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_sign_in_container, fragment_sign_up_company, "fragment_sign_up_company").addToBackStack("fragment_sign_up_company").commit();
        }
    }
    public void DisplayFragmentSignInSignupRepre() {
        fragment_count += 1;
        fragment_sign_up_repress = Fragment_SignUpRepres.newInstance();
        if (fragment_sign_up_repress.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_sign_up_repress).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_sign_in_container, fragment_sign_up_repress, "fragment_sign_up_repress").addToBackStack("fragment_sign_up_repress").commit();
        }
    }
    public void DisplayFragmentSignIn() {
        fragment_count += 1;
        fragment_sign_in = Fragment_Sign_In.newInstance();
        if (fragment_sign_in.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_sign_in).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_sign_in_container, fragment_sign_in, "fragment_sign_in").addToBackStack("fragment_sign_in").commit();
        }
    }
    public void DisplayFragmentpass() {
        fragment_count += 1;
        fragment_forgetpass= Fragment_Forgetpass.newInstance();
        if (fragment_forgetpass.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_forgetpass).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_sign_in_container, fragment_forgetpass, "fragment_forgetpass").addToBackStack("fragment_forgetpass").commit();
        }
    }
    public void displayFragmentNewpass(UserModel userModel) {
        fragment_count ++;
        fragment_newpass = Fragment_Newpass.newInstance(userModel);

        fragmentManager.beginTransaction().add(R.id.fragment_sign_in_container, fragment_newpass, "fragment_newpass").addToBackStack("fragment_newpass").commit();

    }
    /*
        public void DisplayFragmentLanguage() {
            fragment_language = Fragment_Language.newInstance();
            if (fragment_language.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_language).commit();
            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_sign_in_container, fragment_language, "fragment_language").addToBackStack("fragment_language").commit();
            }
        }
        public void displayFragmentForgetpass() {
            fragment_count ++;
            fragment_forgetpass = Fragment_ForgetPassword.newInstance();

            fragmentManager.beginTransaction().add(R.id.fragment_sign_in_container, fragment_forgetpass, "fragment_forgetpass").addToBackStack("fragment_forgetpass").commit();

        }

        public void displayFragmentCodeVerification(UserModel userModel, int type) {
            fragment_count ++;
            fragment_code_verification = Fragment_Code_Verification.newInstance(userModel,type);
            fragmentManager.beginTransaction().add(R.id.fragment_sign_in_container, fragment_code_verification, "fragment_code_verification").addToBackStack("fragment_code_verification").commit();

        }
        public void displayFragmentNewpass(UserModel userModel) {
            fragment_count ++;
            fragment_newpass = Fragment_Newpass.newInstance(userModel);

            fragmentManager.beginTransaction().add(R.id.fragment_sign_in_container, fragment_newpass, "fragment_newpass").addToBackStack("fragment_newpass").commit();

        }*/
    public void RefreshActivity(String selected_language) {
        Paper.book().write("lang", selected_language);
        LanguageHelper.setNewLocale(this, selected_language);
        preferences.setIsLanguageSelected(this);


        Intent intent = getIntent();
        finish();
        startActivity(intent);


    }
    @Override
    public void onBackPressed() {
        Back();
    }

    public void Back() {

        if (fragment_count > 1) {
            fragment_count -= 1;
            super.onBackPressed();


        } else {

            finish();

        }


    }



}
