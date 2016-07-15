package com.winsion.dispatch.activity.chat;

import android.content.Context;

import com.winsion.dispatch.bean.MessageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.ZCM on 2016/6/13.
 * QQ:656025633
 * Company:winsion
 * Version:1.0
 * explain:
 */
public class ChatPresenter implements ChatContract.Presenter {
    private ChatContract.View mView;
    private List<MessageBean> datas;
    private Context mContext;
    @Override
    public void attachView(ChatContract.View view) {
        this.mView = view;
    }

    @Override
    public void detachView() {

    }

    /**
     * 加载网络数据
     * 根据chatid去得文件的名字
     */
    public void loadData(String filename,Context context){
            MessageBean bean;
            datas = new ArrayList<MessageBean>();
  /*      try {
            Dao dao = DatabaseHelper.getHelper(context).getDao(ChatStringBean.class);
            ChatStringBean csbean = new ChatStringBean();
            csbean.setiAndWho(filename);
            csbean.setJsonContent("jsonString");
            dao.create(csbean);
            //根据条件查询
            List<ChatStringBean> l = dao.queryBuilder().where().eq("iAndWho",filename).query();
            for (int i = 0; i <l.size() ; i++) {
                Log.d("random",l.get(i).getJsonContent()+"teset");
            }
            ChatStringBean c = new ChatStringBean();
            c.setiAndWho(filename);
            c.setJsonContent("teeeeeeee");

            dao.update(c);
            List<ChatStringBean> ll = dao.queryBuilder().where().eq("iAndWho",filename).query();
            for (int i = 0; i <ll.size() ; i++) {
                Log.d("random",ll.get(i).getJsonContent()+"teset");
            }

        } catch (SQLException e) {  
            e.printStackTrace();
        }*/

        for (int i = 0; i < 20; i++) {
                    if(i%2==0){
                            bean = new MessageBean();
                            bean.setContent("我是个大好人呢");
                            bean.setContentType("2");
                            bean.setSenderID("2");
                            bean.setSenderHead("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=4077983874,464888782&fm=116&gp=0.jpg");
                            bean.setSenderNick("shdf");
                            datas.add(bean);
                    }
                    else {
                            bean = new MessageBean();
                            bean.setContent("你是个大傻逼");
                            bean.setContentType("1");
                            bean.setSenderID("1");
                            bean.setSenderHead("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1425225084,3700586189&fm=116&gp=0.jpg");
                            bean.setSenderNick("zcm");
                            datas.add(bean);
                    }
            }
            mView.showChatContent(datas);
    }
}
