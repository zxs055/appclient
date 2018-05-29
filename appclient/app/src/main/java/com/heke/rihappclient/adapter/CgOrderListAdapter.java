package com.heke.rihappclient.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextClock;
import android.widget.TextView;

import com.heke.rihappclient.R;
import com.heke.rihappclient.model.rukuinfo;

import java.util.List;

/**
 * Created by wgmhx on 2017/5/1.
 */

public class CgOrderListAdapter extends BaseAdapter {
        private Context context;
        private List<rukuinfo> list;

        public CgOrderListAdapter(Context paramContext, List<rukuinfo> paramList)
        {
            this.context = paramContext;
            this.list = paramList;
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
            ViewHolder localViewHolder=null;
            if (null == paramView)
            {
                localViewHolder = new ViewHolder();
                LayoutInflater mInflater = LayoutInflater.from(context);
                paramView = mInflater.inflate(R.layout.item_listview1, null);
                localViewHolder.tv_billcode = ((TextView)paramView.findViewById(R.id.tv_billcode));
                localViewHolder.tv_money = ((TextView)paramView.findViewById(R.id.tv_money));
                localViewHolder.tv_number = ((TextView)paramView.findViewById(R.id.tv_number));
                localViewHolder.tv_Kname=(TextView)paramView.findViewById(R.id.tv_kindname);
                paramView.setTag(localViewHolder);
            }
            else
            {
                localViewHolder = (ViewHolder) paramView.getTag();
            }
            // set item values to the viewHolder:
            rukuinfo markerItem = getItem(paramInt);
            if (null != markerItem)
            {
                String billcode=markerItem.billcode;
                if(markerItem.billstate.equals("100")){
                    //billcode=billcode+"";
                    billcode=billcode+"<font color='#FF0000'>-暂</font>";
                    //localViewHolder.tv_billcode.setTextColor(R.color.red);
                   //localViewHolder.tv_billcode.setText(str);
                }
                localViewHolder.tv_billcode.setText(Html.fromHtml(billcode));
                localViewHolder.tv_money.setText(markerItem.amount);
                localViewHolder.tv_number.setText(markerItem.quantity);
                localViewHolder.tv_Kname.setText(markerItem.token);
            }
            return paramView;
        }

        public void setList(List<rukuinfo> paramList)
        {
            this.list = paramList;
        }

        private class ViewHolder
        {
            TextView tv_billcode;
            TextView tv_number;
            TextView tv_money;
            TextView tv_Kname;
            private ViewHolder()
            {
            }
        }
    }
