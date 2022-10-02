package com.app.pricepal.ui.compare;

import android.content.Context;
import android.content.SharedPreferences;
import com.app.pricepal.models.history_items;
import java.util.ArrayList;
import java.util.List;

public class ItemCompareHistory {
    Context mContext;
    public static String ITEM_NAME="ITEM_NAME";
    public static String ITEM_ID="ITEM_ID";
    public static String ITEM_COUNT="ITEM_COUNT";
    public static String prefHistKey="shared_prefs";
    SharedPreferences.Editor myEdit;
    SharedPreferences sharedPreferences;
    List<String> history_items=new ArrayList<>();

    public ItemCompareHistory(Context mContext) {
        this.mContext=mContext;
        sharedPreferences = mContext.getSharedPreferences(prefHistKey, Context.MODE_PRIVATE);
        myEdit = sharedPreferences.edit();
    }

    public void setItem(String itemId,String itemName) {
        int nxtId=getItemCount();
        myEdit.putString(ITEM_NAME+nxtId, itemName.trim());
        myEdit.putInt(ITEM_ID+nxtId, nxtId);
    }

    public String getItem(String itemName)
    {
        return sharedPreferences.getString(itemName, "");
    }

    public void incrItemCount()
    {
        int count=getItemCount()+1;
        myEdit.putInt(ITEM_COUNT, count);
    }

    public void decrItemCount()
    {
        int count=getItemCount()-1;
        myEdit.putInt(ITEM_COUNT, count);
    }

    public int getItemCount()
    {
        return sharedPreferences.getInt(ITEM_COUNT,0);
    }

    public List<String> getHistory()
    {
        for (int i=0;i<getItemCount();i++)
        {
            String itm=getItem(ITEM_NAME+i);
            if(!history_items.contains(itm))
                history_items.add(itm);
        }
        return history_items;
    }
    public boolean clearHistory()
    {
        myEdit.clear();
        myEdit.commit();
        return true;
    }
}
