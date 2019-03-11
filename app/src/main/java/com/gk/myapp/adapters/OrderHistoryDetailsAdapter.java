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
import com.gk.myapp.model.OrderHistoryResponse;
import com.gk.myapp.utils.C;

import java.util.List;

/**
 * Created by Gk on 04-02-2018.
 */
public class OrderHistoryDetailsAdapter extends BaseAdapter {
    private Context mContext;
    private List<OrderHistoryResponse.OrderDetails> mList;
    private LayoutInflater inf;

    public OrderHistoryDetailsAdapter(Context mContext, List<OrderHistoryResponse.OrderDetails> mList) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        final OrderHistoryResponse.OrderDetails obj = mList.get(position);
        if (convertView == null) {
            convertView = inf.inflate(R.layout.list_item_order_history_details, parent, false);
            holder = new ViewHolder();
            holder.iv= (ImageView) convertView.findViewById(R.id.iv);
            holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
            holder.txtDesc = (TextView) convertView.findViewById(R.id.txt_desc);
            holder.txtWeight = (TextView) convertView.findViewById(R.id.txt_weight);
            holder.txtQuantity = (TextView) convertView.findViewById(R.id.txt_quantity);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position % 2 == 0) {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.list_grey));
        } else {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.list_green));
        }


        if (obj.getProductImage() != null && !obj.getProductImage().equals("")) {
            Glide.with(mContext)
                    .load(C.BASE_URL_PRODUCT_IMAGES + obj.getProductImage()).placeholder(R.mipmap.dummy_image)
                    .into(holder.iv);
        } else {
            holder.iv.setImageResource(0);
        }
        holder.txtName.setText(obj.getProductName());
        holder.txtDesc.setText(obj.getProductDesc());
        holder.txtQuantity.setText("Quantity: " + obj.getQuantity());
        holder.txtWeight.setText(obj.getWeight() + C.KG);


        return convertView;
    }


    class ViewHolder {
        ImageView iv;
        TextView txtName, txtDesc, txtWeight, txtQuantity;

    }
}
