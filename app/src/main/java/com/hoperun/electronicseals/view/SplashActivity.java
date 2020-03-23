package com.hoperun.electronicseals.view;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.hoperun.electronicseals.R;
import com.hoperun.electronicseals.contract.BaseContract;
import com.hoperun.electronicseals.presenter.SplashPresenter;

public class SplashActivity extends BaseActivity{

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
//                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                    finish();
                    break;
            }
        }
    };

    @Override
    public void initData() {
        handler.sendEmptyMessageAtTime(1, 3000);
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
        return new SplashPresenter();
    }
}
