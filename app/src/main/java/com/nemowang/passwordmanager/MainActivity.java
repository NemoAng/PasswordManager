package com.nemowang.passwordmanager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.nemowang.passwordmanager.databinding.ActivityMainBinding;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

import jp.wasabeef.transformers.picasso.CropCircleTransformation;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private SignInButton signInButton;
    private TextView tvTitle, tvName;
    private ImageView imgView_L, imgView_R;

    static String ls;
    static Set<String> ms;

    public static String SETTING_THEME, SETTING_ENCRYPT;
    public static Boolean SETTING_PASS_NUM, SETTING_PASS_LOW, SETTING_PASS_UPP,
            SETTING_PASS_BEG, SETTING_PASS_SYM, SETTING_PASS_SIM,SETTING_PASS_DUP, SETTING_PASS_SEQ;
    public static boolean BACK_PRESSED = false;

    public static final String PASS_COPY_LABEL = "com.nemowang.passwordmanager.PASS_COPY_LABEL";

    private static final int REQUEST_CODE_SIGN_IN = 0xf007;
    public static final int REQUEST_CODE_READ_WRITE = 0xf0f0;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions mGoogleSignInOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if(savedInstanceState == null) {
//            Log.d("NEMO_DBG", "MainActivity ------------------ onCreate");
//        } else {
//            Log.d("NEMO_DBG", "MainActivity ++++++++++++++++++ onCreate");
//        }
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

        mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.AUTH_ID_WEB))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(MainActivity.this, mGoogleSignInOptions);

        View headerView = navigationView.getHeaderView(0);
        signInButton = headerView.findViewById(R.id.btn_google);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN);
            }
        });

        tvTitle = headerView.findViewById(R.id.tvTitle);
        tvName = headerView.findViewById(R.id.tvName);
        imgView_R = headerView.findViewById(R.id.imgViewUrl);
        imgView_L = headerView.findViewById(R.id.imgView);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;

        int theme_id;
        switch (SETTING_THEME) {
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
                theme_id = R.drawable.side_nav_bar_default;
        }
        if(theme_id == R.drawable.side_nav_bar_default)
            actionBar.setBackgroundDrawable(getResources().getDrawable(theme_id,null));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == REQUEST_CODE_SIGN_IN) {
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
//            Log.w("NEMO_DBG", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account){
        if (account != null) {
            Uri imgUrl = account.getPhotoUrl();

            if (checkPermission()) {
                // You can use the API that requires the permission.
                File profile = new File(Environment.getExternalStorageDirectory() + "/PasswordManager/pm_pro.jpg");

                if (!profile.exists()) {

                    Handler uiHandler = new Handler(Looper.getMainLooper());
                    uiHandler.post(new Runnable(){
                        @Override
                        public void run() {
                            Picasso.get().load(imgUrl).into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                    try {
                                        File mydir = new File(Environment.getExternalStorageDirectory() + "/PasswordManager");
                                        if (!mydir.exists()) {
                                            mydir.mkdirs();
                                        }

                                        String fileUri = mydir.getAbsolutePath()
                                                + File.separator
//                                            + System.currentTimeMillis()
                                                + "pm_pro.jpg";
                                        FileOutputStream outputStream = new FileOutputStream(fileUri);

                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                                        outputStream.flush();
                                        outputStream.close();
                                    } catch(IOException e) {
                                        e.printStackTrace();
                                    }
                                    Log.i("NEMO_DBG", "The image was obtained correctly");
                                }

                                @Override
                                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                    Log.e("NEMO_DBG", "The image was not obtained");
                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {
                                    Log.e("NEMO_DBG", "Getting ready to get the image");
                                }
                            });
                        }
                    });
                } else {
                    String img_path = "file://" + Environment.getExternalStorageDirectory() + "/PasswordManager/pm_pro.jpg";

                    Picasso.get()
                            .load(img_path)
                            .placeholder(R.mipmap.ic_launcher_round)
                            .config(Bitmap.Config.ALPHA_8)
                            .fit()
                            .transform(new CropCircleTransformation())
                            .into(imgView_L, new Callback() {
                                @Override
                                public void onSuccess() {
                                    signInButton.setVisibility(View.GONE);
                                    imgView_R.setVisibility(View.VISIBLE);
                                    tvTitle.setText(account.getDisplayName());
                                    tvName.setText(account.getEmail());
                                }

                                @Override
                                public void onError(Exception e) {
                                    Log.i("NEMO_DBG", e.getMessage());
                                }
                            });

//                    signInButton.setVisibility(View.GONE);
//                    tvTitle.setText(account.getDisplayName());
//                    tvName.setText(account.getEmail());
                }
            }//checkPermission()
            else {
                ActivityCompat.requestPermissions(
                        MainActivity.this,
                        new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_READ_WRITE);
            }
        }
    }

    //runtime storage permission
    public boolean checkPermission() {
        int READ_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int WRITE_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if((READ_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) || (WRITE_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_READ_WRITE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode== REQUEST_CODE_READ_WRITE
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            Log.e("NEMO_DBG", "onRequestPermissionsResult");

            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            updateUI(account);
        }
    }

    //            else if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE )){
//                AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
//
//                //  Inflate the Layout Resource file you created in Step 1
//                View mView = getLayoutInflater().inflate(R.layout.access_permission, null);
//
//                Button mOk = (Button) mView.findViewById(R.id.ok__);
//                Button mCancel = (Button) mView.findViewById(R.id.cancel__);
//
//                //  Create the AlertDialog using everything we needed from above
//                mBuilder.setView(mView).setTitle("Storage access required");
//                final AlertDialog permissionDialog = mBuilder.create();
//
//                //  Set Listener for the OK Button
////                mOk.setOnTouchListener(btnEffect);
//                mOk.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick (View view) {
//                        permissionDialog.dismiss();
//                    }
//                });
//
//                //  Set Listener for the CANCEL Button
////                mCancel.setOnTouchListener(btnEffect);
//                mCancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick (View view) {
//                        permissionDialog.dismiss();
//                    }
//                });
//                permissionDialog.show();
//            }

    private final View.OnTouchListener btnEffect = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    v.setAlpha(0.6F);
                    break;
                }
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL: {
                    v.setAlpha(1F);
                    break;
                }
            }
            v.invalidate();
            return false;
        }
    };

    //useless...
    private class DownloadImage extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... urls) {
            String url = urls[0];

            Picasso.get().load(url).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                    try {
//                        File mydir = new File(Environment.getExternalStorageDirectory() + "/PasswordManager");
//                        if (!mydir.exists()) {
//                            mydir.mkdirs();
//                        }
//
//                        String fileUri = mydir.getAbsolutePath() + File.separator + ".jpg";
//                        FileOutputStream outputStream = new FileOutputStream(fileUri);
//
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//                        outputStream.flush();
//                        outputStream.close();
//                    } catch(IOException e) {
//                        e.printStackTrace();
//                    }
//                    Toast.makeText(getApplicationContext(), "Image Downloaded", Toast.LENGTH_LONG).show();
                    Log.i("NEMO_DBG", "The image was obtained correctly");
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    Log.e("NEMO_DBG", "The image was not obtained");
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    Log.e("NEMO_DBG", "Getting ready to get the image");
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            Log.d("NEMO_DBG", "placeHolderDrawable.toString()");
        }
    }

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
////        Log.d("NEMO_DBG", "MainActivity %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% onSaveInstanceState");
//
//        outState.putInt("WHAT_EVER", 1000);
//    }

