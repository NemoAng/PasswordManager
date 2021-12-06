package com.nemowang.passwordmanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.nemowang.passwordmanager.ui.setting.SettingFragment;

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
    public static final String SETTING_PW = "setting_login_pw";

    public static final String SETTING_PASS_NUM = "setting_pass_number";
    public static final String SETTING_PASS_LOW = "setting_pass_lower";
    public static final String SETTING_PASS_UPP = "setting_pass_upper";
    public static final String SETTING_PASS_BEG = "setting_pass_begin";
    public static final String SETTING_PASS_SYM = "setting_pass_symbol";
    public static final String SETTING_PASS_SIM = "setting_pass_similar";
    public static final String SETTING_PASS_DUP = "setting_pass_duplicate";
    public static final String SETTING_PASS_SEQ = "setting_pass_seq";

    public static boolean BACK_PRESSED = false;

    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_activity);

//        if (savedInstanceState == null) {
        if (true) {
            getSupportFragmentManager()
                    .beginTransaction()
//                    .replace(R.id.settings, new SettingsFragment())
                    .replace(R.id.settings, new SettingFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);

//        SharedPreferences sharedPref =
//                PreferenceManager.getDefaultSharedPreferences(this);
//
//        sharedPref.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
//            @Override
//            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//
//                if(key.equals(SETTING_THEME)) {
//                    recreate();
//                 }
//            }
//        });

        Log.d("NEMO_DBG", "SettingsActivity->onCreate");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        BACK_PRESSED = true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

                if(key.equals(SETTING_THEME)) {
                    SettingsActivity.this.recreate();
                }
                if(key.equals(SETTING_PW)){
                    Log.d("NEMO_DBG", sharedPreferences.getString(SETTING_PW, "") + " 加密这里!!!");
                }
            }
        });

        Log.d("NEMO_DBG", "SettingsActivity->onStart");
    }

//    @Override
//    public void onConfigurationChanged(@NonNull Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        Log.d("NEMO_DBG", "SettingsActivity->onConfigurationChanged");
//    }
//
//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//        Log.d("NEMO_DBG", "SettingsActivity->onPostResume");
//    }
//
//    @Override
//    public void onContentChanged() {
//        super.onContentChanged();
//        Log.d("NEMO_DBG", "SettingsActivity->onContentChanged");
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        Log.d("NEMO_DBG", "SettingsActivity->onPause");
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.d("NEMO_DBG", "SettingsActivity->onResume");
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void setTheme(){
//        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);
        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(this);

        final String theme = sharedPref.getString
                (SettingsActivity.SETTING_THEME, "Default");

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

//    public static class SettingsFragment extends PreferenceFragmentCompat {
//        @Override
//        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
//            setPreferencesFromResource(R.xml.root_preferences, rootKey);
//        }
//    }
}