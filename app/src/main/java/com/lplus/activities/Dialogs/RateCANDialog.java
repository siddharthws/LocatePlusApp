package com.lplus.activities.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lplus.R;
import com.lplus.activities.Interfaces.RateCANInterface;
import com.lplus.activities.Objects.MarkerObject;
import com.lplus.activities.Server.RateCANServerClass;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

/**
 * Created by Sai_Kameswari on 25-03-2018.
 */

public class RateCANDialog implements RateCANInterface {

    private Context context;
    private Dialog rateNameDialog, rateCategoryDialog, ratePhotoDialog;
    private MarkerObject markerObject;
    private TextView name_title, name_que, catgory_title, category_que;
    private TextView rate_photo_que;
    private LinearLayout LL_yes, LL_no, LL_not_sure;
    private LinearLayout cat_LL_yes, cat_LL_no, cat_LL_not_sure;
    private LinearLayout photo_yes, photo_no, photo_not_sure;
    private ImageView rate_photo_view;
    ArrayList<String> CANRate_array;
    private LoadingDialog loadingDialog;

    public RateCANDialog(Context context, MarkerObject markerObject)
    {
        this.context = context;
        this.markerObject = markerObject;
        Init();
    }

    private void Init()
    {

        //Name dialogue
        rateNameDialog = new Dialog(context, R.style.CustomDialogTheme);
        rateNameDialog.setContentView(R.layout.dialog_rate_category);
        rateNameDialog.setCancelable(true);
        rateNameDialog.setCanceledOnTouchOutside(true);
        rateNameDialog.getWindow().getAttributes().windowAnimations = R.style.SlideHorizontalAnimation;
        CANRate_array = new ArrayList<>();

        //fetch all ID's from View
        name_title        = rateNameDialog.findViewById(R.id.rate_title);
        name_que      = rateNameDialog.findViewById(R.id.category_que);

        LL_yes            = rateNameDialog.findViewById(R.id.LL_yes);
        LL_no             = rateNameDialog.findViewById(R.id.LL_no);
        LL_not_sure       = rateNameDialog.findViewById(R.id.LL_not_sure);

        //category dialogue
        rateCategoryDialog = new Dialog(context, R.style.CustomDialogTheme);
        rateCategoryDialog.setContentView(R.layout.dialog_rate_category);
        rateCategoryDialog.setCancelable(true);
        rateCategoryDialog.setCanceledOnTouchOutside(true);
        rateCategoryDialog.getWindow().getAttributes().windowAnimations = R.style.SlideHorizontalAnimation;

        //fetch all ID's from View
        catgory_title        = rateCategoryDialog.findViewById(R.id.rate_title);
        category_que      = rateCategoryDialog.findViewById(R.id.category_que);

        cat_LL_yes            = rateCategoryDialog.findViewById(R.id.LL_yes);
        cat_LL_no             = rateCategoryDialog.findViewById(R.id.LL_no);
        cat_LL_not_sure       = rateCategoryDialog.findViewById(R.id.LL_not_sure);

        //photo dialog
        ratePhotoDialog = new Dialog(context, R.style.CustomDialogTheme);
        ratePhotoDialog.setContentView(R.layout.dialog_rate_photo);
        ratePhotoDialog.setCancelable(true);
        ratePhotoDialog.setCanceledOnTouchOutside(true);
        ratePhotoDialog.getWindow().getAttributes().windowAnimations = R.style.SlideHorizontalAnimation;


        //fetch all ID's from View
        rate_photo_view      = ratePhotoDialog.findViewById(R.id.photo_view);
        rate_photo_que       = ratePhotoDialog.findViewById(R.id.photo_ques);

        photo_yes            = ratePhotoDialog.findViewById(R.id.LL_yes);
        photo_no             = ratePhotoDialog.findViewById(R.id.LL_no);
        photo_not_sure       = ratePhotoDialog.findViewById(R.id.LL_not_sure);

    }

    public void ShowDialog()
    {
        name_title.setText(markerObject.getMarkerName());
        name_que.setText("Is this Name Appropriate?");
        rateNameDialog.show();
        LL_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CANRate_array.add("1");
                rateNameDialog.cancel();
                showCategoryDialogue();

            }
        });

        LL_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CANRate_array.add("-1");
                rateNameDialog.cancel();
                showCategoryDialogue();
            }
        });

        LL_not_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CANRate_array.add("0");
                rateNameDialog.cancel();
                showCategoryDialogue();
            }
        });
    }

    void showCategoryDialogue() {
        catgory_title.setText(markerObject.getMarkerCategory());
        category_que.setText("Is this Category Appropriate?");
        rateCategoryDialog.show();
        cat_LL_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CANRate_array.add("1");
                rateCategoryDialog.cancel();
                showPhotoDialog();
            }
        });

        cat_LL_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CANRate_array.add("-1");
                rateCategoryDialog.cancel();
                showPhotoDialog();
            }
        });

        cat_LL_not_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CANRate_array.add("0");
                rateCategoryDialog.cancel();
                showPhotoDialog();
            }
        });
    }
    void showPhotoDialog() {
        rate_photo_que.setText("Is this Adress appropriate?");
        ratePhotoDialog.show();
        photo_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CANRate_array.add("1");
                ratePhotoDialog.cancel();
                sendToServer();
            }
        });
        photo_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CANRate_array.add("-1");
                ratePhotoDialog.cancel();
                sendToServer();
            }
        });
        photo_not_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CANRate_array.add("0");
                ratePhotoDialog.cancel();
                sendToServer();
            }
        });
    }
    public void sendToServer() {
        loadingDialog = new LoadingDialog(context, "Please Wait...");
        loadingDialog.ShowDialog();
        RateCANServerClass rateCANServerClass = new RateCANServerClass(context, markerObject, CANRate_array);
        rateCANServerClass.SetListener(this);
        rateCANServerClass.execute();
    }

    @Override
    public void onCANStatus(boolean status) {
        if (status)
        {
            loadingDialog.HideDialog();
            Toasty.success(context,"Rate Uploaded", Toast.LENGTH_SHORT,true);
        }
        else
        {
            loadingDialog.HideDialog();
            Toasty.error(context,"Rating Failed", Toast.LENGTH_SHORT,true);
        }
    }
}
