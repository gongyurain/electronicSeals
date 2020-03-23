package com.hoperun.electronicseals.view;

import com.hoperun.electronicseals.R;
import com.hoperun.electronicseals.contract.BaseContract;
import com.hoperun.electronicseals.contract.LoginContract;
import com.hoperun.electronicseals.presenter.LoginPresenter;

public class LoginActivity extends BaseActivity implements LoginContract.LoginView {

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
}
