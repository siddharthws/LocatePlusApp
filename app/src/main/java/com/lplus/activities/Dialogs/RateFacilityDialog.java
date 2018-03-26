package com.lplus.activities.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lplus.R;
import com.lplus.activities.DBHelper.AddFacilityTable;
import com.lplus.activities.Extras.CustomToast;
import com.lplus.activities.Interfaces.RateFacillityInterface;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.Objects.MarkerObject;
import com.lplus.activities.Server.RateFacilityServerClass;
import com.lplus.activities.Server.RatePlaceServerClass;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

/**
 * Created by Sai_Kameswari on 25-03-2018.
 */

public class RateFacilityDialog implements RateFacillityInterface{

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

    public RateFacilityDialog(Context context, MarkerObject markerObject)
    {
        this.context = context;
        this.markerObject = markerObject;
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


        for(String fac_value : markerObject.getMarkerFacilities()) {
            fac_id.add(addFacilityTable.ReadID(fac_value));
        }
        Toasty.info(context,fac_id.toString(), Toast.LENGTH_SHORT,true).show();
        addFacilityTable.CloseConnection();

        //fetch all ID's from View
        rate_title        = rateFacilityDialog.findViewById(R.id.rate_title);
        category_que      = rateFacilityDialog.findViewById(R.id.category_que);
        LL_yes            = rateFacilityDialog.findViewById(R.id.LL_yes);
        LL_no             = rateFacilityDialog.findViewById(R.id.LL_no);
        LL_not_sure       = rateFacilityDialog.findViewById(R.id.LL_not_sure);


        category_que.setText("Is this Category/Facility Appropriate?");
        ShowDialog(markerObject);

    }

    public void ShowDialog(MarkerObject markerObject)
    {
        for(String facility : markerObject.getMarkerFacilities()) {
        rate_title.setText(facility.toUpperCase());
        rateFacilityDialog.show();
        LL_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateFacilityDialog.cancel();
                fac_rate.add("1");
            }
        });

        LL_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateFacilityDialog.cancel();
                fac_rate.add("-1");
            }
        });
        LL_not_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateFacilityDialog.cancel();
                fac_rate.add("0");
            }
        });
    }
        loadingDialog = new LoadingDialog(context, "Please Wait...");
        loadingDialog.ShowDialog();
        RateFacilityServerClass rateFacilityServerClass = new RateFacilityServerClass(context, fac_id, fac_rate);
        rateFacilityServerClass.SetListener(this);
        rateFacilityServerClass.execute();

    }

    @Override
    public void onFacilitySent() {
        loadingDialog.HideDialog();
        Toasty.success(context,"Facility Rated", Toast.LENGTH_SHORT,true);
    }

    @Override
    public void onFacilityFailed() {
        loadingDialog.HideDialog();
        Toasty.error(context,"Facility Rating Failed", Toast.LENGTH_SHORT,true);
    }
}
