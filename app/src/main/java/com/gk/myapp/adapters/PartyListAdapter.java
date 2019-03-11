package com.gk.myapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gk.myapp.R;
import com.gk.myapp.model.PartyModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gk on 27-08-2017.
 */
public class PartyListAdapter extends BaseAdapter {
    private Context mContext;
    private List<PartyModel> mList;
    private LayoutInflater inf;

    public PartyListAdapter(Context mContext, List<PartyModel> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inf = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void notifyList(List<PartyModel> mList){
        this.mList=mList;
        notifyDataSetChanged();
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
        PartyModel obj=mList.get(position);
        if (convertView == null) {
            convertView = inf.inflate(R.layout.spn_item, parent, false);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.txt_spn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtTitle.setText(obj.getPartyName());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        PartyModel obj=mList.get(position);
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
        holder.txtTitle.setText(obj.getPartyName());
        return convertView;
    }

    class ViewHolder {
        TextView txtTitle;
    }
}
