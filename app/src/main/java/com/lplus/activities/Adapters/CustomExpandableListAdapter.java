package com.lplus.activities.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lplus.R;
import com.lplus.activities.JavaFiles.FacilityChildInfo;

import java.util.ArrayList;

/**
 * Created by CHANDEL on 3/20/2018.
 */

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<String> facility_group;
    private ArrayList<ArrayList<FacilityChildInfo>> facility_group_items;
    private LayoutInflater inflater;

    public CustomExpandableListAdapter(Context context, ArrayList<String> facility_group, ArrayList<ArrayList<FacilityChildInfo>> facility_group_items) {
        this.context = context;
        this.facility_group = facility_group;
        this.facility_group_items = facility_group_items;
        inflater = LayoutInflater.from( context );
    }

    @Override
    public int getGroupCount() {
        return facility_group.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return facility_group_items.get( groupPosition ).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return facility_group.get( groupPosition );
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return facility_group_items.get( groupPosition ).get( childPosition );
    }

    @Override
    public long getGroupId(int groupPosition) {
        return (long)( groupPosition*1024 );  // To be consistent with getChildId
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return (long)( groupPosition*1024+childPosition );  // Max 1024 children per group
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View v = null;
        if( convertView != null )
            v = convertView;
        else
            v = inflater.inflate(R.layout.facility_group, parent, false);
        String gt = (String)getGroup( groupPosition );
        TextView facility_header = (TextView)v.findViewById( R.id.facility_header );
        if( gt != null )
            facility_header.setText( gt );
        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View v = null;
        if( convertView != null )
            v = convertView;
        else
            v = inflater.inflate(R.layout.facility_group_items, parent, false);
        FacilityChildInfo c = (FacilityChildInfo)getChild( groupPosition, childPosition );
        TextView sequence = v.findViewById( R.id.facility_child_sequence );
        if( sequence != null )
            sequence.setText( c.getFacility_sequence() );
        TextView facility_name = v.findViewById( R.id.facility_child_name );
        if( facility_name != null )
            facility_name.setText( c.getFacility_name() );
        CheckBox cb = v.findViewById( R.id.facility_child_check );
        cb.setChecked( c.getState() );
        return v;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void onGroupCollapsed (int groupPosition) {}
    public void onGroupExpanded(int groupPosition) {}
}
