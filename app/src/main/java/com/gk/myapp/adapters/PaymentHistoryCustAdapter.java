package com.gk.myapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gk.myapp.R;
import com.gk.myapp.model.PaymentHistoryModel;
import com.gk.myapp.utils.C;

import java.util.ArrayList;

/**
 * Created by Gk on 12-09-2017.
 */
public class PaymentHistoryCustAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<PaymentHistoryModel> mList;

    public PaymentHistoryCustAdapter(Context mContext, ArrayList<PaymentHistoryModel> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public PaymentHistoryModel getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        PaymentHistoryModel obj = mList.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item_payment_history_cust, parent, false);
            holder.txtDate = (TextView) convertView.findViewById(R.id.txt_date);
            holder.txtAmount = (TextView) convertView.findViewById(R.id.txt_amount);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtAmount.setText(obj.getAmount());
        holder.txtDate.setText(obj.getDate());

        return convertView;
    }

    class ViewHolder {
        TextView txtDate, txtAmount;
    }
}
