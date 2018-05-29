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

public class GoodsDetailsList1Adapter extends BaseAdapter {
    private Context context;
    private List<dacdiaobodan1> list;

    public GoodsDetailsList1Adapter(Context paramContext, List<dacdiaobodan1> paramList)
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
        GoodsDetailsList1Adapter.ViewHolder localViewHolder=null;
        if (null == paramView)
        {
            localViewHolder = new GoodsDetailsList1Adapter.ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            paramView = mInflater.inflate(R.layout.item_list_goods_details1, null);
            localViewHolder.tv_barcode = ((TextView)paramView.findViewById(R.id.tv_barcode));
            localViewHolder.tv_price = ((TextView)paramView.findViewById(R.id.tv_price));
            localViewHolder.tv_number = ((TextView)paramView.findViewById(R.id.tv_number));
            localViewHolder.tv_diaobonumber=(TextView)paramView.findViewById(R.id.tv_diaobonumber);
            localViewHolder.tv_goodsName=(TextView)paramView.findViewById(R.id.tv_goodsName);
            localViewHolder.tv_goodsScope=(TextView)paramView.findViewById(R.id.tv_goodsScope);
            paramView.setTag(localViewHolder);
        }
        else
        {
            localViewHolder = (GoodsDetailsList1Adapter.ViewHolder) paramView.getTag();
        }
        // set item values to the viewHolder:
        dacdiaobodan1 markerItem = getItem(paramInt);
        if (null != markerItem)
        {
            localViewHolder.tv_barcode.setText(markerItem.barCode);
            localViewHolder.tv_price.setText(markerItem.cost);
            localViewHolder.tv_number.setText(markerItem.quantity);
            localViewHolder.tv_diaobonumber.setText(markerItem.diaoboquantity);
            localViewHolder.tv_goodsName.setText(markerItem.goodsName);
            localViewHolder.tv_goodsScope.setText(markerItem.goodsScope);
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
        TextView tv_number;
        TextView tv_price;
        TextView tv_diaobonumber;
        TextView tv_goodsName;
        TextView tv_goodsScope;
        private ViewHolder()
        {
        }
    }
}
