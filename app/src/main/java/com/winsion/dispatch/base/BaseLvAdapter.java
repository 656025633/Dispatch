package com.winsion.dispatch.base;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.winsion.dispatch.R;

import java.util.List;

/**
 * Created by yalong on 2016/6/6.
 */
public abstract class BaseLvAdapter<T> extends BaseAdapter {

    private List<T> mDatas;
    private Context mContext;
    private int mLayoutId;

    /**
     * @param context
     * @param datas    填充的数据集合
     * @param layoutId 条目布局ID
     */
    public BaseLvAdapter(Context context, List<T> datas, int layoutId) {
        this.mContext = context;
        this.mDatas = datas;
        this.mLayoutId = layoutId;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = ViewHolder.get(mContext, view, viewGroup, mLayoutId, i);
        convert(viewHolder, mDatas.get(i));
        return viewHolder.getConvertView();
    }

    public abstract void convert(ViewHolder viewHolder, T t);

    /**
     * ViewHolder
     */
    public static class ViewHolder {
        private SparseArray<View> mViews;
        private View mConvertView;

        private ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
            this.mViews = new SparseArray<>();
            mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
            mConvertView.setTag(this);
        }

        public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
            if (convertView == null) {
                return new ViewHolder(context, parent, layoutId, position);
            }
            return (ViewHolder) convertView.getTag();
        }

        public <T extends View> T getView(int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = mConvertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }

        public View getConvertView() {
            return mConvertView;
        }

        public void setText(int viewId, String s) {
            TextView textView = getView(viewId);
            textView.setText(s);
        }

        public void setImage(int viewId, int resId) {
            ImageView imageView = getView(viewId);
            imageView.setImageResource(resId);
        }

        public void setImageButtonOnClickListener(int viewId, View.OnClickListener listener) {
            ImageButton imageButton = getView(viewId);
            imageButton.setOnClickListener(listener);
        }

        public void setTextColor(int viewId, String color) {
            TextView textView = getView(viewId);
            textView.setTextColor(Color.parseColor(color));
        }

        public void setOnClickListener(int viewId, View.OnClickListener listener) {
            Button button = getView(viewId);
            button.setOnClickListener(listener);
        }

        public void fillRecyclerView(int viewId, Context context) {
            RecyclerView recyclerView = getView(viewId);
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if (adapter == null) {
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
                MyAdapter adapter1 = new MyAdapter(context);
                recyclerView.setAdapter(adapter1);
            }
        }

        public void setVisibility(int viewId,boolean isTimeOut){
            TextView tvTimeOut = getView(viewId);
            tvTimeOut.setVisibility(isTimeOut ? View.VISIBLE : View.INVISIBLE);
        }
    }

    static class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private Context mContext;

        public MyAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(mContext).inflate(R.layout.location_grid_item, parent, false);
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            inflate.setLayoutParams(layoutParams);
            MyViewHolder holder = new MyViewHolder(inflate);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 20;
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
