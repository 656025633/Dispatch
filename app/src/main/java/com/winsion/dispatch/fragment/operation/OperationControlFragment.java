package com.winsion.dispatch.fragment.operation;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.winsion.dispatch.R;
import com.winsion.dispatch.base.BaseFragment;
import com.winsion.dispatch.base.BaseLvAdapter;
import com.winsion.dispatch.bean.TrainInfoBean;
import com.winsion.dispatch.bean.UserStation;
import com.winsion.dispatch.iview.IOperationControlView;
import com.winsion.dispatch.presenter.OperationPresenter;
import com.winsion.dispatch.utils.ConvertUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by yalong on 2016/6/13.
 */
public class OperationControlFragment extends BaseFragment implements IOperationControlView {

    @InjectView(R.id.tv_station)
    TextView tvStation;
    @InjectView(R.id.tv_type)
    TextView tvType;
    @InjectView(R.id.lv_list)
    ListView lvList;
    @InjectView(R.id.iv_shade)
    ImageView ivShade;

    // spinner 数据
    private ArrayList<String> mStations = new ArrayList<>();
    private ArrayList<String> mStationType = new ArrayList<>();
    // listview 数据
    private ArrayList<TrainInfoBean> mList = new ArrayList<>();
    private OperationPresenter mPresenter;
    private BaseLvAdapter<TrainInfoBean> lvAdapter;
    private List<UserStation> data;
    private HashMap<String, Integer> map;
    private HashMap<String, String> nameIdMap;
    private PopupWindow stationPopupWindow;
    private PopupWindow typePopupWindow;

    @Override
    public View setContentView() {
        mPresenter = new OperationPresenter(this);
        return mLayoutInflater.inflate(R.layout.operation_control_fragment, null);
    }

    @Override
    protected void init() {
        initData();
        initAdapter();
    }

    private void initData() {
        // spinner数据
        mPresenter.initSpinnerData();
        String[] strings = ConvertUtil.resourcesIdToString(mContext, new int[]{R.string.up, R.string.down});
        mStationType.add(strings[0]);
        mStationType.add(strings[1]);
        map = new HashMap<>();
        map.put(strings[0], 0);
        map.put(strings[1], 1);
    }

    /**
     * 用户对应的车站信息请求成功
     *
     * @param data
     */
    @Override
    public void dataInitComplete(final List<UserStation> data) {
        if (data.size() == 0) {
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                tvStation.setText(data.get(0).getName());
                tvType.setText(ConvertUtil.resourcesIdToString(mContext, new int[]{R.string.up})[0]);
            }
        });
        mPresenter.requestInternet(data.get(0).getId(), "UP");
        this.data = data;
        nameIdMap = new HashMap<>();
        for (int i = 0; i < data.size(); i++) {
            UserStation userStation = data.get(i);
            String name = userStation.getName();
            mStations.add(name);
            nameIdMap.put(name, userStation.getId());
        }
        stationPopupWindow = initPopupWindow(mStations, "station");
        typePopupWindow = initPopupWindow(mStationType, "type");
    }


    @OnClick({R.id.tv_station, R.id.tv_type})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_station:
                ivShade.setVisibility(View.VISIBLE);
                stationPopupWindow.showAsDropDown(view);
                break;
            case R.id.tv_type:
                ivShade.setVisibility(View.VISIBLE);
                typePopupWindow.showAsDropDown(view);
                break;
        }
    }

    /**
     * 初始化对应的PopupWindow
     *
     * @param list
     * @param which
     * @return
     */
    private PopupWindow initPopupWindow(final List<String> list, final String which) {
        BaseLvAdapter adapter =  new BaseLvAdapter<String>(mContext, list, R.layout.spinner_list_item) {
            @Override
            public void convert(ViewHolder viewHolder, final String s) {
                viewHolder.setText(R.id.tv_station_name, s);
            }
        };
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_popwindow, null);
        ListView lvList = (ListView) view.findViewById(R.id.lv_list);
        lvList.setAdapter(adapter);
        final PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(android.R.style.Animation_Translucent);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ivShade.setVisibility(View.GONE);
            }
        });
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String type = null;
                String trainId = null;
                switch (which) {
                    case "station":
                        String typeString = (String) tvType.getText();
                        type = map.get(typeString) == 0 ? "UP" : "DOWN";
                        trainId = data.get(position).getId();
                        tvStation.setText(mStations.get(position));
                        break;
                    case "type":
                        String stationString = (String) tvStation.getText();
                        trainId = nameIdMap.get(stationString);
                        type = map.get(mStationType.get(position)) == 0 ? "UP" : "DOWN";
                        tvType.setText(mStationType.get(position));
                        break;
                }
                // listview请求网络数据
                mPresenter.requestInternet(trainId, type);
                popupWindow.dismiss();
            }
        });
        return popupWindow;
    }

    private void initAdapter() {
        // ListView Adapter
        lvAdapter = new BaseLvAdapter<TrainInfoBean>(mContext, mList, R.layout.operation_list_item) {
            @Override
            public void convert(ViewHolder viewHolder, TrainInfoBean bean) {
                viewHolder.setText(R.id.tv_trainNumber, bean.getTrainNumber());
                viewHolder.setText(R.id.tv_currentTrainStatus, bean.getCurrentTrainStatus());
                viewHolder.setText(R.id.tv_startStationName, bean.getStartStationName());
                viewHolder.setText(R.id.tv_departTime, bean.getDepartTime());
                viewHolder.setText(R.id.tv_waitRoom, bean.getWaitRoom() + "候");
                viewHolder.setText(R.id.tv_checkPort, bean.getCheckPort());
                viewHolder.setText(R.id.tv_platform, bean.getPlatform());
                viewHolder.setText(R.id.tv_endStationName, bean.getEndStationName());
                viewHolder.setText(R.id.tv_arriveTime, bean.getArriveTime());
                viewHolder.setImage(R.id.iv_onTime, bean.getOnTime() ? R.mipmap.on_time : R.mipmap.late);
            }
        };
        lvList.setAdapter(lvAdapter);
    }

    /**
     * 请求列车信息数据成功，刷新界面
     *
     * @param beanList
     */
    @Override
    public void updateData(List<TrainInfoBean> beanList) {
        mList.clear();
        mList.addAll(beanList);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                lvAdapter.notifyDataSetChanged();
            }
        });
    }
}
