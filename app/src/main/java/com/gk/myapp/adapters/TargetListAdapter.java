package com.gk.myapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gk.myapp.R;
import com.gk.myapp.model.TargetModel;

import java.util.ArrayList;

/**
 * Created by Gk on 17-12-2016.
 */
public class TargetListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<TargetModel> mList;
    private LayoutInflater inf;

    public TargetListAdapter(Context mContext, ArrayList<TargetModel> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inf = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 10;
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
        if (convertView == null) {
            convertView = inf.inflate(R.layout.list_item_target, parent, false);
            holder = new ViewHolder();
            holder.txtTargetDate = (TextView) convertView.findViewById(R.id.txt_date);
            holder.txtStatus = (TextView) convertView.findViewById(R.id.txt_status);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position % 2 == 0) {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.list_grey));
        } else {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.list_green));
        }

        return convertView;
    }


    class ViewHolder {
        TextView txtTargetDate, txtStatus;
    }
}
