package com.lplus.activities.Adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lplus.R;

import java.util.ArrayList;

/**
 * Created by Sai_Kameswari on 23-03-2018.
 */

public class ReviewSliderAdapter extends PagerAdapter {

    private ArrayList<String> reviews;
    private LayoutInflater inflater;
    private Context context;

    public ReviewSliderAdapter(Context context, ArrayList<String> reviews) {
        this.context = context;
        this.reviews=reviews;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.reviewslide, view, false);
        TextView myReview =  myImageLayout.findViewById(R.id.slider_review);
        myReview.setText(reviews.get(position));
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
