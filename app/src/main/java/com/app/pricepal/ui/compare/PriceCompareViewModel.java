package com.app.pricepal.ui.compare;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PriceCompareViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public PriceCompareViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is price compare fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}