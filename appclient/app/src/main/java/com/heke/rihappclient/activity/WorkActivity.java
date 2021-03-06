package com.heke.rihappclient.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.heke.rihappclient.R;
import com.heke.rihappclient.WebService.AppService;
import com.heke.rihappclient.adapter.GridViewAdapter;
import com.heke.rihappclient.fragment.SettingFragment;
import com.heke.rihappclient.model.userinfo;
import com.heke.rihappclient.shareprefence.ShareprefenceBean;
import com.heke.rihappclient.view.TagsGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkActivity extends Fragment implements AdapterView.OnItemClickListener {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.currentuser = AppService.getInstance().getCurrentUser();
    }
    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
    {
        return paramLayoutInflater.inflate(R.layout.activity_work, null);
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
        switchFunctionItem(((Map)((Adapter)paramAdapterView.getAdapter()).getItem(paramInt)).get("name").toString());
    }
    @Override
    public void onAttach(Activity paramActivity)
    {
        super.onAttach(paramActivity);
        this.context = paramActivity;
    }
    public void onActivityCreated(Bundle paramBundle)
    {
        super.onActivityCreated(paramBundle);
        this.gridview = ((TagsGridView)getView().findViewById(R.id.gridview));
        this.gridview.setOnItemClickListener(this);
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        ((Activity)this.context).getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        this.mGridViewAdapter = new GridViewAdapter(this.context, localDisplayMetrics.widthPixels);
        this.gridview.setAdapter(this.mGridViewAdapter);
        new functionAsyncTask().execute(new Void[0]);
    }
    public void onStart()
    {
        super.onStart();
    }

    class functionAsyncTask extends AsyncTask<Void, Void, List<Map<String, Object>>>
    {
        functionAsyncTask()
        {
        }

        protected List<Map<String, Object>> doInBackground(Void[] paramArrayOfVoid)
        {
            ArrayList localArrayList = new ArrayList();
//            String[] arrayOfString = {  "采购申请", "采购审核" ,"采购收货确认","按单结算","库存初始化","库存盘点","商品销售","销售审核","大仓调拨","调拨收货确认","通知公告","系统设置"};
//            int[] arrayOfInt = { R.mipmap.ic_follow_record, R.mipmap.ic_follow_record_complete ,R.mipmap.ic_sales_target_issued,R.mipmap.ic_dbdcommit,R.mipmap.ic_sales_report_collect,R.mipmap.ic_scanner_outbund,R.mipmap.ic_sales_report_statistics, R.mipmap.ic_follow_record_complete,R.mipmap.ic_inventory_report_statistics,R.mipmap.ic_sales_target_issued,R.mipmap.ic_tool_data_report,R.mipmap.ic_displayer};
            String[] arrayOfString = {  "采购管理", "销售管理" ,"仓库管理","数字车间","质检管理","设备管理"};
            int[] arrayOfInt = { R.mipmap.ic_follow_record_complete, R.mipmap.ic_follow_record ,R.mipmap.ic_inventory_report_statistics,R.mipmap.ic_dbdcommit,R.mipmap.ic_sales_target_issued,R.mipmap.ic_tool_data_report};

            for (int i = 0; i<6; i++)
            {
                HashMap localHashMap = new HashMap();
                localHashMap.put("name", arrayOfString[i]);
                localHashMap.put("image", Integer.valueOf(arrayOfInt[i]));
                localArrayList.add(localHashMap);
            }
            return localArrayList;
        }

        protected void onPostExecute(List<Map<String, Object>> paramList)
        {
            super.onPostExecute(paramList);
            WorkActivity.this.mGridViewAdapter.setData(paramList);
        }
    }

    public void switchFunctionItem(String paramString) {
        Intent localIntent = new Intent();
        if (paramString.equals("采购管理")) {
            localIntent.setClass(this.context,PlantMainActivity.class);
            localIntent.putExtra("type", "1");
            startActivity(localIntent);
            return;
        }
        if (paramString.equals("销售管理")) {
            localIntent.setClass(this.context,PlantMainActivity.class);
            localIntent.putExtra("type", "2");
            startActivity(localIntent);
            return;
        }
        if (paramString.equals("仓库管理")) {
            localIntent.setClass(this.context,PlantMainActivity.class);
            localIntent.putExtra("type", "3");
            startActivity(localIntent);
            return;
        }
        if (paramString.equals("数字车间")) {
            localIntent.setClass(this.context,TaskIndexActivity.class);
            localIntent.putExtra("systemtype", "1");
            startActivity(localIntent);
            return;
        }
        if (paramString.equals("质检管理")) {
//            Toast.makeText(this.context, "待加入！", Toast.LENGTH_SHORT).show();
            localIntent.setClass(this.context,PlantMainActivity.class);
            localIntent.putExtra("type", "4");
            startActivity(localIntent);
            return;
        }
        if (paramString.equals("设备管理")) {
            localIntent.setClass(this.context,PlantMainActivity.class);
            localIntent.putExtra("type", "5");
            startActivity(localIntent);
            return;
        }
        if (paramString.equals("叉车呼叫")) {
            localIntent.setClass(this.context,Test1.class);
            localIntent.putExtra("type", "6");
            startActivity(localIntent);
            return;
        }


    }

    private Context context;
    private TagsGridView gridview;
    private GridViewAdapter mGridViewAdapter;
    private ProgressDialog pro_dialog;
    private TextView titleTxtView;
    private static final String TAG = "WorkFragment";
    private userinfo currentuser;

}
