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
import com.heke.rihappclient.model.KL_outstore_model;
import com.heke.rihappclient.model.KL_outstore_model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wgmhx on 2017/5/2.
 */

public class KLOutStoreListAdapter extends BaseAdapter {
    private Context context;
    private List<KL_outstore_model> list;

    public KLOutStoreListAdapter(Context paramContext, List<KL_outstore_model> paramList)
    {
        this.context = paramContext;
        this.list = paramList;
    }
    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * @param list
     */
    public void updateListView(List<KL_outstore_model> list) {
        if (list == null) {
            this.list = new ArrayList<KL_outstore_model>();
        } else {
            this.list = list;
        }
        notifyDataSetChanged();
    }
    public int getCount()
    {
        return this.list.size();
    }

    public KL_outstore_model getItem(int paramInt)
    {
        return (KL_outstore_model)this.list.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
        return paramInt;
    }

    public List<KL_outstore_model> getList()
    {
        return this.list;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
        KLOutStoreListAdapter.ViewHolder localViewHolder=null;
        if (null == paramView)
        {
            localViewHolder = new KLOutStoreListAdapter.ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            paramView = mInflater.inflate(R.layout.item_kl_outstore_list, null);
            localViewHolder.tv_billcode = ((TextView)paramView.findViewById(R.id.tv_billcode));
            localViewHolder.tv_billstate = ((TextView)paramView.findViewById(R.id.tv_billstate));
            localViewHolder.tv_client = ((TextView)paramView.findViewById(R.id.tv_client));
            localViewHolder.lin_layout=(LinearLayout)paramView.findViewById(R.id.lin_layout);
            paramView.setTag(localViewHolder);
        }
        else
        {
            localViewHolder = (KLOutStoreListAdapter.ViewHolder) paramView.getTag();
        }
        // set item values to the viewHolder:
        KL_outstore_model markerItem = getItem(paramInt);
        if (null != markerItem)
        {
            localViewHolder.tv_billcode.setText(markerItem.BillNo);
            localViewHolder.tv_billstate.setText(markerItem.StatusName);
            localViewHolder.tv_client.setText(markerItem.Name);
            if(false){
                localViewHolder.lin_layout.setBackgroundColor(Color.parseColor("#00ff00"));
            }else{
                localViewHolder.lin_layout.setBackgroundColor(Color.parseColor("#ffffff"));
            }
        }
        return paramView;
    }

    public void setList(List<KL_outstore_model> paramList)
    {
        this.list = paramList;
    }

    private class ViewHolder
    {
        TextView tv_billcode;
        TextView tv_billstate;
        TextView tv_client;
        LinearLayout lin_layout;
        private ViewHolder()
        {
        }
    }
}
