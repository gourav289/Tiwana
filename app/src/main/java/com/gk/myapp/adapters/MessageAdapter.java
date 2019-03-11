package com.gk.myapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gk.myapp.R;
import com.gk.myapp.model.MessageModel;
import com.gk.myapp.model.NotificationModel;
import com.gk.myapp.utils.C;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gk on 18-12-2016.
 */
public class MessageAdapter extends BaseAdapter {
    private Context mContext;
    private List<MessageModel> mList;
    private LayoutInflater inf;
    private int from=1;

    public MessageAdapter(Context mContext, List<MessageModel> mList,int from) {
        this.mContext = mContext;
        this.mList = mList;
        inf = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.from=from;
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
        MessageModel obj=mList.get(position);
        if (convertView == null) {
            convertView = inf.inflate(R.layout.list_item_message_new, parent, false);
            holder = new ViewHolder();
            holder.txtDesc = (TextView) convertView.findViewById(R.id.txt_desc);
            holder.txtDate = (TextView) convertView.findViewById(R.id.txt_date);
            holder.txtTitle= (TextView) convertView.findViewById(R.id.txt_title);
            holder.txtFrom= (TextView) convertView.findViewById(R.id.txt_from);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtDesc.setText(obj.getMessage());
        holder.txtDate.setText(obj.getCreated());
        if(obj.getTitle().equals(C.GM))
            obj.setSendFrom("General Manager");
        else if(obj.getTitle().equals(C.ASM))
            obj.setSendFrom("Area Sales Manager");
        else if(obj.getTitle().equals(C.IT_ADMIN))
            obj.setSendFrom("IT Admin");
        else
            obj.setSendFrom("Sales Officer");



        if(from==1) {
            holder.txtTitle.setText(obj.getSendFrom());
            holder.txtFrom.setVisibility(View.VISIBLE);
        }
        else {
            if(obj.getCreatedFor().equalsIgnoreCase("ALL") || obj.getCreatedFor().equalsIgnoreCase("ALL SO") ){
                holder.txtTitle.setText(mContext.getString(R.string.broadcast_message));
            }else if(obj.getCreatedFor().equalsIgnoreCase("SO")){
                holder.txtTitle.setText(mContext.getString(R.string.sale_officer));
            }else if(obj.getCreatedFor().equalsIgnoreCase("ASM")){
                holder.txtTitle.setText(mContext.getString(R.string.area_sales_officer));
            }else if(obj.getCreatedFor().equalsIgnoreCase("GM")){
                holder.txtTitle.setText(mContext.getString(R.string.general_manager));
            }else{
                holder.txtTitle.setText("");
            }
            holder.txtFrom.setVisibility(View.GONE);
        }
        holder.txtFrom.setText(obj.getSenderName());

        return convertView;
    }


    class ViewHolder {
        TextView txtDesc, txtDate,txtTitle,txtFrom;
    }
}
