package com.nemowang.passwordmanager.ui.setting;
//JUST PLUS SYMBOL
//THEME LIST->PURPLE
//DROPDOWN->INPUT

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.nemowang.passwordmanager.MainActivity;
import com.nemowang.passwordmanager.R;
import com.nemowang.passwordmanager.SettingsActivity;
import com.nemowang.passwordmanager.databinding.FragmentSettingBinding;


public class SettingFragment extends PreferenceFragmentCompat {
    private Button btnSetting;
    private ListPreference lpTheme;
    private EditTextPreference edtPassword;

    private SettingViewModel settingViewModel;
    private FragmentSettingBinding binding;


//    @Override
//    public void onInflate(@NonNull Context context, @NonNull AttributeSet attrs, @Nullable Bundle savedInstanceState) {
//        super.onInflate(context, attrs, savedInstanceState);
//    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        lpTheme = (ListPreference) findPreference (SettingsActivity.SETTING_THEME);
        lpTheme.setTitle(lpTheme.getValue());

        lpTheme.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                lpTheme.setTitle(newValue.toString());

                if(getActivity().getClass() == MainActivity.class){
                    getActivity().recreate();
                }

                return true;//yes to settings activity
            }
        });

        edtPassword = (EditTextPreference) findPreference (SettingsActivity.SETTING_PW);
        edtPassword.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
//                Log.d("NEMO_DBG", edtPassword.getText() + " 加密这里...");
                return true;//yes to settings activity
            }
        });

        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(getContext());

        sharedPref.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if(!key.equals(SettingsActivity.SETTING_THEME)) {
                    MainActivity.SETTING_THEME = sharedPref.getString
                            (SettingsActivity.SETTING_THEME, "Default");

                    MainActivity.SETTING_PASS_NUM = sharedPref.getBoolean
                            (SettingsActivity.SETTING_PASS_NUM, false);
                    MainActivity.SETTING_PASS_LOW = sharedPref.getBoolean
                            (SettingsActivity.SETTING_PASS_LOW, false);
                    MainActivity.SETTING_PASS_UPP = sharedPref.getBoolean
                            (SettingsActivity.SETTING_PASS_UPP, false);
                    MainActivity.SETTING_PASS_BEG = sharedPref.getBoolean
                            (SettingsActivity.SETTING_PASS_BEG, false);
                    MainActivity.SETTING_PASS_SYM = sharedPref.getBoolean
                            (SettingsActivity.SETTING_PASS_SYM, false);
                    MainActivity.SETTING_PASS_SIM = sharedPref.getBoolean
                            (SettingsActivity.SETTING_PASS_SIM, false);
                    MainActivity.SETTING_PASS_DUP = sharedPref.getBoolean
                            (SettingsActivity.SETTING_PASS_DUP, false);
                    MainActivity.SETTING_PASS_SEQ = sharedPref.getBoolean
                            (SettingsActivity.SETTING_PASS_SEQ, false);

                }
            }
        });

    }

//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        settingViewModel =
//                new ViewModelProvider(this).get(SettingViewModel.class);

//        binding = FragmentSettingBinding.inflate(inflater, container, false);


//        binding = FragmentSettingBinding.inflate(inflater, container, false);
//
//
//        View root = binding.getRoot();

//        final TextView textView = binding.textSetting;
//        settingViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

//        btnSetting = root.findViewById(R.id.btn_setting);
//
//        btnSetting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("NEMO_DBG","Setting Button");
//            }
//        });

//        return root;
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}