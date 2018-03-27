package com.lplus.activities.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lplus.R;
import com.lplus.activities.DBHelper.AddFacilityTable;
import com.lplus.activities.Extras.CustomToast;
import com.lplus.activities.Interfaces.FacilityDialogClickInterface;
import com.lplus.activities.Interfaces.RateFacillityInterface;
import com.lplus.activities.Objects.MarkerObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

/**
 * Created by Sai_Kameswari on 25-03-2018.
 */

public class RateFacilityDialog{

    private Context context;
    private Dialog rateFacilityDialog;
    private MarkerObject markerObject;
    private TextView rate_title, category_que;
    private LinearLayout LL_yes, LL_no, LL_not_sure;
    private ArrayList<String> fac_id;
    private ArrayList<String> fac_names;
    private AddFacilityTable addFacilityTable;
    private ArrayList<String> fac_rate;
    private LoadingDialog loadingDialog;
    private FacilityDialogClickInterface listener;
    private int index;
    public void SetListener(FacilityDialogClickInterface listener)
    {
        this.listener = listener;
    }


    public RateFacilityDialog(Context context, MarkerObject markerObject,int index)
    {
        this.context = context;
        this.markerObject = markerObject;
        this.index = index;
        Init();
    }

    private void Init()
    {
        CustomToast.configToast();

        addFacilityTable = new AddFacilityTable(context);
        rateFacilityDialog = new Dialog(context, R.style.CustomDialogTheme);
        rateFacilityDialog.setContentView(R.layout.dialog_rate_category);
        rateFacilityDialog.setCancelable(true);
        rateFacilityDialog.setCanceledOnTouchOutside(true);
        fac_id = new ArrayList<>();
        fac_names = new ArrayList<>();
        rateFacilityDialog.getWindow().getAttributes().windowAnimations = R.style.SlideHorizontalAnimation;
        fac_rate = new ArrayList<>();




        //fetch all ID's from View
        rate_title        = rateFacilityDialog.findViewById(R.id.rate_title);
        category_que      = rateFacilityDialog.findViewById(R.id.category_que);
        LL_yes            = rateFacilityDialog.findViewById(R.id.LL_yes);
        LL_no             = rateFacilityDialog.findViewById(R.id.LL_no);
        LL_not_sure       = rateFacilityDialog.findViewById(R.id.LL_not_sure);


        category_que.setText("Is this Facility Appropriate?");

    }

    public void ShowDialog()
    {
        rateFacilityDialog.show();
        rate_title.setText(markerObject.getMarkerFacilities().get(index).toUpperCase());
        rateFacilityDialog.show();
        LL_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideDialog();
                listener.onDialogClick("1");
            }
        });
        LL_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideDialog();
                listener.onDialogClick("-1");
            }
        });
        LL_not_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideDialog();
                listener.onDialogClick("0");
            }
        });


    }

    public  void HideDialog() {
        rateFacilityDialog.cancel();
    }
}
