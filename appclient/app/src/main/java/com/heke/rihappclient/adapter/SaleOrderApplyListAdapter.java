package com.heke.rihappclient.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.heke.rihappclient.R;
import com.heke.rihappclient.model.dacdiaobodan1;

import java.util.List;

/**
 * Created by wgmhx on 2017/5/1.
 */

public class SaleOrderApplyListAdapter extends BaseAdapter {
        private Context context;
        private List<dacdiaobodan1> list;

        public SaleOrderApplyListAdapter(Context paramContext, List<dacdiaobodan1> paramList)
        {
            this.context = paramContext;
            this.list = paramList;
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
            ViewHolder localViewHolder=null;
            if (null == paramView)
            {
                localViewHolder = new ViewHolder();
                LayoutInflater mInflater = LayoutInflater.from(context);
                paramView = mInflater.inflate(R.layout.item_listview4, null);
                localViewHolder.tv_billcode = ((TextView)paramView.findViewById(R.id.tv_billcode));
                localViewHolder.tv_money = ((TextView)paramView.findViewById(R.id.tv_money));
                localViewHolder.tv_number = ((TextView)paramView.findViewById(R.id.tv_number));
                localViewHolder.tv_storeName=(TextView)paramView.findViewById(R.id.tv_storeName);
                localViewHolder.tv_createUser=(TextView)paramView.findViewById(R.id.tv_creatUser);
                paramView.setTag(localViewHolder);
            }
            else
            {
                localViewHolder = (ViewHolder) paramView.getTag();
            }
            // set item values to the viewHolder:
            dacdiaobodan1 markerItem = getItem(paramInt);
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
                localViewHolder.tv_money.setText(markerItem.amount);
                localViewHolder.tv_number.setText(markerItem.quantity);
                localViewHolder.tv_createUser.setText(markerItem.userid);
                localViewHolder.tv_storeName.setText(markerItem.desStoreName);
            }
            return paramView;
        }

        public void setList(List<dacdiaobodan1> paramList)
        {
            this.list = paramList;
        }

        private class ViewHolder
        {
            TextView tv_billcode;
            TextView tv_number;
            TextView tv_money;
            TextView tv_storeName;
            TextView tv_createUser;
            private ViewHolder()
            {
            }
        }
    }
