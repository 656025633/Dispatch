package com.winsion.dispatch.activity.chat;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;

import com.winsion.dispatch.utils.T;

import java.io.File;
import java.io.IOException;

/**
 * Created by Mr.ZCM on 2016/6/21.
 * QQ:656025633
 * Company:winsion
 * Version:1.0
 * explain:用于实现音频的录制
 */
public class RecordUtils {
    private MediaRecorder recorder;
    private static RecordUtils mRecordUtils = new RecordUtils();
    private Context mContext;
    private String filePath;
    private boolean isRecording = false;

    private RecordUtils(){

    }
    public static RecordUtils getInstance(){

        return mRecordUtils;
    }
    //启动录音
    public void startRecord(Context context){
        this.mContext = context;
        recorder = new MediaRecorder();
        boolean sdCardIsExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        recorder=new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        if (sdCardIsExist) {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/dispatch/voice");
            if(!file.exists()){
                    file.mkdirs();
            }
            filePath = file.getAbsolutePath()+"/"+System.currentTimeMillis()+".amr";
            recorder.setOutputFile(filePath);
        }
        else{
            T.show(context,"没有存储设备",1);
        }
        if(recorder != null){
            //启动
            try {
                recorder.prepare();
                recorder.start();
                isRecording = true;
                T.show(context,"开始录音",1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            recorder = new MediaRecorder();
        }
    }
    public void stopRecord(){
        if(recorder != null&&isRecording){
            try {
                recorder.stop();
                recorder.reset();
                recorder.release();
                isRecording = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
