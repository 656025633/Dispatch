package com.winsion.dispatch.activity;

import android.app.Activity;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.widget.TextView;

import com.winsion.dispatch.R;
import com.winsion.dispatch.bean.FailedReminderBean;
import com.winsion.dispatch.bean.ReminderBean;
import com.winsion.dispatch.constants.DbConstants;
import com.winsion.dispatch.constants.UserInfo;
import com.winsion.dispatch.db.RemindersDao;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AlarmAlertActivity extends Activity {

    @InjectView(R.id.tv_desc)
    TextView tvDesc;
    private RemindersDao mDao;
    private ReminderBean mBean;
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFinishOnTouchOutside(false);
        setContentView(R.layout.dialog_alarm);
        ButterKnife.inject(this);
        mDao = RemindersDao.getInstance(this);
        Intent intent = getIntent();
        mBean = (ReminderBean) intent.getExtras().getSerializable("bean");
        String content = mBean.getContent();
        tvDesc.setText(content);
        // 播放音乐
        playRing();
        // 开启震动
        startVibrator();
    }

    @OnClick(R.id.btn_confirm)
    public void onClick() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        vibrator.cancel();
        // 更新数据
        update();
        finish();
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
