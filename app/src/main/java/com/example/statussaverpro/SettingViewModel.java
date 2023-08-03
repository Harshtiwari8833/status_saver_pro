package com.example.statussaverpro;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class SettingViewModel extends ViewModel {

    private MutableLiveData<Boolean> nightModeLiveData = new MutableLiveData<>();

    public LiveData<Boolean> getNightModeLiveData() {
        return nightModeLiveData;
    }

    public void setNightMode(Boolean nightMode) {
        nightModeLiveData.setValue(nightMode);
    }

}
