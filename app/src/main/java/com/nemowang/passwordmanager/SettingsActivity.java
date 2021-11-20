package com.nemowang.passwordmanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class SettingsActivity extends AppCompatActivity {
    public static final String SW1 = "switch_preference_1";
    public static final String SW2 = "switch_preference_2";
    public static final String SW3 = "switch_preference_3";


    public static final String SW4 = "switch_preference_4";
    public static final String CK1 = "check_box_preference_1";
    public static final String CK2 = "check_box_preference_2";

    public static final String LS = "list_preference_1";
    public static final String MS = "multi_select_list_preference_1";

    public static final String SETTING_THEME = "setting_theme";

    public static final String SETTING_PASS_NUM = "setting_pass_number";
    public static final String SETTING_PASS_LOW = "setting_pass_lower";
    public static final String SETTING_PASS_UPP = "setting_pass_upper";
    public static final String SETTING_PASS_BEG = "setting_pass_begin";
    public static final String SETTING_PASS_SYM = "setting_pass_symbol";
    public static final String SETTING_PASS_SIM = "setting_pass_similar";
    public static final String SETTING_PASS_DUP = "setting_pass_duplicate";
    public static final String SETTING_PASS_SEQ = "setting_pass_seq";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);
        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

                if(key.equals(SETTING_THEME)) {
//                    final String theme = sharedPreferences.getString
//                            (SETTING_THEME, "-1");
                    recreate();
                    Log.d("NEMO_DBG","Theme changing...");
                }else {
                    Log.d("NEMO_DBG","Settings changing");
                }
            }
        });
    }

    public void setTheme(){
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);
        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(this);

        final String theme = sharedPref.getString
                (SettingsActivity.SETTING_THEME, "-1");

        switch (theme) {
            case "Red":
                setTheme(R.style.Theme_Red_700_900);
                break;
            case "Purple":
                setTheme(R.style.Theme_Purple_700_900);
                break;
            case "Indigo":
                setTheme(R.style.Theme_Indigo_700_900);
                break;
            case "Green":
                setTheme(R.style.Theme_Green_700_900);
                break;
            case "Orange":
                setTheme(R.style.Theme_Orange_700_900);
                break;
            default:
                setTheme(R.style.Theme_PasswordManager);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
}