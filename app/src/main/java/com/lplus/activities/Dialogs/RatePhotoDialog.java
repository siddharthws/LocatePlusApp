package com.lplus.activities.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lplus.R;
import com.lplus.activities.Objects.MarkerObject;

/**
 * Created by Sai_Kameswari on 25-03-2018.
 */

public class RatePhotoDialog implements View.OnClickListener {

    private Context context;
    private Dialog ratePhotoDialog;
    private MarkerObject markerObject;
    private TextView photo_ques;
    private ImageView photo_view;
    private LinearLayout LL_yes, LL_no, LL_not_sure;

    public RatePhotoDialog(Context context, MarkerObject markerObject)
    {
        this.context = context;
        this.markerObject = markerObject;
        Init();
    }

    private void Init()
    {

        ratePhotoDialog = new Dialog(context, R.style.CustomDialogTheme);
        ratePhotoDialog.setContentView(R.layout.dialog_rate_photo);
        ratePhotoDialog.setCancelable(true);
        ratePhotoDialog.setCanceledOnTouchOutside(true);
        ratePhotoDialog.getWindow().getAttributes().windowAnimations = R.style.SlideHorizontalAnimation;


        //fetch all ID's from View
        photo_ques        = ratePhotoDialog.findViewById(R.id.photo_ques);
        photo_view        = ratePhotoDialog.findViewById(R.id.photo_view);

        LL_yes            = ratePhotoDialog.findViewById(R.id.LL_yes);
        LL_no             = ratePhotoDialog.findViewById(R.id.LL_no);
        LL_not_sure       = ratePhotoDialog.findViewById(R.id.LL_not_sure);


        photo_ques.setText("Is this Photo Appropriate?");
        //set listeners for linear layouts
        LL_yes.setOnClickListener(this);
        LL_no.setOnClickListener(this);
        LL_not_sure.setOnClickListener(this);

    }

    public void ShowDialog()
    {
        ratePhotoDialog.show();
    }

    public void HideDialog()
    {
        ratePhotoDialog.cancel();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.LL_yes:
            {
                break;
            }

            case R.id.LL_no:
            {
                break;
            }

            case R.id.LL_not_sure:
            {
                break;
            }
        }
    }
}
