package com.nemowang.passwordmanager.ui.passgen;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nemowang.passwordmanager.MainActivity;
import com.nemowang.passwordmanager.R;
import com.nemowang.passwordmanager.databinding.FragmentPassgenBinding;

import java.util.Random;


public class PassgenFragment extends Fragment {
    private final String passLower = "abcdefghijklmnopqrstuvwxyz";//26
    private final String passUpper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";//26
    private final String passNumber = "0123456789";//10
    private final String passSymbol = "!\";#$%&'()*+,-./:;<=>?@[]^_`{|}~";//32
    private final String passSimilar = "il1Lo0O";//7
    private boolean noSimilar = false;
    private boolean noDuplicate = false;
    private boolean noSequential = false;
    private final Integer DEFAULT_PASS_LEN = 6;
    private Integer passwordLen;
    private final String PASS_LEN_KEY = "com.nemowang.passwordmanager.ui.passgen.PASS_LEN_KEY";
    private final String PASS_COPY_LABEL = "com.nemowang.passwordmanager.ui.passgen.PASS_COPY_LABEL";

    Random rand = new Random();

    private Button btnPassGen, btnCopy;
    private TextView tvPassTxt;
    private ScrollView scvPass;
    private View.OnClickListener btnPassGenClick;
    private FloatingActionButton fab;

    private PassgenViewModel passgenViewModel;
    private FragmentPassgenBinding binding;
    View root;
    private boolean spinnerTouched = false;

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putString(PASS_LEN_KEY, "50");
//    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        if(savedInstanceState == null) {
            Log.d("NEMO_DBG", "savedInstanceState is null.------");
        } else {
            Log.d("NEMO_DBG", "savedInstanceState not null.++++++");
//            String len = savedInstanceState.getString(PASS_LEN_KEY);
//            Log.d("NEMO_DBG", len);
        }

//        passgenViewModel =
//                new ViewModelProvider(this).get(PassgenViewModel.class);

        binding = FragmentPassgenBinding.inflate(inflater, container, false);
        root = binding.getRoot();

//        final TextView textView = binding.textPassgen;
//        passgenViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

//        fab = root.findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Log.d("NEMO_DBG", MainActivity.SETTING_THEME);
//
//                Snackbar.make(view, "HAHAHA...", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        tvPassTxt = root.findViewById(R.id.tvPass);
//        MainActivity.setBackgroundResource(tvPassTxt);

        scvPass = root.findViewById(R.id.scvPass);
        MainActivity.setBackgroundResource(scvPass);

        Spinner spinner = (Spinner) root.findViewById(R.id.spinner_password);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.pass_length, R.layout.spinner_pass);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_pass_dropdown);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        SharedPreferences preferences = getActivity().getSharedPreferences("pass_length", Context.MODE_PRIVATE);
        int position = preferences.getInt("PASS_LEN_POSITION", 0);
        passwordLen = PasswordLength(position);//position + DEFAULT_PASS_LEN;

        spinner.setSelected(false);  // must
        spinner.setSelection(position,true);  //must

        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                spinnerTouched = true;
                return false;
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinnerTouched) {
                    SharedPreferences preferences = getActivity().getSharedPreferences("pass_length", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("PASS_LEN_POSITION", position);
                    if (editor.commit()) {
                        passwordLen = PasswordLength(position);//position + DEFAULT_PASS_LEN;
                    }
                    tvPassTxt.setText(null);
                    spinnerTouched = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("NEMO_DBG", "Nothing Selected.");
            }
        });

        btnCopy = root.findViewById(R.id.btn_copy);
        btnCopy.setOnTouchListener(btnEffect);

//        btnCopy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ClipboardManager cbManager =  (ClipboardManager)root.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
//
//                // Creates a new text clip to put on the clipboard
//                ClipData clip = ClipData.newPlainText(PASS_COPY_LABEL, (String) tvPassTxt.getText());
//                cbManager.setPrimaryClip(clip);
//            }
//        });

        btnPassGen = root.findViewById(R.id.btn_passgen);

        btnPassGen.setOnTouchListener(btnEffect);

