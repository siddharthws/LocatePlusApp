package com.lplus.activities.activities;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lplus.R;
import com.lplus.activities.Extras.TinyDB;
import com.lplus.activities.Macros.Keys;

public class SmsActivity extends AppCompatActivity {

    private TinyDB tinyDB;
    private LinearLayout ll_blink;
    private ObjectAnimator anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        ll_blink = findViewById(R.id.ll_blink);
        manageBlinkEffect();
        if(tinyDB == null)
        {
            tinyDB = TinyDB.Init(this);
        }
        long contact1 = tinyDB.getLong(Keys.CONTACT_1, -1);
        long contact2 = tinyDB.getLong(Keys.CONTACT_2, -1);
        long contact3 = tinyDB.getLong(Keys.CONTACT_3, -1);

        anim.start();
        //Getting intent and PendingIntent instance
        Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
        PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);

        //Get the SmsManager instance and call the sendTextMessage method to send message
        SmsManager sms=SmsManager.getDefault();
        sms.sendTextMessage(String.valueOf(contact1), null, "SOS Message sent from User", pi,null);
        SystemClock.sleep(1000);
        sms.sendTextMessage(String.valueOf(contact2), null, "SOS Message sent from User", pi,null);
        SystemClock.sleep(1000);
        sms.sendTextMessage(String.valueOf(contact3), null, "SOS Message sent from User", pi,null);
        SystemClock.sleep(1000);
        anim.cancel();


        Toast.makeText(this,"SOS Message Sent", Toast.LENGTH_LONG).show();
        finish();
    }

    private void manageBlinkEffect() {
        anim = ObjectAnimator.ofInt(ll_blink, "backgroundColor", Color.WHITE, Color.RED, Color.WHITE);
        anim.setDuration(1500);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.setRepeatCount(ValueAnimator.INFINITE);
    }
}
