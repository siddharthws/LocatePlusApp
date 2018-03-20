package com.lplus.activities.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lplus.R;
import com.lplus.activities.Adapters.CustomExpandableListAdapter;
import com.lplus.activities.FacilityChildInfo;
import com.lplus.activities.FacilityGroupInfo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class AddPlaceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private LinkedHashMap<String, FacilityGroupInfo> subjects = new LinkedHashMap<String, FacilityGroupInfo>();
    private ArrayList<FacilityGroupInfo> deptList = new ArrayList<FacilityGroupInfo>();

    private CustomExpandableListAdapter listAdapter;
    private ExpandableListView simpleExpandableListView;

    private EditText place_name;
    private TextView address;
    private CardView save,cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        place_name = findViewById(R.id.add_place_name);
        place_name.setText("Rest Rooms");

        address = findViewById(R.id.address_add);
        address.setText("is the an appropriate facility ?");

        save = findViewById(R.id.save_add);
        cancel = findViewById(R.id.cancel_add);
        //TextView yes = dialog.findViewById(R.id.ok_text);

        //select category
        Spinner spinner = findViewById(R.id.category_spinner);
        List<String> list = new ArrayList<>();
        list.add("category one");              //delete these
        list.add("category two");
        list.add("category three");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(this);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddPlaceActivity.this, "Save Clicked", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddPlaceActivity.this, "Cancel Clicked", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        Toast.makeText(this,"Selected Category : " + parent.getItemAtPosition(position).toString(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}

  /*
        //Add photo
        cameraPhoto = new CameraPhoto(getApplicationContext());
        addphoto = dialog.findViewById(R.id.addImage);
        addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PackageManager pm = getPackageManager();

                    if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                        startActivityForResult(cameraPhoto.takePhotoIntent(),CAMERA_REQUEST);
                        cameraPhoto.addToGallery();
                    }
                }catch(Exception e) {
                    Log.v("Camera",e.getMessage());
                    Toast.makeText(getApplicationContext(),"Something Wrong while taking photos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }*/

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if(requestCode == CAMERA_REQUEST) {
                String PhotoPath = cameraPhoto.getPhotoPath();
                try {
                    showPhoto = dialog.findViewById(R.id.ivImage);
                    Bitmap bitmap = ImageLoader.init().from(PhotoPath).requestSize(512,512).getBitmap();
                    showPhoto.setImageBitmap(bitmap);

                    //  String encoded = ImageBase64.encode(bitmap);
                    //  Bitmap bitmap = ImageBase64.decode(encodedString);
                }catch(Exception e) {

                    Toast.makeText(getApplicationContext(),"Something Wrong while loading photos"+ e, Toast.LENGTH_SHORT).show();
                }
            }
        }*/
