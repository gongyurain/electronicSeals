package com.hoperun.electronicseals.view.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoperun.electronicseals.R;
import com.hoperun.electronicseals.adapter.ExceptionItemAdapter;
import com.hoperun.electronicseals.bean.ExceptionItemNode;
import com.hoperun.electronicseals.contract.BaseContract;
import com.hoperun.electronicseals.view.activity.CaptureActivity;
import com.hoperun.electronicseals.wiget.pullableview.PullToRefreshLayout;
import com.hoperun.electronicseals.wiget.pullableview.PullableListView;

import java.util.ArrayList;
import java.util.List;

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

    private PullRefreshListener pullRefreshListener;
    private ExceptionItemAdapter sealItemAdapter;
    private List<ExceptionItemNode> sealItemNodes = new ArrayList<>();
    private int itemIndex = 1;
    private int pageIndex = 0;
    private int pageSize = 10;
    private String dealStatus;

    @OnClick({R.id.item_status1_tv, R.id.item_status2_tv, R.id.item_status3_tv})
    public void itemStatusClick(View view){

        switch (view.getId()){
            case R.id.item_status1_tv:
                if(itemIndex==1)
                    return;
                itemIndex = 1;
                dealStatus = null;
                setStatusItemV(1);
                break;
            case R.id.item_status2_tv:
                if(itemIndex==2)
                    return;
                itemIndex = 2;
                dealStatus = "0";
                setStatusItemV(2);
                break;
            case R.id.item_status3_tv:
                if(itemIndex==3)
                    return;
                itemIndex = 3;
                dealStatus = "1";
                setStatusItemV(3);
                break;
        }
        pageIndex = 0;
        getProblemItemsData();
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
        pullRL.isPullDown(false);
        pullRL.isPullUp(false);
        pullRefreshListener = new PullRefreshListener();
        pullRL.setOnRefreshListener(pullRefreshListener);
        sealItemAdapter = new ExceptionItemAdapter(getContext(), sealItemNodes);
        listView.setAdapter(sealItemAdapter);
        listView.setOnItemClickListener(sealItemClick);
        pageIndex = 0;
        getProblemItemsData();
    }

    private AdapterView.OnItemClickListener sealItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            ExceptionItemNode node = sealItemNodes.get(position);
//            Intent intent = new Intent(ExceptionsActivity.this, ExceptionInfoActivity.class);
//            intent.putExtra("Id", node.getId());
//            startActivity(intent);
        }
    };

    private class PullRefreshListener implements PullToRefreshLayout.OnRefreshListener{
        private PullToRefreshLayout refreshLayout, loadLayout;

        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
            refreshLayout = pullToRefreshLayout;
            pageIndex = 0;
            getProblemItemsData();
        }

        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            loadLayout = pullToRefreshLayout;
            pageIndex++;
            getProblemItemsData();
        }

        public void closeRefreshLoad(){
            if(refreshLayout!=null)
                refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            if(loadLayout!=null)
                loadLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        }
    }

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
        List<ExceptionItemNode> itemNodes = new ArrayList<>();
        for (int i = 0; i <= 10; i++) {
            ExceptionItemNode exceptionItemNode = new ExceptionItemNode();
            exceptionItemNode.setSealId("12342355464");
            exceptionItemNode.setDealStatus(1);
            exceptionItemNode.setCreateTime(1582119l);
            exceptionItemNode.setSealOperName("gongyu");
            exceptionItemNode.setSealLoca("北京市海淀区");
            itemNodes.add(exceptionItemNode);
        }
        if(pullRefreshListener!=null)
            pullRefreshListener.closeRefreshLoad();
        if(pageIndex==0)
            sealItemNodes = itemNodes;
        else
            sealItemNodes.addAll(itemNodes);
        if(sealItemAdapter!=null)
            sealItemAdapter.updateViews(sealItemNodes);

        if(itemNodes.size()>0)
            pullRL.isPullDown(true);
        else
            pullRL.isPullDown(false);

        if(itemNodes.size() < pageSize*(pageIndex+1))
            pullRL.isPullUp(false);
        else
            pullRL.isPullUp(true);

        if(sealItemNodes!=null&&sealItemNodes.size()!=0){
            noDataIV.setVisibility(View.GONE);
        }else {
            noDataIV.setVisibility(View.VISIBLE);
        }
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
