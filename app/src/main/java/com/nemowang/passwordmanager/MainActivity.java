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
import com.nemowang.passwordmanager.databinding.ActivityMainBinding;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    static String ls;
    static Set<String> ms;

    public static String SETTING_THEME;
    public static Boolean SETTING_PASS_NUM,SETTING_PASS_LOW, SETTING_PASS_UPP,
            SETTING_PASS_BEG, SETTING_PASS_SYM, SETTING_PASS_SIM,SETTING_PASS_DUP, SETTING_PASS_SEQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSettings();
        this.setTheme();
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null) {
            Log.d("NEMO_DBG", "------------------");
        } else {
            Log.d("NEMO_DBG", "++++++++++++++++++");
        }


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

//        binding.appBarMain.show.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_passgen, R.id.nav_setting)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        // QQQQ
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @SuppressLint("NonConstantResourceId")
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                Log.d("NEMO_DBG","onNavigationItemSelected: " + item.getTitle());
//
//                item.setChecked(true);
//                drawer.closeDrawers();
//
//                switch (item.getItemId()) {
//                    case R.id.nav_home:
//                        navController.navigate(R.id.nav_home);
//                        break;
//                    case R.id.nav_gallery:
//                        navController.navigate(R.id.nav_gallery);
//                        break;
//                    case R.id.nav_passgen:
//                        navController.navigate(R.id.nav_passgen);
//                        break;
//                    case R.id.nav_setting:
//                        navController.navigate(R.id.nav_setting);
//                        break;
//                }
//                return true;
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("NEMO_DBG", "++++++++++++++++++ onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("NEMO_DBG", "++++++++++++++++++ onPause");

//        Log.d("NEMO_DBG", "Current Navi Item: " + getViewModelStore().);
    }
    

    public static void setBackgroundResource(View view){
//        LinearLayout nav_header = (LinearLayout)findViewById(R.id.nav_header);
        switch (SETTING_THEME) {
            case "Red":
                view.setBackgroundResource(R.drawable.side_nav_bar_red);
                break;
            case "Purple":
                view.setBackgroundResource(R.drawable.side_nav_bar_purple);
                break;
            case "Indigo":
                view.setBackgroundResource(R.drawable.side_nav_bar_indigo);
                break;
            case "Green":
                view.setBackgroundResource(R.drawable.side_nav_bar_green);
                break;
            case "Orange":
                view.setBackgroundResource(R.drawable.side_nav_bar_orange);
                break;
            default:
                view.setBackgroundResource(R.drawable.side_nav_bar_default);
        }
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
            case "Orange":
                setTheme(R.style.Theme_Orange_700_900_NoActionBar);
                break;
            default:
                setTheme(R.style.Theme_PasswordManager_NoActionBar);
        }
    }

    private void getSettings(){
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);

        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(this);

        ls = sharedPref.getString
                (SettingsActivity.LS, "-1");
        ms = sharedPref.getStringSet
                (SettingsActivity.MS, null);

        SETTING_THEME = sharedPref.getString
                (SettingsActivity.SETTING_THEME, "-1");

        SETTING_PASS_NUM = sharedPref.getBoolean
                (SettingsActivity.SETTING_PASS_NUM, false);
        SETTING_PASS_LOW = sharedPref.getBoolean
                (SettingsActivity.SETTING_PASS_LOW, false);
        SETTING_PASS_UPP = sharedPref.getBoolean
                (SettingsActivity.SETTING_PASS_UPP, false);
        SETTING_PASS_BEG = sharedPref.getBoolean
                (SettingsActivity.SETTING_PASS_BEG, false);
        SETTING_PASS_SYM = sharedPref.getBoolean
                (SettingsActivity.SETTING_PASS_SYM, false);
        SETTING_PASS_SIM = sharedPref.getBoolean
                (SettingsActivity.SETTING_PASS_SIM, false);
        SETTING_PASS_DUP = sharedPref.getBoolean
                (SettingsActivity.SETTING_PASS_DUP, false);
        SETTING_PASS_SEQ = sharedPref.getBoolean
                (SettingsActivity.SETTING_PASS_SEQ, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        
        this.setBackgroundResource(findViewById(R.id.nav_header));


        getMenuInflater().inflate(R.menu.action_menu, menu);

        Log.d("NEMO_DBG", "onCreateOptionsMenu");
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        Log.d("NEMO_DBG", "onSupportNavigateUp");

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

//        return super.onOptionsItemSelected(item); // yes
        return false; // yes
    }
}