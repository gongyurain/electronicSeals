package com.hoperun.electronicseals.presenter;

import com.hoperun.electronicseals.contract.LoginContract;
import com.hoperun.electronicseals.model.LoginModel;

import java.util.HashMap;

public class LoginPresenter extends LoginContract.LoginPresenter {
    public static final int SUCCESS = 1;

    public static final int FAILED = 0;

    public static final int NULL = -1;

    @Override
    protected LoginContract.LoginModel creatModel() {
        return new LoginModel();
    }

    @Override
    public void loginDo(String name, String password) {
        view.showLoginProgess();
        int loginResult = model.login(name, password);
        view.hideLoginProgess();
        view.showLoginResult(loginResult);
    }

    @Override
    public void savePassword(String name, String password) {
        int save = model.save(name, password);
    }

    @Override
    public HashMap<String, String> getPassword() {
        return model.get();
    }
}
