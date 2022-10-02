package com.app.pricepal.ui.stores;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.app.pricepal.R;
import com.app.pricepal.models.stores_model;
import com.squareup.picasso.Picasso;
import java.util.List;

public class AdapterStores extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<stores_model> storesList;

    public AdapterStores(Context mContext, List<stores_model> storesList) {
        this.mContext = mContext;
        this.storesList = storesList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_item_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final stores_model store = storesList.get(position);
        viewHolder.tvStoreName.setText(store.getStoreName());
        viewHolder.tvStoreAddr.setText(store.getStoreAddress());
        viewHolder.tvStoreNav.setText("Navigate to location");
        viewHolder.rootView.setOnClickListener(view -> mContext.startActivity(new Intent(mContext, StoreProductsActivity.class)
                .putExtra("storeId", store.getId())
                .putExtra("storeName", store.getStoreName())
        ));
        viewHolder.tvStoreNav.setOnClickListener(view -> {
                    String geoUri = "http://maps.google.com/maps?q=loc:" + store.getStoreGeoLocationLang()
                            + "," + store.getStoreGeoLocationLat();
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                    mContext.startActivity(mapIntent);
        });
            Picasso.get()
                .load(store.getStoreImg())
                .placeholder(R.drawable.storeicon)
                .error(R.drawable.storeicon)
                .into(viewHolder.imgStore);
    }
    @Override
    public int getItemCount() {
        return storesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View rootView;
        public TextView tvStoreName;
        public TextView tvStoreAddr;
        public TextView tvStoreNav;
        public ImageView imgStore;

        public ViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            this.imgStore = rootView.findViewById(R.id.imgStore);
            this.tvStoreName = rootView.findViewById(R.id.tvStoreName);
            this.tvStoreAddr = rootView.findViewById(R.id.tvStoreAddr);
            this.tvStoreNav = rootView.findViewById(R.id.tvStoreNav);
        }

    }

}
