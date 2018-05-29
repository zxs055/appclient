package com.heke.rihappclient.XListView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.heke.rihappclient.R;

/**
 * Created by wgmhx on 2017/4/26.
 */

public class XListViewFooter extends LinearLayout
{
    public static final int STATE_LOADING = 2;
    public static final int STATE_NORMAL = 0;
    public static final int STATE_READY = 1;
    private View mContentView;
    private Context mContext;
    private TextView mHintView;
    private View mProgressBar;

    public XListViewFooter(Context paramContext)
    {
        super(paramContext);
        initView(paramContext);
    }

    public XListViewFooter(Context paramContext, AttributeSet paramAttributeSet)
    {
        super(paramContext, paramAttributeSet);
        initView(paramContext);
    }

    private void initView(Context paramContext)
    {
        this.mContext = paramContext;
        LinearLayout localLinearLayout = (LinearLayout) LayoutInflater.from(this.mContext).inflate(R.layout.xlistview_footer, null);
        addView(localLinearLayout);
        localLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        this.mContentView = localLinearLayout.findViewById(R.id.xlistview_footer_content);
        this.mProgressBar = localLinearLayout.findViewById(R.id.xlistview_footer_progressbar);
        this.mHintView = ((TextView)localLinearLayout.findViewById(R.id.xlistview_footer_hint_textview));
    }

    public int getBottomMargin()
    {
        return ((LinearLayout.LayoutParams)this.mContentView.getLayoutParams()).bottomMargin;
    }

    public void hide()
    {
        LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams)this.mContentView.getLayoutParams();
        localLayoutParams.height = 0;
        this.mContentView.setLayoutParams(localLayoutParams);
    }

    public void loading()
    {
        mHintView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void normal()
    {
        mHintView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    public void setBottomMargin(int paramInt)
    {
        if (paramInt < 0)
            return;
        LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams)this.mContentView.getLayoutParams();
        localLayoutParams.bottomMargin = paramInt;
        this.mContentView.setLayoutParams(localLayoutParams);
    }

    public void setState(int state)
    {
        mHintView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mHintView.setVisibility(View.INVISIBLE);
        if (state == STATE_READY) {
            mHintView.setVisibility(View.VISIBLE);
            mHintView.setText(R.string.xlistview_footer_hint_ready);
        } else if (state == STATE_LOADING) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mHintView.setVisibility(View.VISIBLE);
            mHintView.setText(R.string.xlistview_footer_hint_normal);
        }
    }

    public void show()
    {
        LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams)this.mContentView.getLayoutParams();
        localLayoutParams.height = -2;
        this.mContentView.setLayoutParams(localLayoutParams);
    }
}

