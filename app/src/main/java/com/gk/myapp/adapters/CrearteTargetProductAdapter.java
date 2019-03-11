package com.gk.myapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gk.myapp.R;
import com.gk.myapp.interfaces.QuantityDialogListener;
import com.gk.myapp.model.ProductDetailModel;
import com.gk.myapp.utils.CircleImageView;
import com.gk.myapp.utils.U;

import java.util.ArrayList;

/**
 * Created by Gk on 18-12-2016.
 */
public class CrearteTargetProductAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ProductDetailModel> mList;
    private LayoutInflater inf;

    public CrearteTargetProductAdapter(Context mContext, ArrayList<ProductDetailModel> mList) {
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
        ProductDetailModel obj = mList.get(position);
        if (convertView == null) {
            convertView = inf.inflate(R.layout.list_item_create_target_product, parent, false);
            holder = new ViewHolder();
            holder.ivProfile = (CircleImageView) convertView.findViewById(R.id.iv_profile_pic);
            holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
            holder.txtQuantityKg = (TextView) convertView.findViewById(R.id.txt_quantity_kg);
            holder.txtQuantityNo = (TextView) convertView.findViewById(R.id.txt_quantity_number);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtQuantityKg.setText(obj.getQuantityKg());
        holder.txtQuantityNo.setText(obj.getQuantityNo());

        holder.txtQuantityNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                U.showEdDialog(mContext, "Quantity in Number", position, "createtarget", new QuantityDialogListener() {
                    @Override
                    public void onOkClicked(int pos, String type, String text) {
                        mList.get(pos).setQuantityNo(text);
                        notifyDataSetChanged();
                    }
                });
            }
        });

        holder.txtQuantityKg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                U.showEdDialog(mContext, "Quantity in KG", position, "createtarget", new QuantityDialogListener() {
                    @Override
                    public void onOkClicked(int pos, String type, String text) {
                        mList.get(pos).setQuantityKg(text);
                        notifyDataSetChanged();
                    }
                });
            }
        });

        return convertView;
    }


    class ViewHolder {
        CircleImageView ivProfile;
        TextView txtName, txtQuantityKg, txtQuantityNo;
    }
}
