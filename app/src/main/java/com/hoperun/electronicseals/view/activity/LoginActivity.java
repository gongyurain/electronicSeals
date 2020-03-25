package com.hoperun.electronicseals.view.activity;

import com.hoperun.electronicseals.R;
import com.hoperun.electronicseals.contract.BaseContract;
import com.hoperun.electronicseals.contract.LoginContract;
import com.hoperun.electronicseals.presenter.LoginPresenter;
import com.hoperun.electronicseals.view.activity.BaseActivity;

public class LoginActivity extends BaseActivity implements LoginContract.LoginView {

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
    }

    @Override
    public void initView() {

    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_splash;
    }

    @Override
    public BaseContract.BasePresenter initPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void showLoginProgess() {

    }

    @Override
    public void hideLoginProgess() {

    }

    @Override
    public void showLoginResult(int state) {

    }
}
