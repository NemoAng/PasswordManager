package com.nemowang.passwordmanager.ui.setting;
//JUST PLUS SYMBOL
//THEME LIST->PURPLE
//DROPDOWN->INPUT

import android.os.Bundle;
import android.widget.Button;

import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.nemowang.passwordmanager.R;
import com.nemowang.passwordmanager.databinding.FragmentSettingBinding;


public class SettingFragment extends PreferenceFragmentCompat {

    private Button btnSetting;

    private SettingViewModel settingViewModel;
    private FragmentSettingBinding binding;


//    @Override
//    public void onInflate(@NonNull Context context, @NonNull AttributeSet attrs, @Nullable Bundle savedInstanceState) {
//        super.onInflate(context, attrs, savedInstanceState);
//    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        ListPreference lpTheme = (ListPreference) findPreference ("setting_theme");
        lpTheme.setTitle(lpTheme.getValue());
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