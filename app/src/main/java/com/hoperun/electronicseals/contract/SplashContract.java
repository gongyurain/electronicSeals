package com.hoperun.electronicseals.contract;

public interface SplashContract {
    interface SplashView extends BaseContract.BaseView {}

    interface SplashModel extends BaseContract.BaseModel {}

    public abstract class SplashPresenter extends BaseContract.BasePresenter<SplashView,
            SplashModel> {}
}
