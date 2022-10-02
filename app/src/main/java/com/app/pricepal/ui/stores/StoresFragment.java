package com.app.pricepal.ui.stores;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pricepal.MainActivity;
import com.app.pricepal.databinding.FragmentStoresBinding;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import com.app.pricepal.main.BaseFragment;
import  com.app.pricepal.models.stores_model;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StoresFragment extends BaseFragment {

    private FragmentStoresBinding binding;
    private TextView textView;
    private RecyclerView recyclerView;
    private AdapterStores adapterStores;
    private List<stores_model> storesList=new ArrayList<>();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private StoresViewModel storesViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        storesViewModel =
                new ViewModelProvider(this).get(StoresViewModel.class);
        binding = FragmentStoresBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        textView= binding.textStores;
        recyclerView= binding.rvStoresList;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Stores");

        getStores();
        updateUi();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void getStores()
    {
        showProgressDialog(getContext());
        storesList.clear();
        databaseReference.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        int id = ds.child("id").getValue(Integer.class);
                        String name = ds.child("storeName").getValue(String.class);
                        double loc_lat = ds.child("storeGeoLocationLat").getValue(Double.class);
                        double loc_lang = ds.child("storeGeoLocationLang").getValue(Double.class);
                        String img = ds.child("storeImg").getValue(String.class);
                        String address = ds.child("storeAddress").getValue(String.class);
                        boolean status = ds.child("storeStatus").getValue(Boolean.class);
                        storesList.add(new stores_model(
                                id, name, address,
                                loc_lang, loc_lat,
                                img, status)
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                updateUi();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void updateUi()
    {
        if(storesList.size() !=0 ) {
            textView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            adapterStores = new AdapterStores(getContext(), storesList);
            recyclerView.setAdapter(adapterStores);
        }else{
            storesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
            textView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        adapterStores = new AdapterStores(getContext(), storesList);
        recyclerView.setAdapter(adapterStores);
        hideProgressDialog();
    }
}