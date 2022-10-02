package com.app.pricepal.ui.compare;

import static com.app.pricepal.ui.compare.ItemCompareHistory.ITEM_COUNT;
import static com.app.pricepal.ui.compare.ItemCompareHistory.ITEM_ID;
import static com.app.pricepal.ui.compare.ItemCompareHistory.ITEM_NAME;
import static com.app.pricepal.ui.compare.ItemCompareHistory.prefHistKey;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.app.pricepal.databinding.FragmentCompareBinding;
import com.app.pricepal.models.history_items;
import com.app.pricepal.ui.stores.StoresViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PriceCompareFragment extends Fragment {
    private FragmentCompareBinding binding;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    ArrayList<String> list= new ArrayList<String>();
    List<String> list_hist= new ArrayList<String>();
    ListView listview_hist;
    TextView clearSearchHistory;
    SharedPreferences sharedpreferences;
    private RecyclerView recyclerView;
    private List<history_items> history_items;
    private AdapterHistory adapterHistory;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        StoresViewModel storesViewModel =
                new ViewModelProvider(this).get(StoresViewModel.class);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Products");
        binding = FragmentCompareBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        sharedpreferences = getContext().getSharedPreferences(prefHistKey, Context.MODE_PRIVATE);
        listview_hist = binding.listHist;
        clearSearchHistory=binding.clearSearchHistory;
        //  list = binding.list;
        readHistorydata();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.select_dialog_item, list);
        AutoCompleteTextView actv = binding.searchText;
        actv.setThreshold(3);
        actv.setAdapter(adapter);

        actv.setOnItemClickListener((adapterView, view, i, l) -> {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    int count=sharedpreferences.getInt(ITEM_COUNT,0);
                    editor.putString(ITEM_NAME+count, adapterView.getAdapter().getItem(i).toString());
                    editor.putInt(ITEM_ID+count, count);
                    editor.putInt(ITEM_COUNT, count+1);
                    //to save our data with key and value.
                    editor.apply();
                    readHistorydata();
                    actv.setText("");
                    startActivity(new Intent(getContext(), PriceCompareActivity.class)
                            .putExtra("itemName", adapterView.getAdapter().getItem(i).toString())
                    );
                }
        );

        clearSearchHistory.setOnClickListener(view -> {
            ItemCompareHistory itemCompareHistory= new ItemCompareHistory(getContext());
            itemCompareHistory.clearHistory();
            readHistorydata();
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i=0;
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    if (dsp != null) {
                        String name = dsp.child("itemName").getValue(String.class);
                        try {
                            if (!name.isEmpty()) {
                                if (!list.contains(name)) {
                                    list.add(name);
                                    actv.setThreshold(3);
                                    actv.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                            }catch(Exception ex){
                                ex.printStackTrace();
                                Toast.makeText(getContext(), "error :" + i, Toast.LENGTH_SHORT).show();
                            }
                            i++;

                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (getContext(), android.R.layout.select_dialog_item, list);
                AutoCompleteTextView actv = binding.searchText;
                actv.setThreshold(3);
                actv.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void readHistorydata()
    {
        ArrayAdapter<String> arr;
        ItemCompareHistory itemCompareHistory= new ItemCompareHistory(getContext());
        try {
            list_hist = itemCompareHistory.getHistory();
            arr = new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    list_hist);
            listview_hist.setAdapter(arr);
            listview_hist.setOnItemClickListener((adapterView, view, i, l) ->
            {
                startActivity(new Intent(getContext(), PriceCompareActivity.class)
                        .putExtra("itemName", adapterView.getAdapter().getItem(i).toString())
                );
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}