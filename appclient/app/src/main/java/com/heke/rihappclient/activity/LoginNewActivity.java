package com.heke.rihappclient.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.heke.rihappclient.R;
import com.heke.rihappclient.adapter.SelectOperatorAdapter;
import com.heke.rihappclient.application.BaseActivity;
import com.heke.rihappclient.model.dacdiaobodan1;
import com.heke.rihappclient.model.userinfo;
import com.heke.rihappclient.shareprefence.ShareprefenceBean;
import com.heke.rihappclient.utils.JellyInterpolator;
import com.lzy.okgo.OkGo;

;import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LoginNewActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener,AdapterView.OnItemClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        this.mSharedPreferences = getSharedPreferences(ShareprefenceBean.SHAREPREFENCE_NAME, 0);
        this.initView();
        this.Init();
    }
    public void initView()
    {
        mBtnLogin = (TextView) findViewById(R.id.main_btn_login);
        progress = findViewById(R.id.layout_progress);
        mInputLayout = findViewById(R.id.input_layout);
        mName = (LinearLayout) findViewById(R.id.input_layout_name);
        mOrganization=(LinearLayout)findViewById(R.id.input_layout_organization);
        mPsw = (LinearLayout) findViewById(R.id.input_layout_psw);
        mBtnLogin.setOnClickListener(this);
    }
    private void Init(){
        loaddata();
    }

    private void loaddata() {
        dacdiaobodan1 ddbd=new dacdiaobodan1();
        ddbd.goodsName="绝缘半成品";
        sheblist.add(ddbd);
        dacdiaobodan1 ddbd1=new dacdiaobodan1();
        ddbd1.goodsName="绝缘半成品";
        sheblist.add(ddbd1);
        mMyAdapter=new SelectOperatorAdapter(sheblist,LoginNewActivity.this);
//        pullDownMenu=(PullDownMenu)findViewById(R.id.tv_pullDown_sczx);
//        List<String> stringList=new ArrayList<>();
//        stringList.add("鼓楼");
//        stringList.add("数学");
//        stringList.add("英语");
//        stringList.add("物理");
//        stringList.add("物理");
//        pullDownMenu.setData("选择",stringList,false);

    }

    @Override
    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.main_btn_login:
                // 计算出控件的高与宽
                mWidth = mBtnLogin.getMeasuredWidth();
                mHeight = mBtnLogin.getMeasuredHeight();
                // 隐藏输入框
                mName.setVisibility(View.INVISIBLE);
                mPsw.setVisibility(View.INVISIBLE);
                mOrganization.setVisibility(View.INVISIBLE);

                inputAnimator(mInputLayout, mWidth, mHeight);
                final Intent intt=new Intent(this,Main2Activity.class);
                Timer timer=new Timer();
                TimerTask ttsk=new TimerTask() {
                    @Override
                    public void run() {
                        startActivity(intt);
                        finish();
                    }
                };
                timer.schedule(ttsk,3000);
                break;
        }
    }

    /**
     * 输入框的动画效果
     *
     * @param view
     *            控件
     * @param w
     *            宽
     * @param h
     *            高
     */
    private void inputAnimator(final View view, float w, float h) {

        AnimatorSet set = new AnimatorSet();//实例化动画集合

        ValueAnimator animator = ValueAnimator.ofFloat(0, w);//创建动画实例
        //添加监听
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view
                        .getLayoutParams();
                params.leftMargin = (int) value;
                params.rightMargin = (int) value;
                view.setLayoutParams(params);
            }
        });

        //第一个参数：指定执行动画的控件，第二个参数：指定控件的属性，第三个参数是可变长参数
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout,
                "scaleX", 1f, 0.5f);//scaleX:缩放动画：在x轴缩放

        set.setDuration(1000);//设置一个时长
        //Interpolator 被用来修饰动画效果，定义动画的变化率
        set.setInterpolator(new AccelerateDecelerateInterpolator());//在动画开始与结束的地方速率改变比较慢，在中间的时候加速
        set.playTogether(animator, animator2);//添加一组动画，播放顺序为一起播放
        set.start();
        set.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                /**
                 * 动画结束后，先显示加载的动画，然后再隐藏输入框
                 */
                progress.setVisibility(View.VISIBLE);
                progressAnimator(progress);
                mInputLayout.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });

    }

    /**
     * 出现进度动画
     *
     * @param view
     */
    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);//组合动画
        animator3.setDuration(1000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();

    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode==101) {
            Init();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @SuppressWarnings("unchecked")

    private void setUserInfo(userinfo data) {

    }
    public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean)
    {

    }

    private TextView mBtnLogin;

    private View progress;
    private View mInputLayout;
    private ListView mlistView;
    private float mWidth, mHeight;
    private List<dacdiaobodan1> sheblist=new ArrayList<dacdiaobodan1>();
    private SelectOperatorAdapter mMyAdapter;
    private LinearLayout mName, mPsw;
    private LinearLayout mOrganization;
    private PullDownMenu pullDownMenu;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Activity销毁时，取消网络请求
        OkGo.getInstance().cancelTag(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
