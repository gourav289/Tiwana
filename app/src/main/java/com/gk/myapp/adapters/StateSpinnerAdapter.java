package com.gk.myapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gk.myapp.R;
import com.gk.myapp.model.PartyModel;
import com.gk.myapp.model.SelectAsmModel;
import com.gk.myapp.model.StateResponseModel;
import com.gk.myapp.utils.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gk on 09-09-2017.
 */
public class StateSpinnerAdapter extends BaseAdapter {
    private Context mContext;
    private List<StateResponseModel.StateDetails> mList;
    private LayoutInflater inf;

    public StateSpinnerAdapter(Context mContext, List<StateResponseModel.StateDetails> mList) {
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
        StateResponseModel.StateDetails obj = mList.get(position);
        if (convertView == null) {
            convertView = inf.inflate(R.layout.spn_item, parent, false);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.txt_spn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtTitle.setText(obj.getName());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        StateResponseModel.StateDetails obj = mList.get(position);
        if (convertView == null) {
            convertView = inf.inflate(R.layout.spn_item_drop_down, parent, false);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.txt_spn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 0) {
            holder.txtTitle.setVisibility(View.GONE);
        } else {
            holder.txtTitle.setVisibility(View.VISIBLE);
        }
        holder.txtTitle.setText(obj.getName());
        return convertView;
    }

    class ViewHolder {
        TextView txtTitle;
    }
}
