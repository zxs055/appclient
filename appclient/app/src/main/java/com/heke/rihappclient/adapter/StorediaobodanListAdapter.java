package com.heke.rihappclient.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.heke.rihappclient.R;
import com.heke.rihappclient.model.dacdiaobodan;
import com.heke.rihappclient.model.dacdiaobodan1;
import com.heke.rihappclient.model.rukuinfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wgmhx on 2017/5/1.
 */

public class StorediaobodanListAdapter extends BaseAdapter {
        private Context context;
        private List<dacdiaobodan> list;

        public StorediaobodanListAdapter(Context paramContext, List<dacdiaobodan> paramList)
        {
            this.context = paramContext;
            this.list = paramList;
        }
    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * @param list
     */
    public void updateListView(List<dacdiaobodan> list) {
        if (list == null) {
            this.list = new ArrayList<dacdiaobodan>();
        } else {
            this.list = list;
        }
        notifyDataSetChanged();
    }
        public int getCount()
        {
            return this.list.size();
        }

        public dacdiaobodan getItem(int paramInt)
        {
            return (dacdiaobodan) this.list.get(paramInt);
        }

        public long getItemId(int paramInt)
        {
            return paramInt;
        }

        public List<dacdiaobodan> getList()
        {
            return this.list;
        }

        public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
        {
            ViewHolder localViewHolder=null;
            if (null == paramView)
            {
                localViewHolder = new ViewHolder();
                LayoutInflater mInflater = LayoutInflater.from(context);
                paramView = mInflater.inflate(R.layout.item_listview2, null);
                localViewHolder.tv_billcode = ((TextView)paramView.findViewById(R.id.tv_billcode));
                //localViewHolder.tv_money = ((TextView)paramView.findViewById(R.id.tv_money));
                localViewHolder.tv_number = ((TextView)paramView.findViewById(R.id.tv_number));
                localViewHolder.tv_srcStore=((TextView)paramView.findViewById(R.id.tv_srcStore));
                localViewHolder.tv_desStore=((TextView)paramView.findViewById(R.id.tv_desStore));
                paramView.setTag(localViewHolder);
            }
            else
            {
                localViewHolder = (ViewHolder) paramView.getTag();
            }
            // set item values to the viewHolder:
            dacdiaobodan markerItem = getItem(paramInt);
            if (null != markerItem)
            {
                String billcode=markerItem.billCode;
                if(markerItem.billState.equals("100")){
                    //billcode=billcode+"";
                    billcode=billcode+"<font color='#FF0000'>-暂</font>";
                    //localViewHolder.tv_billcode.setTextColor(R.color.red);
                   //localViewHolder.tv_billcode.setText(str);
                }
                localViewHolder.tv_billcode.setText(Html.fromHtml(billcode));
                //localViewHolder.tv_money.setText(markerItem.amount);
                localViewHolder.tv_number.setText(markerItem.totalQuantity);
                localViewHolder.tv_srcStore.setText(markerItem.srcStoreName);
                localViewHolder.tv_desStore.setText(markerItem.desStoreName);
            }
            return paramView;
        }

        public void setList(List<dacdiaobodan> paramList)
        {
            this.list = paramList;
        }

        private class ViewHolder
        {
            TextView tv_billcode;
            TextView tv_number;
            TextView tv_srcStore;
            TextView tv_desStore;
            private ViewHolder()
            {
            }
        }
    }
