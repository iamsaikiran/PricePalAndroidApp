package com.app.pricepal.ui.compare;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pricepal.R;
import com.app.pricepal.main.BaseActivity;
import com.app.pricepal.models.compare_price_model;
import com.app.pricepal.models.history_items;
import com.app.pricepal.models.stores_model;
import com.app.pricepal.ui.compare.AdapterStores;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PriceCompareActivity extends BaseActivity {
    ImageView itemImg;
    TextView tvItemName,tvItemQty,tvStoreName,tvPrice,tvDateFilter;
    RecyclerView recyclerView;
    private AdapterComparePriceItem adapterItems;
    final Calendar myCalendar = Calendar.getInstance();
    private String mainDate="01/01/2022";
    private String itemName="";
    private List<compare_price_model> productsList=new ArrayList<>();
    private List<stores_model> storesList=new ArrayList<>();
    private String main_itemId;
    private String main_itemName="";

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private TextView textView;
    private AdapterStores adapterStores;
    double minPrice=0.00;
    double maxPrice=0.00;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_compare_details);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24);
        getSupportActionBar().setTitle("Price Comparison");
        textView=findViewById(R.id.text_stores);
        firebaseDatabase = FirebaseDatabase.getInstance();
        initViews();
    }
    @SuppressLint("SetTextI18n")
    private void initViews() {
        itemImg = findViewById(R.id.itemImg);
        tvItemName = findViewById(R.id.tvItemName);
        tvItemQty = findViewById(R.id.tvItemQty);
        tvStoreName = findViewById(R.id.tvStoreName);
        tvPrice = findViewById(R.id.tvPrice);
        recyclerView = findViewById(R.id.rvPriceList);
        tvDateFilter = findViewById(R.id.tvDateFilter);
        if (getIntent().getStringExtra("itemName") != null)
            itemName = getIntent().getStringExtra("itemName");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        mainDate=String.format("%02d", (myCalendar.get(Calendar.DAY_OF_MONTH)))+"/"+String.format("%02d", (myCalendar.get(Calendar.MONTH)+1))+"/"+myCalendar.get(Calendar.YEAR);
        tvDateFilter.setText("Showing result for - "+mainDate);
        getProducts();
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mainDate=String.format("%02d", dayOfMonth)+"/"+String.format("%02d", (monthOfYear+1)) +"/"+year;
            tvDateFilter.setText("Showing results for - "+mainDate);
            getProducts();
        };
        DatePickerDialog datePickerDialog=  new DatePickerDialog(PriceCompareActivity.this,R.style.DialogTheme, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        tvDateFilter.setOnClickListener(view -> {
            datePickerDialog.show();
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

    private void getProducts()
    {
        showProgressDialog();
        //searched product
        productsList.clear();
        storesList.clear();
        minPrice=0.00;
        maxPrice=0.00;

        databaseReference = firebaseDatabase.getReference("Products");
        databaseReference.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        String itemName_local = ds.child("itemName").getValue(String.class);
                        if(itemName_local.equals(itemName))
                        {
                            String id = ds.child("id").getValue(String.class);
                            String itemQty = ds.child("itemQty").getValue(String.class);
                            String itemImg_st = ds.child("itemImg").getValue(String.class);
                            String itemName_st = ds.child("itemName").getValue(String.class);

                            main_itemId = id;
                            main_itemName=itemName_st;
                            tvItemName.setText(itemName);
                            tvItemQty.setText(itemQty);
                            Picasso.get()
                                    .load(itemImg_st)
                                    .placeholder(R.drawable.storeicon)
                                    .error(R.drawable.storeicon)
                                    .into(itemImg);
                            //prices --> read all stores along with prices
                            if(!main_itemId.isEmpty()) {
                                databaseReference = firebaseDatabase.getReference("Prices");
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            try {
                                                String itm_name = ds.child("itemName").getValue(String.class);
                                                int main_store_id = ds.child("storeId").getValue(int.class);

                                                String st_date = ds.child("date").getValue(String.class);

                                                if(main_itemName.equals(itm_name) && mainDate.equals(st_date)) {
                                                    double main_price = ds.child("price").getValue(Double.class);
                                                    if(main_price < minPrice || minPrice == 0) minPrice = main_price;
                                                    if(maxPrice < main_price) maxPrice=main_price;
                                                    //fetch store details
                                                    databaseReference = firebaseDatabase.getReference("Stores");
                                                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @RequiresApi(api = Build.VERSION_CODES.N)
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                                try {
                                                                    int store_id = ds.child("id").getValue(int.class);
                                                                    if(main_store_id == store_id)
                                                                    {
                                                                        String name = ds.child("storeName").getValue(String.class);
                                                                        double loc_lat = ds.child("storeGeoLocationLat").getValue(Double.class);
                                                                        double loc_lang = ds.child("storeGeoLocationLang").getValue(Double.class);
                                                                        String img = ds.child("storeImg").getValue(String.class);
                                                                        String address = ds.child("storeAddress").getValue(String.class);
                                                                        boolean status = ds.child("storeStatus").getValue(Boolean.class);
                                                                        if(!storesList.stream().anyMatch(stores_model -> stores_model.getStoreName().equals(name)))
                                                                            storesList.add(new stores_model(
                                                                                    store_id, name, address,
                                                                                    loc_lang, loc_lat,
                                                                                    img, main_price,status)
                                                                            );
                                                                    }
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
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                            }
                        }
                    }catch(Exception e){
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

    @SuppressLint("SetTextI18n")
    private void updateUi()
    {
        adapterItems = new AdapterComparePriceItem(this, productsList);
        recyclerView.setAdapter(adapterItems);
//        recyclerView.addItemDecoration(new DividerItemDecoration(this,
//                DividerItemDecoration.VERTICAL));
        if(storesList.size() !=0 )
        {
            textView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            adapterStores = new AdapterStores(this, storesList);
            recyclerView.setAdapter(adapterStores);
            tvPrice.setText("$ "+minPrice +" ~ " +maxPrice);
        }else
        {
            textView.setText("price list not found!");
            textView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        adapterStores = new AdapterStores(this, storesList);
        recyclerView.setAdapter(adapterStores);
        hideProgressDialog();
    }
}