package com.app.pricepal.ui.scan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.pricepal.R;
import com.app.pricepal.models.items_model;
import com.squareup.picasso.Picasso;

public class ProductDetailsActivity extends AppCompatActivity {
    ImageView itemImg;
    TextView tvItemId,tvItemName,tvItemQty,tvStoreName,tvPrice,tvStoreId;
    items_model item_details;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details_search);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.teal_700)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24);
        getSupportActionBar().setTitle("Product Details");
        initViews();
    }

    @SuppressLint("SetTextI18n")
    private void initViews() {
        itemImg = findViewById(R.id.itemImg);
        tvItemName = findViewById(R.id.tvItemName);
        tvItemId= findViewById(R.id. tvItemId);
        tvItemQty = findViewById(R.id.tvItemQty);
        tvStoreName = findViewById(R.id.tvStoreName);
        tvStoreId = findViewById(R.id.tvStoreId);
        tvPrice = findViewById(R.id.tvItemPrice);
        try {
            item_details = (items_model) getIntent().getSerializableExtra("product");
            tvItemId.setText("Id# "+item_details.getId());
            tvItemName.setText(item_details.getItemName());
            tvItemQty.setText(item_details.getItemQty());
            tvStoreName.setText(item_details.getStoreName());
            tvStoreId.setText("#"+item_details.getStoreId());
            tvPrice.setText("$ "+item_details.getItemPrice());
            Picasso.get()
                    .load(item_details.getItemImg())
                    .placeholder(R.drawable.storeicon)
                    .error(R.drawable.storeicon)
                    .into(itemImg);
        }catch (Exception e){
            e.printStackTrace();
        }
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
}