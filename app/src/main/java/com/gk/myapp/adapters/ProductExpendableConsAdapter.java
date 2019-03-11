package com.gk.myapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gk.myapp.R;
import com.gk.myapp.interfaces.ExpEdListener;
import com.gk.myapp.interfaces.QuantityDialogListener;
import com.gk.myapp.model.ProductListResponse;
import com.gk.myapp.utils.C;
import com.gk.myapp.utils.CircleImageView;
import com.gk.myapp.utils.U;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Gk on 10-09-2017.
 */
public class ProductExpendableConsAdapter extends BaseExpandableListAdapter implements  ExpEdListener {
    private Context mContext;
    private List<ProductListResponse.ProductDetails> mListHeaderData;
    private HashMap<String, List<ProductListResponse.ProductAttributes>> mListChildData;
    private LayoutInflater inf;
    private AddCartClick mListener;

    public ProductExpendableConsAdapter(Context mContext, List<ProductListResponse.ProductDetails> mListHeaderData, HashMap<String, List<ProductListResponse.ProductAttributes>> mListChildData, AddCartClick mListener) {
        this.mContext = mContext;
        this.mListHeaderData = mListHeaderData;
        this.mListChildData = mListChildData;
        inf = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mListener = mListener;
    }


    @Override
    public int getGroupCount() {
        return mListHeaderData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mListChildData.get(mListHeaderData.get(groupPosition).getId()).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mListHeaderData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mListChildData.get(mListHeaderData.get(groupPosition).getId()).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int position, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final ProductListResponse.ProductDetails obj = (ProductListResponse.ProductDetails) getGroup(position);
        if (convertView == null) {
            convertView = inf.inflate(R.layout.list_item_product_cons, parent, false);
            holder = new ViewHolder();
            holder.ivProfile = (ImageView) convertView.findViewById(R.id.iv_profile_pic);
            holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
            holder.txtDesc = (TextView) convertView.findViewById(R.id.txt_desc);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        convertView.setBackgroundColor(mContext.getResources().getColor(R.color.list_green));

        if (obj.getImage() != null && !obj.getImage().equals("")) {
            Glide.with(mContext)
                    .load(C.BASE_URL_PRODUCT_IMAGES + obj.getImage()).placeholder(R.mipmap.dummy_image)
                    .into(holder.ivProfile);
        } else {
            holder.ivProfile.setImageResource(0);
        }
        holder.txtName.setText(obj.getName());
        holder.txtDesc.setText(obj.getDescription());


        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int position, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final ProductListResponse.ProductAttributes obj = (ProductListResponse.ProductAttributes) getChild(groupPosition, position);
        if (convertView == null) {
            convertView = inf.inflate(R.layout.list_item_product_child, parent, false);
            holder = new ViewHolder();
            holder.txtWeight = (TextView) convertView.findViewById(R.id.txt_weight);
            holder.txtQuantity = (TextView) convertView.findViewById(R.id.txt_quantity);
            holder.txtAdd = (TextView) convertView.findViewById(R.id.txt_plus);
            holder.txtSubtract = (TextView) convertView.findViewById(R.id.txt_minus);
            holder.txtAddCart = (TextView) convertView.findViewById(R.id.txt_add_to_cart);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        convertView.setBackgroundColor(mContext.getResources().getColor(R.color.list_grey));

        holder.txtWeight.setText(mContext.getString(R.string.weight) + "  " + obj.getWeight() + " KG");


//        holder.txtAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int q = obj.getQuantity();
//                obj.setQuantity(++q);
//                notifyDataSetChanged();
//            }
//        });
//
//        holder.txtSubtract.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int q = obj.getQuantity();
//                if (q > 0 && q != 1)
//                    obj.setQuantity(--q);
//                notifyDataSetChanged();
//            }
//        });

        holder.txtQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                U.showEdDialogExpendable(mContext, "Enter quantity", obj, "quantity", ProductExpendableConsAdapter.this);
            }
        });

        holder.txtAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onAddToCart(obj);
                }
            }
        });

        holder.txtQuantity.setText("" + obj.getQuantity());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public void onOkClick(ProductListResponse.ProductAttributes obj, String type, int text) {
        obj.setQuantity(text);
    }


    class ViewHolder {
        ImageView ivProfile;
        TextView txtName, txtDesc, txtWeight, txtQuantity, txtAdd, txtSubtract, txtAddCart;

    }

    public interface AddCartClick {
        void onAddToCart(ProductListResponse.ProductAttributes obj);
    }

}
