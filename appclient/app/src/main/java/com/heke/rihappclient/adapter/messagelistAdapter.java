package com.heke.rihappclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.heke.rihappclient.R;
import com.heke.rihappclient.model.baseinfo;

import java.util.List;

/**
 * Created by wgmhx on 2017/5/12.
 */

public class messagelistAdapter extends BaseAdapter {
    private Context context;
    private List<baseinfo> list;

    public messagelistAdapter(Context paramContext, List<baseinfo> paramList)
    {
        this.context = paramContext;
        this.list = paramList;
    }

    public int getCount()
    {
        return this.list.size();
    }

    public baseinfo getItem(int paramInt)
    {
        return (baseinfo)this.list.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
        return paramInt;
    }

    public List<baseinfo> getList()
    {
        return this.list;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
        messagelistAdapter.ViewHolder localViewHolder=null;
        if (null == paramView)
        {
            localViewHolder = new messagelistAdapter.ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            paramView = mInflater.inflate(R.layout.layout_notice_item, null);
            localViewHolder.tv_itemname = ((TextView)paramView.findViewById(R.id.itemname));
            localViewHolder.tv_itemdate = ((TextView)paramView.findViewById(R.id.itemdate));
            paramView.setTag(localViewHolder);
        }
        else
        {
            localViewHolder = (messagelistAdapter.ViewHolder) paramView.getTag();
        }
        // set item values to the viewHolder:
        baseinfo markerItem = getItem(paramInt);
        if (null != markerItem)
        {
            localViewHolder.tv_itemname.setText(markerItem.barcode);
            localViewHolder.tv_itemdate.setText(markerItem.invcode+"【阅读"+markerItem.baseid+"次】");
        }
        return paramView;
    }

    public void setList(List<baseinfo> paramList)
    {
        this.list = paramList;
    }

    private class ViewHolder
    {
        TextView tv_itemname;
        TextView tv_itemdate;
        private ViewHolder()
        {
        }
    }
}
