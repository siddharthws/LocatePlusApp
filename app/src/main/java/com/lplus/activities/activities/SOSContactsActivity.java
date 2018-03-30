package com.lplus.activities.activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lplus.R;
import com.lplus.activities.Extras.TinyDB;
import com.lplus.activities.Macros.Keys;

public class SOSContactsActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_1, et_2, et_3;
    private Button bt;
    private TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soscontacts);

        //set all view
        et_1 = findViewById(R.id.et_1);
        et_2 = findViewById(R.id.et_2);
        et_3 = findViewById(R.id.et_3);
        bt = findViewById(R.id.bt);

        Toolbar mToolbar =  findViewById(R.id.toolbar);
        mToolbar.setTitle("Emergency Contacts");
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(3);
                finish();
            }
        });

        bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.bt:
            {
                String contact1 = et_1.getText().toString();
                String contact2 = et_2.getText().toString();
                String contact3 = et_3.getText().toString();

                if(contact1.length()==0 || contact1.length()<10)
                {
                    Toast.makeText(this, "Please add a valid number", Toast.LENGTH_LONG).show();
                    break;
                }
                if(contact2.length()==0 || contact2.length()<10)
                {
                    Toast.makeText(this, "Please add a valid number", Toast.LENGTH_LONG).show();
                    break;
                }
                if(contact3.length()==0 || contact3.length()<10)
                {
                    Toast.makeText(this, "Please add a valid number", Toast.LENGTH_LONG).show();
                    break;
                }

                tinyDB = TinyDB.Init(this);
                tinyDB.putLong(Keys.CONTACT_1, Long.parseLong(contact1));
                tinyDB.putLong(Keys.CONTACT_2, Long.parseLong(contact2));
                tinyDB.putLong(Keys.CONTACT_3, Long.parseLong(contact3));

                //success
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setMessage("SOS Contacts saved successfully");

                alertDialogBuilder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {finish();}
                        });

                //Show Dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
            }
        }

    }
}
