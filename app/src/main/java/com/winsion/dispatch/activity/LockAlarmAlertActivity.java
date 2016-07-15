package com.winsion.dispatch.activity;

import android.app.Activity;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.winsion.dispatch.R;
import com.winsion.dispatch.bean.FailedReminderBean;
import com.winsion.dispatch.bean.ReminderBean;
import com.winsion.dispatch.constants.DbConstants;
import com.winsion.dispatch.constants.UserInfo;
import com.winsion.dispatch.db.RemindersDao;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by admin on 2016/7/11.
 */
public class LockAlarmAlertActivity extends Activity {

    @InjectView(R.id.tv_lock_time)
    TextView tvLockTime;
    @InjectView(R.id.tv_lock_desc)
    TextView tvLockDesc;
    private RemindersDao mDao;
    private ReminderBean mBean;
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    private float downY;
    private float downX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.alarm_alert_on_lock);
        ButterKnife.inject(this);
        mDao = RemindersDao.getInstance(this);
        Intent intent = getIntent();
        mBean = (ReminderBean) intent.getExtras().getSerializable("bean");
        String planDate = mBean.getPlanDate();
        String[] split = planDate.split(" ");
        tvLockTime.setText(split[1].substring(0, 5));
        tvLockDesc.setText(mBean.getContent());
        // 播放音乐
        playRing();
        // 开启震动
        startVibrator();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float upX = event.getX();
                float upY = event.getY();
                float disY = upY - downY;
                float disX = upX - downX;
                if (disY < -20 && Math.abs(disY) > Math.abs(disX)) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                    }
                    vibrator.cancel();
                    // 更新数据
                    update();
                    finish();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void update() {
        // 先更新本地数据库
        ContentValues values = new ContentValues();
        values.put("finished", "1");
        mDao.update(DbConstants.REMINDERS_TABLE_NAME, values, "date=?", new String[]{mBean.getDate()});
        // 发送广播刷新界面
        sendBroadcast();
        // 更新失败库中该条提醒事项的状态为已完成
        int update = mDao.update(DbConstants.FAILED_REMINDERS_TABLE_NAME, values, "date=?", new String[]{mBean.getDate()});
        if (update == 0) {
            String noteId = mDao.queryOneNoteId(mBean.getDate());
            mDao.insertToFailed(new FailedReminderBean(noteId, UserInfo.getId(), "update",
                    mBean.getDate(), true, mBean.getContent(), mBean.getPlanDate()));
        }
    }

    /**
     * 播放铃声
     */
    private void playRing() {
        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release();
            }
        });
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    /**
     * 开启震动
     */
    private void startVibrator() {
        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[]{300, 700}, 0);
    }

    /**
     * 发送广播通知页面刷新
     */
    private void sendBroadcast() {
        Intent intent = new Intent();
        intent.setAction("UPDATE_UI");
        sendBroadcast(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
