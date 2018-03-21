package com.lplus.activities.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.android.photoutil.CameraPhoto;
import com.lplus.R;
import com.lplus.activities.Adapters.CustomExpandableListAdapter;
import com.lplus.activities.Adapters.ImageSliderAdapter;
import com.lplus.activities.Dialogs.LoadingDialog;
import com.lplus.activities.Extras.TinyDB;
import com.lplus.activities.Interfaces.AddPlaceInterface;
import com.lplus.activities.JavaFiles.FacilityChildInfo;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Objects.TempNewPlaceObject;
import com.lplus.activities.Server.AddPlaceServerClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class AddPlaceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AddPlaceInterface, View.OnClickListener {

    private static final String LOG_TAG = "UCHIHA";
    private CustomExpandableListAdapter listAdapter;
    private ExpandableListView simpleExpandableListView;

    CameraPhoto cameraPhoto;
    final int CAMERA_REQUEST = 13323;
    final int GALLERY_REQUEST = 22131;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private List<String> XMEN = new ArrayList<>();
    private List<String> XMENArray = new ArrayList<>();

    private EditText place_name,place_review;
    private TextView address;
    private CardView save,cancel;
    private String address_result, place_name_string;
    private double latitude, longitude;
    private static String category, facilities;
    private ArrayList<String> list, fac_list, selected_fac, cat_key, fac_key;
    private SharedPreferences app_sharePref;
    private LoadingDialog loadingDialog =null;
    private TinyDB tinyDB;

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
       tinyDB = new TinyDB(AddPlaceActivity.this);
        list = new ArrayList<>();
        fac_list = new ArrayList<>();
        selected_fac = new ArrayList<>();

        cat_key = new ArrayList<>();
        fac_key = new ArrayList<>();

        place_name = findViewById(R.id.add_place_name);
        place_review = findViewById(R.id.addplace_review);

        cameraPhoto = new CameraPhoto(getApplicationContext());
        ImageView addphoto = findViewById(R.id.addImage);

        address = findViewById(R.id.address_add);
        address.setText(address_result);

        save = findViewById(R.id.save_add);
        cancel = findViewById(R.id.cancel_add);

        //select category
        Spinner spinner = findViewById(R.id.category_spinner);

        list = tinyDB.getListString(Keys.CATEGORY_VALUE);
        fac_list = tinyDB.getListString(Keys.FACILITIES_VALUE);
        cat_key = tinyDB.getListString(Keys.CATEGORY_KEY);
        fac_key = tinyDB.getListString(Keys.FACILITIES_KEY);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(this);

        // add data for displaying in expandable list view
        ArrayList<String> groupNames = new ArrayList<>();
        groupNames.add( "Select Facility" );
        ArrayList<ArrayList<FacilityChildInfo>> facility_arr = new ArrayList<>();
        final ArrayList<FacilityChildInfo> facility = new ArrayList<>();

        for(int i=0; i<fac_list.size();i++)
        {
            System.out.println("Fac key i list: "+fac_key.get(i)+"having: "+fac_list.get(i));
            facility.add( new FacilityChildInfo(fac_key.get(i), i+1, fac_list.get(i)));
        }
        facility_arr.add( facility );
        simpleExpandableListView = findViewById(R.id.simpleExpandableListView);

        listAdapter = new CustomExpandableListAdapter(this,groupNames,facility_arr);
        simpleExpandableListView.setAdapter( listAdapter );
        simpleExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Log.d( LOG_TAG, "onChildClick: "+childPosition );
                CheckBox cb = v.findViewById( R.id.facility_child_check );
                if(selected_fac.contains(facility.get(childPosition).getFac_id()))
                {
                    selected_fac.remove(facility.get(childPosition).getFac_id());
                }
                else{
                    selected_fac.add(facility.get(childPosition).getFac_id());
                }
                if( cb != null )
                    if (cb.isChecked())
                    {
                        facility.get(childPosition).setState(false);
                        cb.setChecked(false);
                    }
                    else {
                        facility.get(childPosition).setState(true);
                        cb.setChecked(true);
                    }
                return false;
            }
        });

        save.setOnClickListener(this);

        cancel.setOnClickListener(this);
        //Add photo
        cameraPhoto = new CameraPhoto(getApplicationContext());
        addphoto.setOnClickListener(this);
    }

    public void onContentChanged  () {
        super.onContentChanged();
        Log.d( LOG_TAG, "onContentChanged" );
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
                TempNewPlaceObject tempNewPlaceObject = new TempNewPlaceObject(place_name_string, address_result, category, selected_fac, latitude, longitude);
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
            case R.id.addImage:
            {
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        category = cat_key.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                String PhotoPath = cameraPhoto.getPhotoPath();
                try {
                    /*showPhoto = findViewById(R.id.ivImage);
                    Bitmap bitmap = ImageLoader.init().from(PhotoPath).requestSize(512, 512).getBitmap();
                    showPhoto.setImageBitmap(bitmap);*/

                    //  String encoded = ImageBase64.encode(bitmap);
                    //  Bitmap bitmap = ImageBase64.decode(encodedString);
                } catch (Exception e) {

                    Toast.makeText(getApplicationContext(), "Something Wrong while loading photos" + e, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void beginSlide() {
        for(int i=0;i<XMEN.size();i++)
            XMENArray.add(XMEN.get(i));

        mPager =  findViewById(R.id.pager);
        mPager.setAdapter(new ImageSliderAdapter(AddPlaceActivity.this,XMENArray));

        CircleIndicator indicator =  findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == XMEN.size()) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 1500, 1500);
    }
}





