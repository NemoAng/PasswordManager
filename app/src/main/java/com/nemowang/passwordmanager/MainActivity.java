package com.nemowang.passwordmanager;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.nemowang.passwordmanager.databinding.ActivityMainBinding;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private  Button btnNav;

    static String ls;
    static Set<String> ms;

    public static String SETTING_THEME;
    public static Boolean SETTING_PASS_NUM,SETTING_PASS_LOW, SETTING_PASS_UPP,
            SETTING_PASS_BEG, SETTING_PASS_SYM, SETTING_PASS_SIM,SETTING_PASS_DUP, SETTING_PASS_SEQ;

    public static final String PASS_COPY_LABEL = "com.nemowang.passwordmanager.PASS_COPY_LABEL";

    private static final int RC_SIGN_IN = 007;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions mGoogleSignInOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState == null) {
            Log.d("NEMO_DBG", "MainActivity ------------------ onCreate");
        } else {
            Log.d("NEMO_DBG", "MainActivity ++++++++++++++++++ onCreate");
        }
        getSettings();
        this.setTheme();
        super.onCreate(savedInstanceState);


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


        View headerView = navigationView.getHeaderView(0);
        btnNav = headerView.findViewById(R.id.button_nav_xxxx);
        btnNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                v.setVisibility(View.GONE);
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        if(savedInstanceState == null) {
            mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.AUTH_ID_WEB))
                    .requestEmail()
                    .build();

            mGoogleSignInClient = GoogleSignIn.getClient(MainActivity.this, mGoogleSignInOptions);

            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            updateUI(account);
        }


//        // QQQQ
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
//                binding.drawerLayout.closeDrawer(GravityCompat.START, true);
//                return false;
////                return true;
//            }
//        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            updateUI(account);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("NEMO_DBG", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account){
        if (account != null) {
            Log.d("NEMO_DBG", account.getEmail());
            Log.d("NEMO_DBG", account.getDisplayName());
            Log.d("NEMO_DBG", account.getPhotoUrl().toString());
        }else {
            Toast.makeText(this, "Sign in with Google failed...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("NEMO_DBG2", "MainActivity %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% onSaveInstanceState");

        outState.putInt("WHAT_EVER", 1000);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("NEMO_DBG", "MainActivity %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% onRestoreInstanceState");
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        Log.d("NEMO_DBG", "MainActivity %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% onRestoreInstanceState 2");
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d("NEMO_DBG", "MainActivity ++++++++++++++++++ onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("NEMO_DBG2", "MainActivity ++++++++++++++++++ onPause");

//        Log.d("NEMO_DBG", "Current Navi Item: " + getViewModelStore().);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.d("NEMO_DBG", "MainActivity ++++++++++++++++++ onPostCreate");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d("NEMO_DBG2", "MainActivity ++++++++++++++++++ onPostResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("NEMO_DBG2", "MainActivity ++++++++++++++++++ onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("NEMO_DBG2", "MainActivity ++++++++++++++++++ onDestroy");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("NEMO_DBG2", "MainActivity ++++++++++++++++++ onPause");
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        Log.d("NEMO_DBG", "MainActivity ++++++++++++++++++ onResumeFragments");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("NEMO_DBG", "MainActivity ++++++++++++++++++ onBackPressed");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Log.d("NEMO_DBG", "MainActivity ++++++++++++++++++ onCreate 2");
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("NEMO_DBG", "MainActivity ++++++++++++++++++ onRestart");
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

    private void setTheme(){

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

    public void onAccountClick__(View view) {
        Toast.makeText(this, "Account name copied.", Toast.LENGTH_SHORT).show();

        View v1 = ((ViewGroup)view).getChildAt(0);
        View v2 = ((ViewGroup)view).getChildAt(1);
        View v3 = ((ViewGroup)view).getChildAt(2);

        String title = ((TextView)v1).getText().toString();
        String name = ((TextView)v2).getText().toString();
        String pass = ((TextView)v3).getText().toString();


        Log.d("NEMO_DBG", title + "--" + name + "::" + pass);
    }

    public void onAccountClick(View view) {
//        Toast.makeText(this, "Account name copied.", Toast.LENGTH_SHORT).show();

//        View v1 = ((ViewGroup)view).getChildAt(0);
        View v2 = ((ViewGroup)view).getChildAt(1);
//        View v3 = ((ViewGroup)view).getChildAt(2);

//        String title = ((TextView)v1).getText().toString();
        String name = ((TextView)v2).getText().toString();
//        String pass = ((TextView)v3).getText().toString();

        ClipboardManager cbManager =  (ClipboardManager)this.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(PASS_COPY_LABEL, name);
        cbManager.setPrimaryClip(clip);

        Toast.makeText(this, "Account name copied.", Toast.LENGTH_SHORT).show();
    }

}