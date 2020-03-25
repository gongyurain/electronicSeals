package com.hoperun.electronicseals.model;

import com.hoperun.electronicseals.contract.LoginContract;

import java.util.HashMap;

public class LoginModel implements LoginContract.LoginModel {
    @Override
    public int login(String name, String password) {
        return 0;
    }

    @Override
    public int save(String name, String password) {
        return 0;
    }

    @Override
    public HashMap<String, String> get() {
        return null;
    }
}
