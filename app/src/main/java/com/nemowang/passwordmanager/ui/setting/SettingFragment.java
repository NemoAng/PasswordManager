package com.nemowang.passwordmanager.ui.setting;
//JUST PLUS SYMBOL
//THEME LIST->PURPLE
//DROPDOWN->INPUT

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.nemowang.passwordmanager.databinding.FragmentSettingBinding;


public class SettingFragment extends Fragment {

    private Button btnSetting;

    private SettingViewModel settingViewModel;
    private FragmentSettingBinding binding;


    @Override
    public void onInflate(@NonNull Context context, @NonNull AttributeSet attrs, @Nullable Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingViewModel =
                new ViewModelProvider(this).get(SettingViewModel.class);

//        binding = FragmentSettingBinding.inflate(inflater, container, false);


        binding = FragmentSettingBinding.inflate(inflater, container, false);


        View root = binding.getRoot();

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

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}