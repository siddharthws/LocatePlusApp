package com.lplus.activities.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lplus.R;
import com.lplus.activities.Objects.MarkerObject;

/**
 * Created by Sai_Kameswari on 25-03-2018.
 */

public class RateCategoryDialog implements View.OnClickListener {

    private Context context;
    private Dialog rateCategoryDialog;
    private MarkerObject markerObject;
    private TextView rate_title, category_que;
    private LinearLayout LL_yes, LL_no, LL_not_sure;

    public RateCategoryDialog(Context context, MarkerObject markerObject)
    {
        this.context = context;
        this.markerObject = markerObject;
        Init();
    }

    private void Init()
    {

        rateCategoryDialog = new Dialog(context, R.style.CustomDialogTheme);
        rateCategoryDialog.setContentView(R.layout.dialog_rate_category);
        rateCategoryDialog.setCancelable(true);
        rateCategoryDialog.setCanceledOnTouchOutside(true);
        rateCategoryDialog.getWindow().getAttributes().windowAnimations = R.style.SlideHorizontalAnimation;


        //fetch all ID's from View
        rate_title        = rateCategoryDialog.findViewById(R.id.rate_title);
        category_que      = rateCategoryDialog.findViewById(R.id.category_que);

        LL_yes            = rateCategoryDialog.findViewById(R.id.LL_yes);
        LL_no             = rateCategoryDialog.findViewById(R.id.LL_no);
        LL_not_sure       = rateCategoryDialog.findViewById(R.id.LL_not_sure);


        rate_title.setText("Facility/Category");
        category_que.setText("Is this Category/Facility Appropriate?");
        //set listeners for linear layouts
        LL_yes.setOnClickListener(this);
        LL_no.setOnClickListener(this);
        LL_not_sure.setOnClickListener(this);

    }

    public void ShowDialog()
    {
        rateCategoryDialog.show();
    }

    public void HideDialog()
    {
        rateCategoryDialog.cancel();
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
