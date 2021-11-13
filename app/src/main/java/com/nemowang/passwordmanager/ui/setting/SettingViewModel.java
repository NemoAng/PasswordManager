package com.nemowang.passwordmanager.ui.setting;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SettingViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SettingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is xxxxxxxxxxxxxxxxxxx fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}