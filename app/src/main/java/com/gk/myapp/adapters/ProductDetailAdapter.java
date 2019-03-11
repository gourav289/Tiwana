package com.gk.myapp.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gk.myapp.R;
import com.gk.myapp.model.ProductDetailModel;
import com.gk.myapp.utils.C;
import com.gk.myapp.utils.CircleImageView;

import java.util.ArrayList;

/**
 * Created by Gk on 18-12-2016.
 */
public class ProductDetailAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ProductDetailModel> mList;
    private LayoutInflater inf;

    public ProductDetailAdapter(Context mContext, ArrayList<ProductDetailModel> mList) {
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
        ProductDetailModel obj = mList.get(position);
        if (convertView == null) {
            convertView = inf.inflate(R.layout.list_item_product_detail, parent, false);
            holder = new ViewHolder();
            holder.ivProfile = (CircleImageView) convertView.findViewById(R.id.iv_profile_pic);
            holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
            holder.txtDesc = (TextView) convertView.findViewById(R.id.txt_quantity);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position % 2 == 0) {
            convertView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_grey));
        } else {
            convertView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_green));
        }

        holder.txtName.setText(obj.getProductName());
        if (obj.getProductImage() != null && !obj.getProductImage().equals(""))
            Glide.with(mContext)
                    .load(C.BASE_URL_PRODUCT_IMAGES + obj.getProductImage())
                    .into(holder.ivProfile);
        else
            holder.ivProfile.setBackgroundResource(0);

        holder.txtDesc.setText(obj.getSoldQuantity()+" KG sold out of "+obj.getTotalQuantity()+C.KG);

        return convertView;
    }


    class ViewHolder {
        CircleImageView ivProfile;
        TextView txtName, txtDesc;
    }
}
