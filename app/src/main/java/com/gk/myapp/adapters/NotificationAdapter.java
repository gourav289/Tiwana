package com.gk.myapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gk.myapp.R;
import com.gk.myapp.model.NotificationModel;

import java.util.ArrayList;

/**
 * Created by Gk on 18-12-2016.
 */
public class NotificationAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<NotificationModel> mList;
    private LayoutInflater inf;

    public NotificationAdapter(Context mContext, ArrayList<NotificationModel> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inf = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void notifyList(ArrayList<NotificationModel> mList){
        this.mList=mList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        NotificationModel obj=mList.get(position);
        if (convertView == null) {
            convertView = inf.inflate(R.layout.list_item_sales_notif, parent, false);
            holder = new ViewHolder();
            holder.txtDesc = (TextView) convertView.findViewById(R.id.txt_desc);
            holder.txtDate = (TextView) convertView.findViewById(R.id.txt_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtDate.setText(obj.getDate());
        holder.txtDesc.setText(obj.getMessage());

        return convertView;
    }


    class ViewHolder {
        TextView  txtDesc, txtDate;
    }
}
