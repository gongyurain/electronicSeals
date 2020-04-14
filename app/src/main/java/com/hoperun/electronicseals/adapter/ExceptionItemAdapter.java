package com.hoperun.electronicseals.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hoperun.electronicseals.R;
import com.hoperun.electronicseals.bean.DeviceEventResp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ExceptionItemAdapter extends BaseAdapter {
    private Context context;
    private List<DeviceEventResp> nodes = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public ExceptionItemAdapter(Context context, List<DeviceEventResp> nodes){
        this.context = context;
        if(nodes!=null) {
            this.nodes = nodes;
        }
        layoutInflater = LayoutInflater.from(context);
    }

    public void updateViews(List<DeviceEventResp> nodes){
        if(nodes==null) {
            nodes = new ArrayList<>();
        }
        this.nodes = nodes;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return nodes.size();
    }

    @Override
    public Object getItem(int position) {
        return nodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        DeviceEventResp node = nodes.get(position);
        if(view==null){
            viewHolder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.item_exception, null);
            viewHolder.sealIdTV = view.findViewById(R.id.box_seal_id_tv);
            viewHolder.sealTimeTV = view.findViewById(R.id.box_seal_time_tv);
            viewHolder.sealAddressTV = view.findViewById(R.id.box_seal_addresss_tv);
            viewHolder.sealInfoTV = view.findViewById(R.id.box_seal_info_tv);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.sealIdTV.setText(node.getSn());

        String timeText=format.format(node.getTime());
        viewHolder.sealTimeTV.setText(timeText);
        if (node.getType().equals(0)) {
            viewHolder.sealInfoTV.setText("设备电量不足");
        } else if(node.getType().equals(1)) {
            viewHolder.sealInfoTV.setText("设备被移动");
        } else if(node.getType().equals(2)) {
            viewHolder.sealInfoTV.setText("设备加封");
        } else if(node.getType().equals(3)) {
            viewHolder.sealInfoTV.setText("设备已解封");
        }
        viewHolder.sealAddressTV.setText(node.getAddr());
        return view;
    }

    class ViewHolder {
        TextView sealIdTV;
        TextView sealTimeTV;
        TextView sealAddressTV;
        TextView sealInfoTV;
    }
}
