package com.app.pricepal.ui.items;

import static android.content.ContentValues.TAG;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.app.pricepal.databinding.FragmentItemsBinding;
import com.app.pricepal.models.items_model;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ItemsFragment extends Fragment {

    private FragmentItemsBinding binding;
    private final List<items_model> itemsList=new ArrayList<>();
    private List<items_model> filterItemsList=new ArrayList<>();
    private AdapterItems adapterItems;
    private TextView textView;
    private ItemsViewModel homeViewModel;
    private RecyclerView recyclerView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private EditText tvSearchProduct;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(ItemsViewModel.class);
        binding = FragmentItemsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        textView = binding.textItems;
        tvSearchProduct = binding.tvSearchProduct;
        recyclerView= binding.rvProductsList;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Products");
        fetchProducts();
        updateUi();
        tvSearchProduct.addTextChangedListener(new TextWatcher() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                Log.e(TAG, "onTextChanged: "+cs);
                getFilter(cs);
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void fetchProducts()
    {
        itemsList.clear();
        databaseReference.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        String id = ds.child("id").getValue(String.class);
                        String itemName = ds.child("itemName").getValue(String.class);
                        String itemQty = ds.child("itemQty").getValue(String.class);
                        double itemPrice = ds.child("itemPrice").getValue(double.class);
                        String itemImg = ds.child("itemImg").getValue(String.class);
                        int storeId = ds.child("storeId").getValue(int.class);
                        String storeName = ds.child("storeName").getValue(String.class);
                        boolean itemStatus = ds.child("itemStatus").getValue(boolean.class);
                        itemsList.add(new items_model(
                                id, itemName, itemQty,
                                itemPrice, itemImg,
                                storeId, storeName, itemStatus)
                        );

                    }catch (Exception e){
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

    private void getFilter(CharSequence ch) {
        String charString = ch.toString();
        if (charString.isEmpty()) {
            filterItemsList = itemsList;
        } else {
            List<items_model> filteredList = new ArrayList<>();
            for (items_model item : itemsList) {
                if (item.getItemName().toLowerCase().contains(charString.toLowerCase())) {
                    filteredList.add(item);
                }
            }
            filterItemsList = filteredList;
        }
        adapterItems.filterList(filterItemsList);
        if (filterItemsList.isEmpty()) {
            textView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            textView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

    }
    private void updateUi(){
        if(itemsList.size() !=0 ) {
            textView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            adapterItems = new AdapterItems(getContext(), itemsList);
            recyclerView.setAdapter(adapterItems);
        }else{
            homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
            textView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }
}