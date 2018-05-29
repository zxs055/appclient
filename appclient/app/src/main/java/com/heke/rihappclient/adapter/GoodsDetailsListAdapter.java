package com.heke.rihappclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.heke.rihappclient.R;
import com.heke.rihappclient.model.rukuinfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wgmhx on 2017/5/2.
 */

public class GoodsDetailsListAdapter extends BaseAdapter {
    private Context context;
    private List<rukuinfo> list;

    public GoodsDetailsListAdapter(Context paramContext, List<rukuinfo> paramList)
    {
        this.context = paramContext;
        this.list = paramList;
    }
    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * @param list
     */
    public void updateListView(List<rukuinfo> list) {
        if (list == null) {
            this.list = new ArrayList<rukuinfo>();
        } else {
            this.list = list;
        }
        notifyDataSetChanged();
    }
    public int getCount()
    {
        return this.list.size();
    }

    public rukuinfo getItem(int paramInt)
    {
        return (rukuinfo)this.list.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
        return paramInt;
    }

    public List<rukuinfo> getList()
    {
        return this.list;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
        GoodsDetailsListAdapter.ViewHolder localViewHolder=null;
        if (null == paramView)
        {
            localViewHolder = new GoodsDetailsListAdapter.ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            paramView = mInflater.inflate(R.layout.item_list_goods_details, null);
            localViewHolder.tv_barcode = ((TextView)paramView.findViewById(R.id.tv_barcode));
            localViewHolder.tv_price = ((TextView)paramView.findViewById(R.id.tv_price));
            localViewHolder.tv_number = ((TextView)paramView.findViewById(R.id.tv_number));
            localViewHolder.tv_goodsName=(TextView)paramView.findViewById(R.id.tv_goodsName);
            localViewHolder.tv_goodsScope=(TextView)paramView.findViewById(R.id.tv_goodsScope);
            paramView.setTag(localViewHolder);
        }
        else
        {
            localViewHolder = (GoodsDetailsListAdapter.ViewHolder) paramView.getTag();
        }
        // set item values to the viewHolder:
        rukuinfo markerItem = getItem(paramInt);
        if (null != markerItem)
        {
            localViewHolder.tv_barcode.setText(markerItem.barcode);
            localViewHolder.tv_price.setText(markerItem.price);
            localViewHolder.tv_number.setText(markerItem.quantity);
            localViewHolder.tv_goodsName.setText(markerItem.invname);
            localViewHolder.tv_goodsScope.setText(markerItem.invspec);
        }
        return paramView;
    }

    public void setList(List<rukuinfo> paramList)
    {
        this.list = paramList;
    }

    private class ViewHolder
    {
        TextView tv_barcode;
        TextView tv_number;
        TextView tv_price;
        TextView tv_goodsName;
        TextView tv_goodsScope;
        private ViewHolder()
        {
        }
    }
}