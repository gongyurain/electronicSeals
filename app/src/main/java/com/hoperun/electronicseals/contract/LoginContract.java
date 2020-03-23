package com.hoperun.electronicseals.contract;

public interface LoginContract {
    public interface LoginView extends BaseContract.BaseView{
        public void initView();
    }

    public interface LoginModel extends BaseContract.BaseModel{
    }

    public abstract class LoginPresenter extends BaseContract.BasePresenter<LoginView,LoginModel>{
    }
}
