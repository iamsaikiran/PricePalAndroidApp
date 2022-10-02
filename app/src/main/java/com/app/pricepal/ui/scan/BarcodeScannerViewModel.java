package com.app.pricepal.ui.scan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BarcodeScannerViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    public BarcodeScannerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is barcode scanner fragment");
    }
    public LiveData<String> getText() {
        return mText;
    }

}