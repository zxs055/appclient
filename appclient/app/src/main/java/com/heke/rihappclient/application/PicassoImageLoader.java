package com.heke.rihappclient.application;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.heke.rihappclient.R;
import com.lzy.ninegrid.NineGridView;

/**
 * Created by wgmhx on 2017/4/21.
 */

public class PicassoImageLoader implements NineGridView.ImageLoader {

    @Override
    public void onDisplayImage(Context context, ImageView imageView, String url) {
        com.squareup.picasso.Picasso.with(context).load(url)//
                .placeholder(R.drawable.ic_default_image)//
                .error(R.drawable.ic_default_image)//
                .into(imageView);
    }

    @Override
    public Bitmap getCacheImage(String url) {
        return null;
    }
}
