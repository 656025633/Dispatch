package com.winsion.dispatch.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.winsion.dispatch.R;
import com.winsion.dispatch.adapter.abslistview.MultiItemCommonAdapter;
import com.winsion.dispatch.adapter.abslistview.MultiItemTypeSupport;
import com.winsion.dispatch.bean.MessageBean;
import com.winsion.dispatch.utils.SPUtils;

import java.util.List;

/**
 * Created by zcm on 15/9/4.
 */
public class ChatAdapter extends MultiItemCommonAdapter<MessageBean>
{
    public ChatAdapter(final Context context, List<MessageBean> datas)
    {
        super(context, datas, new MultiItemTypeSupport<MessageBean>()
        {
            @Override
            public int getLayoutId(int position, MessageBean msg)
            {
                if (msg.getSenderID().equals((String) SPUtils.get(context,"ownid","ownid")))
                {
                    return R.layout.main_chat_send_msg;
                }
                return R.layout.main_chat_from_msg;
            }

            @Override
            public int getViewTypeCount()
            {
                return 2;
            }

          
            @Override
            public int getItemViewType(int postion, MessageBean msg)
            {
                if (msg.getSenderID().equals((String)SPUtils.get(context,"ownid","ownid")))
                {
                    return 0;
                }
                return 1;
            }
        });
    }

    @Override
    public void convert(ViewHolder holder, MessageBean chatMessage)
    {

        switch (holder.getLayoutId())
        {
            case R.layout.main_chat_from_msg:
                holder.setText(R.id.chat_from_content, chatMessage.getContent());
                holder.setText(R.id.chat_from_name, chatMessage.getSenderNick());
                Glide.with(mContext).load(chatMessage.getSenderHead()).into((ImageView) holder.getConvertView().findViewById(R.id.chat_from_icon));
                break;
            case R.layout.main_chat_send_msg:
                holder.setText(R.id.chat_send_content, chatMessage.getContent());
                holder.setText(R.id.chat_send_name, chatMessage.getSenderNick());
                Glide.with(mContext).load(chatMessage.getSenderHead()).into((ImageView) holder.getConvertView().findViewById(R.id.chat_send_icon));
                break;
        }
    }
}
