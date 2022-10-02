package com.app.pricepal.ui.compare;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pricepal.R;
import com.app.pricepal.models.history_items;
import com.app.pricepal.models.items_model;
import com.app.pricepal.ui.scan.ProductDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterHistory extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<history_items> itemsList;

    public AdapterHistory(Context mContext, List<history_items> itemsList) {
        this.mContext = mContext;
        this.itemsList = itemsList;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void filterList(List<history_items> filterList) {
        itemsList = filterList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final history_items item = itemsList.get(position);
        viewHolder.tvItemName.setText(item.getItemName());
        viewHolder.tvStoreName.setText(item.getId());
//
//        viewHolder.rootView.setOnClickListener(view ->
//                mContext.startActivity(new Intent(mContext, ProductDetailsActivity.class)
//                .putExtra("product",item)));
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
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
        public TextView tvItemName;
        public TextView tvStoreName;
        public TextView tvItemQty;
        public TextView tvItemPrice;
        public ImageView tvItemImg;

        public ViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            this.tvItemName = rootView.findViewById(R.id.tvItemName);
            this.tvStoreName = rootView.findViewById(R.id.tvStoreName);
            this.tvItemPrice = rootView.findViewById(R.id.tvPrice);
            this.tvItemQty = rootView.findViewById(R.id.tvItemQty);
            this.tvItemImg = rootView.findViewById(R.id.itemImg);
        }
    }
}
