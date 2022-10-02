package com.app.pricepal.ui.stores;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StoresViewModel extends ViewModel {
    private final MutableLiveData<String> mText;
    public StoresViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("No Stores found!");
    }
    public LiveData<String> getText() {
        return mText;
    }
}