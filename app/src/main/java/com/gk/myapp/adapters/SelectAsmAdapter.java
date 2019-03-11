package com.gk.myapp.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gk.myapp.R;
import com.gk.myapp.model.SelectAsmModel;
import com.gk.myapp.utils.CircleImageView;

import java.util.ArrayList;

/**
 * Created by Gk on 18-12-2016.
 */
public class SelectAsmAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<SelectAsmModel> mList;
    private LayoutInflater inf;

    public SelectAsmAdapter(Context mContext, ArrayList<SelectAsmModel> mList) {
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
        SelectAsmModel obj=mList.get(position);
        if (convertView == null) {
            convertView = inf.inflate(R.layout.list_item_select_asm, parent, false);
            holder = new ViewHolder();
            holder.ivProfile = (CircleImageView) convertView.findViewById(R.id.iv_profile_pic);
            holder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
            holder.txtLocation = (TextView) convertView.findViewById(R.id.txt_location);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position % 2 == 0) {
            convertView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.list_grey));
        } else {
            convertView.setBackgroundColor(ContextCompat.getColor(mContext,R.color.list_green));
        }

        holder.txtName.setText(obj.getAsmName());
        holder.txtLocation.setText(obj.getLocation());


        return convertView;
    }


    class ViewHolder {
        CircleImageView ivProfile;
        TextView txtName, txtLocation;
    }
}
