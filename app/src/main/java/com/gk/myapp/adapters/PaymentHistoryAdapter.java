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
 * Created by Gk on 31-01-2017.
 */
public class PaymentHistoryAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<PaymentHistoryModel> mList;

    public PaymentHistoryAdapter(Context mContext, ArrayList<PaymentHistoryModel> mList) {
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
            convertView = mInflater.inflate(R.layout.list_item_payment_history, parent, false);
            holder.txtPaymentMode = (TextView) convertView.findViewById(R.id.txt_payment_mode);
            holder.txtTransId = (TextView) convertView.findViewById(R.id.txt_trans_id);
            holder.txtDate = (TextView) convertView.findViewById(R.id.txt_date);
            holder.txtReceivedFrom = (TextView) convertView.findViewById(R.id.txt_received_from);
            holder.txtAmount = (TextView) convertView.findViewById(R.id.txt_amount);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (obj.getTitle().equals(C.CASH))
            holder.txtPaymentMode.setText(mContext.getString(R.string.cash));
        else if (obj.getTitle().equals(C.CHEQUE))
            holder.txtPaymentMode.setText(mContext.getString(R.string.cheque));
        else if (obj.getTitle().equals(C.NEFT))
            holder.txtPaymentMode.setText(mContext.getString(R.string.neft));
        else if (obj.getTitle().equals(C.IMPS))
            holder.txtPaymentMode.setText(mContext.getString(R.string.imps));
        else if (obj.getTitle().equals(C.RTGS))
            holder.txtPaymentMode.setText(mContext.getString(R.string.rtgs));
        else if (obj.getTitle().equals(C.ONLINE))
            holder.txtPaymentMode.setText(mContext.getString(R.string.online));

        if (obj.getTitle().equals(C.CASH))
            holder.txtTransId.setVisibility(View.GONE);
        else
            holder.txtTransId.setVisibility(View.VISIBLE);

        holder.txtTransId.setText(obj.getTransactionId());
        holder.txtAmount.setText(obj.getAmount());
        holder.txtReceivedFrom.setText(obj.getPartyName());
        holder.txtDate.setText(obj.getDate());

        return convertView;
    }

    class ViewHolder {
        TextView txtPaymentMode, txtTransId, txtDate, txtReceivedFrom, txtAmount;
    }
}
