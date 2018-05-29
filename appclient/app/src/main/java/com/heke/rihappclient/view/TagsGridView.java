package com.heke.rihappclient.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by wgmhx on 2017/8/28.
 */

public class TagsGridView extends GridView
{
    public TagsGridView(Context paramContext)
    {
        super(paramContext);
    }

    public TagsGridView(Context paramContext, AttributeSet paramAttributeSet)
    {
        super(paramContext, paramAttributeSet);
    }

    public TagsGridView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
    {
        super(paramContext, paramAttributeSet, paramInt);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

        //super.onMeasure(paramInt1, View.MeasureSpec.makeMeasureSpec(536870911, paramInt2));
        // super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

}


