package com.hoperun.electronicseals.view.activity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hoperun.electronicseals.R;
import com.hoperun.electronicseals.bean.DeviceEventDetailResp;
import com.hoperun.electronicseals.contract.BaseContract;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExceptionInfoActivity extends BaseActivity {
    @BindView(R.id.box_status_tv)
    TextView boxStatusTv;
    @BindView(R.id.box_seal_time_tv)
    TextView boxSealTimeTv;
    @BindView(R.id.box_seal_addr_tv)
    TextView boxSealAddrTv;
    @BindView(R.id.expt_type_tv)
    TextView exptTypeTv;
    @BindView(R.id.expt_desc_tv)
    TextView exptDescTv;
    @BindView(R.id.expt_pics_ll)
    LinearLayout exptPicsLl;
    @BindView(R.id.issuing_unit)
    TextView issuingUnit;
    @BindView(R.id.sealing_up_reason)
    TextView sealingUpReason;
    @BindView(R.id.unsealing_method)
    TextView unsealingMethod;
    @BindView(R.id.unsealing_time)
    TextView unsealingTime;
    @BindView(R.id.executee_info)
    TextView executeeInfo;
    @BindView(R.id.create_time)
    TextView createTime;
    @BindView(R.id.failure_time)
    TextView failureTime;

    private DeviceEventDetailResp info;
    @BindView(R.id.title_tv)
    TextView titleTv;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        //Message{id=0, topic='/smartseal/s2c/eventinfo', body='{"id":1,"sn":"864480040662891",
        // "time":1586795195000,"addr":"西安市莲湖区永安路91号","type":"1","creator":"西安市莲湖区人民法院","sealReason":"房贷逾期","sealTime":1586848581735,"unsealMethod":"归还房贷",
        // "unsealTime":0,"targetInfo":"张三，男，18700000091","createTime":0,"invalidTime":0}'}
    }

    @Override
    public void initView() {
        titleTv.setText("电子封条详情");
        if (info != null) {
            executeeInfo.setText(info.getTargetInfo());
            String tvInvalidTime = format.format(info.getInvalidTime());
            failureTime.setText(tvInvalidTime);
            String tvCreateTime = format.format(info.getCreateTime());
            createTime.setText(tvCreateTime);
            String tvUnsealingTime = format.format(info.getUnsealTime());
            unsealingTime.setText(tvUnsealingTime);
            unsealingMethod.setText(info.getUnsealMethod());
            sealingUpReason.setText(info.getSealReason());
            issuingUnit.setText(info.getCreator());
            boxSealAddrTv.setText(info.getAddr());
            String tvBoxSealTimeTv = format.format(info.getTime());
            boxSealTimeTv.setText(tvBoxSealTimeTv);
            boxStatusTv.setText(info.getSn());
        }
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
        info = (DeviceEventDetailResp) getIntent().getSerializableExtra("info");
    }
}
