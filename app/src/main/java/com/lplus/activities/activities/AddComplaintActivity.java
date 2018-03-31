package com.lplus.activities.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.lplus.activities.Interfaces.AddComplaintInterface;
import com.lplus.activities.Interfaces.AddPhotoInterface;
import com.lplus.activities.Interfaces.AddPlaceInterface;
import com.lplus.activities.Interfaces.GetMarkerInteface;
import com.lplus.activities.JavaFiles.CameraPhoto;
import com.lplus.activities.JavaFiles.FacilityChildInfo;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Objects.TempNewComplaintObject;
import com.lplus.activities.Objects.TempNewPhotoObject;
import com.lplus.activities.Objects.TempNewPlaceObject;
import com.lplus.activities.Objects.UnSyncObject;
import com.lplus.activities.Server.AddComplaintServerClass;
import com.lplus.activities.Server.AddPhotoFirstServerClass;
import com.lplus.activities.Server.AddPlaceServerClass;
import com.lplus.activities.Server.GetMarkersServerClass;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import es.dmoral.toasty.Toasty;
import me.relex.circleindicator.CircleIndicator;

public class AddComplaintActivity extends AppCompatActivity implements  AddComplaintInterface,
    View.OnClickListener,
    GetMarkerInteface,
    AddPhotoInterface

    {

        private CustomExpandableListAdapter listAdapter;
        private ListView simpleExpandableListView;
        File file;

        CameraPhoto cameraPhoto;
        final int CAMERA_REQUEST = 13323;
        private static ViewPager mPager;
        private static int currentPage = 0;
        private ArrayList<String> XMEN = new ArrayList<>();
        private ArrayList<String> XMENUUID = new ArrayList<>();

        private EditText etName,etDesc;
        private TextView tvAddress;
        private CardView save,cancel;
        private String address, name, desc;
        private double latitude, longitude;
        private ArrayList<String> list, cat_key;
        private LoadingDialog loadingDialog =null;
        private TinyDB tinyDB;

        ImageView edit_photo;
        ImageView addphoto;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_complaint);

        CustomToast.configToast();

        loadingDialog = new LoadingDialog(this, "Adding Complaint...");
        //Get Extras from Intent
        Intent addComplaintIntent = getIntent();
        if (null != addComplaintIntent)
        { //Null Checking
            address     = addComplaintIntent.getStringExtra(Keys.CENTER_ADDRESS);
            latitude    = addComplaintIntent.getDoubleExtra(Keys.CENTER_LATITUDE, 0.0);
            longitude   = addComplaintIntent.getDoubleExtra(Keys.CENTER_LONGITUDE, 0.0);
        }
        tinyDB = new TinyDB(AddComplaintActivity.this);
        list = new ArrayList<>();

        cat_key = new ArrayList<>();

        etName = findViewById(R.id.et_name);
        etDesc = findViewById(R.id.et_desc);

        cameraPhoto = new CameraPhoto(getApplicationContext());
        addphoto = findViewById(R.id.addImage);
        edit_photo = findViewById(R.id.edit_photo);

        tvAddress = findViewById(R.id.tv_address);
        tvAddress.setText(address);

        save = findViewById(R.id.save_add);
        cancel = findViewById(R.id.cancel_add);

        //select category
        Spinner spinner = findViewById(R.id.category_spinner);

        list = tinyDB.getListString(Keys.CATEGORY_VALUE);
        cat_key = tinyDB.getListString(Keys.CATEGORY_KEY);

        save.setOnClickListener(this);

        cancel.setOnClickListener(this);
        //Add photo
        cameraPhoto = new CameraPhoto(getApplicationContext());
        addphoto.setOnClickListener(this);
        edit_photo.setOnClickListener(this);
    }

    public void onContentChanged  () {
        super.onContentChanged();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.save_add:
            {
                Animation myAnim = AnimationUtils.loadAnimation(AddComplaintActivity.this, R.anim.bounce);
                save.startAnimation(myAnim);
                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {

                        //apply constraint checks everywhere and as necessary
                        name = etName.getText().toString();
                        desc = etDesc.getText().toString();

                        //Check constraints
                        if(name.length() == 0 )
                        {
                            Toasty.error(AddComplaintActivity.this, "Please add a name", Toast.LENGTH_SHORT,true).show();
                            return;
                        }
                        if(tinyDB.getListString(Keys.TINYDB_PHOTO_LIST).size() == 0)
                        {
                            Toasty.error(AddComplaintActivity.this, "Please add a photo", Toast.LENGTH_SHORT, true).show();
                            return;
                        }
                        loadingDialog.ShowDialog();
                        ArrayList<String> photo_uuid_array = new ArrayList<>();
                        ArrayList<String> photo_path_array = new ArrayList<>();

                        photo_uuid_array = tinyDB.getListString(Keys.TINYDB_PHOTO_UUID_LIST);
                        photo_path_array = tinyDB.getListString(Keys.TINYDB_PHOTO_LIST);
                        UnSyncObject unSyncObject = new UnSyncObject();

                        if(photo_path_array.size()>0 && photo_uuid_array.size()>0)
                        {
                            StringBuilder photo_uuids=new StringBuilder();
                            StringBuilder photo_paths=new StringBuilder();
                            StringBuilder place_fac=new StringBuilder();
                            photo_uuids.insert(0,photo_uuid_array.get(0));
                            photo_paths.insert(0,photo_path_array.get(0));
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

                            //set members of unsync object
                            unSyncObject.setPhoto_ids(photo_uuids.toString());
                            unSyncObject.setPhoto_paths(photo_paths.toString());
                            unSyncObject.setPlace_lat(latitude);
                            unSyncObject.setPlace_lng(longitude);
                            unSyncObject.setPlace_name(name);
                            unSyncObject.setPlace_addres(address);
                            unSyncObject.setPlace_facilities(place_fac.toString());
                            unSyncObject.setPlace_decription(desc);
                        }
                        else {
                            Toasty.error(AddComplaintActivity.this,"Some Data is Missing",Toast.LENGTH_SHORT,true).show();
                            return;
                        }
                        //Add Data to Database
                        AddToUnsync(unSyncObject);

                        TempNewPhotoObject tempNewPhotoObject = new TempNewPhotoObject(photo_uuid_array, photo_path_array);
                        AddPhotoFirstServerClass addPhotoFirstServerClass = new AddPhotoFirstServerClass(AddComplaintActivity.this,tempNewPhotoObject);
                        addPhotoFirstServerClass.SetListener(AddComplaintActivity.this);
                        addPhotoFirstServerClass.execute();
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

                break;
            }
            case R.id.cancel_add:
            {
                Animation myAnim = AnimationUtils.loadAnimation(AddComplaintActivity.this, R.anim.bounce);
                cancel.startAnimation(myAnim);
                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {

                        Toast.makeText(AddComplaintActivity.this, "Cancel Clicked", Toast.LENGTH_SHORT).show();
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
                Animation myAnim = AnimationUtils.loadAnimation(AddComplaintActivity.this, R.anim.bounce);
                addphoto.startAnimation(myAnim);
                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if(XMEN.size() == 5)
                        {
                            Toasty.error(AddComplaintActivity.this, "Max Photo Limit 5 Reached", Toast.LENGTH_SHORT,true).show();
                            return;
                        }

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
                Animation myAnim = AnimationUtils.loadAnimation(AddComplaintActivity.this, R.anim.bounce);
                edit_photo.startAnimation(myAnim);
                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Intent intent = new Intent(AddComplaintActivity.this,EditPhotosActivity.class);
                        startActivity(intent);
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
        mPager.setAdapter(new ImageSliderAdapter(AddComplaintActivity.this,XMEN));

        CircleIndicator indicator =  findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        if(XMEN.size() == 5)
        {
            addphoto.setImageResource(R.drawable.add_photo_red);
        }
        else {
            addphoto.setImageResource(R.drawable.add_photo_white);
           /* addphoto.setEnabled(true);*/
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

    }

    @Override
    protected void onPostResume() {
        beginSlide();
        super.onPostResume();
    }

    public void AddToUnsync(UnSyncObject unSyncObject) {
        AddUnSyncTable addUnSyncTable = new AddUnSyncTable(AddComplaintActivity.this);
        addUnSyncTable.SaveRecord(unSyncObject);
    }


    @Override
    public void onPhotoRecieve(boolean status) {

        if (status)
        {
            TempNewComplaintObject tco = new TempNewComplaintObject(name, address, tinyDB.getListString(Keys.TINYDB_PHOTO_UUID_LIST), latitude, longitude, desc);
            AddComplaintServerClass addComplaintServerClass = new AddComplaintServerClass(AddComplaintActivity.this, tco);
            addComplaintServerClass.SetListener(this);
            addComplaintServerClass.execute();
        }
    }

    @Override
    public void onComplaintAddStatus(boolean status) {
        if (status)
        {
            loadingDialog.HideDialog();
            Toast.makeText(AddComplaintActivity.this, "Complaint Added Successfully", Toast.LENGTH_SHORT).show();
            // add markers from database to the map
            AddUnSyncTable addUnSyncTable = new AddUnSyncTable(AddComplaintActivity.this);
            if(addUnSyncTable.DeleteAll())
            {
                GetMarkersServerClass getMarkersServerClass = new GetMarkersServerClass(this);
                getMarkersServerClass.SetListener(this);
                getMarkersServerClass.execute();
            }
        }
        else
        {
            loadingDialog.HideDialog();
            Toast.makeText(AddComplaintActivity.this, "Complaint not added..Try again", Toast.LENGTH_SHORT).show();
            setResult(2);
            finish();
        }
    }

    @Override
    public void onMarkerFetchStatus(boolean status) {

        setResult(2);
        finish();
    }
}
