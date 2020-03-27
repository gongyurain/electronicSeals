package com.hoperun.electronicseals.model;

import com.hoperun.electronicseals.contract.LoginContract;
import com.hoperun.electronicseals.utils.SharedPreferencesUtil;

import java.util.HashMap;

import static com.hoperun.electronicseals.presenter.LoginPresenter.FAILED;
import static com.hoperun.electronicseals.presenter.LoginPresenter.NULL;
import static com.hoperun.electronicseals.presenter.LoginPresenter.SUCCESS;

public class LoginModel implements LoginContract.LoginModel {

    @Override
    public int login(String name, String password) {
        if (name == null || password == null) {
            return FAILED;
        }
        if (name.equals("") || password.equals("")) {
            return NULL;
        }
        if (name.equals("admin") && password.equals("admin")) {
            return SUCCESS;
        }
        return 0;
    }

    @Override
    public int save(String name, String password) {
        SharedPreferencesUtil.putData("name", name);
        SharedPreferencesUtil.putData("password", password);
        return SUCCESS;
    }

    @Override
    public HashMap<String, String> get() {
        Object name = SharedPreferencesUtil.getData("name", FAILED);
        Object password = SharedPreferencesUtil.getData("password", FAILED);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", (String) name);
        hashMap.put("password", (String) password);
        return hashMap;
    }
}
