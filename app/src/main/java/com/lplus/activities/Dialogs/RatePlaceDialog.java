package com.lplus.activities.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lplus.R;
import com.lplus.activities.Extras.CustomToast;
import com.lplus.activities.Extras.TinyDB;
import com.lplus.activities.Interfaces.RatePlaceInterface;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Objects.MarkerObject;
import com.lplus.activities.Server.RatePlaceServerClass;

import es.dmoral.toasty.Toasty;

/**
 * Created by Sai_Kameswari on 25-03-2018.
 */

public class RatePlaceDialog implements View.OnClickListener, RatePlaceInterface {

    private Context context;
    private Dialog ratePlaceDialog;
    private MarkerObject markerObject;
    private TextView rate_que, rate_update;
    private ImageView star1, star2, star3, star4, star5;
    private LinearLayout sure_rate, no_later;
    private int previous = 0;
    private LoadingDialog loadingDialog;
    TinyDB tinyDB;

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
        tinyDB = new TinyDB(context);
        CustomToast.configToast();


        //fetch all ID's from View
        rate_que        = ratePlaceDialog.findViewById(R.id.rate_que);
        star1           = ratePlaceDialog.findViewById(R.id.star1);
        star2           = ratePlaceDialog.findViewById(R.id.star2);
        star3           = ratePlaceDialog.findViewById(R.id.star3);
        star4           = ratePlaceDialog.findViewById(R.id.star4);
        star5           = ratePlaceDialog.findViewById(R.id.star5);
        sure_rate       = ratePlaceDialog.findViewById(R.id.sure_LL);
        no_later        = ratePlaceDialog.findViewById(R.id.not_sure_LL);
        rate_update     = ratePlaceDialog.findViewById(R.id.update_rate);
        if(tinyDB.getBoolean(markerObject.getMarkerID()+"exist"))
        {
            rate_update.setText("UPDATE");
            int value = tinyDB.getInt(markerObject.getMarkerID()+"value");
            previous = value;
            switch (value) {
                case 1: {
                    selectOneStar();
                    break;
                }
                case 2: {
                    selectTwoStar();
                    break;
                }
                case 3: {
                    selectThreeStar();
                    break;
                }
                case 4: {
                    selectFourStar();
                    break;
                }
                case 5: {
                    selectFiveStar();
                    break;
                }
            }
        }

        rate_que.setText("How much would you rate overall place?");
        //set listeners for views
        star1.setOnClickListener(this);
        star2.setOnClickListener(this);
        star3.setOnClickListener(this);
        star4.setOnClickListener(this);
        star5.setOnClickListener(this);
        sure_rate.setOnClickListener(this);
        no_later.setOnClickListener(this);
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
                selectOneStar();
                tinyDB.putInt(markerObject.getMarkerID()+"value",1);
                break;
            }

            case R.id.star2:
            {
                selectTwoStar();
                tinyDB.putInt(markerObject.getMarkerID()+"value",2);
                break;
            }

            case R.id.star3:
            {
                selectThreeStar();
                tinyDB.putInt(markerObject.getMarkerID()+"value",3);
                break;
            }

            case R.id.star4:
            {
                selectFourStar();
                tinyDB.putInt(markerObject.getMarkerID()+"value",4);
                break;
            }

            case R.id.star5:
            {
                selectFiveStar();
                tinyDB.putInt(markerObject.getMarkerID()+"value",5);
                break;
            }
            case R.id.sure_LL:
            {
                if(tinyDB.getInt(markerObject.getMarkerID()+"value") == 0) {
                    Toasty.error(context,"Change your rating", Toast.LENGTH_SHORT,true).show();
                    return;
                }
                if(previous == tinyDB.getInt(markerObject.getMarkerID()+"value")) {
                    Toasty.error(context,"Select a rating", Toast.LENGTH_SHORT,true).show();
                    return;
                }
                loadingDialog = new LoadingDialog(context, "Please Wait...");
                loadingDialog.ShowDialog();
                RatePlaceServerClass ratePlaceServerClass = new RatePlaceServerClass(context, markerObject, String.valueOf(tinyDB.getInt(markerObject.getMarkerID()+"value")));
                ratePlaceServerClass.SetListener(this);
                ratePlaceServerClass.execute();
                HideDialog();
                break;
            }
            case R.id.not_sure_LL:
            {
                if(tinyDB.getBoolean(markerObject.getMarkerID()+"exist")) {
                    tinyDB.putInt(markerObject.getMarkerID()+"value",previous);
                }else {
                    tinyDB.putInt(markerObject.getMarkerID()+"value",0);
                    tinyDB.putBoolean(markerObject.getMarkerID()+"exist",false);
                }
                HideDialog();
                break;
            }
        }

    }
    public void selectOneStar()
    {
        star1.setImageResource(R.drawable.icons8_star_filled_96);
        star2.setImageResource(R.drawable.icons8_star_96);
        star3.setImageResource(R.drawable.icons8_star_96);
        star4.setImageResource(R.drawable.icons8_star_96);
        star5.setImageResource(R.drawable.icons8_star_96);
    }
    public void selectTwoStar()
    {
        star1.setImageResource(R.drawable.icons8_star_filled_96);
        star2.setImageResource(R.drawable.icons8_star_filled_96);
        star3.setImageResource(R.drawable.icons8_star_96);
        star4.setImageResource(R.drawable.icons8_star_96);
        star5.setImageResource(R.drawable.icons8_star_96);
    }
    public void selectThreeStar()
    {
        star1.setImageResource(R.drawable.icons8_star_filled_96);
        star2.setImageResource(R.drawable.icons8_star_filled_96);
        star3.setImageResource(R.drawable.icons8_star_filled_96);
        star4.setImageResource(R.drawable.icons8_star_96);
        star5.setImageResource(R.drawable.icons8_star_96);
    }
    public void selectFourStar()
    {
        star1.setImageResource(R.drawable.icons8_star_filled_96);
        star2.setImageResource(R.drawable.icons8_star_filled_96);
        star3.setImageResource(R.drawable.icons8_star_filled_96);
        star4.setImageResource(R.drawable.icons8_star_filled_96);
        star5.setImageResource(R.drawable.icons8_star_96);
    }
    public void selectFiveStar()
    {
        star1.setImageResource(R.drawable.icons8_star_filled_96);
        star2.setImageResource(R.drawable.icons8_star_filled_96);
        star3.setImageResource(R.drawable.icons8_star_filled_96);
        star4.setImageResource(R.drawable.icons8_star_filled_96);
        star5.setImageResource(R.drawable.icons8_star_filled_96);
    }

    @Override
    public void onRatePlaceSuccess(boolean status) {
        if (status)
        {
            tinyDB.putBoolean(markerObject.getMarkerID()+"exist",true);
            loadingDialog.HideDialog();
            Toasty.success(context,"Rate Successfully Updated", Toast.LENGTH_SHORT,true).show();
        }
        else
        {
            if(tinyDB.getBoolean(markerObject.getMarkerID()+"exist")) {
                tinyDB.putInt(markerObject.getMarkerID()+"value",previous);
            }else {
                tinyDB.putInt(markerObject.getMarkerID()+"value",0);
                tinyDB.putBoolean(markerObject.getMarkerID()+"exist",false);
            }
            loadingDialog.HideDialog();
            Toasty.error(context,"Rate Upload Failed", Toast.LENGTH_SHORT,true).show();
        }
    }
}
