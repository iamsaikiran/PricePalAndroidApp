package com.app.pricepal.ui.stores;

import static android.content.ContentValues.TAG;
import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.app.pricepal.R;
import com.app.pricepal.main.BaseActivity;
import com.app.pricepal.main.BaseFragment;
import com.app.pricepal.models.items_model;
import com.app.pricepal.ui.items.AdapterItems;
import com.app.pricepal.ui.scan.ProductDetailsActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class StoreProductsActivity extends BaseActivity {
    private final List<items_model> itemsList=new ArrayList<>();
    private List<items_model> filterItemsList=new ArrayList<>();
    private AdapterItems adapterItems;
    private int storeId=0;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private TextView textView;
    private RecyclerView recyclerView;
    private EditText tvSearchProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_products_activity);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24);
        initViews();
    }

    @SuppressLint("SetTextI18n")
    private void initViews()
    {
        if (getIntent().getStringExtra("storeName") != null) {
            getSupportActionBar().setTitle(getIntent().getStringExtra("storeName"));
            storeId=getIntent().getIntExtra("storeId",0);
        }
        textView = findViewById(R.id.text_items);
        tvSearchProduct = findViewById(R.id.tvSearchProduct);
        recyclerView = findViewById(R.id.rvProductsList);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Products");
        searchItems();
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
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    boolean check=false;

    private void searchItems()
    {
        itemsList.clear();
        if(storeId != 0)
        {
            showProgressDialog();
            databaseReference.addListenerForSingleValueEvent( new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    for (DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        try {
                            int id = ds.child("storeId").getValue(Integer.class);
                            if(id == storeId) {
                                check = true;
                                String itemName = ds.child("itemName").getValue(String.class);
                                String itemQty = ds.child("itemQty").getValue(String.class);
                                double itemPrice = ds.child("itemPrice").getValue(double.class);
                                String itemImg = ds.child("itemImg").getValue(String.class);
                                int storeId = ds.child("storeId").getValue(int.class);
                                String storeName = ds.child("storeName").getValue(String.class);
                                boolean itemStatus = ds.child("itemStatus").getValue(boolean.class);
                                itemsList.add(
                                        new items_model(
                                                String.valueOf(id), itemName, itemQty,
                                                itemPrice, itemImg,
                                                storeId, storeName, itemStatus)
                                );
                                updateUi();
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    hideProgressDialog();
                    if (!check)
                        Toast.makeText(getApplicationContext(),"no items found!",Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    private void updateUi()
    {
        if(itemsList.size() !=0 ) {
            textView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            adapterItems = new AdapterItems(this, itemsList);
            recyclerView.setAdapter(adapterItems);
        }else{
            textView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetTextI18n")
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
            Toast.makeText(this, "no products found!", Toast.LENGTH_SHORT).show();
            textView.setVisibility(View.VISIBLE);
            textView.setText("no products found!");
            recyclerView.setVisibility(View.GONE);
        }else{
            textView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

}