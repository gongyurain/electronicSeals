package com.hoperun.electronicseals.view.activity;

import android.os.Bundle;

import com.hoperun.electronicseals.contract.BaseContract;
import com.hoperun.electronicseals.utils.SharedPreferencesUtil;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

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
        SharedPreferencesUtil.getInstance(this,"data");
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
}
