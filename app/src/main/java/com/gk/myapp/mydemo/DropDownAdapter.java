package com.gk.myapp.mydemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gk.myapp.R;

import java.util.List;

/**
 * Created by Gk on 16-04-2017.
 */
public class DropDownAdapter extends BaseAdapter {


    private Context mContext;
    private List<String> mList;
    LayoutInflater mInflate;

    public DropDownAdapter(Context mContext, List<String> mList) {

        this.mContext = mContext;
        this.mList = mList;
        mInflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void notifyList(List<String> mList){
        this.mList=mList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public String getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflate.inflate(R.layout.list_item_drop_down, parent, false);
            holder.txtDropDown = (TextView) convertView.findViewById(R.id.txt_drop_down);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtDropDown.setText(mList.get(position));
        return convertView;
    }

    class ViewHolder {
        TextView txtDropDown;
    }
}
