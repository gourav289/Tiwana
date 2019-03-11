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
import com.gk.myapp.model.ProductDetailModel;
import com.gk.myapp.model.ProductListResponse;
import com.gk.myapp.utils.C;
import com.gk.myapp.utils.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gk on 10-09-2017.
 */
public class CartConsAdapter extends BaseAdapter {
    private Context mContext;
    private List<CartListResponse.CartDetails> mList;
    private LayoutInflater inf;
    private DeleteCartClick mListener;

    public CartConsAdapter(Context mContext, List<CartListResponse.CartDetails> mList, DeleteCartClick mListener) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        final CartListResponse.CartDetails obj = mList.get(position);
        if (convertView == null) {
            convertView = inf.inflate(R.layout.list_item_cart_cons, parent, false);
            holder = new ViewHolder();
            holder.ivProfile = (ImageView) convertView.findViewById(R.id.iv_profile_pic);
            holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
            holder.txtDesc = (TextView) convertView.findViewById(R.id.txt_desc);
            holder.txtWeight = (TextView) convertView.findViewById(R.id.txt_weight);
            holder.txtQuantity = (TextView) convertView.findViewById(R.id.txt_quantity);
            holder.txtDelete = (TextView) convertView.findViewById(R.id.txt_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position % 2 == 0) {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.list_grey));
        } else {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.list_green));
        }

        if (obj.getImage() != null && !obj.getImage().equals("")) {
            Glide.with(mContext)
                    .load(C.BASE_URL_PRODUCT_IMAGES + obj.getImage()).placeholder(R.mipmap.dummy_image)
                    .into(holder.ivProfile);
        } else {
            holder.ivProfile.setImageResource(0);
        }
        holder.txtName.setText(obj.getName());
        holder.txtWeight.setText(obj.getWeight() + " KG");
        holder.txtDesc.setText(obj.getDescription());


        holder.txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onCartDelete(obj);
                }
            }
        });

        holder.txtQuantity.setText("" + obj.getQuantity());

        return convertView;
    }


    class ViewHolder {
        ImageView ivProfile;
        TextView txtName, txtDesc, txtWeight, txtQuantity, txtDelete;

    }

    public interface DeleteCartClick {
        void onCartDelete(CartListResponse.CartDetails obj);
    }

}
