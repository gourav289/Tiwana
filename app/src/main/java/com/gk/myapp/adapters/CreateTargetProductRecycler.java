package com.gk.myapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.gk.myapp.R;
import com.gk.myapp.model.ProductDetailModel;
import com.gk.myapp.utils.CircleImageView;

import java.util.List;

/**
 * Created by Gk on 25-12-2016.
 */
public class CreateTargetProductRecycler extends RecyclerView.Adapter<CreateTargetProductRecycler.MyViewHolder> {

    private List<ProductDetailModel> mList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView ivProfile;
        TextView txtName;
        EditText  txtQuantityKg, txtQuantityNo;

        public MyViewHolder(View view) {
            super(view);
            ivProfile= (CircleImageView) view.findViewById(R.id.iv_profile_pic);
            txtName = (TextView) view.findViewById(R.id.txt_name);
            txtQuantityKg = (EditText) view.findViewById(R.id.txt_quantity_kg);
            txtQuantityNo = (EditText) view.findViewById(R.id.txt_quantity_number);
        }
    }


    public CreateTargetProductRecycler(Context mContext,List<ProductDetailModel> mList) {
        this.mList = mList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_create_target_product, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ProductDetailModel obj = mList.get(position);
//        holder.ivProfile
        holder.txtName.setText(obj.getProductName());
        holder.txtQuantityKg.setText(obj.getQuantityKg());
        holder.txtQuantityNo.setText(obj.getQuantityNo());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}