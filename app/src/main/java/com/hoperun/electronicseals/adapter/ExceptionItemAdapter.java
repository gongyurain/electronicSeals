package com.hoperun.electronicseals.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hoperun.electronicseals.R;
import com.hoperun.electronicseals.bean.DeviceList;
import com.hoperun.electronicseals.bean.ExceptionItemNode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ExceptionItemAdapter extends BaseAdapter {
    private Context context;
    private List<DeviceList> nodes = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public ExceptionItemAdapter(Context context, List<DeviceList> nodes){
        this.context = context;
        if(nodes!=null) {
            this.nodes = nodes;
        }
        layoutInflater = LayoutInflater.from(context);
    }

    public void updateViews(List<DeviceList> nodes){
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
        DeviceList node = nodes.get(position);
        if(view==null){
            viewHolder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.item_exception, null);
            viewHolder.sealIdTV = view.findViewById(R.id.box_seal_id_tv);
            viewHolder.sealTimeTV = view.findViewById(R.id.box_seal_time_tv);
            viewHolder.sealTypeTV = view.findViewById(R.id.box_seal_type_tv);
            viewHolder.sealInfoTV = view.findViewById(R.id.box_seal_info_tv);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.sealIdTV.setText(node.getSN());

        String timeText=format.format(node.getGMT());
        viewHolder.sealTimeTV.setText(timeText);
        if (node.getEV().equals(0)) {
            viewHolder.sealTypeTV.setText("设备电量不足");
        } else if(node.getEV().equals(1)) {
            viewHolder.sealTypeTV.setText("设备被移动");
        } else if(node.getEV().equals(2)) {
            viewHolder.sealTypeTV.setText("设备加封");
        } else if(node.getEV().equals(3)) {
            viewHolder.sealTypeTV.setText("设备已解封");
        }
        viewHolder.sealInfoTV.setText((String)node.getDATA());
        return view;
    }

    class ViewHolder {
        TextView sealIdTV;
        TextView sealTimeTV;
        TextView sealTypeTV;
        TextView sealInfoTV;
    }
}
