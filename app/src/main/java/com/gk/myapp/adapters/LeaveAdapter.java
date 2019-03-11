package com.gk.myapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gk.myapp.R;
import com.gk.myapp.model.LeaveDetailModel;
import com.gk.myapp.utils.C;

import java.util.ArrayList;

/**
 * Created by Gk on 18-12-2016.
 */
public class LeaveAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<LeaveDetailModel> mList;
    private LayoutInflater inf;
    private CancelLeaveListener mListener;

    public LeaveAdapter(Context mContext, ArrayList<LeaveDetailModel> mList, CancelLeaveListener mListener) {
        this.mContext = mContext;
        this.mList = mList;
        inf = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mListener = mListener;
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
        final LeaveDetailModel obj = mList.get(position);
        if (convertView == null) {
            convertView = inf.inflate(R.layout.list_item_leave, parent, false);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.txt_title);
            holder.txtStatus = (TextView) convertView.findViewById(R.id.txt_status);
            holder.txtDate = (TextView) convertView.findViewById(R.id.txt_date);
            holder.txtCancel = (TextView) convertView.findViewById(R.id.txt_cancel);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtTitle.setText(obj.getReason());
        holder.txtDate.setText(obj.getStartDate());
        if (obj.getStatus().equals(C.PENDING)) {
            holder.txtStatus.setText(mContext.getString(R.string.pending));
//            holder.txtStatus.setBackgroundResource(R.drawable.bg_leave_status_orange);
        } else if (obj.getStatus().equals(C.APPROVE)) {
            holder.txtStatus.setText(mContext.getText(R.string.approve));
//            holder.txtStatus.setBackgroundResource(R.drawable.bg_leave_status_green);
        } else if (obj.getStatus().equals(C.CANCEL)) {
            holder.txtStatus.setText(mContext.getText(R.string.rejected));
//            holder.txtStatus.setBackgroundResource(R.drawable.bg_leave_status_orange);
        }else if (obj.getStatus().equals(C.CANCEL_BY_ME)) {
            holder.txtStatus.setText(mContext.getText(R.string.cancelled));
//            holder.txtStatus.setBackgroundResource(R.drawable.bg_leave_status_orange);
        }

        if (obj.getStatus().equals(C.CANCEL)||obj.getStatus().equals(C.CANCEL_BY_ME)||obj.getStatus().equals(C.APPROVE))
            holder.txtCancel.setVisibility(View.GONE);
        else
            holder.txtCancel.setVisibility(View.VISIBLE);

        holder.txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null)
                    mListener.onCancelListener(obj);
            }
        });

        return convertView;
    }

    public void notifyData(ArrayList<LeaveDetailModel> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }


    class ViewHolder {
        TextView txtTitle, txtStatus, txtDate, txtCancel;
    }

    public interface CancelLeaveListener {
        void onCancelListener(LeaveDetailModel obj);
    }
}
