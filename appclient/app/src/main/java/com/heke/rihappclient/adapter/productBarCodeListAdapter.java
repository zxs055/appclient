package com.heke.rihappclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.heke.rihappclient.R;
import com.heke.rihappclient.model.dacdiaobodan1;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wgmhx on 2017/5/2.
 */

public class productBarCodeListAdapter extends BaseAdapter {
    private Context context;
    private List<dacdiaobodan1> list;

    public productBarCodeListAdapter(Context paramContext, List<dacdiaobodan1> paramList)
    {
        this.context = paramContext;
        this.list = paramList;
    }
    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * @param list
     */
    public void updateListView(List<dacdiaobodan1> list) {
        if (list == null) {
            this.list = new ArrayList<dacdiaobodan1>();
        } else {
            this.list = list;
        }
        notifyDataSetChanged();
    }
    public int getCount()
    {
        return this.list.size();
    }

    public dacdiaobodan1 getItem(int paramInt)
    {
        return (dacdiaobodan1)this.list.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
        return paramInt;
    }

    public List<dacdiaobodan1> getList()
    {
        return this.list;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
        productBarCodeListAdapter.ViewHolder localViewHolder=null;
        if (null == paramView)
        {
            localViewHolder = new productBarCodeListAdapter.ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            paramView = mInflater.inflate(R.layout.item_list_productbarcode, null);
            localViewHolder.tv_barcode = ((TextView)paramView.findViewById(R.id.tv_barcode));
            paramView.setTag(localViewHolder);
        }
        else
        {
            localViewHolder = (productBarCodeListAdapter.ViewHolder) paramView.getTag();
        }
        // set item values to the viewHolder:
        dacdiaobodan1 markerItem = getItem(paramInt);
        if (null != markerItem)
        {
            localViewHolder.tv_barcode.setText(markerItem.barCode);
        }
        return paramView;
    }

    public void setList(List<dacdiaobodan1> paramList)
    {
        this.list = paramList;
    }

    private class ViewHolder
    {
        TextView tv_barcode;
        private ViewHolder()
        {
        }
    }
}
