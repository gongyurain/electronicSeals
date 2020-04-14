package com.hoperun.electronicseals.view.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ganxin.library.LoadDataLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hoperun.electronicseals.R;
import com.hoperun.electronicseals.adapter.ExceptionItemAdapter;
import com.hoperun.electronicseals.bean.DeviceList;
import com.hoperun.electronicseals.contract.BaseContract;
import com.hoperun.electronicseals.service.IMqttCallBack;
import com.hoperun.electronicseals.view.activity.CaptureActivity;
import com.hoperun.electronicseals.view.activity.ExceptionInfoActivity;
import com.hoperun.electronicseals.view.activity.MainActivity;
import com.hoperun.electronicseals.wiget.pullableview.PullToRefreshLayout;
import com.hoperun.electronicseals.wiget.pullableview.PullableListView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.OnClick;

public class SearchFragment extends BaseFragment {
    @BindView(R.id.item_status1_tv)
    TextView status1TV;
    @BindView(R.id.title_tv)
    TextView titleTV;
    @BindView(R.id.item_status1_v)
    View status1V;
    @BindView(R.id.item_status2_tv)
    TextView status2TV;
    @BindView(R.id.item_status2_v)
    View status2V;
    @BindView(R.id.item_status3_tv)
    TextView status3TV;
    @BindView(R.id.item_status3_v)
    View status3V;
    @BindView(R.id.refreshview)
    PullToRefreshLayout pullRL;
    @BindView(R.id.listview)
    PullableListView listView;
    @BindView(R.id.no_data_iv)
    ImageView noDataIV;
    @BindView(R.id.loadDataLayout)
    LoadDataLayout loadDataLayout;
    private ExceptionItemAdapter sealItemAdapter;
    private List<DeviceList> sealItemNodes = new ArrayList<>();
    private int itemIndex = 1;
    private String dealStatus;
    private MainActivity activity;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            List<DeviceList> itemNodes = new ArrayList<>();
            if (itemNodes.size() == 0) {
                loadDataLayout.setStatus(LoadDataLayout.EMPTY);
                return;
            }
            itemNodes.addAll((List<DeviceList>)msg.obj);
            sealItemNodes.addAll(itemNodes);
            if(sealItemAdapter!=null) {
                sealItemAdapter.updateViews(sealItemNodes);
            }
            loadDataLayout.setStatus(LoadDataLayout.SUCCESS);
        }
    };
    @OnClick({R.id.item_status1_tv, R.id.item_status2_tv, R.id.item_status3_tv})
    public void itemStatusClick(View view){

        switch (view.getId()){
            case R.id.item_status1_tv:
                if(itemIndex==1)
                    return;
                itemIndex = 1;
                dealStatus = null;
                setStatusItemV(1);
                getProblemItemsData();
                break;
            case R.id.item_status2_tv:
                if(itemIndex==2)
                    return;
                itemIndex = 2;
                dealStatus = "0";
                setStatusItemV(2);
                loadDataLayout.setStatus(LoadDataLayout.EMPTY);
                break;
            case R.id.item_status3_tv:
                if(itemIndex==3)
                    return;
                itemIndex = 3;
                dealStatus = "1";
                setStatusItemV(3);
                loadDataLayout.setStatus(LoadDataLayout.EMPTY);
                break;
        }
    }

    @OnClick(R.id.add_iv)
    public void addBtnClick(View view){
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivity(intent);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        activity = (MainActivity) getActivity();
        loadDataLayout.setEmptyImage(R.mipmap.icon_no_data);
        pullRL.isPullDown(false);
        pullRL.isPullUp(false);
        sealItemAdapter = new ExceptionItemAdapter(getContext(), sealItemNodes);
        listView.setAdapter(sealItemAdapter);
        listView.setOnItemClickListener(sealItemClick);
        getProblemItemsData();
    }

    private AdapterView.OnItemClickListener sealItemClick = (parent, view, position, id) -> {
        DeviceList node = sealItemNodes.get(position);
        Intent intent = new Intent(getActivity(), ExceptionInfoActivity.class);
        intent.putExtra("Id", node.getId());
        startActivity(intent);
    };

    private void setStatusItemV(int index){
        status1TV.setTextColor(getResources().getColor(R.color.grayDarkX));
        status2TV.setTextColor(getResources().getColor(R.color.grayDarkX));
        status3TV.setTextColor(getResources().getColor(R.color.grayDarkX));
        status1V.setVisibility(View.INVISIBLE);
        status2V.setVisibility(View.INVISIBLE);
        status3V.setVisibility(View.INVISIBLE);
        switch (index){
            case 1:
                status1TV.setTextColor(getResources().getColor(R.color.redDark));
                status1V.setVisibility(View.VISIBLE);
                break;
            case 2:
                status2TV.setTextColor(getResources().getColor(R.color.redDark));
                status2V.setVisibility(View.VISIBLE);
                break;
            case 3:
                status3TV.setTextColor(getResources().getColor(R.color.redDark));
                status3V.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void getProblemItemsData(){
        loadDataLayout.setStatus(LoadDataLayout.LOADING);
        activity.getDeviceInfo(new IMqttCallBack() {
            @Override
            public void messageArrived(String topic, String message, int qos) {
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<DeviceList>>(){}.getType();
                List<DeviceList> lists = gson.fromJson(message, type);
                Message message1 = Message.obtain();
                message1.obj = lists;
                handler.sendMessage(message1);
            }

            @Override
            public void connectionLost(Throwable arg0) {
                loadDataLayout.setStatus(LoadDataLayout.ERROR);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken arg0) {

            }
        });
    }


    @Override
    public int getLayoutViewId() {
        return R.layout.activity_exceptions;
    }

    @Override
    public BaseContract.BasePresenter createPresenter() {
        return null;
    }
}
