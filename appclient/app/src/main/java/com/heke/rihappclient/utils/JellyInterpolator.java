package com.heke.rihappclient.utils;

import android.view.animation.LinearInterpolator;

/**
 * 插值器
 * Created by xssx5 on 2018-03-15.
 */

public class JellyInterpolator extends LinearInterpolator{

    private float factor;

    public JellyInterpolator(){
        this.factor=0.15f;
    }

    @Override
    public float getInterpolation(float input) {
        return (float)(Math.pow(2,-10*input)*Math.sin((input-factor/4)*(2*Math.PI)/factor)+1);
    }
}
