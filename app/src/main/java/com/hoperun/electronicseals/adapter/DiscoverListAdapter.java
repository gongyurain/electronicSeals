package com.hoperun.electronicseals.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.hoperun.electronicseals.R;
import com.inuker.bluetooth.library.beacon.Beacon;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.search.SearchResult;

import java.util.List;

public class DiscoverListAdapter extends BaseAdapter {
    private static final String TAG = DiscoverListAdapter.class.getSimpleName();

    public static class Item {
        public SearchResult searchResult;
        public BleGattProfile profile;

        public Item(SearchResult searchResult, BleGattProfile profile) {
            this.searchResult = searchResult;
            this.profile = profile;
        }

        public String getAddress() {
            return searchResult.getAddress();
        }

        public String getName() {
            return searchResult.getName();
        }

        public Beacon getBeacon(){
            return new Beacon(searchResult.scanRecord);
        }

        public String getProfile() {
            return profile == null ? null : profile.toString();
        }
    }

    private LayoutInflater mLayoutInflater;
    private List<Item> mList;
    View.OnClickListener buttClickListener;

    public DiscoverListAdapter(LayoutInflater inflater, View.OnClickListener buttClickListener) {
        mLayoutInflater = inflater;
        this.buttClickListener = buttClickListener;
    }

    public void setData(List<Item> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Item getItem(int position) {
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
            holder.prof = convertView.findViewById(R.id.prof);

            holder.conn.setOnClickListener(buttClickListener);
            convertView.setOnClickListener(v -> {
                ViewHolder holder2 = (ViewHolder) v.getTag();
                holder2.expand = !holder2.expand;
                holder2.desc.setVisibility( holder2.expand ? View.VISIBLE : View.GONE);
                holder2.prof.setVisibility( holder2.expand ? View.VISIBLE : View.GONE);
            });

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Item item = getItem(position);
        if (item != null) {
            holder.name.setText(item.getName());
            holder.addr.setText(item.getAddress());
            holder.desc.setText("扫描信息：\n"+item.getBeacon());
            holder.prof.setText("服务描述：\n"+item.getProfile());
            holder.conn.setTag(item);
            holder.desc.setVisibility( holder.expand ? View.VISIBLE : View.GONE);
            holder.prof.setVisibility( holder.expand ? View.VISIBLE : View.GONE);
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
        public TextView prof;
        public Button conn;
        public boolean expand = false;
    }
}

