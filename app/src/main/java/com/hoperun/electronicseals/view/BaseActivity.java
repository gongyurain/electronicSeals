package com.hoperun.electronicseals.view;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.hoperun.electronicseals.contract.BaseContract;
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
        basePresenter.attachView(this);
        initData();
        initView();

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
        basePresenter.detchView();
    }

    public abstract void initData();

    public abstract void initView();

    public abstract int getLayoutView();

    public abstract BaseContract.BasePresenter initPresenter();

}
