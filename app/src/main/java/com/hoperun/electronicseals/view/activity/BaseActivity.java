package com.hoperun.electronicseals.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hoperun.electronicseals.R;
import com.hoperun.electronicseals.contract.BaseContract;
import com.hoperun.electronicseals.qrcodemoduel.QRCodeBaseActivity;
import com.hoperun.electronicseals.utils.SharedPreferencesUtil;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import androidx.appcompat.app.ActionBar;
import butterknife.ButterKnife;
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
        //todo 可以在这里添加点击事件 弹出popupwindow
        ImageButton rightButton = (ImageButton) actionBar.getCustomView().findViewById(R.id.action_bar_right_btn);
        if (!hasSettingButton) {
            rightButton.setVisibility(View.INVISIBLE);
        } else {
            rightButton.setVisibility(View.VISIBLE);
            rightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BaseActivity.this, CaptureActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}
