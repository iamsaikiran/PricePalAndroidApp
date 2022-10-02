package com.app.pricepal.admin;

import static java.lang.System.exit;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.pricepal.R;
import com.app.pricepal.main.BaseActivity;
import com.app.pricepal.main.Login;
import com.app.pricepal.models.User;
import com.app.pricepal.models.history_items;
import com.app.pricepal.models.items_model;
import com.app.pricepal.models.prices_model;
import com.app.pricepal.models.stores_model;
import com.app.pricepal.ui.compare.PriceCompareActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ManagePrices extends BaseActivity {
    Button add_btn,admin_logout;
    TextView tvDateFilter,tvReload;
    final Calendar myCalendar = Calendar.getInstance();
    private String stDate="01/01/2022";
    Spinner sp_item,sp_store;
    EditText et_amount;
    double st_amount=0.00;
    ProgressDialog dialog;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<history_items> items_list,stores_list;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_price_details);
        getSupportActionBar().hide();

        add_btn=findViewById(R.id.add_btn);
        et_amount=findViewById(R.id.et_amount);
        sp_item=findViewById(R.id.sp_item);
        sp_store=findViewById(R.id.sp_store);
        tvDateFilter=findViewById(R.id.tvDateFilter);
        tvReload=findViewById(R.id.tvReload);
        admin_logout=findViewById(R.id.admin_logout);

        items_list=new ArrayList<>();
        stores_list=new ArrayList<>();

        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        readData();
        add_btn.setOnClickListener(view -> addPrice(view));
        admin_logout.setOnClickListener(view -> {
            Intent intent=new Intent(this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            this.finish();
        });
        tvDateFilter.setText(stDate);

        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            monthOfYear++;
            stDate=((dayOfMonth < 10) ? "0"+dayOfMonth : dayOfMonth)+"/"+ ((monthOfYear <10 ) ? "0"+monthOfYear :monthOfYear) +"/"+year;
            tvDateFilter.setText(stDate);
        };

        tvDateFilter.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog=  new DatePickerDialog(this,R.style.DialogTheme, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
            datePickerDialog.show();
        });
        tvReload.setOnClickListener(view -> {
            Intent i = new Intent(this, ManagePrices.class);
            finish();
            startActivity(i);
        });
    }
    boolean flag=true;

    private void addPrice(View view) {
        if (!validateForm()) {
            return;
        }
        showProgressDialog();
        double st_amount=Double.parseDouble(et_amount.getText().toString().trim());
        try {
            history_items sel_store = (history_items) sp_store.getSelectedItem();
            int sel_store_id = Integer.parseInt(sel_store.getId());
            String sel_store_name = sel_store.getItemName();
            history_items sel_item = (history_items) sp_item.getSelectedItem();
            String sel_item_id=sel_item.getId();
            String sel_item_name=sel_item.getItemName();

            int id= (int) (System.currentTimeMillis()/1000);
            databaseReference = firebaseDatabase.getReference("Prices");
            databaseReference.addListenerForSingleValueEvent( new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        try {
                            String date = ds.child("date").getValue(String.class);
                            int storeId = ds.child("storeId").getValue(Integer.class);
                            String itemName = ds.child("itemName").getValue(String.class);
                            if(itemName.equals(sel_item_name) &&
                                    sel_store_id == storeId  &&
                                    date.equals(stDate)
                            )
                            {
                                hideProgressDialog();
                                Snackbar.make(view, "price list already exist!", Snackbar.LENGTH_SHORT).show();
                                flag=false;
                                return;
                            }
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                            Toast.makeText(ManagePrices.this, "ex: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(flag)
                        writeNewPrice(view,id,sel_item_id,sel_store_id,sel_item_name,stDate,st_amount);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        }catch (Exception ex)
        {
            ex.printStackTrace();
            Toast.makeText(this, "something went wrong - "+ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(et_amount.getText().toString())) {
            et_amount.setError("Required");
            result = false;
        } else {
            et_amount.setError(null);
        }
        if(sp_item.getSelectedItemId() == 0)
        {
            Toast.makeText(this, "no item selected!", Toast.LENGTH_SHORT).show();
            result=false;
        }
        if(sp_store.getSelectedItemId() == 0)
        {
            Toast.makeText(this, "no store selected!", Toast.LENGTH_SHORT).show();
            result=false;
        }
        return result;
    }

    // [START basic_write]
    private void writeNewPrice(View view,int id,String item_id,int storeId,String itemName,String date,double price) {
        prices_model row = new prices_model(id, storeId,itemName,date,price);
        try {
            databaseReference = firebaseDatabase.getReference();
            databaseReference.child("Prices").child(String.valueOf(id)).setValue(row,new DatabaseReference.CompletionListener() {
                public void onComplete(DatabaseError error, DatabaseReference ref) {
                    String today_date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                    if(date.equals(today_date)) {
                        try {
                            databaseReference = firebaseDatabase.getReference("Products");
                            databaseReference.addListenerForSingleValueEvent( new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        try
                                        {
                                            String id = ds.child("id").getValue(String.class);
                                            if(Objects.equals(id, item_id))
                                            {
                                                String key=ds.getKey();
                                                assert key != null;
                                                databaseReference.child(key).child("itemPrice").setValue(price);
                                                Toast.makeText(getApplicationContext(), "success!", Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(getApplicationContext(), ManagePrices.class);
                                                finish();
                                                startActivity(i);
                                            }
                                        }catch (Exception e)
                                        {
                                            e.printStackTrace();
                                            Toast.makeText(ManagePrices.this, "ex: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }else
                    {
                        Toast.makeText(getApplicationContext(), "success!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), ManagePrices.class);
                        finish();
                        startActivity(i);
                    }
                }
            });

        }catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this, "failed to add list - "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        hideProgressDialog();
    }
    private void readData()
    {
        stores_list.add(new history_items("0","- select store -"));
        items_list.add(new history_items("0","- select product -"));

        //reading items
        databaseReference = firebaseDatabase.getReference("Products");
        databaseReference.addListenerForSingleValueEvent( new ValueEventListener() {
            @SuppressLint("NewApi")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showProgressDialog();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        String id = ds.child("id").getValue(String.class);
                        String itemName = ds.child("itemName").getValue(String.class);
                        if(!items_list.stream().anyMatch(history_items -> history_items.getItemName().equals(itemName)))
                            items_list.add(new history_items(String.valueOf(id),itemName));
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    hideProgressDialog();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        ArrayAdapter<history_items> adp1 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, items_list);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_item.setAdapter(adp1);

        //reading stores
        databaseReference = firebaseDatabase.getReference("Stores");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showProgressDialog();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        int id = ds.child("id").getValue(int.class);
                        String name = ds.child("storeName").getValue(String.class);
                        stores_list.add(new history_items(String.valueOf(id),name));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                hideProgressDialog();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        hideProgressDialog();
        ArrayAdapter<history_items> adp2 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, stores_list);
        adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_store.setAdapter(adp2);
    }
}
