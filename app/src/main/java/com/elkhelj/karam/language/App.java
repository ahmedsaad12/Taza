package com.elkhelj.karam.language;

import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.elkhelj.karam.preferences.Preferences;


public class App extends MultiDexApplication {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageHelper.updateResources(base, Preferences.newInstance().getLanguage(base)));
    }

    @Override
    public void onCreate() {
        super.onCreate();





    }

}
