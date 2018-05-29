package com.heke.rihappclient.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.heke.rihappclient.R;
import com.heke.rihappclient.model.dacdiaobodan1;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/10/9.
 */

public class SelectOperatorAdapter extends BaseAdapter {
    public List<dacdiaobodan1> data;
    private Context context;

    public SelectOperatorAdapter(List<dacdiaobodan1> data, Context context) {
        this.data = data;
        this.context = context;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * @param list
     */
    public void updateListView(List<dacdiaobodan1> list) {
        if (list == null) {
            this.data = new ArrayList<>();
        } else {
            this.data = list;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHoder hd;
        if (view == null) {
            hd = new ViewHoder();
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.operatoritem_list, null);
            hd.textView = (TextView) view.findViewById(R.id.text_title);
            hd.checkBox = (CheckBox) view.findViewById(R.id.ckb);
            view.setTag(hd);
        }
        dacdiaobodan1 mModel = data.get(i);
        hd = (ViewHoder) view.getTag();
        hd.textView.setText(mModel.goodsName);

        Log.e("myadapter", mModel.goodsName + "------" + mModel.ischeck());
        final ViewHoder hdFinal = hd;
        hd.checkBox.setChecked(mModel.ischeck());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //监听每个item，把checkbox都为未选中状态
                for (dacdiaobodan1 model : data) {
                    if (model.ischeck()) {
                        model.setIscheck(false);
                    }
                    notifyDataSetChanged();
                }
                CheckBox checkBox = hdFinal.checkBox;
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                    data.get(i).setIscheck(false);
                } else {
                    checkBox.setChecked(true);
                    data.get(i).setIscheck(true);
                }
            }
        });


        return view;
    }

    class ViewHoder {
        TextView textView;
        CheckBox checkBox;
    }

}