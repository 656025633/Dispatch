package com.winsion.dispatch.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.winsion.dispatch.R;
import com.winsion.dispatch.bean.ReminderBean;
import com.winsion.dispatch.db.RemindersDao;
import com.winsion.dispatch.iview.IAddReminderView;
import com.winsion.dispatch.presenter.AddReminderPresenter;
import com.winsion.dispatch.utils.ConvertUtil;
import com.winsion.dispatch.utils.ToastUtil;

import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by yalong on 2016/6/15.
 */
public class AddReminderActivity extends Activity implements IAddReminderView {

    @InjectView(R.id.tv_date)
    TextView tvDate;
    @InjectView(R.id.tv_time)
    TextView tvTime;
    @InjectView(R.id.et_desc)
    EditText etDesc;

    private Context mContext;
    private AddReminderPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getBaseContext();
        setContentView(R.layout.add_reminder_fragment);
        ButterKnife.inject(this);
        presenter = new AddReminderPresenter(this, mContext);
        // 初始化日期和时间控件默认显示为当前时间
        initViewData();
    }

    private void initViewData() {
        Bundle extras = getExtras();
        String planDate = null;
        String desc = null;
        if (extras != null) {
            ReminderBean bean = (ReminderBean) extras.getSerializable("bean");
            planDate = bean.getPlanDate();
            desc = bean.getContent();
        } else {
            long millis = System.currentTimeMillis();
            planDate = ConvertUtil.millisToDate(millis);
            desc = "";
        }
        String[] split = planDate.split(" ");
        tvDate.setText(split[0]);
        tvTime.setText(split[1].substring(0, 5));
        etDesc.setText(desc);
    }

    @Override
    public String getDesc() {
        return etDesc.getText().toString();
    }

    @Override
    public String getTime() {
        return tvTime.getText().toString();
    }

    @Override
    public String getDate() {
        return tvDate.getText().toString();
    }

    @Override
    public Bundle getExtras() {
        return getIntent().getExtras();
    }

    @Override
    public RemindersDao getRemindersDao() {
        return RemindersDao.getInstance(mContext);
    }

    @Override
    public void onUpdateComplete() {
        ToastUtil.showToast(mContext, "更新成功");
        finish();
    }

    @Override
    public void onAddComplete() {
        ToastUtil.showToast(mContext, "添加成功");
        finish();
    }

    @OnClick({R.id.tv_date, R.id.tv_time, R.id.btn_save, R.id.btn_give_up})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_date:
                // 显示日期选择器对话框
                showDatePicker();
                break;
            case R.id.tv_time:
                // 显示时间选择器对话框
                showTimePicker();
                break;
            case R.id.btn_save:
                // 保存一条数据
                presenter.addReminder();
                break;
            case R.id.btn_give_up:
                finish();
                break;
        }
    }

    /**
     * 显示日期选择器
     */
    private void showDatePicker() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etDesc.getWindowToken(), 0);
        TimePickerView pvDate = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        String date = tvDate.getText().toString();
        Date currentDate = new Date(ConvertUtil.dateToMillis(date + " 00:00:00"));
        Calendar calendar = Calendar.getInstance();
        //要在setTime 之前才有效果哦
        pvDate.setRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 20);
        pvDate.setTime(currentDate);
        pvDate.setCyclic(false);
        pvDate.setCancelable(true);
        //时间选择后回调
        pvDate.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                tvDate.setText(ConvertUtil.millisToDate(date.getTime()).split(" ")[0]);
            }
        });
        pvDate.show();
    }

    /**
     * 显示时间选择器
     */
    private void showTimePicker() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etDesc.getWindowToken(), 0);
        TimePickerView pvTime = new TimePickerView(this, TimePickerView.Type.HOURS_MINS);
        String time = tvTime.getText().toString();
        String[] split1 = time.split(":");
        Date date1 = new Date();
        date1.setHours(Integer.valueOf(split1[0]));
        date1.setMinutes(Integer.valueOf(split1[1]));
        pvTime.setTime(date1);
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                tvTime.setText(ConvertUtil.millisToDate(date.getTime()).split(" ")[1].substring(0, 5));
            }
        });
        pvTime.show();
    }
}
