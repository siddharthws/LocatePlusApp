package com.lplus.activities.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lplus.R;
import com.lplus.activities.Interfaces.MarkerReviewInterface;
import com.lplus.activities.Objects.MarkerObject;
import com.lplus.activities.Server.MarkerReviewServerClass;

import java.util.ArrayList;

/**
 * Created by Sai_Kameswari on 22-03-2018.
 */

public class MarkerDescriptionDialog implements View.OnClickListener, MarkerReviewInterface {

    private Context context;
    private Dialog markerdescriptionDialog;
    private MarkerObject markerObject;
    private TextView dec_place_name, dec_category, desc_address, dec_facilities, tv_review;
    private LinearLayout direction_layout, desc_layout;
    private ImageButton review_send;
    private LoadingDialog loadingDialog;

    public MarkerDescriptionDialog(Context context, MarkerObject markerObject)
    {
        this.context = context;
        this.markerObject = markerObject;
        Init();
    }

    private void Init()
    {
        markerdescriptionDialog = new Dialog(context, R.style.CustomDialogTheme);
        markerdescriptionDialog.setContentView(R.layout.dialog_place_description);
        markerdescriptionDialog.setCancelable(true);
        markerdescriptionDialog.setCanceledOnTouchOutside(true);

        //fetch all ID's from View
        dec_place_name = markerdescriptionDialog.findViewById(R.id.dec_place_name);
        dec_category = markerdescriptionDialog.findViewById(R.id.dec_category);
        desc_address = markerdescriptionDialog.findViewById(R.id.desc_address);
        dec_facilities = markerdescriptionDialog.findViewById(R.id.dec_facilities);
        tv_review = markerdescriptionDialog.findViewById(R.id.tv_review);
        review_send = markerdescriptionDialog.findViewById(R.id.review_send);

        direction_layout = markerdescriptionDialog.findViewById(R.id.direction_layout);
        desc_layout = markerdescriptionDialog.findViewById(R.id.desc_layout);

        setData();
        //set listeners for linear layouts
        direction_layout.setOnClickListener(this);
        desc_layout.setOnClickListener(this);
        review_send.setOnClickListener(this);
    }

    private void setData()
    {
        //place data to views in dialog
        dec_place_name.setText(markerObject.getMarkerName());
        dec_category.setText(markerObject.getMarkerCategory());
        desc_address.setText(markerObject.getMarkerAddress());
        String facility_item = "";
        ArrayList<String> items = markerObject.getMarkerFacilities();
        for(int i=0; i<items.size();i++)
        {
            facility_item = facility_item + items.get(i) + "\n";
        }
        dec_facilities.setText(facility_item);
    }

    public void ShowDialog()
    {
        markerdescriptionDialog.show();
    }

    public void HideDialog()
    {
        markerdescriptionDialog.cancel();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.direction_layout:
            {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+markerObject.getMarkerLatitude()+","+markerObject.getMarkerLongitude());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                context.startActivity(mapIntent);
                break;
            }

            case R.id.desc_layout:
            {
                break;
            }

            case R.id.review_send:
            {
                String review = tv_review.getText().toString();
                if (review.length() == 0)
                {
                    Toast.makeText(context, "Write Something", Toast.LENGTH_SHORT).show();
                    return;
                }
                loadingDialog = new LoadingDialog(context, "Sending Review...");
                loadingDialog.ShowDialog();
                MarkerReviewServerClass markerReviewServerClass = new MarkerReviewServerClass(context, markerObject, review);
                markerReviewServerClass.SetListener(this);
                markerReviewServerClass.execute();
                break;
            }
        }
    }

    @Override
    public void onReviewSent()
    {
        loadingDialog.HideDialog();
        Toast.makeText(context, "Review Submitted..", Toast.LENGTH_SHORT).show();
        tv_review.setText("");
    }

    @Override
    public void onReviewFailed()
    {
        loadingDialog.HideDialog();
        Toast.makeText(context, "Review Not Sent..", Toast.LENGTH_SHORT).show();
    }
}