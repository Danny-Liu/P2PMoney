package com.ly.p2pmon.common;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by 12758 on 2016/5/15.
 */
public abstract class MyBaseAdapter1<T> extends BaseAdapter {
    protected List<T> list;

    public MyBaseAdapter1(List<T> list) {
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
        T t = list.get(position);
        ViewHolder holder = null;
        if(convertView == null){
            convertView = View.inflate(parent.getContext(),getLayoutId(),null);
            holder = new ViewHolder(convertView);//new ViewHolder对象的时候  就顺便传convertView对象去setTag了
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        //设置数据
        setData(convertView,t);
        return convertView;
    }

    protected abstract void setData(View convertView, T t);

    public abstract int getLayoutId();


    static class ViewHolder{

        public ViewHolder(View convertView) {
            convertView.setTag(this);
        }
    }
}
