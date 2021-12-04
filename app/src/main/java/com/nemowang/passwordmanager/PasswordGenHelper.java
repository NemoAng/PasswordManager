package com.nemowang.passwordmanager;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

public class PasswordGenHelper {
    private static final String passLower = "abcdefghijklmnopqrstuvwxyz";//26
    private static final String passUpper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";//26
    private static final String passNumber = "0123456789";//10
    private static final String passSymbol = "!\";#$%&'()*+,-./:;<=>?@[]^_`{|}~";//32
    private static final String passSimilar = "il1Lo0O";//7
    private boolean noSimilar = false;
    private boolean noDuplicate = false;
    private boolean noSequential = false;
    private final Integer DEFAULT_PASS_LEN = 6;
    private Integer passwordLen;

    private static final Random rand = new Random();


    public static String Password(View v, int passwordLen, boolean SETTING_PASS_UPP__,
                                           boolean SETTING_PASS_LOW__, boolean SETTING_PASS_NUM__,
                                           boolean SETTING_PASS_SYM__, boolean SETTING_PASS_DUP__,
                                           boolean SETTING_PASS_SEQ__, boolean SETTING_PASS_BEG__,
                                           boolean SETTING_PASS_SIM__) {
        StringBuilder sources = new StringBuilder();
        StringBuilder passbuilder = new StringBuilder();

        int password_len = passwordLen;

        if(! SETTING_PASS_NUM__ && !SETTING_PASS_LOW__ && !SETTING_PASS_UPP__){
            Toast.makeText(v.getContext(), "Password generator options not correct.", Toast.LENGTH_SHORT).show();
            return "";
        }

        if(SETTING_PASS_UPP__){
            sources.append(passUpper);
        }
        if(SETTING_PASS_LOW__){
            sources.append(passLower);
        }
        if(SETTING_PASS_NUM__){
            sources.append(passNumber);
        }
        if(SETTING_PASS_SYM__){
            sources.append(passSymbol);
        }

        if(SETTING_PASS_SIM__) {
            for (char ch : passSimilar.toCharArray()) {
                int idx = passSimilar.indexOf(String.valueOf(ch));
                if(idx != -1) {
                    sources.deleteCharAt(idx);
                }
            }
        }

        int pass_number = SETTING_PASS_NUM__ ? 1 : 0;
        int pass_lower = SETTING_PASS_LOW__ ? 1 : 0;
        int pass_upper = SETTING_PASS_UPP__ ? 1 : 0;

        if(SETTING_PASS_BEG__) {
//                    String pass1 = passLower+passUpper;
//                    passbuilder.append(pass1.charAt(rand.nextInt(pass1.length())));
            int idx;

            if(!SETTING_PASS_LOW__ && !SETTING_PASS_UPP__) {
                Toast.makeText(v.getContext(), "Settings error.", Toast.LENGTH_SHORT).show();
                return "";
            } else if(SETTING_PASS_LOW__ && SETTING_PASS_UPP__){
                //ilo
                //LO
                idx = rand.nextInt((pass_lower+ pass_upper)*26 - 5);
            }
            else if(SETTING_PASS_LOW__){
                idx = rand.nextInt((pass_lower+ pass_upper)*26 - 3);
            }
            else {
                idx = rand.nextInt((pass_lower+ pass_upper)*26 - 2);
            }

            passbuilder.append(sources.charAt(idx));
            if(SETTING_PASS_DUP__) {
                sources.deleteCharAt(idx);
            }
            password_len -= 1;
        }

        if(SETTING_PASS_DUP__) {
            int src_len = sources.length();

//            Log.d("NEMO_DBG_X", String.valueOf(src_len));
//            Log.d("NEMO_DBG_X", String.valueOf(password_len));

            if(src_len < password_len)
            {
                Toast.makeText(v.getContext(), "\"Duplicate\" setting conflicts against password length.", Toast.LENGTH_SHORT).show();
                return "";
            }else {
                int idx, idx_comp = sources.length();
                if(SETTING_PASS_SEQ__) {
                    for (int i=0; i < password_len; i++) {
                        int len = sources.length();
                        idx = rand.nextInt(len);
                        while(len >= 3 && (idx == idx_comp) || ((idx_comp != 0) && (idx == (idx_comp-1)))){
                            idx = rand.nextInt(sources.length());
                        }
                        passbuilder.append(sources.charAt(idx));
                        sources.deleteCharAt(idx);
                        idx_comp = idx;
                    }

                    Log.d("NEMO_DBG","WHAT " + sources.toString());
                }
                else {
                    for (int i=0; i < password_len; i++) {
                        idx = rand.nextInt(sources.length());
                        passbuilder.append(sources.charAt(idx));
                        sources.deleteCharAt(idx);
                    }
                }
            }
        } else {
            int idx, idx_comp = sources.length();
            if(SETTING_PASS_SEQ__) {
                for (int i=0; i < password_len; i++) {
                    idx = rand.nextInt(sources.length());
                    while((idx == idx_comp) || ((idx_comp != 0) && (idx == (idx_comp-1)))){
                        idx = rand.nextInt(sources.length());
                    }
                    passbuilder.append(sources.charAt(idx));

                    idx_comp = idx;
                }
            }
            else {
                for (int i=0; i < password_len; i++) {
                    idx = rand.nextInt(sources.length());
                    passbuilder.append(sources.charAt(idx));
                }
            }
        }

        if(password_len <= 15 || (!SETTING_PASS_SYM__ && (pass_number + pass_lower + pass_upper) < 3)){
            Toast.makeText(v.getContext(), "Your password is too weak.", Toast.LENGTH_SHORT).show();
        }
        else if(password_len <= 128){
            Toast.makeText(v.getContext(), "Your password is strong.", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(v.getContext(), "Your password is unbelievable.", Toast.LENGTH_SHORT).show();
        }

//        tvPassTxt.setText(passbuilder.toString());
        return  passbuilder.toString();
    }
}
