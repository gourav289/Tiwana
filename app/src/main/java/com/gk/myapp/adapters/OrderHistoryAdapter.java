package com.gk.myapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gk.myapp.R;
import com.gk.myapp.model.CartListResponse;
import com.gk.myapp.model.OrderHistoryResponse;
import com.gk.myapp.utils.C;
import com.gk.myapp.utils.P;

import java.util.List;

/**
 * Created by Gk on 04-02-2018.
 */
public class OrderHistoryAdapter extends BaseAdapter {
    private Context mContext;
    private List<OrderHistoryResponse.OrderHistoryList> mList;
    private LayoutInflater inf;
    private CancelOrderListener mListener;
    private int totalRecords = 0, page = 1, currentLoading = 1;
    private P mPreferences;

    public OrderHistoryAdapter(Context mContext, List<OrderHistoryResponse.OrderHistoryList> mList, CancelOrderListener mListener) {
        this.mContext = mContext;
        this.mList = mList;
        inf = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mListener = mListener;
        mPreferences = new P(mContext);
    }

    public void notifyDataChanged(int totalRecords, int page, List<OrderHistoryResponse.OrderHistoryList> mList) {
        this.mList = mList;
        this.totalRecords = totalRecords;
        this.page = page;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final OrderHistoryResponse.OrderHistoryList obj = mList.get(position);
        if (convertView == null) {
            convertView = inf.inflate(R.layout.list_item_order_history, parent, false);
            holder = new ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
            holder.txtPartyName = (TextView) convertView.findViewById(R.id.txt_party_name);
            holder.txtStatus = (TextView) convertView.findViewById(R.id.txt_status);
            holder.txtCancel = (TextView) convertView.findViewById(R.id.txt_cancel);
            holder.txtDate = (TextView) convertView.findViewById(R.id.txt_date);
            holder.txtquantity = (TextView) convertView.findViewById(R.id.txt_cancel);
            holder.txtProductname = (TextView) convertView.findViewById(R.id.txt_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        if (position % 2 == 0) {
//            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.list_grey));
//        } else {
//            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.list_green));
//        }

        holder.txtName.setText(obj.getName());
        holder.txtPartyName.setText(obj.getPartyName());

        if (obj.getStatus().equals(C.ORDER_PENDING)) {
            holder.txtCancel.setVisibility(View.VISIBLE);
            if (mPreferences.getStringPref(P.USER_TYPE).equals(C.CONSUMER) && obj.getCreatedFor().equals(mPreferences.getStringPref(P.USER_ID))) {
                holder.txtCancel.setVisibility(View.VISIBLE);
            } else {
                holder.txtCancel.setVisibility(View.GONE);
            }
        } else {
            holder.txtCancel.setVisibility(View.GONE);
        }



        if (obj.getStatus().equals(C.ORDER_PENDING))
            holder.txtStatus.setText("Your order is Pending");
        else if (obj.getStatus().equals(C.ORDER_COMPLETE))
            holder.txtStatus.setText("Your order has been Approved");
        else if (obj.getStatus().equals(C.ORDER_CANCELLED))
            holder.txtStatus.setText("Your order has been Cancelled");
        else
            holder.txtStatus.setText("");

        holder.txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onCancelOrder(obj.getOrderId());
                }
            }
        });
        holder.txtDate.setText(obj.getCreateDate());

        if (position == mList.size() - 1 && totalRecords < mList.size()) {   //1-all shipments
//            holder.mProgress.setVisibility(View.VISIBLE);
            if (mListener != null && page != currentLoading) {
                currentLoading = page;
                mListener.onLoadMore();
            }
        } else {
//            holder.mProgress.setVisibility(View.GONE);
        }

//        holder.txtProductname.setText(obj.getProductName());
//        holder.txtquantity.setText(obj.getQuantity());

        return convertView;
    }


    class ViewHolder {
        ImageView ivProfile;
        TextView txtName, txtPartyName, txtStatus, txtCancel, txtDate,txtProductname,txtquantity;
    }

    public interface CancelOrderListener {
        public void onCancelOrder(String orderId);

        public void onLoadMore();
    }

}
