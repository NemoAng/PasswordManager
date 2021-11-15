package com.nemowang.passwordmanager.ui.passgen;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.nemowang.passwordmanager.MainActivity;
import com.nemowang.passwordmanager.R;
import com.nemowang.passwordmanager.databinding.FragmentPassgenBinding;

import java.util.Random;


public class PassgenFragment extends Fragment {
    private final String passNumber = "0123456789";
    private final String passLower = "abcdefghijklmnopqrstuvwxyz";
    private final String passUpper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String passSymbol = "!\";#$%&'()*+,-./:;<=>?@[]^_`{|}~";
    private final String passSimilar = "il1Lo0O";
    private boolean noSimilar = false;
    private boolean noDuplicate = false;
    private boolean noSequential = false;
    private final Integer DEFAULT_PASS_LEN = 6;
    private Integer passwordLen = DEFAULT_PASS_LEN;

    Random rand = new Random();

    private Button btnPassGen;
    private FloatingActionButton fab;

    private PassgenViewModel passgenViewModel;
    private FragmentPassgenBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

//        passgenViewModel =
//                new ViewModelProvider(this).get(PassgenViewModel.class);

        binding = FragmentPassgenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



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

        Spinner spinner = (Spinner) root.findViewById(R.id.spinner_password);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.pass_length, R.layout.spinner_pass);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_pass_dropdown);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                passwordLen = position + DEFAULT_PASS_LEN;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("NEMO_DBG", String.valueOf(parent));
            }
        });



        btnPassGen = root.findViewById(R.id.btn_passgen);
        btnPassGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("NEMO_DBG","Password Generated...");

                Log.d("NEMO_DBG", MainActivity.SETTING_PASS_NUM?"Enable1":"Disable1");
                Log.d("NEMO_DBG", MainActivity.SETTING_PASS_LOW?"Enable2":"Disable2");
                Log.d("NEMO_DBG", MainActivity.SETTING_PASS_UPP?"Enable3":"Disable3");
                Log.d("NEMO_DBG", MainActivity.SETTING_PASS_BEG?"Enable4":"Disable4");
                Log.d("NEMO_DBG", MainActivity.SETTING_PASS_SYM?"Enable5":"Disable5");
                Log.d("NEMO_DBG", MainActivity.SETTING_PASS_SIM?"Enable6":"Disable6");
                Log.d("NEMO_DBG", MainActivity.SETTING_PASS_DUP?"Enable7":"Disable7");
                Log.d("NEMO_DBG", MainActivity.SETTING_PASS_SEQ?"Enable8":"Disable8");

                if(! MainActivity.SETTING_PASS_NUM && !MainActivity.SETTING_PASS_LOW && !MainActivity.SETTING_PASS_UPP){
                    Toast.makeText(getActivity(), "Password generator options not correct.", Toast.LENGTH_SHORT).show();

                    Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                }

            }
        });



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}