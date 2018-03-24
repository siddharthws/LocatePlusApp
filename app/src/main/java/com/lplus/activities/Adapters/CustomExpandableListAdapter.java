package com.lplus.activities.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lplus.R;
import com.lplus.activities.JavaFiles.FacilityChildInfo;

import java.util.ArrayList;

/**
 * Created by CHANDEL on 3/20/2018.
 */

public class CustomExpandableListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<FacilityChildInfo> facility_group_items;
    private LayoutInflater inflater;

    public CustomExpandableListAdapter(Context context, ArrayList<FacilityChildInfo> facility_group_items) {
        this.context = context;
        this.facility_group_items = facility_group_items;
        inflater = LayoutInflater.from( context );
    }
    @Override
    public int getCount() {
        return facility_group_items.size();
    }

    @Override
    public Object getItem(int position) {
        return facility_group_items.get( position );
    }

    @Override
    public long getItemId(int position) {
        //return (long)( groupPosition*1024+childPosition );
        return (long)( position );
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;
        if( convertView != null )
            v = convertView;
        else
            v = inflater.inflate(R.layout.facility_group_items, parent, false);
        FacilityChildInfo c = (FacilityChildInfo)getItem( position );
        TextView sequence = v.findViewById( R.id.facility_child_sequence );
        if( sequence != null )
            sequence.setText( String.valueOf(c.getFacility_sequence()));
        TextView facility_name = v.findViewById( R.id.facility_child_name );
        if( facility_name != null )
            facility_name.setText( c.getFacility_name() );
        CheckBox cb = v.findViewById( R.id.facility_child_check );
        cb.setChecked( c.getState() );
        return v;
    }
}
