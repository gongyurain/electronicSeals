package com.hoperun.electronicseals.contract;

import java.util.HashMap;

public interface LoginContract {
    public interface LoginView extends BaseContract.BaseView{
        void showLoginProgess();

        void hideLoginProgess();

        void showLoginResult(int state);
    }

    public interface LoginModel extends BaseContract.BaseModel{
        int login(String name, String password);

        int save(String name, String password);

        HashMap<String, String> get();
    }

    public abstract class LoginPresenter extends BaseContract.BasePresenter<LoginView,LoginModel>{
        public abstract void loginDo(String name, String password);

        public abstract void savePassword(String name, String password);

        public abstract HashMap<String, String> getPassword();
    }
}