//    @Override
//    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        Log.d("NEMO_DBG", "MainActivity %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% onRestoreInstanceState");
//    }
//
//    @Override
//    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
//        super.onRestoreInstanceState(savedInstanceState, persistentState);
//        Log.d("NEMO_DBG", "MainActivity %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% onRestoreInstanceState 2");
//    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(this);
        String theme = sharedPref.getString
                (SettingsActivity.SETTING_THEME, "Default");

        if(SettingsActivity.BACK_PRESSED && SETTING_THEME != theme) {
            recreate();
        }
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        Log.d("NEMO_DBG", "MainActivity ++++++++++++++++++ onPause");
//    }
//
//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//        Log.d("NEMO_DBG", "MainActivity ++++++++++++++++++ onPostResume");
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Log.d("NEMO_DBG", "MainActivity ++++++++++++++++++ onStop");
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Log.d("NEMO_DBG", "MainActivity ++++++++++++++++++ onDestroy");
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.d("NEMO_DBG", "MainActivity ++++++++++++++++++ onPause");
//    }
//
//    @Override
//    protected void onResumeFragments() {
//        super.onResumeFragments();
//        Log.d("NEMO_DBG", "MainActivity ++++++++++++++++++ onResumeFragments");
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BACK_PRESSED = true;
    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
//        super.onCreate(savedInstanceState, persistentState);
//        Log.d("NEMO_DBG", "MainActivity ++++++++++++++++++ onCreate 2");
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Log.d("NEMO_DBG", "MainActivity ++++++++++++++++++ onRestart");
//    }

    public static void setBackgroundResource(View view){
//        LinearLayout nav_header = (LinearLayout)findViewById(R.id.nav_header);
        switch (SETTING_THEME) {
            case "Red":
            case "红色":
                view.setBackgroundResource(R.drawable.side_nav_bar_red);
                break;
            case "Purple":
            case "紫色":
                view.setBackgroundResource(R.drawable.side_nav_bar_purple);
                break;
            case "Indigo":
            case "靛青":
                view.setBackgroundResource(R.drawable.side_nav_bar_indigo);
                break;
            case "Green":
            case "绿色":
                view.setBackgroundResource(R.drawable.side_nav_bar_green);
                break;
            case "Orange":
            case "橙色":
                view.setBackgroundResource(R.drawable.side_nav_bar_orange);
                break;
            default:
                view.setBackgroundResource(R.drawable.side_nav_bar_default);
        }
    }

    private void setTheme(){
        switch (SETTING_THEME) {
            case "Red":
            case "红色":
                setTheme(R.style.Theme_Red_700_900_NoActionBar);
                break;
            case "Purple":
            case "紫色":
                setTheme(R.style.Theme_Purple_700_900_NoActionBar);
                break;
            case "Indigo":
            case "靛青":
                setTheme(R.style.Theme_Indigo_700_900_NoActionBar);
                break;
            case "Green":
            case "绿色":
                setTheme(R.style.Theme_Green_700_900_NoActionBar);
                break;
            case "Orange":
            case "橙色":
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
                (SettingsActivity.SETTING_THEME, "Default");

        SETTING_ENCRYPT = sharedPref.getString
                (SettingsActivity.SETTING_ENCRYPT, "MD5");

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
        else if(item.getItemId() == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
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