package com.lplus.activities.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lplus.R;
import com.lplus.activities.Extras.CustomToast;
import com.lplus.activities.Interfaces.RateFacillityInterface;
import com.lplus.activities.Interfaces.RatePhotosInterface;
import com.lplus.activities.Objects.MarkerObject;
import com.lplus.activities.Server.RateFacilityServerClass;
import com.lplus.activities.Server.RatePhotoServerClass;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

/**
 * Created by Sai_Kameswari on 25-03-2018.
 */

public class RatePhotoDialog implements RatePhotosInterface {

    private Context context;
    private Dialog ratePhotoDialog;
    private MarkerObject markerObject;
    private TextView photo_ques;
    private ImageView photo_view;
    private LinearLayout LL_yes, LL_no, LL_not_sure;
    private ArrayList<String> photo_rate;
    private ArrayList<String> photo_uuid;
    private Bitmap bmImg;
    private LoadingDialog loadingDialog;

    public RatePhotoDialog(Context context, MarkerObject markerObject, ArrayList<String> photo_rate, ArrayList<String> photo_uuid)
    {
        this.context = context;
        this.markerObject = markerObject;
        this.photo_rate = photo_rate;
        this.photo_uuid = photo_uuid;
        Init();
    }

    private void Init()
    {

        ratePhotoDialog = new Dialog(context, R.style.CustomDialogTheme);
        ratePhotoDialog.setContentView(R.layout.dialog_rate_photo);
        ratePhotoDialog.setCancelable(true);
        ratePhotoDialog.setCanceledOnTouchOutside(true);
        ratePhotoDialog.getWindow().getAttributes().windowAnimations = R.style.SlideHorizontalAnimation;
        CustomToast.configToast();


        //fetch all ID's from View
        photo_ques        = ratePhotoDialog.findViewById(R.id.photo_ques);
        photo_view        = ratePhotoDialog.findViewById(R.id.photo_view);

        LL_yes            = ratePhotoDialog.findViewById(R.id.LL_yes);
        LL_no             = ratePhotoDialog.findViewById(R.id.LL_no);
        LL_not_sure       = ratePhotoDialog.findViewById(R.id.LL_not_sure);


        photo_ques.setText("Is this Photo Appropriate?");
        ShowDialog(markerObject);

    }

    public void ShowDialog(MarkerObject markerObject)
    {
        /*File imgFile = new  File(“filepath”);
        if(imgFile.exists())
        {
            ImageView myImage = new ImageView(this);
            myImage.setImageURI(Uri.fromFile(imgFile));

        }*/

        for(String path : markerObject.getMarkerFacilities()) {                         //give path array here from tinyDB
            bmImg = BitmapFactory.decodeFile(path);
            photo_view.setImageBitmap(bmImg);
            ratePhotoDialog.show();
            LL_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ratePhotoDialog.cancel();
                    photo_rate.add("1");
                }
            });

            LL_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ratePhotoDialog.cancel();
                    photo_rate.add("-1");
                }
            });
            LL_not_sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ratePhotoDialog.cancel();
                    photo_rate.add("0");
                }
            });
        }
        loadingDialog = new LoadingDialog(context, "Please Wait...");
        loadingDialog.ShowDialog();
        RatePhotoServerClass ratePhotoServerClass = new RatePhotoServerClass(context, markerObject, photo_uuid, photo_rate);
        ratePhotoServerClass.SetListener(this);
        ratePhotoServerClass.execute();
    }

    public void HideDialog()
    {
        ratePhotoDialog.cancel();
    }


    @Override
    public void onPhotoSent() {
        loadingDialog.HideDialog();
        Toasty.success(context,"Photos Successfully Rated", Toast.LENGTH_SHORT,true);
    }

    @Override
    public void onPhotosFailed() {
        loadingDialog.HideDialog();
        Toasty.error(context,"Photos Rating Failed", Toast.LENGTH_SHORT,true);
    }
}
