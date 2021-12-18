package com.nemowang.passwordmanager;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class AboutActivity extends AppCompatActivity {
    private String theme;
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

//        Toolbar toolbar = (Toolbar)findViewById(R.id.about_toolbar);
//        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;

        int theme_id;
        switch (theme) {
            case "Red":
            case "红色":
                theme_id = R.drawable.side_nav_bar_red;
                break;
            case "Purple":
            case "紫色":
                theme_id = R.drawable.side_nav_bar_purple;
                break;
            case "Indigo":
            case "靛青":
                theme_id = R.drawable.side_nav_bar_indigo;
                break;
            case "Green":
            case "绿色":
                theme_id = R.drawable.side_nav_bar_green;
                break;
            case "Orange":
            case "橙色":
                theme_id = R.drawable.side_nav_bar_orange;
                break;
            default:
                theme_id = R.raw.bar_src;
        }
        actionBar.setBackgroundDrawable(getResources().getDrawable(theme_id,null));
    }

    public void setTheme(){
        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(this);

        theme = sharedPref.getString
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