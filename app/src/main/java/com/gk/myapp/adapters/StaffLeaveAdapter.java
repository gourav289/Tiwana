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
 * Created by Gk on 25-12-2016.
 */
public class StaffLeaveAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<LeaveDetailModel> mList;
    private LayoutInflater inf;
    private AcceptRejectLeave mListener;

    public StaffLeaveAdapter(Context mContext, ArrayList<LeaveDetailModel> mList,AcceptRejectLeave mListener) {
        this.mContext = mContext;
        this.mList = mList;
        this.mListener=mListener;
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
        final LeaveDetailModel obj=mList.get(position);
        if (convertView == null) {
            convertView = inf.inflate(R.layout.list_item_staff_leave, parent, false);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.txt_title);
            holder.txtDate = (TextView) convertView.findViewById(R.id.txt_date);
            holder.txtApprove= (TextView) convertView.findViewById(R.id.txt_approve);
            holder.txtReject= (TextView) convertView.findViewById(R.id.txt_reject);
            holder.txtStatus= (TextView) convertView.findViewById(R.id.txt_status);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtTitle.setText(obj.getReason());
        holder.txtDate.setText(obj.getStartDate());

        if(obj.getStatus().equals(C.APPROVE)){
            holder.txtApprove.setVisibility(View.GONE);
            holder.txtReject.setVisibility(View.GONE);
            holder.txtStatus.setText(mContext.getString(R.string.approve));
            holder.txtStatus.setVisibility(View.VISIBLE);
        }else if(obj.getStatus().equals(C.CANCEL)){
            holder.txtApprove.setVisibility(View.GONE);
            holder.txtReject.setVisibility(View.GONE);
            holder.txtStatus.setText(mContext.getString(R.string.rejected));
        }else if(obj.getStatus().equals(C.CANCEL_BY_ME)){
            holder.txtApprove.setVisibility(View.GONE);
            holder.txtReject.setVisibility(View.GONE);
            holder.txtStatus.setText(mContext.getString(R.string.cancelled));
        }else{
            holder.txtApprove.setVisibility(View.VISIBLE);
            holder.txtReject.setVisibility(View.VISIBLE);
            holder.txtReject.setText(mContext.getString(R.string.reject));
            holder.txtApprove.setText(mContext.getString(R.string.approveee));
            holder.txtStatus.setText(mContext.getString(R.string.approve));
            holder.txtStatus.setVisibility(View.GONE);
        }


        holder.txtApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null && obj.getStatus().equals(C.PENDING))
                    mListener.onAcceptLeave(obj.getId());
            }
        });

        holder.txtReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null && obj.getStatus().equals(C.PENDING))
                    mListener.onRejectLeave(obj.getId());
            }
        });

        return convertView;
    }

    public void notifyList(ArrayList<LeaveDetailModel> staffLeave) {
        this.mList=staffLeave;
        notifyDataSetChanged();
    }


    class ViewHolder {
        TextView txtTitle,  txtDate,txtApprove,txtReject,txtStatus;
    }

    public interface AcceptRejectLeave{
        void onAcceptLeave(String leaveId);
        void onRejectLeave(String leaveId);
    }
}
