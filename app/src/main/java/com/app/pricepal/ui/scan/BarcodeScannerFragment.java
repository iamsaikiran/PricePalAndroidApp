package com.app.pricepal.ui.scan;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.app.pricepal.databinding.FragmentBarcodeBinding;
import com.app.pricepal.main.BaseFragment;
import com.app.pricepal.models.items_model;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BarcodeScannerFragment extends BaseFragment {

    private FragmentBarcodeBinding binding;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    boolean check=false;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        BarcodeScannerViewModel barcodeScannerViewModel =
                new ViewModelProvider(this).get(BarcodeScannerViewModel.class);
        binding = FragmentBarcodeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Products");

        final TextView textView = binding.tvEmptyData;
        final TextView tvSearchProduct = binding.tvSearchProduct;
        final EditText etSearchProduct= binding.etSearchProduct;

        tvSearchProduct.setOnClickListener(view -> {
            String intentData=etSearchProduct.getText().toString().trim();
            if(!intentData.isEmpty())
            {
                showProgressDialog(getContext());
                databaseReference.addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            String id = ds.child("id").getValue(String.class);
                            if(String.valueOf(id).equals(intentData))
                            {
                                check=true;
                                String itemName = ds.child("itemName").getValue(String.class);
                                String itemQty = ds.child("itemQty").getValue(String.class);
                                double itemPrice = ds.child("itemPrice").getValue(double.class);
                                String itemImg = ds.child("itemImg").getValue(String.class);
                                int storeId = ds.child("storeId").getValue(int.class);
                                String storeName = ds.child("storeName").getValue(String.class);
                                boolean itemStatus = ds.child("itemStatus").getValue(boolean.class);

                                items_model items_model= new items_model(
                                        id,itemName,itemQty,
                                        itemPrice,itemImg,
                                        storeId,storeName,itemStatus);
                                startActivity(new Intent(getContext(), ProductDetailsActivity.class)
                                        .putExtra("barcode", items_model));
                            }
                        }
                        hideProgressDialog();
                        if (!check)
                        Toast.makeText(getContext(),"invalid barcode!",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
            else
                Snackbar.make(view, "barcode should not be empty!", Snackbar.LENGTH_SHORT)
                        .setAction("CLOSE", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        })
                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                        .show();
        });
        final ImageView ivScanCode= binding.tvBarcodeScan;
        ivScanCode.setOnClickListener(view -> startActivity(new Intent(getContext(), BarcodeScannerActivity.class)));
        barcodeScannerViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}