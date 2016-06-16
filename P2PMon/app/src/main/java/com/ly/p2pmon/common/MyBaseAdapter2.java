package com.ly.p2pmon.common;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by 12758 on 2016/5/15.
 */
public abstract class MyBaseAdapter2<T> extends BaseAdapter {

    private List<T> list;

    public MyBaseAdapter2(List<T> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseHolder holder = null;
        if(convertView==null){
            holder = getHolder();
        }else{
            holder = (BaseHolder) convertView.getTag();
        }
        //设置数据
        holder.setData(list.get(position));
        return holder.getRootView();
    }

    public abstract BaseHolder getHolder();
}
