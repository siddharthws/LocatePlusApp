package com.lplus.activities.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lplus.R;

import java.util.List;

/**
 * Created by Sai_Kameswari on 26-03-2018.
 */

public class FetchedImageSlider extends PagerAdapter {
    private List<Bitmap> images;
    private LayoutInflater inflater;
    private Context context;

    public FetchedImageSlider(Context context, List<Bitmap> images) {
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
        myImage.setImageBitmap(images.get(0));
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
