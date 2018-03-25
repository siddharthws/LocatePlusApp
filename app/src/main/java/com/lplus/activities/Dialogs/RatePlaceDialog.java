package com.lplus.activities.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lplus.R;
import com.lplus.activities.Objects.MarkerObject;

/**
 * Created by Sai_Kameswari on 25-03-2018.
 */

public class RatePlaceDialog implements View.OnClickListener {

    private Context context;
    private Dialog ratePlaceDialog;
    private MarkerObject markerObject;
    private TextView rate_que;
    private ImageView star1, star2, star3, star4, star5;
    private Bitmap bitmap;

    public RatePlaceDialog(Context context, MarkerObject markerObject)
    {
        this.context = context;
        this.markerObject = markerObject;
        Init();
    }

    private void Init()
    {

        ratePlaceDialog = new Dialog(context, R.style.CustomDialogTheme);
        ratePlaceDialog.setContentView(R.layout.dialog_rate_place);
        ratePlaceDialog.setCancelable(true);
        ratePlaceDialog.setCanceledOnTouchOutside(true);
        ratePlaceDialog.getWindow().getAttributes().windowAnimations = R.style.SlideHorizontalAnimation;


        //fetch all ID's from View
        rate_que        = ratePlaceDialog.findViewById(R.id.rate_que);
        star1           = ratePlaceDialog.findViewById(R.id.star1);

        star2           = ratePlaceDialog.findViewById(R.id.star2);
        star3           = ratePlaceDialog.findViewById(R.id.star3);
        star4           = ratePlaceDialog.findViewById(R.id.star4);
        star5           = ratePlaceDialog.findViewById(R.id.star5);

        rate_que.setText("How do you the Overall Place?");
        //set listeners for views
        star1.setOnClickListener(this);
        star2.setOnClickListener(this);
        star3.setOnClickListener(this);
        star4.setOnClickListener(this);
        star5.setOnClickListener(this);
    }

    public void ShowDialog()
    {
        ratePlaceDialog.show();
    }

    public void HideDialog()
    {
        ratePlaceDialog.cancel();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.star1:
            {
                break;
            }

            case R.id.star2:
            {
                break;
            }

            case R.id.star3:
            {
                break;
            }

            case R.id.star4:
            {
                break;
            }

            case R.id.star5:
            {
                break;
            }
        }

    }
}
