package com.lplus.activities.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kosalgeek.android.photoutil.ImageLoader;
import com.lplus.R;
import com.lplus.activities.Dialogs.LoadingDialog;
import com.lplus.activities.Extras.TinyDB;
import com.lplus.activities.Interfaces.ListDataChangedInterface;
import com.lplus.activities.JavaFiles.PhotoStoreInfo;
import com.lplus.activities.Macros.Keys;
import com.lplus.activities.activities.AddPlaceActivity;
import com.lplus.activities.activities.EditPhotosActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by CHANDEL on 3/21/2018.
 */

public class CustomDeletePhotosAdapter extends RecyclerView.Adapter<CustomDeletePhotosAdapter.ViewHolder> {
    Context context;
    PhotoStoreInfo photoStoreInfos;
    File imgFile;
    private ListDataChangedInterface listDataChangedInterface = null;
    private TinyDB tinyDB;
    ArrayList<String> photoPath;
    ArrayList<String> photouuid;
    File file;

    public void setRefreshListener(ListDataChangedInterface listener)
    {
        listDataChangedInterface = listener;
    }

    LayoutInflater inflater;

    public CustomDeletePhotosAdapter(Context context, PhotoStoreInfo photoStoreInfos) {
        this.context = context;
        this.photoStoreInfos = photoStoreInfos;
        inflater = LayoutInflater.from(context);
        tinyDB = new TinyDB(context);
        //ArrayList<String> photoPath = photoStoreInfos.getPhoto_array().get(position);
        photoPath = tinyDB.getListString(Keys.TINYDB_PHOTO_LIST);
        photouuid = tinyDB.getListString(Keys.TINYDB_PHOTO_UUID_LIST);
    }

    @Override
    public CustomDeletePhotosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_photo_list_view, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//Loading image from Glide library.
        Glide.with(context).load(Uri.fromFile(new File(photoPath.get(position)))).into(holder.photo);
    }

    @Override
    public int getItemCount() {
        if(photoStoreInfos.getPhoto_array() != null) {
            return photoStoreInfos.getPhoto_array().size();
        }else {
            return 0;
        }
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView photo;
        int position;
        public ViewHolder(final View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.photo_available);


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
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
                    position=getAdapterPosition();


                    delete_photo_selection.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.cancel();
                            final LoadingDialog loadingDialog = new LoadingDialog(context,"Updating Selection");
                            loadingDialog.ShowDialog();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("Length of array "," "+position);
                                    file = new File(photoStoreInfos.getPhoto_array().get(position));
                                    boolean isSuccess = file.delete();
                                    photoStoreInfos.getPhoto_array().remove(position);
                                    photoStoreInfos.getPhoto_uuid_array().remove(position);
                                    tinyDB.putListString(Keys.TINYDB_PHOTO_LIST,photoStoreInfos.getPhoto_array());
                                    tinyDB.putListString(Keys.TINYDB_PHOTO_UUID_LIST,photoStoreInfos.getPhoto_uuid_array());
                                    loadingDialog.HideDialog();
                                    notifyItemRemoved(position);
                                    //this line below gives you the animation and also updates the
                                    //list items after the deleted item
                                    notifyItemRangeChanged(position, getItemCount());
                                    listDataChangedInterface.onDataChanged();
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

        }
    }
}
