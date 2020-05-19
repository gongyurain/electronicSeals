package com.hoperun.electronicseals.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.hoperun.electronicseals.R;
import com.hoperun.electronicseals.contract.BaseContract;
import com.hoperun.electronicseals.contract.LoginContract;
import com.hoperun.electronicseals.presenter.LoginPresenter;
import com.hoperun.electronicseals.utils.ClickUtils;
import com.hoperun.electronicseals.utils.SharedPreferencesUtil;
import com.ljs.lovelytoast.LovelyToast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

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
            switch (msg.what) {
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
        if (isRemember) {
            String name = (String) SharedPreferencesUtil.getData("name", "");
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
        ButterKnife.bind(this);
//        initP();
        iniPermission();
    }

//    /**
//     * 申请权限
//     */
//    private void initP() {
//        if (android.os.Build.VERSION.SDK_INT >= 23) {
//            if (ContextCompat.checkSelfPermission(LoginActivity.this,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                //请求权限
//                ActivityCompat.requestPermissions(LoginActivity.this, new String[]{
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA,},
//                        1);
//                //判断是否需要 向用户解释，为什么要申请该权限
//                if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                    Toast.makeText(LoginActivity.this, "shouldShowRequestPermissionRationale", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//
//    }

    private void iniPermission() {
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            List<String> permissionList = new ArrayList<>();
            if (ContextCompat.checkSelfPermission(LoginActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (ContextCompat.checkSelfPermission(LoginActivity.this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.CAMERA);
            }
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.
                    permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.
                    permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.
                    permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (!permissionList.isEmpty()) {
                String[] permissions = permissionList.toArray(new String[permissionList.size()]);
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
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
            SharedPreferencesUtil.putData("remember_password", false);
        }
        ((LoginPresenter) basePresenter).loginDo(mName.getText().toString(),
                mPassword.getText().toString());
    }

    public void remPassword() {
        SharedPreferencesUtil.putData("remember_password", true);
        ((LoginPresenter) basePresenter).savePassword(mName.getText().toString(),
                mPassword.getText().toString());
    }
}
