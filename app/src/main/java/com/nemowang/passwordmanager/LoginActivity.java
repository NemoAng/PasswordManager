package com.nemowang.passwordmanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.preference.PreferenceManager;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity  {
    public static final String NAME = "NAME";
    public static final String BIRTH = "BIRTH";


    private Executor executor;
    private BiometricPrompt bmtPrompt;
    private BiometricPrompt.PromptInfo pmtInfo;
    private androidx.biometric.BiometricManager bmtManager;

    private final int RQ_CODE_FINGER_PRINT = 0x0FFE;

    private boolean bmtBtnEnable = false;
    private SharedPreferences sharedPref;
    private String SETTING_PASSWORD, SETTING_THEME, SETTING_ENCRYPT;

    ActivityResultLauncher<Intent> startBiometricActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Log.d("NEMO_DBG", "After Fingerprint Enrolled.");
                    }
                }});

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref =
                PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);

        SETTING_THEME = sharedPref.getString
                (SettingsActivity.SETTING_THEME, "Default");

        this.setTheme(SETTING_THEME);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edtPassword = findViewById(R.id.edtPassword);
                String passwordEnc, password = edtPassword.getText().toString();

                SETTING_ENCRYPT = sharedPref.getString(SettingsActivity.SETTING_ENCRYPT, "");
                SETTING_PASSWORD = sharedPref.getString(SettingsActivity.SETTING_PW, "");

                if(SETTING_ENCRYPT.equals(EncryptDecrypt.MD5)) {
                    passwordEnc = EncryptDecrypt.MD5(password);
                }
                else if(SETTING_ENCRYPT.equals(EncryptDecrypt.BASE64)) {
                    passwordEnc = EncryptDecrypt.BASE64(password);
                }
                else {
                    passwordEnc = EncryptDecrypt.DES(password);
                }

                if(SETTING_PASSWORD.isEmpty() || passwordEnc.equals(SETTING_PASSWORD)){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Password error!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        bmtManager = androidx.biometric.BiometricManager.from(this);

        switch (bmtManager.canAuthenticate(androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            case androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("NEMO_DBG", "App can authenticate using biometrics.");
                bmtBtnEnable = true;
                break;
            case androidx.biometric.BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e("NEMO_DBG", "No biometric features available on this device.");
                break;
            case androidx.biometric.BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e("NEMO_DBG", "Biometric features are currently unavailable.");
                break;
            case androidx.biometric.BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Log.e("NEMO_DBG", "The user hasn't associated " +
                    "any biometric credentials with their account.");

                bmtBtnEnable = false;

                if(bmtBtnEnable) {
                    final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);

                    enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                            androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG |
                                    androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL);
//                    startActivityForResult(enrollIntent, RQ_CODE_FINGER_PRINT);//REQUEST_CODE

                    startBiometricActivity.launch(enrollIntent);
                }
            break;
        }

        if(bmtBtnEnable) {
            executor = ContextCompat.getMainExecutor(this);
            bmtPrompt = new BiometricPrompt(LoginActivity.this,
                    executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode,
                                                  @NotNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);

//                    Toast.makeText(getApplicationContext(),
//                            "Authentication error: " + errString, Toast.LENGTH_SHORT)
//                            .show();
                    Log.d("NEMO_DBG", "Use App password.");
                }

                @Override
                public void onAuthenticationSucceeded(
                        @NotNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);

//                    Toast.makeText(getApplicationContext(),
//                            "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Toast.makeText(getApplicationContext(), "Authentication failed",
                            Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }

        ImageView bmtLoginButton = findViewById(R.id.imageViewFP);
        if(bmtBtnEnable){
            pmtInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Biometric Login For Password Manager")
                    .setSubtitle("Log in using your biometric credential")
                    .setNegativeButtonText("Use Password")
                    .setConfirmationRequired(true)
                    .build();

            bmtLoginButton.setOnClickListener(view -> {
//                Typeface typeface = ResourcesCompat.getFont(this, R.font.cour);
//
//                View v1 = ((ViewGroup)view).getChildAt(0);
//                ((TextView)v1).setTypeface(typeface);
//
//                View v2 = ((ViewGroup)view).getChildAt(1);
//                v2.setAlpha(0.5F);
//                View v3 = ((ViewGroup)view).getChildAt(2);
//                v3.setAlpha(0.8F);

                bmtPrompt.authenticate(pmtInfo);
            });
        }
        else {
            bmtLoginButton.setVisibility(View.GONE);
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
//        if (requestCode == RQ_CODE_FINGER_PRINT) {
//            // The Task returned from this call is always completed, no need to attach
//            // a listener.
//            Log.d("NEMO_DBG", "After Fingerprint Enrolled.");
//        }
//    }

    public void onFPrintBtnClick(View view) {
        Toast.makeText(LoginActivity.this, "onFPrintBtnClick",Toast.LENGTH_SHORT).show();
    }

    private void setTheme(String theme){
        switch (theme) {
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

    @Override
    protected void onResume() {
        super.onResume();

        if(MainActivity.BACK_PRESSED && MainActivity.SETTING_THEME != SETTING_THEME) {
           recreate();
        }
    }
}