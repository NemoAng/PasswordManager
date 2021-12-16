package com.nemowang.passwordmanager;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

//        Toolbar toolbar = (Toolbar)findViewById(R.id.about_toolbar);
//        setSupportActionBar(toolbar);
    }

    public void setTheme(){
        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(this);

        final String theme = sharedPref.getString
                (SettingsActivity.SETTING_THEME, "Default");

        switch (theme) {
            case "Red":
            case "红色":
                setTheme(R.style.Theme_Red_700_900);
                break;
            case "Purple":
            case "紫色":
                setTheme(R.style.Theme_Purple_700_900);
                break;
            case "Indigo":
            case "靛青":
                setTheme(R.style.Theme_Indigo_700_900);
                break;
            case "Green":
            case "绿色":
                setTheme(R.style.Theme_Green_700_900);
                break;
            case "Orange":
            case "橙色":
                setTheme(R.style.Theme_Orange_700_900);
                break;
            default:
                setTheme(R.style.Theme_PasswordManager);
        }
    }
}