package com.winsion.dispatch.fragment.operation;

import android.annotation.SuppressLint;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ListView;

import com.winsion.dispatch.R;
import com.winsion.dispatch.base.BaseFragment;
import com.winsion.dispatch.base.BaseLvAdapter;

import java.util.ArrayList;

/**
 * Created by yalong on 2016/6/17.
 */
@SuppressLint("ValidFragment")
public class LocationDisplayFragment extends BaseFragment {

    private ListView listView;
    ArrayList<String> mockDatas = new ArrayList<>();
    private boolean mType;

    public LocationDisplayFragment(boolean type) {
        this.mType = type;
    }

    @Override
    public View setContentView() {
        listView = new ListView(mContext);
        ViewPager.LayoutParams params = new ViewPager.LayoutParams();
        params.width = ViewPager.LayoutParams.MATCH_PARENT;
        params.height = ViewPager.LayoutParams.MATCH_PARENT;
        listView.setLayoutParams(params);
        for (int i = 0; i < 30; i++) {
            mockDatas.add("");
        }
        return listView;
    }

    @Override
    protected void init() {
        if (mType) {
            BaseLvAdapter<String> adapter = new BaseLvAdapter<String>(mContext, mockDatas, R.layout.location_list_item_by_user) {
                @Override
                public void convert(ViewHolder viewHolder, String s) {

                }
            };
            listView.setAdapter(adapter);
        } else {
            BaseLvAdapter<String> adapter = new BaseLvAdapter<String>(mContext, mockDatas, R.layout.location_list_item_by_area) {
                @Override
                public void convert(ViewHolder viewHolder, String s) {
                    viewHolder.fillRecyclerView(R.id.rv_list, mContext);
                }
            };
            listView.setAdapter(adapter);
        }

    }
}
