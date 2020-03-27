package com.hoperun.electronicseals.view.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.hoperun.electronicseals.R;
import com.hoperun.electronicseals.contract.BaseContract;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExceptionInfoActivity extends BaseActivity {
    @BindView(R.id.title_tv)
    TextView titleTv;

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        titleTv.setText("设备详情");
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_exception;
    }

    @Override
    public BaseContract.BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
