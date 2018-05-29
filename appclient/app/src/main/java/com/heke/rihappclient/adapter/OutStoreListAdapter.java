package com.heke.rihappclient.adapter;

import android.animation.IntArrayEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.heke.rihappclient.R;
import com.heke.rihappclient.model.KL_outstore_model_detail;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wgmhx on 2017/5/2.
 */

public class OutStoreListAdapter extends BaseAdapter {
    private Context context;
    private List<KL_outstore_model_detail> list;

    public OutStoreListAdapter(Context paramContext, List<KL_outstore_model_detail> paramList)
    {
        this.context = paramContext;
        this.list = paramList;
    }
    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * @param list
     */
    public void updateListView(List<KL_outstore_model_detail> list) {
        if (list == null) {
            this.list = new ArrayList<KL_outstore_model_detail>();
        } else {
            this.list = list;
        }
        notifyDataSetChanged();
    }
    public int getCount()
    {
        return this.list.size();
    }

    public KL_outstore_model_detail getItem(int paramInt)
    {
        return (KL_outstore_model_detail)this.list.get(paramInt);
    }

    public long getItemId(int paramInt)
    {
        return paramInt;
    }

    public List<KL_outstore_model_detail> getList()
    {
        return this.list;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
        OutStoreListAdapter.ViewHolder localViewHolder=null;
        if (null == paramView)
        {
            localViewHolder = new OutStoreListAdapter.ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            paramView = mInflater.inflate(R.layout.item_kl_outstore_list_details, null);
            localViewHolder.tv_jsStandard=(TextView)paramView.findViewById(R.id.tv_jsStandard);
            localViewHolder.tv_jsorderno=(TextView)paramView.findViewById(R.id.tv_jsorderno);
            localViewHolder.tv_batchno=(TextView)paramView.findViewById(R.id.tv_batchno);
            localViewHolder.tv_outStockQty=(TextView)paramView.findViewById(R.id.tv_outStockQty);
            localViewHolder.tv_storeP=(TextView)paramView.findViewById(R.id.tv_storeP);
            localViewHolder.tv_caijiQty=(TextView)paramView.findViewById(R.id.tv_caijiQty);
            localViewHolder.lin_layout=(LinearLayout)paramView.findViewById(R.id.lin_layout);
            paramView.setTag(localViewHolder);
        }
        else
        {
            localViewHolder = (OutStoreListAdapter.ViewHolder) paramView.getTag();
        }
        // set item values to the viewHolder:
        KL_outstore_model_detail markerItem = getItem(paramInt);
        if (null != markerItem)
        {
            double newQty=0;double oldQty=0;
            if(markerItem.outStockQty!=null&&!markerItem.outStockQty.equals("")){
                oldQty=new Double(markerItem.outStockQty);
            }
            if(markerItem.caijiQty!=null&&!markerItem.caijiQty.equals("")){
                newQty= new Double(markerItem.caijiQty);
            }
            localViewHolder.tv_jsStandard.setText(markerItem.jsStandard);
            localViewHolder.tv_jsorderno.setText(markerItem.jsorderno);
            localViewHolder.tv_batchno.setText(markerItem.batchno);
            localViewHolder.tv_outStockQty.setText(markerItem.outStockQty);
            localViewHolder.tv_caijiQty.setText(markerItem.caijiQty);
            localViewHolder.tv_storeP.setText(markerItem.storeP);

            if(newQty==oldQty){
                localViewHolder.lin_layout.setBackgroundColor(Color.parseColor("#00ff00"));
            }else{
                localViewHolder.lin_layout.setBackgroundColor(Color.parseColor("#ffffff"));
            }
        }
        return paramView;
    }

    public void setList(List<KL_outstore_model_detail> paramList)
    {
        this.list = paramList;
    }

    private class ViewHolder
    {
        TextView tv_jsStandard;
        TextView tv_jsorderno;
        TextView tv_batchno;
        TextView tv_outStockQty;
        TextView tv_caijiQty;
        TextView tv_storeP;


        LinearLayout lin_layout;
        private ViewHolder()
        {
        }
    }
}
