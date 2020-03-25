package com.hoperun.electronicseals.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoperun.electronicseals.contract.BaseContract;
import com.trello.rxlifecycle.components.support.RxFragment;


import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends RxFragment implements BaseContract.BaseView {
    public BaseContract.BasePresenter basePresenter;

    private View rootView;
    Unbinder unbinder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = View.inflate(getContext(), getLayoutViewId(), null);
        unbinder= ButterKnife.bind(this, rootView);
        if (basePresenter != null) {
            basePresenter.attachView(this);
        }
        initView();
        initData();
        initListener();
        return rootView;
    }

    public abstract void initListener();

    public abstract void initData();

    public abstract void initView();

    public abstract int getLayoutViewId();

    public abstract BaseContract.BasePresenter createPresenter();

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();

    }
}
