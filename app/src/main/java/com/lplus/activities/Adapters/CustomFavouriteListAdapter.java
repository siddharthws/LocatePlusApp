package com.lplus.activities.Adapters;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lplus.R;
import com.lplus.activities.DBHelper.AddFavoutiteTable;
import com.lplus.activities.Dialogs.LoadingDialog;
import com.lplus.activities.Interfaces.ListDataChangedInterface;
import com.lplus.activities.Objects.FavouriteObject;

import java.util.ArrayList;

/**
 * Created by CHANDEL on 3/19/2018.
 */

public class CustomFavouriteListAdapter extends BaseAdapter{
    Context context;
    ArrayList<String> place_id;
    ArrayList<String> placeNames;
    ArrayList<String> addresses;
    AddFavoutiteTable addFavoutiteTable = null;
    private FavouriteObject selectedFavoriteObject;;
    private ListDataChangedInterface listDataChangedInterface = null;
    public void setListener(ListDataChangedInterface listener)
    {
        listDataChangedInterface = listener;
    }

    LayoutInflater inflater;
    boolean isSelected = true;

    public CustomFavouriteListAdapter(Context context, ArrayList<String> place_id, ArrayList<String> placeNames, ArrayList<String> addresses) {
        this.context = context;
        this.place_id = place_id;
        this.placeNames = placeNames;
        this.addresses = addresses;
        inflater = LayoutInflater.from(context);
        addFavoutiteTable = new AddFavoutiteTable(context);
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
        final LinearLayout favLayout = view.findViewById(R.id.favourite_item);
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
                            addFavoutiteTable.RemoveFavourite(place_id.get(position));
                            placeNames.remove(position);
                            addresses.remove(position);
                            place_id.remove(position);
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

        favLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.bounce);
                favLayout.startAnimation(myAnim);
                myAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }
                    @Override
                    public void onAnimationEnd(Animation animation)
                    {
                        selectedFavoriteObject = addFavoutiteTable.getClickedRecord(place_id.get(position));
                        listDataChangedInterface.onItemClicked(selectedFavoriteObject);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });


        return view;
    }
}
