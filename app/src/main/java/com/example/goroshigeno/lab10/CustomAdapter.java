package com.example.goroshigeno.lab10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by steven on 2018/1/5.
 */

public class CustomAdapter extends BaseAdapter implements Filterable {
    List<String> Address;
    List<String> Name;
    List<String> Lang;
    List<String> Long;

    List<String> originalitem;

    private LayoutInflater mLayout;
    String[] Search = new String[4];


    public CustomAdapter(Context context, List<String> mList , List<String> mName , List<String> mLang , List<String> mLong) {
        mLayout = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.Address = mList;
        this.Name = mName;
        this.Lang = mLang;
        this.Long = mLong;
        //this.info = info;

    }

    @Override
    public int getCount() {
        return Address.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // R.layout.custom_layout 是你自己創的自訂layout( 可參考下面)
        View v = mLayout.inflate(R.layout.address_list_show,parent,false);

        TextView title = (TextView)v.findViewById(R.id.item_address_show);


        title.setText(Address.get(position).toString());
        //content.setText(info.get(position).toString());

        return v;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                constraint = constraint.toString();
                FilterResults result = new FilterResults();
                if(originalitem == null){
                    synchronized (this){
                        originalitem = new ArrayList<String>(Address);
                        // 若originalitem 沒有資料，會複製一份item的過來.
                    }
                }
                if(constraint != null && constraint.toString().length()>0){
                    ArrayList<String> filteredItem = new ArrayList<String>();
                    for(int i=0;i<originalitem.size();i++){
                        String title = originalitem.get(i).toString();
                        if(title.contains(constraint)){
                            filteredItem.add(title);
                        }
                    }
                    result.count = filteredItem.size();
                    result.values = filteredItem;
                }else{
                    synchronized (this){
                        ArrayList<String> list = new ArrayList<String>(originalitem);
                        result.values = list;
                        result.count = list.size();

                    }
                }

                return result;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                Address = (ArrayList<String>)results.values;
                if(results.count>0){
                    notifyDataSetChanged();
                }else{
                    notifyDataSetInvalidated();
                }
            }
        };

        return filter;
    }

    public String[] getSearch_name (int position)
    {
        Search[0] = Address.get(position).toString();
        Search[1] = Name.get(position).toString();
        Search[2] = Lang.get(position).toString();
        Search[3] = Long.get(position).toString();
        return Search;
    }
}