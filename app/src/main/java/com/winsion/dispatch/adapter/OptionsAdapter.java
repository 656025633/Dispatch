package com.winsion.dispatch.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.winsion.dispatch.R;

import java.util.List;
import java.util.Map;

public class OptionsAdapter extends BaseAdapter {
    private List<Map<String, Object>> list;
    private Activity activity = null;
    private Handler handler;

    /**
     * 自定义构造方法
     *
     * @param activity
     * @param handler
     * @param
     */
    public OptionsAdapter(Activity activity, Handler handler, List<Map<String, Object>> listItems) {
        this.activity = activity;
        this.handler = handler;
        this.list = listItems;
    }

    @Override
    public int getCount() {
        return list.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            //下拉项布局
            convertView = LayoutInflater.from(activity).inflate(R.layout.option_item, null);
            holder.textView = (TextView) convertView.findViewById(R.id.item_text);
            holder.delImage = (ImageView) convertView.findViewById(R.id.delImage);
            holder.headImage = (ImageView) convertView.findViewById(R.id.head_option);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText((String) list.get(position)
                .get("number"));
        Bitmap headImage = BitmapFactory.decodeFile((String) list.get(position).get("headImage"));
        holder.headImage.setImageBitmap(headImage);
        //为下拉框选项文字部分设置事件，最终效果是点击将其文字填充到文本框
        holder.textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Message msg = new Message();
                Bundle data = new Bundle();
                //设置选中索引
                data.putInt("selIndex", position);
                msg.setData(data);
                msg.what = 1;
                //发出消息
                handler.sendMessage(msg);
            }
        });
        //为下拉框选项删除图标部分设置事件，最终效果还是点击将该项删除
        holder.delImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Message msg = new Message();
                Bundle data = new Bundle();
                //设置删除索引
                data.putInt("delIndex", position);
                msg.setData(data);
                msg.what = 2;
                //发出消息
                handler.sendMessage(msg);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView textView;
        ImageView delImage;
        ImageView headImage;
    }

}