//        btnPassGen.setOnClickListener(new View.OnClickListener() {
        btnPassGenClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder sources = new StringBuilder();
                StringBuilder passbuilder = new StringBuilder();

                int password_len = passwordLen;

                if(! MainActivity.SETTING_PASS_NUM && !MainActivity.SETTING_PASS_LOW && !MainActivity.SETTING_PASS_UPP){
                    Toast.makeText(getActivity(), "Password generator options not correct.", Toast.LENGTH_SHORT).show();
//                    Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                    return;
                }

                if(MainActivity.SETTING_PASS_UPP){
                    sources.append(passUpper);
                }
                if(MainActivity.SETTING_PASS_LOW){
                    sources.append(passLower);
                }
                if(MainActivity.SETTING_PASS_NUM){
                    sources.append(passNumber);
                }
                if(MainActivity.SETTING_PASS_SYM){
                    sources.append(passSymbol);
                }

                if(MainActivity.SETTING_PASS_SIM) {
                    for (char ch : passSimilar.toCharArray()) {
                        int idx = passSimilar.indexOf(String.valueOf(ch));
                        if(idx != -1) {
                            sources.deleteCharAt(idx);
                        }
                    }
                }

                int pass_number = MainActivity.SETTING_PASS_NUM ? 1 : 0;
                int pass_lower = MainActivity.SETTING_PASS_LOW ? 1 : 0;
                int pass_upper = MainActivity.SETTING_PASS_UPP ? 1 : 0;

                if(MainActivity.SETTING_PASS_BEG) {
//                    String pass1 = passLower+passUpper;
//                    passbuilder.append(pass1.charAt(rand.nextInt(pass1.length())));
                    int idx;
                    if(!MainActivity.SETTING_PASS_LOW && !MainActivity.SETTING_PASS_UPP) {
                        Toast.makeText(getActivity(), "Settings error.", Toast.LENGTH_SHORT).show();
                        return;
                    } else if(MainActivity.SETTING_PASS_LOW && MainActivity.SETTING_PASS_UPP){
                        //ilo
                        //LO
                        idx = rand.nextInt((pass_lower+ pass_upper)*26 - 5);
                    }
                    else if(MainActivity.SETTING_PASS_LOW){
                        idx = rand.nextInt((pass_lower+ pass_upper)*26 - 3);
                    }
                    else {
                        idx = rand.nextInt((pass_lower+ pass_upper)*26 - 2);
                    }

                    passbuilder.append(sources.charAt(idx));
                    if(MainActivity.SETTING_PASS_DUP) {
                        sources.deleteCharAt(idx);
                    }
                    password_len -= 1;
                }

                if(MainActivity.SETTING_PASS_DUP) {
                    int src_len = sources.length();
                    if(src_len < password_len)
                    {
                        Toast.makeText(getActivity(), "\"Duplicate\" setting conflicts against password length.", Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        int idx, idx_comp = sources.length();
                        if(MainActivity.SETTING_PASS_SEQ) {
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
                    if(MainActivity.SETTING_PASS_SEQ) {
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

                if(password_len <= 15 || (!MainActivity.SETTING_PASS_SYM && (pass_number + pass_lower + pass_upper) < 3)){
                    Toast.makeText(getActivity(), "Your password is too weak.", Toast.LENGTH_SHORT).show();
                }
                else if(password_len <= 128){
                    Toast.makeText(getActivity(), "Your password is strong.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), "Your password is unbelievable.", Toast.LENGTH_SHORT).show();
                }

                tvPassTxt.setText(passbuilder.toString());
            }
        }
//        )
        ;
        return root;
    }

//    public PassgenFragment(int contentLayoutId) {
//        super(contentLayoutId);
//        Log.d("NEMO_DBG", "PassgenFragment ++++++++++++++++++ PassgenFragment");
//    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d("NEMO_DBG", "PassgenFragment ++++++++++++++++++ onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("NEMO_DBG", "PassgenFragment ++++++++++++++++++ onCreate");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("NEMO_DBG", "PassgenFragment ++++++++++++++++++ onViewCreated");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d("NEMO_DBG", "PassgenFragment ++++++++++++++++++ onViewStateRestored");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("NEMO_DBG", "PassgenFragment ++++++++++++++++++ onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("NEMO_DBG", "PassgenFragment ++++++++++++++++++ onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("NEMO_DBG", "PassgenFragment ++++++++++++++++++ onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("NEMO_DBG", "PassgenFragment ++++++++++++++++++ onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("NEMO_DBG", "PassgenFragment ++++++++++++++++++ onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("NEMO_DBG", "PassgenFragment ++++++++++++++++++ onDetach");
    }

    @Override
    public void onPrimaryNavigationFragmentChanged(boolean isPrimaryNavigationFragment) {
        super.onPrimaryNavigationFragmentChanged(isPrimaryNavigationFragment);
        Log.d("NEMO_DBG", "PassgenFragment ++++++++++++++++++ onPrimaryNavigationFragmentChanged");
    }

    private int PasswordLength(int position){
        if(position == 126)
            return 2048;
        else if(position == 125)
            return 1024;
        else if(position == 124)
            return 512;
        else if(position == 123)
            return 256;
        else
            return DEFAULT_PASS_LEN + position;
    }

    private View.OnTouchListener btnEffect = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    v.setAlpha(0.6F);
                    break;
                }
                case MotionEvent.ACTION_UP:
                    // Your action here on button click
                    if(v.getId() == R.id.btn_copy) {
                        ClipboardManager cbManager =  (ClipboardManager)root.getContext().getSystemService(Context.CLIPBOARD_SERVICE);

                        // Creates a new text clip to put on the clipboard
                        ClipData clip = ClipData.newPlainText(PASS_COPY_LABEL, (String) tvPassTxt.getText());
                        cbManager.setPrimaryClip(clip);

                        Toast.makeText(getActivity(), "Password copied.", Toast.LENGTH_SHORT).show();
                    }
                    else if(v.getId() == R.id.btn_passgen){
                        btnPassGenClick.onClick(v);
                    }
                case MotionEvent.ACTION_CANCEL: {
                    v.setAlpha(1F);
                    break;
                }
            }
            v.invalidate();
            return true;
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}