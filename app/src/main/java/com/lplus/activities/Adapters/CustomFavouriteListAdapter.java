package com.lplus.activities.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lplus.R;
import com.lplus.activities.Dialogs.LoadingDialog;
import com.lplus.activities.Interfaces.ListDataChangedInterface;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by CHANDEL on 3/19/2018.
 */

public class CustomFavouriteListAdapter extends BaseAdapter{
    Context context;
    ArrayList<String> placeNames;
    ArrayList<String> addresses;
    private ListDataChangedInterface listDataChangedInterface = null;
    public void setListener(ListDataChangedInterface listener)
    {
        listDataChangedInterface = listener;
    }

    LayoutInflater inflater;
    boolean isSelected = true;

    public CustomFavouriteListAdapter(Context context, ArrayList<String> placeNames, ArrayList<String> addresses) {
        this.context = context;
        this.placeNames = placeNames;
        this.addresses = addresses;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return placeNames.size();
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
        view = inflater.inflate(R.layout.list_view_item,null);
        final TextView placename = view.findViewById(R.id.fav_place);
        placename.setText(placeNames.get(position));
        TextView address = view.findViewById(R.id.fav_address);
        address.setText(addresses.get(position));
        final ImageView heart = view.findViewById(R.id.fav_image);
        heart.setTag("ON");


        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(heart.getTag().equals("ON"))
                {
                    Toast.makeText(context,"if position "+position,Toast.LENGTH_SHORT).show();
                    heart.setImageResource(R.drawable.icons8_heart_black_96);
                    heart.setTag("OFF");
                    final LoadingDialog loadingDialog = new LoadingDialog(context,"Updating Favourites");
                    loadingDialog.ShowDialog();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            placeNames.remove(position);
                            addresses.remove(position);
                            loadingDialog.HideDialog();
                            listDataChangedInterface.onDataChanged();

                        }
                    }, 1000);

                }else {
                    Toast.makeText(context,"else position"+position,Toast.LENGTH_SHORT).show();
                    heart.setImageResource(R.drawable.icons8_heart_red_96);
                    heart.setTag("ON");
                }

            }
        });


        return view;
    }
}
