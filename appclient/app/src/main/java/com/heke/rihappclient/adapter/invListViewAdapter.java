package com.heke.rihappclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.heke.rihappclient.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wgmhx on 2017/4/28.
 */

public class invListViewAdapter extends BaseAdapter
    {
        private Context context;
        private LayoutInflater inflater;
        private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        public invListViewAdapter(Context paramContext)
        {
            this.context = paramContext;
            this.inflater = LayoutInflater.from(paramContext);
        }

        public int getCount()
        {
            return this.list.size();
        }

        public Object getItem(int paramInt)
        {
            return this.list.get(paramInt);
        }

        public long getItemId(int paramInt)
        {
            return paramInt;
        }

        public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
        {
            TextView localTextView;
            if (paramView == null)
            {
                paramView = this.inflater.inflate(R.layout.popupwindow_list_textview, null);
                localTextView = (TextView)paramView.findViewById(R.id.textview_popup);
                paramView.setTag(localTextView);
            }
            localTextView = (TextView)paramView.getTag();
            Map localMap = (Map)this.list.get(paramInt);
            if (localMap != null)
                localTextView.setText(localMap.get("invname").toString());
            return paramView;


        }

        public void setData(List<Map<String, Object>> paramList)
        {
            this.list = paramList;
            notifyDataSetChanged();
        }
    }

