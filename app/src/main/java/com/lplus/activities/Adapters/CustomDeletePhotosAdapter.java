package com.lplus.activities.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lplus.R;
import com.lplus.activities.Dialogs.LoadingDialog;
import com.lplus.activities.Interfaces.ListDataChangedInterface;
import com.lplus.activities.JavaFiles.PhotoStoreInfo;

import java.util.ArrayList;

/**
 * Created by CHANDEL on 3/21/2018.
 */

public class CustomDeletePhotosAdapter extends BaseAdapter {
    Context context;
    PhotoStoreInfo photoStoreInfos;
    private ListDataChangedInterface listDataChangedInterface = null;
    public void setRefreshListener(ListDataChangedInterface listener)
    {
        listDataChangedInterface = listener;
    }

    LayoutInflater inflater;

    public CustomDeletePhotosAdapter(Context context, PhotoStoreInfo photoStoreInfos) {
        this.context = context;
        this.photoStoreInfos = photoStoreInfos;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return photoStoreInfos.getPhoto_array().size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.edit_photo_list_view,null);
        final ImageView photo = view.findViewById(R.id.photo_available);

        photo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final Dialog dialog = new Dialog(context,R.style.CustomDialogTheme);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_delete_photo);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.show();
                dialog.setCanceledOnTouchOutside(true);
                LinearLayout delete_photo_selection = dialog.findViewById(R.id.photo_yes);
                LinearLayout cancel_photo_selection = dialog.findViewById(R.id.photo_no);


                delete_photo_selection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final LoadingDialog loadingDialog = new LoadingDialog(context,"Updating Favourites");
                        loadingDialog.ShowDialog();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                photoStoreInfos.getPhoto_array().remove(photoStoreInfos.getPhoto_array().get(position));
                                loadingDialog.HideDialog();
                                listDataChangedInterface.onDataChanged();
                                dialog.cancel();

                            }
                        }, 2000);

                    }
                });

                cancel_photo_selection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                return false;
            }
        });


        return view;
    }
}
