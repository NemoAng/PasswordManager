package com.nemowang.passwordmanager.ui.passgen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PassgenViewModel extends ViewModel {

    private MutableLiveData<String> mText;

//    public PassgenViewModel() {
//        mText = new MutableLiveData<>();
//        mText.setValue("This is password gen........ ");
//    }

    public LiveData<String> getText() {
        return mText;
    }
}