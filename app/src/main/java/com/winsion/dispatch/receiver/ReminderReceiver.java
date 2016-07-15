package com.winsion.dispatch.receiver;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.winsion.dispatch.activity.AlarmAlertActivity;
import com.winsion.dispatch.activity.LockAlarmAlertActivity;
import com.winsion.dispatch.bean.ReminderBean;

/**
 * Created by yalong on 2016/5/31.
 */
public class ReminderReceiver extends BroadcastReceiver {

    private Context mContext;

    @Override
    public void onReceive(final Context context, final Intent data) {
        this.mContext = context;
        ReminderBean bean = (ReminderBean) data.getSerializableExtra("bean");
        // 判断是否锁屏
        KeyguardManager mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        boolean flag = mKeyguardManager.inKeyguardRestrictedInputMode();
        Intent intent = null;
        if (flag) {
            // 锁屏或者屏幕是黑的
            intent = new Intent(context, LockAlarmAlertActivity.class);
        } else {
            // 解锁状态
            // 跳转到对话框activity
            intent = new Intent(context, AlarmAlertActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("bean", bean);
        mContext.startActivity(intent);
    }
}
