package com.nemowang.passwordmanager;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;


public class LoginActivity extends AppCompatActivity  {
    public static final String PW = "setting_login_pw";

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    private androidx.biometric.BiometricManager biometricManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        biometricManager = androidx.biometric.BiometricManager.from(this);

        switch (biometricManager.canAuthenticate()) {
            case androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("NEMO_XYZ", "App can authenticate using biometrics.");
                break;
            case androidx.biometric.BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e("NEMO_XYZ", "No biometric features available on this device.");
                break;
            case androidx.biometric.BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e("NEMO_XYZ", "Biometric features are currently unavailable.");
                break;
            case androidx.biometric.BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Log.e("NEMO_XYZ", "The user hasn't associated " +
                    "any biometric credentials with their account.");

                final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG |
                                androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL);
                startActivityForResult(enrollIntent, 0x008);//REQUEST_CODE
            break;
        }

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(LoginActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NotNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NotNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Login For Password Manager")
                .setSubtitle("Log in using your biometric credential")
                .setDescription("sdfafasfsafasf")
                .setNegativeButtonText("Use Account Password")
                .build();


        Button login_btn__ = findViewById(R.id.login_btn__);
        login_btn__.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref =
                        PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                String pw__ = sharedPref.getString(PW, "");

                EditText editText_PW = findViewById(R.id.login_pass__);
                String pwd = editText_PW.getText().toString();

                Log.d("NEMO_DBG", pw__);

                if(pw__.isEmpty() || pwd.equals(pw__)){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Password error!",Toast.LENGTH_SHORT).show();

                }
            }
        });

        // Prompt appears when user clicks "Log in".
        // Consider integrating with the keystore to unlock cryptographic operations,
        // if needed by your app.
        ImageView biometricLoginButton = findViewById(R.id.imageViewFP);
        biometricLoginButton.setOnClickListener(view -> {
            biometricPrompt.authenticate(promptInfo);
        });


    }

    public void onFingerPrintClick(View view) {
        Toast.makeText(LoginActivity.this, "WWWWW",Toast.LENGTH_SHORT).show();
    }
}