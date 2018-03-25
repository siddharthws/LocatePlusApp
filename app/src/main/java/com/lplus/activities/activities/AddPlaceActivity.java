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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lplus.R;
import com.lplus.activities.Adapters.CustomExpandableListAdapter;
import com.lplus.activities.Adapters.ImageSliderAdapter;
import com.lplus.activities.DBHelper.AddUnSyncTable;
import com.lplus.activities.Dialogs.LoadingDialog;
import com.lplus.activities.Extras.CustomToast;
import com.lplus.activities.Extras.TinyDB;
import com.lplus.activities.Interfaces.AddPhotoInterface;
import com.lplus.activities.Interfaces.AddPlaceInterface;
import com.lplus.activities.Interfaces.GetMarkerInteface;
import com.lplus.activities.JavaFiles.CameraPhoto;
import com.lplus.activities.JavaFiles.FacilityChildInfo;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Objects.TempNewPhotoObject;
import com.lplus.activities.Objects.TempNewPlaceObject;
import com.lplus.activities.Objects.UnSyncObject;
import com.lplus.activities.Server.AddPhotoFirstServerClass;
import com.lplus.activities.Server.AddPlaceServerClass;
import com.lplus.activities.Server.GetMarkersServerClass;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import es.dmoral.toasty.Toasty;
import me.relex.circleindicator.CircleIndicator;

public class AddPlaceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AddPlaceInterface, View.OnClickListener, GetMarkerInteface, AddPhotoInterface {

    private static final String LOG_TAG = "UCHIHA";
    private CustomExpandableListAdapter listAdapter;
    private ListView simpleExpandableListView;
    File file;

    CameraPhoto cameraPhoto;
    final int CAMERA_REQUEST = 13323;
    final int GALLERY_REQUEST = 22131;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private ArrayList<String> XMEN = new ArrayList<>();
    private ArrayList<String> XMENUUID = new ArrayList<>();

    private EditText place_name,place_description;
    private LinearLayout selected_fac_layout;
    private TextView address, selected_fac_value;
    private CardView save,cancel;
    private String address_result, place_name_string, place_description_string;
    private double latitude, longitude;
    private static String category, facilities;
    private ArrayList<String> list, fac_list, selected_fac, cat_key, fac_key, fac_value;
    private SharedPreferences app_sharePref;
    private LoadingDialog loadingDialog =null;
    private TinyDB tinyDB;
    CardView facilityListLayout;

    ImageView edit_photo;
    ImageView addphoto;
    ImageView facility_drop_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        CustomToast.configToast();

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
        place_description = findViewById(R.id.addplace_description);

        cameraPhoto = new CameraPhoto(getApplicationContext());
        addphoto = findViewById(R.id.addImage);
        edit_photo = findViewById(R.id.edit_photo);

        address = findViewById(R.id.address_add);
        address.setText(address_result);

        selected_fac_layout = findViewById(R.id.selected_fac_layout);
        selected_fac_value = findViewById(R.id.selected_fac);

        facilityListLayout = findViewById(R.id.fac_list_on_off);
        facility_drop_image = findViewById(R.id.fac_list_image);
        facility_drop_image.setTag("OFF");
        save = findViewById(R.id.save_add);
        cancel = findViewById(R.id.cancel_add);

        //select category
        Spinner spinner = findViewById(R.id.category_spinner);

        list = tinyDB.getListString(Keys.CATEGORY_VALUE);
        fac_list = tinyDB.getListString(Keys.FACILITIES_VALUE);
        cat_key = tinyDB.getListString(Keys.CATEGORY_KEY);
        fac_key = tinyDB.getListString(Keys.FACILITIES_KEY);
        fac_value = new ArrayList<>();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(this);

        // add data for displaying in expandable list view
        final ArrayList<FacilityChildInfo> facility = new ArrayList<>();
        for(int i=0; i<fac_list.size();i++)
        {
            System.out.println("Fac key i list: "+fac_key.get(i)+"having: "+fac_list.get(i));
            facility.add( new FacilityChildInfo(fac_key.get(i), i+1, fac_list.get(i)));
        }
        simpleExpandableListView = findViewById(R.id.simpleExpandableListView);

