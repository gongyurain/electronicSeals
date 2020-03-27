package com.hoperun.electronicseals.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.hoperun.electronicseals.R;
import com.hoperun.electronicseals.contract.BaseContract;
import com.hoperun.electronicseals.contract.LoginContract;
import com.hoperun.electronicseals.presenter.LoginPresenter;
import com.hoperun.electronicseals.utils.ClickUtils;
import com.hoperun.electronicseals.utils.SharedPreferencesUtil;
import com.ljs.lovelytoast.LovelyToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hoperun.electronicseals.presenter.LoginPresenter.FAILED;
import static com.hoperun.electronicseals.presenter.LoginPresenter.NULL;

public class LoginActivity extends BaseActivity implements LoginContract.LoginView {

    @BindView(R.id.et_userName)
    EditText mName;

    @BindView(R.id.et_password)
    EditText mPassword;

    @BindView(R.id.cb_checkbox)
    CheckBox mCheckBox;

    @BindView(R.id.btn_login)
    Button mLogin;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };


    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        boolean isRemember = (boolean) SharedPreferencesUtil.getData("remember_password",
                false);
        if (isRemember){
            String name = (String) SharedPreferencesUtil.getData("name","");
            String password = (String) SharedPreferencesUtil.getData("password",
                    "");
            mName.setText(name);
            mPassword.setText(password);
            mCheckBox.setChecked(true);
        }
    }

    @Override
    public void initView() {

    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_login;
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
        if (state == NULL) {
            LovelyToast.makeText(this, " 请输入用户名和密码", LovelyToast.LENGTH_SHORT,
                    LovelyToast.SUCCESS);
        } else if (state == FAILED) {
            LovelyToast.makeText(this, " 登录失败", LovelyToast.LENGTH_SHORT,
                    LovelyToast.SUCCESS);
        } else {
            LovelyToast.makeText(this, " 登录成功", LovelyToast.LENGTH_SHORT,
                    LovelyToast.SUCCESS);
            handler.sendEmptyMessageDelayed(1, 2000);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_login)
    public void login() {
        if (ClickUtils.isFastClick()) {
            return;
        }
        if (mCheckBox.isChecked()) {
            remPassword();
        } else {
            ((LoginPresenter) basePresenter).savePassword("",
                    "");
            SharedPreferencesUtil.putData("remember_password",false);
        }
        ((LoginPresenter) basePresenter).loginDo(mName.getText().toString(),
                mPassword.getText().toString());
    }

    public void remPassword() {
        SharedPreferencesUtil.putData("remember_password",true);
        ((LoginPresenter) basePresenter).savePassword(mName.getText().toString(),
                mPassword.getText().toString());
    }
}
