package com.heke.rihappclient.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.heke.rihappclient.R;
import com.heke.rihappclient.model.dacdiaobodan1;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wgmhx on 2017/5/2.
 */

public class OutStoreList_zt_Adapter extends BaseAdapter {
    private Context context;
    private List<dacdiaobodan1> list;

    public OutStoreList_zt_Adapter(Context paramContext, List<dacdiaobodan1> paramList)
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
        OutStoreList_zt_Adapter.ViewHolder localViewHolder=null;
        if (null == paramView)
        {
            localViewHolder = new OutStoreList_zt_Adapter.ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            paramView = mInflater.inflate(R.layout.item_kl_zt_outstore_list_details, null);
            localViewHolder.tv_goodsName=(TextView)paramView.findViewById(R.id.goodsname);
            localViewHolder.tv_shuoming=(TextView)paramView.findViewById(R.id.txtshuoming);

            localViewHolder.lin_layout=(LinearLayout)paramView.findViewById(R.id.lin_layout);
            paramView.setTag(localViewHolder);
        }
        else
        {
            localViewHolder = (OutStoreList_zt_Adapter.ViewHolder) paramView.getTag();
        }
        // set item values to the viewHolder:
        dacdiaobodan1 markerItem = getItem(paramInt);
        if (null != markerItem)
        {
            localViewHolder.tv_goodsName.setText(markerItem.goodsName);
            localViewHolder.tv_shuoming.setText("整包拣入，成功5卷");
//            if(markerItem.onhandQuantity.equals(markerItem.diaoboquantity)){
//                localViewHolder.lin_layout.setBackgroundColor(Color.parseColor("#00ff00"));
//            }else{
//                localViewHolder.lin_layout.setBackgroundColor(Color.parseColor("#ffffff"));
//            }
        }
        return paramView;
    }

    public void setList(List<dacdiaobodan1> paramList)
    {
        this.list = paramList;
    }

    private class ViewHolder
    {
        TextView tv_goodsName;
        TextView tv_shuoming;


        LinearLayout lin_layout;
        private ViewHolder()
        {
        }
    }
}
