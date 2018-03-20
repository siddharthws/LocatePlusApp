package com.lplus.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.lplus.R;
import com.lplus.activities.Adapters.CustomExpandableListAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class AddPlaceActivity extends AppCompatActivity {
    private LinkedHashMap<String, FacilityGroupInfo> subjects = new LinkedHashMap<String, FacilityGroupInfo>();
    private ArrayList<FacilityGroupInfo> deptList = new ArrayList<FacilityGroupInfo>();

    private CustomExpandableListAdapter listAdapter;
    private ExpandableListView simpleExpandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_place_add);

        // add data for displaying in expandable list view
        loadData();

        //get reference of the ExpandableListView
        simpleExpandableListView = (ExpandableListView) findViewById(R.id.simpleExpandableListView);
        // create the adapter by passing your ArrayList data
        listAdapter = new CustomExpandableListAdapter(AddPlaceActivity.this, deptList);
        // attach the adapter to the expandable list view
        simpleExpandableListView.setAdapter(listAdapter);

        //expand all the Groups
        expandAll();

        // setOnChildClickListener listener for child row click
        simpleExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //get the group header
                FacilityGroupInfo headerInfo = deptList.get(groupPosition);
                //get the child info
                FacilityChildInfo detailInfo =  headerInfo.getList().get(childPosition);
                //display it or do something with it
                Toast.makeText(getBaseContext(), " Clicked on :: " + headerInfo.getName()
                        + "/" + detailInfo.getFacility_name(), Toast.LENGTH_LONG).show();
                return false;
            }
        });
        // setOnGroupClickListener listener for group heading click
        simpleExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //get the group header
                FacilityGroupInfo headerInfo = deptList.get(groupPosition);
                //display it or do something with it
                Toast.makeText(getBaseContext(), " Header is :: " + headerInfo.getName(),
                        Toast.LENGTH_LONG).show();

                return false;
            }
        });
    }


    //method to expand all groups
    private void expandAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            simpleExpandableListView.expandGroup(i);
        }
    }

    //method to collapse all groups
    private void collapseAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            simpleExpandableListView.collapseGroup(i);
        }
    }

    //load some initial data into out list
    private void loadData(){

        addProduct("Android","ListView");
        addProduct("Android","ExpandableListView");
        addProduct("Android","GridView");
        addProduct("Android","ListView");
        addProduct("Android","ExpandableListView");
        addProduct("Android","GridView");
        addProduct("Android","ListView");
        addProduct("Android","ExpandableListView");
        addProduct("Android","GridView");
        addProduct("Android","ListView");
        addProduct("Android","ExpandableListView");
        addProduct("Android","GridView");
        addProduct("Java","PolyMorphism");
        addProduct("Java","Collections");

    }



    //here we maintain our products in various departments
    private int addProduct(String department, String product){

        int groupPosition = 0;

        //check the hash map if the group already exists
        FacilityGroupInfo headerInfo = subjects.get(department);
        //add the group if doesn't exists
        if(headerInfo == null){
            headerInfo = new FacilityGroupInfo();
            headerInfo.setName(department);
            subjects.put(department, headerInfo);
            deptList.add(headerInfo);
        }

        //get the children for the group
        ArrayList<FacilityChildInfo> productList = headerInfo.getList();
        //size of the children list
        int listSize = productList.size();
        //add to the counter
        listSize++;

        //create a new child and add that to the group
        FacilityChildInfo detailInfo = new FacilityChildInfo();
        detailInfo.setFacility_sequence(String.valueOf(listSize));
        detailInfo.setFacility_name(product);
        productList.add(detailInfo);
        headerInfo.setList(productList);

        //find the group position inside the list
        groupPosition = deptList.indexOf(headerInfo);
        return groupPosition;
    }
}
