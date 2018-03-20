package com.lplus.activities.activities;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.lplus.activities.Dialogs.LoadingDialog;
import com.lplus.activities.Interfaces.AddPlaceInterface;
import com.lplus.activities.JavaFiles.FacilityChildInfo;
import com.lplus.activities.JavaFiles.FacilityGroupInfo;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Models.TempNewPlaceObject;
import com.lplus.activities.Server.AddPlaceServerClass;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class AddPlaceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AddPlaceInterface, View.OnClickListener {

    private LinkedHashMap<String, FacilityGroupInfo> subjects = new LinkedHashMap<String, FacilityGroupInfo>();
    private ArrayList<FacilityGroupInfo> deptList = new ArrayList<FacilityGroupInfo>();

    private CustomExpandableListAdapter listAdapter;
    private ExpandableListView simpleExpandableListView;


    private EditText place_name;
    private TextView address;
    private CardView save,cancel;
    private String address_result, place_name_string;
    private double latitude, longitude;
    private static String category, facilities;
    private List<String> list, fac_list, cat_key, fac_key;
    private SharedPreferences app_sharePref;
    private LoadingDialog loadingDialog =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        app_sharePref = getSharedPreferences(Keys.SHARED_PREF_NAME, MODE_PRIVATE);
        loadingDialog = new LoadingDialog(this, "Adding Place...");
        //Get Extras from Intent
        Intent addPlaceIntent = getIntent();
        if (null != addPlaceIntent)
        { //Null Checking
            address_result= addPlaceIntent.getStringExtra(Keys.CENTER_ADDRESS);
            latitude = addPlaceIntent.getDoubleExtra(Keys.CENTER_LATITUDE, 0.0);
            longitude = addPlaceIntent.getDoubleExtra(Keys.CENTER_LONGITUDE, 0.0);
        }

        list = new ArrayList<>();
        fac_list = new ArrayList<>();
        cat_key = new ArrayList<>();
        fac_key = new ArrayList<>();

        place_name = findViewById(R.id.add_place_name);

        address = findViewById(R.id.address_add);
        address.setText(address_result);

        save = findViewById(R.id.save_add);
        cancel = findViewById(R.id.cancel_add);

        //select category
        Spinner spinner = findViewById(R.id.category_spinner);

        Set<String> categoriesSet = new HashSet<>();
        Set<String> facilitiesSet = new HashSet<>();
        Set<String> faciltiesKeys = new HashSet<>();
        Set<String> categoryKeys = new HashSet<>();

        categoriesSet = app_sharePref.getStringSet(Keys.CATEGORY_VALUE, categoriesSet);
        facilitiesSet = app_sharePref.getStringSet(Keys.FACILITIES_VALUE, facilitiesSet);
        faciltiesKeys = app_sharePref.getStringSet(Keys.FACILITIES_KEY, faciltiesKeys);
        categoryKeys = app_sharePref.getStringSet(Keys.CATEGORY_KEY, categoryKeys);

        list.addAll(categoriesSet);
        fac_list.addAll(facilitiesSet);
        cat_key.addAll(categoryKeys);
        fac_key.addAll(faciltiesKeys);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(this);

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
                facilities = fac_key.get(childPosition);
                return false;
            }
        });
        // setOnGroupClickListener listener for group heading click
        simpleExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //get the group header
                FacilityGroupInfo headerInfo = deptList.get(groupPosition);
                return false;
            }
        });

        save.setOnClickListener(this);

        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.save_add:
            {
                loadingDialog.ShowDialog();
                //apply constraint checks everywhere and as necessary
                place_name_string = place_name.getText().toString();
                TempNewPlaceObject tempNewPlaceObject = new TempNewPlaceObject(place_name_string, address_result, category, facilities, latitude, longitude);
                AddPlaceServerClass addPlaceServerClass = new AddPlaceServerClass(AddPlaceActivity.this, tempNewPlaceObject);
                addPlaceServerClass.SetListener(this);
                addPlaceServerClass.execute();
                break;
            }
            case R.id.cancel_add:
            {
                Toast.makeText(AddPlaceActivity.this, "Cancel Clicked", Toast.LENGTH_SHORT).show();
                finish();
                break;
            }
        }

    }

    @Override
    public void onPlaceAddSucces()
    {
        loadingDialog.HideDialog();
        Toast.makeText(AddPlaceActivity.this, "Place Added Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onPlaceAddFailed()
    {
        loadingDialog.HideDialog();
        Toast.makeText(AddPlaceActivity.this, "Place not added..Try again", Toast.LENGTH_SHORT).show();
        finish();
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

        for(String item : fac_list)
        {
            addProduct(Keys.DEPARTMENT, item);
        }
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
        category = cat_key.get(position);
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
