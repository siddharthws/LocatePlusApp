package com.lplus.activities.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lplus.R;
import com.lplus.activities.Extras.CustomToast;
import com.lplus.activities.Interfaces.FacilityDialogClickInterface;
import com.lplus.activities.Interfaces.PhotosDialogClickInterface;
import com.lplus.activities.Interfaces.RateFacillityInterface;
import com.lplus.activities.Interfaces.RatePhotosInterface;
import com.lplus.activities.Objects.MarkerObject;
import com.lplus.activities.Server.RateFacilityServerClass;
import com.lplus.activities.Server.RatePhotoServerClass;

import java.io.File;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

/**
 * Created by Sai_Kameswari on 25-03-2018.
 */

public class RatePhotoDialog {

    private Context context;
    private Dialog ratePhotoDialog;
    private MarkerObject markerObject;
    private TextView photo_ques;
    private ImageView photo_view;
    private LinearLayout LL_yes, LL_no, LL_not_sure;
    private ArrayList<String> photo_path;
    private ArrayList<String> photo_uuid;
    private Bitmap bmImg;
    private int index;
    private LoadingDialog loadingDialog;

    private PhotosDialogClickInterface listener;
    public void SetListener(PhotosDialogClickInterface listener)
    {
        this.listener = listener;
    }

    public RatePhotoDialog(Context context, MarkerObject markerObject, ArrayList<String> photo_path, ArrayList<String> photo_uuid, int index)
    {
        this.context = context;
        this.markerObject = markerObject;
        this.photo_path = photo_path;
        this.photo_uuid = photo_uuid;
        this.index = index;
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
    }

    public void ShowDialog()
    {
        File imgFile = new  File(photo_path.get(index));
        if(imgFile.exists())
        {
            photo_view.setImageURI(Uri.fromFile(imgFile));

        }
        else {
            Toasty.error(context,"Photos Doesn't Exist", Toast.LENGTH_SHORT,true).show();
        }
        ratePhotoDialog.show();
        LL_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideDialog();
                listener.onPhotosDialogClick("1");
            }
        });
        LL_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideDialog();
                listener.onPhotosDialogClick("-1");
            }
        });
        LL_not_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideDialog();
                listener.onPhotosDialogClick("0");
            }
        });

        /**/
    }

    public void HideDialog()
    {
        ratePhotoDialog.cancel();
    }

}
