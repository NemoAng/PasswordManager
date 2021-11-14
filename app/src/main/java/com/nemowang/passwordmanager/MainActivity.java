package com.nemowang.passwordmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.nemowang.passwordmanager.databinding.ActivityMainBinding;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    static Boolean sw1, sw2, sw3, sw4, ck1, ck2;
    static String ls;
    static String SETTING_THEME;
    static Set<String> ms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSettings();
        this.setTheme();
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_setting)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    public void setTheme(){
        switch (SETTING_THEME) {
            case "Red":
                setTheme(R.style.Theme_Red_700_900_NoActionBar);
                break;
            case "Purple":
                setTheme(R.style.Theme_Purple_700_900_NoActionBar);
                break;
            case "Indigo":
                setTheme(R.style.Theme_Indigo_700_900_NoActionBar);
                break;
            case "Green":
                setTheme(R.style.Theme_Green_700_900_NoActionBar);
                break;
            default:
                setTheme(R.style.Theme_PasswordManager_NoActionBar);
        }
    }


    private void getSettings(){
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);

        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(this);

        sw1 = sharedPref.getBoolean
                (SettingsActivity.SW1, false);
        sw2 = sharedPref.getBoolean
                (SettingsActivity.SW2, false);
        sw3 = sharedPref.getBoolean
                (SettingsActivity.SW3, false);

        sw4 = sharedPref.getBoolean
                (SettingsActivity.SW4, false);
        ck1 = sharedPref.getBoolean
                (SettingsActivity.CK1, false);
        ck2 = sharedPref.getBoolean
                (SettingsActivity.CK2, false);

        ls = sharedPref.getString
                (SettingsActivity.LS, "-1");
        ms = sharedPref.getStringSet
                (SettingsActivity.MS, null);

        SETTING_THEME = sharedPref.getString
                (SettingsActivity.SETTING_THEME, "-1");

        Log.d("NEMO_DBG","preference 1: " + (sw1 ? "Enabled" : "Disabled"));
        Log.d("NEMO_DBG","preference 2: " + (sw2 ? "Enabled" : "Disabled"));
        Log.d("NEMO_DBG","preference 3: " + (sw3 ? "Enabled" : "Disabled"));

        Log.d("NEMO_DBG","preference 4: " + (sw4 ? "Enabled" : "Disabled"));
        Log.d("NEMO_DBG","preference 5: " + (ck1 ? "Checked" : "Unchecked"));
        Log.d("NEMO_DBG","preference 6: " + (ck2 ? "Checked" : "Unchecked"));

        Log.d("NEMO_DBG","preference 7: " + ls);
        Log.d("NEMO_DBG","preference 8: " + ms);

        Log.d("NEMO_DBG","theme: " + SETTING_THEME);
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("NEMO_DBG","onOptionsItemSelected");

        if (item.getItemId() == R.id.action_settings) {
            Log.d("NEMO_DBG","action_settings");
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }
}