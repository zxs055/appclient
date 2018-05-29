package com.heke.rihappclient.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.heke.rihappclient.R;
import com.heke.rihappclient.XListView.XListView;
import com.heke.rihappclient.application.BaseActivity;
import com.heke.rihappclient.model.rukuinfo;
import com.heke.rihappclient.shareprefence.ShareprefenceBean;

import java.util.List;

public class HPChoseActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hpchose);
        this.mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, 0);
    }
    public void initControl() {
        this.backImageButton = ((ImageButton) findViewById(R.id.back));
        //this.titleTextView = ((TextView) findViewById(R.id.title));
        this.scanImageButton = ((ImageButton) findViewById(R.id.scanImgBtn));
        this.mEditText1 = ((EditText) findViewById(R.id.listtext1));
        this.mXListView1 = ((XListView) findViewById(R.id.hplist1));
        this.jianhuokuangLayout = ((FrameLayout) findViewById(R.id.add_hp));
        this.comfirmBtn = ((TextView) findViewById(R.id.show_hp));
        this.mLinearLayout = ((LinearLayout) findViewById(R.id.tabpager));
        this.numbershowTxtView = ((TextView) findViewById(R.id.numbershow));
        this.searchDelBtn = ((ImageView) findViewById(R.id.del_cha));
        this.searchDelBtn.setOnClickListener(this);
        this.backImageButton.setOnClickListener(this);
        this.scanImageButton.setOnClickListener(this);
        this.mXListView1.setPullLoadEnable(true);
        this.mXListView1.setPullRefreshEnable(true);
        //this.mXListView1.setXListViewListener(this);
        this.jianhuokuangLayout.setOnClickListener(this);
        this.comfirmBtn.setOnClickListener(this);
        this.mEditText1.addTextChangedListener(this);
        this.mEditText1.setOnClickListener(this);
        this.mEditText1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView paramTextView, int paramInt, KeyEvent paramKeyEvent) {
                if ((paramInt == 3) || ((paramKeyEvent != null) && (paramKeyEvent.getKeyCode() == 66))) {
                }
                return false;
            }
        });
    }
    public void onClick(View paramView) {
        Intent localIntent = new Intent();
        switch (paramView.getId()) {

        }
    }
    public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
    {
        if (paramCharSequence.length() > 0)
        {
            this.searchDelBtn.setVisibility(View.VISIBLE);
            return;
        }
        this.searchDelBtn.setVisibility(View.GONE);
    }
    public void afterTextChanged(Editable paramEditable)
    {
        paramEditable.toString().replace("'", "");
        this.searchlist.clear();
        this.HplistAdapter.notifyDataSetChanged();
    }

    public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
    {
    }
    private class MyAdapter extends BaseAdapter
    {
        View[] itemViews;
        public MyAdapter(List<rukuinfo> mlistInfo)
        {
            itemViews = new View[mlistInfo.size()];
            for(int i=0;i<mlistInfo.size();i++){
                rukuinfo getInfo=(rukuinfo)mlistInfo.get(i);	//获取第i个对象
                //调用makeItemView，实例化一个Item
                itemViews[i]=makeItemView(
                        getInfo
                );
            }
        }

        public int getCount()
        {
            return this.itemViews.length;
        }

        public Object getItem(int paramInt)
        {
            return this.itemViews[paramInt];
        }

        public long getItemId(int paramInt)
        {
            return paramInt;
        }
        //绘制Item的函数
        private View makeItemView(rukuinfo pinfo) {
            LayoutInflater inflater = (LayoutInflater) HPChoseActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // 使用View的对象itemView与R.layout.item关联
            View itemView = inflater.inflate(R.layout.hplistitem, null);

            // 通过findViewById()方法实例R.layout.item内各组件
            TextView invcode = (TextView) itemView.findViewById(R.id.itemcode);
            invcode.setText(pinfo.invcode);	//填入相应的值
            TextView invname = (TextView) itemView.findViewById(R.id.itemname);
            invname.setText(pinfo.invname);
            TextView invspec = (TextView) itemView.findViewById(R.id.itemgg);
            invspec.setText(pinfo.invspec);
            TextView rknum = (TextView) itemView.findViewById(R.id.itemnum);
            rknum.setText(pinfo.kucnum);
            return itemView;
        }
        public View getView(int position, View paramView, ViewGroup paramViewGroup)
        {
            if (paramView == null)
                return itemViews[position];
            return paramView;
        }

        public void setData(List<rukuinfo> paramList)
        {
            searchlist = paramList;
            notifyDataSetChanged();
        }
    }
    private ImageButton backImageButton;
    private TextView comfirmBtn;
    private LayoutInflater inflater;
    private FrameLayout jianhuokuangLayout;
    private EditText mEditText1;
    private LinearLayout mLinearLayout;
    private XListView mXListView1;
    private ImageButton scanImageButton;
    private ProgressDialog proDialog;
    private Button searchBtn2;
    private ImageView searchDelBtn;
    private String numString = "";
    private TextView numbershowTxtView;
    private TextView titleTextView;
    private List<rukuinfo> searchlist;
    private HPChoseActivity.MyAdapter HplistAdapter;
}
