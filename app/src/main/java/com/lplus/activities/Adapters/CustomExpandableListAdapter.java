package com.lplus.activities.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.lplus.R;
import com.lplus.activities.JavaFiles.FacilityChildInfo;
import com.lplus.activities.JavaFiles.FacilityGroupInfo;

import java.util.ArrayList;

/**
 * Created by CHANDEL on 3/20/2018.
 */

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<FacilityGroupInfo> deptList;

    public CustomExpandableListAdapter(Context context, ArrayList<FacilityGroupInfo> deptList) {
        this.context = context;
        this.deptList = deptList;
    }


    @Override
    public int getGroupCount() {
        return deptList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<FacilityChildInfo> productList = deptList.get(groupPosition).getList();
        return productList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return deptList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<FacilityChildInfo> productList = deptList.get(groupPosition).getList();
        return productList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
        FacilityGroupInfo headerInfo = (FacilityGroupInfo) getGroup(groupPosition);
        if (view == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.facility_group, null);
        }

        TextView heading = (TextView) view.findViewById(R.id.facility_header);
        heading.setText(headerInfo.getName().trim());

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
        FacilityChildInfo detailInfo = (FacilityChildInfo) getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.facility_group_items, null);
        }

        TextView sequence = (TextView) view.findViewById(R.id.facility_sequence);
        sequence.setText(detailInfo.getFacility_sequence().trim() + ". ");
        TextView childItem = (TextView) view.findViewById(R.id.facility_childItem);
        childItem.setText(detailInfo.getFacility_name().trim());

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
