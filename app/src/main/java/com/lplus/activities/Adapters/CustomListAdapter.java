package com.lplus.activities.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.lplus.R;
import com.lplus.activities.Interfaces.CustomAdapterListener;

import java.util.ArrayList;

/**
 * Created by Sai_Kameswari on 19-03-2018.
 */

public class CustomListAdapter extends BaseAdapter {
    Context context;
   ArrayList<String> categoriesList;
    LayoutInflater inflter;
    private CustomAdapterListener listener = null;
    public void setListener(CustomAdapterListener listener)
    {
        this.listener = listener;
    }

    public CustomListAdapter(Context applicationContext, ArrayList<String> categoriesList) {
        this.context = applicationContext;
        this.categoriesList = categoriesList;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return categoriesList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.filter_item, null);
        final TextView country =  view.findViewById(R.id.tv_filter);
        final Switch tb_filter = view.findViewById(R.id.tb_filter);
        tb_filter.setFocusableInTouchMode(false);
        tb_filter.setFocusable(false);
        country.setText(categoriesList.get(i));

        country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                    if(tb_filter.isChecked())
                    {
                        tb_filter.setChecked(false);
                    }
                    else
                    {
                        tb_filter.setChecked(true);
                        Toast.makeText(context, "Item Clicked: "+categoriesList.get(i), Toast.LENGTH_SHORT).show();
                    }
                    listener.onItemClick(categoriesList.get(i));
            }
        });
        return view;
    }
}
