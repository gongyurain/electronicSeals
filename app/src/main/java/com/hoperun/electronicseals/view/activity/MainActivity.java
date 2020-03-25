package com.hoperun.electronicseals.view.activity;

import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.hoperun.electronicseals.R;
import com.hoperun.electronicseals.contract.BaseContract;
import com.hoperun.electronicseals.view.fragment.OperateFragment;
import com.hoperun.electronicseals.view.fragment.SearchFragment;
import com.hoperun.electronicseals.view.fragment.UserFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    @BindView(R.id.radiogroup)
    RadioGroup radioGroup;

    List<Fragment> fragments;
    private Fragment preFragment;

    private boolean isExit = false;

    @Override
    public void initListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.operate_tab:
                        changefragment(0);
                        break;
                    case R.id.search_tab:
                        changefragment(1);
                        break;
                    case R.id.user_tab:
                        changefragment(2);
                        break;
                }
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        fragments = new ArrayList<Fragment>();
        fragments.add(new OperateFragment());
        fragments.add(new SearchFragment());
        fragments.add(new UserFragment());
        changefragment(0);
    }

    protected void changefragment(int index) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        Fragment fragment = fragments.get(index);
        if (preFragment != null) {
            transaction.hide(preFragment);
        }
        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.framelayout, fragment);
        }
        transaction.commit();
        preFragment = fragment;
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_main;
    }

    @Override
    public BaseContract.BasePresenter initPresenter() {
        return null;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            exit();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    isExit = false;
                    break;
            }
        }
    };

    public void exit(){
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            handler.sendEmptyMessageDelayed(1, 2000);
        } else {
            finish();
        }
    }
}