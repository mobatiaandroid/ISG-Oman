package com.algubra.activity.home.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.algubra.R;
import com.algubra.manager.AppUtilityMethods;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

/**
 * Created by gayatri on 2/6/17.
 */
public class ImagePagerDrawableAdapter extends PagerAdapter {
    Context mContext;
    ArrayList<String> mImagesArrayListUrlBg;
    private LayoutInflater mInflaters;
    ImageView imageView;

    public ImagePagerDrawableAdapter(ArrayList<String> mImagesArrayListUrlBg, Context context) {
        this.mImagesArrayListUrlBg = new ArrayList<String>();
        this.mContext = context;
        this.mImagesArrayListUrlBg = mImagesArrayListUrlBg;
    }

    @Override
    public int getCount() {
        return mImagesArrayListUrlBg.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        View pageview = null;
        mInflaters = LayoutInflater.from(mContext);
        pageview = mInflaters.inflate(R.layout.layout_image_pager_adapter, null);
        imageView = (ImageView) pageview.findViewById(R.id.adImg);

//        imageView.setBackgroundResource(mImagesArrayListBg.get(position));
        if (!mImagesArrayListUrlBg.get(position).equals("")) {
            //  loadImage(mContext, mImagesArrayListUrlBg.get(position).replaceAll(" ", "%20"), imageView);   //OLD COMMENTED DATA NTN
//            imageView.setAdjustViewBounds(true);
            Picasso.with(mContext).load(AppUtilityMethods.replace(mImagesArrayListUrlBg.get(position))).fit().into(imageView, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    System.out.println("Image Succes:" + AppUtilityMethods.replace(mImagesArrayListUrlBg.get(position)));

                    // imageView.setAdjustViewBounds(true);

                }

                @Override
                public void onError() {
//            Glide.with(mContext).load(AppUtils.replace(mImagesArrayListUrlBg.get(position).toString())).centerCrop().into(imageView);
                    //  imageView.setAdjustViewBounds(true);

                }
            });
        }


        ((ViewPager) container).

                addView(pageview, 0);

        return pageview;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    private Target mTarget;

    void loadImage(Context context, String url, final ImageView img) {


        mTarget = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                //Do something

                img.setImageBitmap(bitmap);

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(context)
                .load(url).placeholder(R.drawable.homebgschool)
                .into(mTarget);
    }
}