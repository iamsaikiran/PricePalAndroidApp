package com.app.pricepal.main;

import android.app.ProgressDialog;
import android.content.Context;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;

public class BaseFragment extends Fragment {

    private ProgressDialog mProgressDialog;

    public void showProgressDialog(Context mContext) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();
    }
    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}