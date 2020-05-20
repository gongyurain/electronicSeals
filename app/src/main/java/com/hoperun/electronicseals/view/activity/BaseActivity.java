package com.hoperun.electronicseals.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhouwei.library.CustomPopWindow;
import com.hoperun.electronicseals.R;
import com.hoperun.electronicseals.contract.BaseContract;
import com.hoperun.electronicseals.qrcodemoduel.QRCodeBaseActivity;
import com.hoperun.electronicseals.utils.SharedPreferencesUtil;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import androidx.appcompat.app.ActionBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public abstract class BaseActivity extends RxAppCompatActivity implements BaseContract.BaseView {
    Unbinder unBind;

    BaseContract.BasePresenter basePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutView());
        unBind = ButterKnife.bind(this);
        basePresenter = initPresenter();
        if (basePresenter != null) {
            basePresenter.attachView(this);
        }
        SharedPreferencesUtil.getInstance(this, "data");
        initData();
        initView();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unBind.unbind();
        if (basePresenter != null) {
            basePresenter.detchView();
        }
    }

    public abstract void initListener();

    public abstract void initData();

    public abstract void initView();

    public abstract int getLayoutView();

    public abstract BaseContract.BasePresenter initPresenter();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void buildCustomActionBar(String title, boolean hasBackButton, boolean hasSettingButton) {
        View viewTitleBar = getLayoutInflater().inflate(R.layout.actionbar_view, null);
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(viewTitleBar, lp);
        actionBar.setDisplayShowHomeEnabled(false); //去掉导航
        actionBar.setDisplayShowTitleEnabled(false);//去掉标题
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        TextView tvTitle = (TextView) actionBar.getCustomView().findViewById(R.id.action_bar_title);
        tvTitle.setText(title);
        LinearLayout leftImageButton = (LinearLayout) actionBar.getCustomView().findViewById(R.id.action_bar_left_btn);
        if (!hasBackButton) {
            leftImageButton.setVisibility(View.INVISIBLE);
        } else {
            leftImageButton.setVisibility(View.VISIBLE);
            leftImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseActivity.this.finish();
                }
            });
        }

        ImageButton rightButton = (ImageButton) actionBar.getCustomView().findViewById(R.id.action_bar_right_btn);
        if (!hasSettingButton) {
            rightButton.setVisibility(View.INVISIBLE);
        } else {
            rightButton.setVisibility(View.VISIBLE);
            rightButton.setOnClickListener(this::addBtnClick);
        }
    }

    CustomPopWindow mCustomPopWindow;

    public void addBtnClick(View view) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_layout1, null);
        //处理popWindow 显示内容
        handleLogic(contentView);
        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)//显示的布局，还可以通过设置一个View
                .setFocusable(true)//是否获取焦点，默认为ture
                .setOutsideTouchable(true)//是否PopupWindow 以外触摸dissmiss
                .create()//创建PopupWindow
                .showAsDropDown(view, 0, 10);//显示PopupWindow
    }

    private void handleLogic(View contentView) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCustomPopWindow != null) {
                    mCustomPopWindow.dissmiss();
                }
                String showContent = "";
                switch (v.getId()) {
                    case R.id.tv_ble:
                        DiscoverActivity.start(BaseActivity.this);
                        break;
                    case R.id.tv_map:
                        showContent = "点击 Item菜单2";
                        Toast.makeText(BaseActivity.this, showContent, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.tv_qrcode:
                        Intent intent = new Intent(BaseActivity.this, CaptureActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        };
        contentView.findViewById(R.id.tv_ble).setOnClickListener(listener);
        contentView.findViewById(R.id.tv_map).setOnClickListener(listener);
        contentView.findViewById(R.id.tv_qrcode).setOnClickListener(listener);
    }
}
