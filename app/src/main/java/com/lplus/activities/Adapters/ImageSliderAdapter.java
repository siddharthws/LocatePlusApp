package com.lplus.activities.Adapters;

/**
 * Created by Sai_Kameswari on 02-02-2018.
 */

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.lplus.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ImageSliderAdapter extends PagerAdapter {

    private List<String> images;
    private LayoutInflater inflater;
    private Context context;

    public ImageSliderAdapter(Context context, List<String> images) {
        this.context = context;
        this.images=images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slide, view, false);
        ImageView myImage =  myImageLayout.findViewById(R.id.slider_image);
        Picasso.with(context).load(Uri.fromFile(new File(images.get(position)))).into(myImage);
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}