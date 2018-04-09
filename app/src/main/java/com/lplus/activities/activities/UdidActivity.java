package com.lplus.activities.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lplus.R;
import com.lplus.activities.Dialogs.LoadingDialog;
import com.lplus.activities.Extras.TinyDB;
import com.lplus.activities.Interfaces.RegisterUdidInterface;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Server.UdidRegisterServerClass;

import es.dmoral.toasty.Toasty;

public class UdidActivity extends AppCompatActivity implements RegisterUdidInterface {

    LoadingDialog loadingDialog;
    EditText udid = null;
    EditText name = null;
    CardView register;
    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udid);

        udid = findViewById(R.id.udid);
        name = findViewById(R.id.name);
        register = findViewById(R.id.register);
        tinyDB = new TinyDB(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                loadingDialog = new LoadingDialog(UdidActivity.this, "Registering");
                loadingDialog.ShowDialog();
                UdidRegisterServerClass udidRegisterServerClass = new UdidRegisterServerClass(UdidActivity.this, udid.getText().toString(), name.getText().toString());
                udidRegisterServerClass.SetListener(UdidActivity.this);
                udidRegisterServerClass.execute();
            }
        });

    }


    @Override
    public void onRegisterSuccess(boolean status) {
        if (status == true) {
            loadingDialog.HideDialog();
            Toasty.success(UdidActivity.this, "Registration Successful", Toast.LENGTH_SHORT,true).show();
            tinyDB.putBoolean(Keys.TINYDB_UDID, true);
            Intent intent = new Intent(UdidActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            loadingDialog.HideDialog();
            tinyDB.putBoolean(Keys.TINYDB_UDID, false);
            Toasty.error(UdidActivity.this, "UDID incorrect or not Found", Toast.LENGTH_SHORT,true).show();
            Intent intent = new Intent(UdidActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
