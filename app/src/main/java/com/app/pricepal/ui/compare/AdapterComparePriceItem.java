package com.app.pricepal.ui.compare;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.app.pricepal.R;
import com.app.pricepal.models.compare_price_model;
import java.util.List;


public class AdapterComparePriceItem extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<compare_price_model> storeList;

    public AdapterComparePriceItem(Context mContext, List<compare_price_model> storeList) {
        this.mContext = mContext;
        this.storeList = storeList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.compare_product_item_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final compare_price_model store = storeList.get(position);
        viewHolder.tvStoreName.setText(store.getStoreName());
        viewHolder.tvStoreAddr.setText(store.getStoreAddress());
        viewHolder.tvPrice.setText("$ "+store.getPrice());
        viewHolder.tvStoreNav.setText("Navigate to location");

    }

    @Override
    public int getItemCount() {
        return storeList.size();
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
        public TextView tvPrice;
        public ImageView imgStore;

        public ViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            this.imgStore = rootView.findViewById(R.id.imgStore);
            this.tvStoreName = rootView.findViewById(R.id.tvStoreName);
            this.tvStoreAddr = rootView.findViewById(R.id.tvStoreAddr);
            this.tvStoreNav = rootView.findViewById(R.id.tvStoreNav);
            this.tvPrice = rootView.findViewById(R.id.tvPrice);
        }
    }
}
