package com.hoperun.electronicseals.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.hoperun.electronicseals.R;
import com.inuker.bluetooth.library.beacon.Beacon;
import com.inuker.bluetooth.library.search.SearchResult;

import java.util.List;

public class ListAdapter extends BaseAdapter {
    private static final String TAG = ListAdapter.class.getSimpleName();

    private LayoutInflater mLayoutInflater;
    private List<SearchResult> mList;
    View.OnClickListener buttClickListener;

    public ListAdapter(LayoutInflater inflater, View.OnClickListener buttClickListener) {
        mLayoutInflater = inflater;
        this.buttClickListener = buttClickListener;
    }

    public void setData(List<SearchResult> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public SearchResult getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.discover_list_item, parent, false);
            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.name);
            holder.addr = convertView.findViewById(R.id.addr);
            holder.desc = convertView.findViewById(R.id.desc);
            holder.conn = convertView.findViewById(R.id.connect);

            holder.conn.setOnClickListener(buttClickListener);
            convertView.setOnClickListener(v -> {
                ViewHolder holder2 = (ViewHolder) v.getTag();
                holder2.desc.setVisibility(holder2.desc.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            });

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SearchResult item = getItem(position);
        if (item != null) {
            holder.name.setText((item.getName() == null ||
                    item.getName().equalsIgnoreCase("NULL")) ? "未知" : item.getName());
            holder.addr.setText(item.getAddress());
            holder.desc.setText(new Beacon(item.scanRecord).toString());
            holder.conn.setTag(item);
//            int connStatus = BleService.getInstance().getClient().getConnectStatus(item.getAddress());
//            switch (connStatus){
//                case STATUS_DEVICE_DISCONNECTED:
//                case STATUS_UNKNOWN:
//                    holder.conn.setText("连接");
//                    holder.conn.setEnabled(true);
//                    break;
//                case STATUS_DEVICE_CONNECTED:
//                case STATUS_DEVICE_CONNECTING:
//                case STATUS_DEVICE_DISCONNECTING:
//                default:
//                    holder.conn.setText("已连接");
//                    holder.conn.setEnabled(false);
//                    break;
//            }
        }
        return convertView;
    }

    private class ViewHolder {
        public TextView name;
        public TextView addr;
        public TextView desc;
        public Button conn;
    }
}