        listAdapter = new CustomExpandableListAdapter(this,facility);
        simpleExpandableListView.setAdapter( listAdapter );
        simpleExpandableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d( LOG_TAG, "onChildClick: "+position );
                CheckBox cb = view.findViewById( R.id.facility_child_check );
                if(selected_fac.contains(facility.get(position).getFac_id()))
                {
                    selected_fac.remove(facility.get(position).getFac_id());
                    fac_value.remove(facility.get(position).getFacility_name());
                }
                else{
                    selected_fac.add(facility.get(position).getFac_id());
                    fac_value.add(facility.get(position).getFacility_name());
                }
                if(fac_value.size() == 0) {
                    selected_fac_value.setText(" ");
                    selected_fac_layout.setVisibility(View.GONE);
                } else {
                    selected_fac_layout.setVisibility(View.VISIBLE);
                    selected_fac_value.setText(" ");
                    for(int i = 0;i<fac_value.size()-1;i++ ) {
                        selected_fac_value.append(fac_value.get(i));
                        selected_fac_value.append(", ");
                    }
                    selected_fac_value.append(fac_value.get(fac_value.size()-1));
                }
                if( cb != null )
                    if (cb.isChecked())
                    {
                        facility.get(position).setState(false);
                        cb.setChecked(false);
                    }
                    else {
                        facility.get(position).setState(true);
                        cb.setChecked(true);
                    }
            }
        });
        facilityListLayout.setOnClickListener(this);

        save.setOnClickListener(this);

        cancel.setOnClickListener(this);
        //Add photo
        cameraPhoto = new CameraPhoto(getApplicationContext());
        addphoto.setOnClickListener(this);
        edit_photo.setOnClickListener(this);
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
                Animation myAnim = AnimationUtils.loadAnimation(AddPlaceActivity.this, R.anim.bounce);
                save.startAnimation(myAnim);
                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {

                        //apply constraint checks everywhere and as necessary
                        place_name_string = place_name.getText().toString();
                        place_description_string = place_description.getText().toString();

                        //Check constraints
                        if(place_name_string.length() == 0 )
                        {
                            Toasty.error(AddPlaceActivity.this, "Please add a name", Toast.LENGTH_SHORT,true).show();
                            return;
                        }
                        if(tinyDB.getListString(Keys.TINYDB_PHOTO_LIST).size() == 0)
                        {
                            Toasty.error(AddPlaceActivity.this, "Please add a photo", Toast.LENGTH_SHORT, true).show();
                            return;
                        }
                        loadingDialog.ShowDialog();
                        ArrayList<String> photo_uuid_array = new ArrayList<>();
                        ArrayList<String> photo_path_array = new ArrayList<>();

                        photo_uuid_array = tinyDB.getListString(Keys.TINYDB_PHOTO_UUID_LIST);
                        photo_path_array = tinyDB.getListString(Keys.TINYDB_PHOTO_LIST);
                        UnSyncObject unSyncObject = new UnSyncObject();

                        if(photo_path_array.size()>0 && photo_uuid_array.size()>0 && selected_fac.size()>0)
                        {
                            StringBuilder photo_uuids=new StringBuilder();
                            StringBuilder photo_paths=new StringBuilder();
                            StringBuilder place_fac=new StringBuilder();
                            photo_uuids.insert(0,photo_uuid_array.get(0));
                            photo_paths.insert(0,photo_path_array.get(0));
                            place_fac.insert(0,selected_fac.get(0));
                            int j=1;
                            for (int i = 1; i < photo_uuid_array.size(); i++)
                            {
                                j=i+1;
                                photo_uuids.insert(j, "_");
                                photo_uuids.insert(i, photo_uuid_array.get(i));
                            }
                            j=1;
                            for (int i = 1; i < photo_path_array.size(); i++)
                            {
                                j=i+1;
                                photo_paths.insert(j, "_");
                                photo_paths.insert(i, photo_path_array.get(i));
                            }
                            j=1;
                            for (int i = 1; i < selected_fac.size(); i++)
                            {
                                j=i+1;
                                place_fac.insert(j, "_");
                                place_fac.insert(i, selected_fac.get(i));
                            }

                            //set members of unsync object
                            unSyncObject.setPhoto_ids(photo_uuids.toString());
                            unSyncObject.setPhoto_paths(photo_paths.toString());
                            unSyncObject.setPlace_lat(latitude);
                            unSyncObject.setPlace_lng(longitude);
                            unSyncObject.setPlace_name(place_name_string);
                            unSyncObject.setPlace_addres(address_result);
                            unSyncObject.setPlace_category(category);
                            unSyncObject.setPlace_facilities(place_fac.toString());
                            unSyncObject.setPlace_decription(place_description_string);
                        }
                        //Add Data to Database
                        AddToUnsync(unSyncObject);

                        TempNewPhotoObject tempNewPhotoObject = new TempNewPhotoObject(photo_uuid_array, photo_path_array);
                        AddPhotoFirstServerClass addPhotoFirstServerClass = new AddPhotoFirstServerClass(AddPlaceActivity.this,tempNewPhotoObject);
                        addPhotoFirstServerClass.SetListener(AddPlaceActivity.this);
                        addPhotoFirstServerClass.execute();

                        TempNewPlaceObject tempNewPlaceObject = new TempNewPlaceObject(place_name_string, address_result, category, selected_fac,tinyDB.getListString(Keys.TINYDB_PHOTO_UUID_LIST), latitude, longitude, place_description_string);
                        AddPlaceServerClass addPlaceServerClass = new AddPlaceServerClass(AddPlaceActivity.this, tempNewPlaceObject);
                        addPlaceServerClass.SetListener(AddPlaceActivity.this);
                        addPlaceServerClass.execute();
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

                break;
            }
            case R.id.cancel_add:
            {
                Animation myAnim = AnimationUtils.loadAnimation(AddPlaceActivity.this, R.anim.bounce);
                cancel.startAnimation(myAnim);
                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {

                        Toast.makeText(AddPlaceActivity.this, "Cancel Clicked", Toast.LENGTH_SHORT).show();
                        for(int i = 0;i<tinyDB.getListString(Keys.TINYDB_PHOTO_LIST).size();i++)
                        {
                            file = new File(tinyDB.getListString(Keys.TINYDB_PHOTO_LIST).get(i));
                            boolean isSuccess = file.delete();
                        }
                        tinyDB.putListString(Keys.TINYDB_PHOTO_LIST,new ArrayList<String>());
                        tinyDB.putListString(Keys.TINYDB_PHOTO_UUID_LIST,new ArrayList<String>());
                        finish();
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                break;
            }
            case R.id.addImage:
            {
                Animation myAnim = AnimationUtils.loadAnimation(AddPlaceActivity.this, R.anim.bounce);
                addphoto.startAnimation(myAnim);
                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {

                        try {
                            PackageManager pm = getPackageManager();

                            if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                                startActivityForResult(cameraPhoto.takePhotoIntent(),CAMERA_REQUEST);
                                //cameraPhoto.addToGallery();
                            }
                        }catch(Exception e) {
                            Log.v("Camera",e.getMessage());
                            Toast.makeText(getApplicationContext(),"Something Wrong while taking photos", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                break;
            }
            case R.id.edit_photo:
            {
                Animation myAnim = AnimationUtils.loadAnimation(AddPlaceActivity.this, R.anim.bounce);
                edit_photo.startAnimation(myAnim);
                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Intent intent = new Intent(AddPlaceActivity.this,EditPhotosActivity.class);
                        startActivity(intent);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                break;
            }
            case R.id.fac_list_on_off:
            {
                Animation myAnim = AnimationUtils.loadAnimation(AddPlaceActivity.this, R.anim.bounce);
                facilityListLayout.startAnimation(myAnim);
                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {

                        if(facility_drop_image.getTag() == "OFF") {
                            facility_drop_image.setImageResource(R.drawable.icons8_drop_up_48);
                            simpleExpandableListView.setVisibility(View.VISIBLE);
                            facility_drop_image.setTag("ON");
                        } else {
                            facility_drop_image.setImageResource(R.drawable.icons8_drop_down_48);
                            simpleExpandableListView.setVisibility(View.GONE);
                            facility_drop_image.setTag("OFF");
                        }
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                break;
            }
        }
    }

    @Override
    public void onPlaceAddSucces()
    {
        loadingDialog.HideDialog();
        Toast.makeText(AddPlaceActivity.this, "Place Added Successfully", Toast.LENGTH_SHORT).show();
        // add markers from database to the map
        AddUnSyncTable addUnSyncTable = new AddUnSyncTable(AddPlaceActivity.this);
        if(addUnSyncTable.DeleteAll())
        {
            GetMarkersServerClass getMarkersServerClass = new GetMarkersServerClass(this);
            getMarkersServerClass.SetListener(this);
            getMarkersServerClass.execute();
        } else {

        }

    }

    @Override
    public void onPlaceAddFailed()
    {
        loadingDialog.HideDialog();
        Toast.makeText(AddPlaceActivity.this, "Place not added..Try again", Toast.LENGTH_SHORT).show();
        setResult(2);
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
                XMEN.add(PhotoPath);
                XMENUUID.add(UUID.randomUUID().toString());
                tinyDB.putListString(Keys.TINYDB_PHOTO_LIST,XMEN);
                tinyDB.putListString(Keys.TINYDB_PHOTO_UUID_LIST,XMENUUID);
                Toast.makeText(getApplicationContext(), "Path = " +PhotoPath, Toast.LENGTH_SHORT).show();
                beginSlide();
            }
        }
    }

    public void beginSlide() {
        mPager =  findViewById(R.id.pager);
        XMEN = tinyDB.getListString(Keys.TINYDB_PHOTO_LIST);
        /*if(XMEN == null || XMEN.size() == 0)
            return;*/
        mPager.setAdapter(new ImageSliderAdapter(AddPlaceActivity.this,XMEN));

        CircleIndicator indicator =  findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        if(XMEN.size() == 5)
        {
            addphoto.setImageResource(R.drawable.add_photo_red);
            addphoto.setEnabled(false);
            Toasty.error(AddPlaceActivity.this, "Max Photo Limit 5", Toast.LENGTH_SHORT,true).show();
        }
        else {
            addphoto.setImageResource(R.drawable.add_photo_white);
            addphoto.setEnabled(true);
        }
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
        }, 1500, 5000);
    }

    @Override
    protected void onPostResume() {
        beginSlide();
        super.onPostResume();
    }

    @Override
    public void onMarkerFetched()
    {
        setResult(2);
        finish();
    }

    @Override
    public void onMarkerFailed()
    {
            setResult(2);
            finish();
    }

    public void AddToUnsync(UnSyncObject unSyncObject) {
        AddUnSyncTable addUnSyncTable = new AddUnSyncTable(AddPlaceActivity.this);
        addUnSyncTable.SaveRecord(unSyncObject);
    }

    @Override
    public void onPhotoAddSucces() {
        TempNewPlaceObject tempNewPlaceObject = new TempNewPlaceObject(place_name_string, address_result, category, selected_fac,tinyDB.getListString(Keys.TINYDB_PHOTO_UUID_LIST), latitude, longitude, place_description_string);
        AddPlaceServerClass addPlaceServerClass = new AddPlaceServerClass(AddPlaceActivity.this, tempNewPlaceObject);
        addPlaceServerClass.SetListener(this);
        addPlaceServerClass.execute();
    }

    @Override
    public void onPhotoAddFailed() {

    }
}
