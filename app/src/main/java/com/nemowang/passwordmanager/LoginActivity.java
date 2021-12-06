package com.nemowang.passwordmanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity  {
    public static final String PASSWORD = "setting_login_pw";
    public static final String NAME = "NAME";
    public static final String BIRTH = "BIRTH";

    private Executor executor;
    private BiometricPrompt bmtPrompt;
    private BiometricPrompt.PromptInfo pmtInfo;
    private androidx.biometric.BiometricManager bmtManager;

    private final int RQ_CODE_FINGER_PRINT = 0x0FFE;

    private boolean bmtBtnEnable = false;


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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref =
                        PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                String prefPW = sharedPref.getString(PASSWORD, "");

                EditText edtPassword = findViewById(R.id.edtPassword);
                String pwd = edtPassword.getText().toString();


                if(prefPW.isEmpty() || pwd.equals(prefPW)){
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
                    .build();

            bmtLoginButton.setOnClickListener(view -> {
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
        Toast.makeText(LoginActivity.this, "WWWWW",Toast.LENGTH_SHORT).show();
    }
}