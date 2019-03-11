package com.gk.myapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gk.myapp.R;
import com.gk.myapp.model.DealerTargetList;
import com.gk.myapp.model.LeaveDetailModel;
import com.gk.myapp.utils.C;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gk on 31-01-2018.
 */
public class MonthListAdapter extends BaseAdapter {
    private Context mContext;
    private List<DealerTargetList.MonthList> mList;
    private LayoutInflater inf;

    public MonthListAdapter(Context mContext, List<DealerTargetList.MonthList> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inf = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        final DealerTargetList.MonthList obj=mList.get(position);
        if (convertView == null) {
            convertView = inf.inflate(R.layout.list_item_month, parent, false);
            holder = new ViewHolder();
            holder.txtMonth = (TextView) convertView.findViewById(R.id.txt_month);
            holder.txtStatus = (TextView) convertView.findViewById(R.id.txt_status);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtMonth.setText(obj.getMonthDetail());
        holder.txtStatus.setText(obj.getTargetStatus());


        return convertView;
    }

    public void notifyList(ArrayList<DealerTargetList.MonthList> staffLeave) {
        this.mList=staffLeave;
        notifyDataSetChanged();
    }


    class ViewHolder {
        TextView txtMonth,  txtStatus;
    }

}
