package com.lplus.activities.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lplus.R;

import java.util.ArrayList;
import java.util.List;

public class AddPlaceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

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
