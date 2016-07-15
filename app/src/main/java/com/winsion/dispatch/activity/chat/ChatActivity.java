package com.winsion.dispatch.activity.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jakewharton.rxbinding.view.RxView;
import com.winsion.dispatch.R;
import com.winsion.dispatch.adapter.ChatAdapter;
import com.winsion.dispatch.base.BaseActivity;
import com.winsion.dispatch.bean.MessageBean;
import com.winsion.dispatch.utils.SPUtils;
import com.winsion.dispatch.utils.T;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

public class ChatActivity extends BaseActivity implements ChatContract.View,ReceiveMessage{

    @InjectView(R.id.chat_rv)
    RecyclerView mChatRv;
    @InjectView(R.id.chat_et)
    EditText chatEt;
    @InjectView(R.id.chat_send)
    Button mChatSend;
    @InjectView(R.id.chat_pressToSay)
    Button mChatPressToSay;
    @InjectView(R.id.chat_ll_voice)
    LinearLayout mChatLlVoice;
    @InjectView(R.id.chat_changeToText)
    ImageView mChatChangeToText;
    @InjectView(R.id.chat_text)
    LinearLayout mChatText;
    private ChatPresenter mChatPresenter;
    private static  ChatAdapter adapter;
    @InjectView(R.id.chat_changeToVoice)
    ImageView mChatVoice;
    @InjectView(R.id.listview)
    ListView lv;
    @InjectView(R.id.chat_all_layout)
    LinearLayout mLinearLayout;
    private ArrayList<MessageBean> datas;
    private boolean isSaying = false;
    RecordUtils mRecordUtils = RecordUtils.getInstance();
    private String chatId;
    private NioEventLoopGroup clientGroup;
    private Channel ch;
    //定义我的主机地址
    private String HOST = "172.16.6.71";
    private int  PORT =  8992;

    @Override
    protected void beforeView() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_msg_chat;
    }

    @Override
    protected void obtainIntent() {
        Intent intent = getIntent();
        chatId = intent.getStringExtra("chatId");
    }

    @Override
    protected void initView(Bundle saveInstanceState) {
        ButterKnife.inject(this);
    }

    @Override
    protected void initListener() {
        lv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN) {
                    imm.hideSoftInputFromWindow(chatEt.getWindowToken(), 0);
                }
                return false;
            }
        });

        chatEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lv.setSelection(adapter.getCount() - 1);
            }
        });


        mChatSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  if (!TextUtils.isEmpty(chatEt.getText().toString().trim())) {
                    //发送出去
                    MessageBean bean = new MessageBean();
                    bean = new MessageBean();
                    bean.setContent(chatEt.getText().toString().trim());
                    bean.setContentType("2");
                    //获取自己的userid
                    String ownid = (String) SPUtils.get(ChatActivity.this,"ownid","ownid");
                    bean.setSenderID(ownid);
                    bean.setSenderHead("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=4077983874,464888782&fm=116&gp=0.jpg");
                    bean.setSenderNick("shdf");
                    datas.add(bean);
                    adapter.notifyDataSetChanged();
                    lv.setSelection(adapter.getCount() - 1);
                    //清空edittext
                    chatEt.setText("");
                }*/
                    if (!TextUtils.isEmpty(chatEt.getText().toString().trim())) {
                        String line = chatEt.getText().toString().trim();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    ChannelFuture lastWriteFuture = null;
                                    lastWriteFuture = ch.writeAndFlush(line + "\r\n");

                                    if (line.equalsIgnoreCase("bey")) {
                                     //   ch.closeFuture().sync();
                                    }
                                    if (lastWriteFuture != null) {
                                        lastWriteFuture.sync();
                                    }
                                }
                                catch (InterruptedException e3)
                                {
                                    // TODO Auto-generated catch block
                                    e3.printStackTrace();
                                } finally
                                {
                                    //clientGroup.shutdownGracefully();
                                }
                            }
                        }).start();
                        chatEt.setText("");
                    }
            }
        });
        //点击录音按钮，切换到录音界面
        RxView.clicks(mChatVoice).subscribe(v -> {
            mChatText.setVisibility(View.GONE);
            //键盘收回
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(chatEt.getWindowToken(), 0);
            mChatLlVoice.setVisibility(View.VISIBLE);
        });
        //切换到发送文字的界面
        RxView.clicks(mChatChangeToText).subscribe(v -> {
            //
            mChatLlVoice.setVisibility(View.GONE);
            mChatText.setVisibility(View.VISIBLE);
        });
        RxView.clicks(mChatPressToSay).subscribe(v -> {

        });

        mChatPressToSay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //按下的时候
                    mChatPressToSay.setText("正在说话。。。");
                        //启动录音
                        if(!isSaying){
                     mRecordUtils.startRecord(ChatActivity.this);
                    }
                    isSaying =true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mChatPressToSay.setText("按住说话");
                    //将录音发送出去并发送给服务器
                    if(isSaying) {
                        mRecordUtils.stopRecord();
                        //提交到
                        T.show(ChatActivity.this,"提交到服务器",1);
                    }
                    isSaying = false;
                }
                return true;
            }
        });
        mChatPressToSay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.show(ChatActivity.this,"按住的时间太短",1);
            }
        });
    }

    @Override
    protected void initData() {
        datas = new ArrayList<>();
        adapter = new ChatAdapter(this, datas);
        lv.setAdapter(new ChatAdapter(this, datas));
        mChatPresenter = new ChatPresenter();
        mChatPresenter.attachView(this);
        mChatPresenter.loadData(SPUtils.get(this,"ownid","ownid")+chatId,this);
        //初始化netty
        initNetty();
    }

    @Override
    public void showChatContent(List<MessageBean> messages) {
        datas.clear();
        datas.addAll(messages);
        adapter.notifyDataSetChanged();
        lv.setSelection(adapter.getCount() - 1);
    }
    //初始化netty
    public void initNetty(){
        final SslContext sslctx;
        try {
            sslctx = SslContext.newClientContext(InsecureTrustManagerFactory.INSTANCE);
            clientGroup = new NioEventLoopGroup();
            Bootstrap b = new Bootstrap();
            b.group(clientGroup).channel(NioSocketChannel.class).handler(new SecureChatClientInitializer(sslctx,ChatActivity.this));
            ch = b.connect(HOST, PORT).sync().channel();


        } catch (SSLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public void receive(String msg) {
        MessageBean bean = new MessageBean();
        bean = new MessageBean();
        bean.setContent(msg);
        bean.setContentType("2");
        //获取自己的userid
        String ownid = (String) SPUtils.get(ChatActivity.this,"ownid","ownid");
        bean.setSenderID(ownid);
        bean.setSenderHead("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=4077983874,464888782&fm=116&gp=0.jpg");
        bean.setSenderNick("shdf");
        datas.add(bean);
        adapter.notifyDataSetChanged();
        lv.setSelection(adapter.getCount() - 1);
        //清空edittext
        chatEt.setText("");

    }

    //内部类
    class SecureChatClientHandler extends SimpleChannelInboundHandler<String> {
        private ReceiveMessage mContext;

        @Override
        public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

            //String msg2=processMsg(msg);
            //获取到服务器返回的消息，添加到适配器中
          //  ChatClient.cList.add("\r\n"+msg,-1);
           // T.show(ChatActivity.this,"msg："+msg,1);
            mContext.receive(msg);
            System.err.println("random"+msg);
            Log.d("random",""+msg);
        }
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }
        SecureChatClientHandler(Context context){
            this.mContext = (ReceiveMessage) context;
        }
    }
    class SecureChatClientInitializer extends ChannelInitializer<SocketChannel> {

        private final SslContext sslCtx;
        private Context mContext;

        public SecureChatClientInitializer(SslContext sslCtx,Context context) {
            this.sslCtx = sslCtx;
            this.mContext = context;
        }
        @Override
        public void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            // Add SSL handler first to encrypt and decrypt everything.
            // In this example, we use a bogus certificate in the server side
            // and accept any invalid certificates in the client side.
            // You will need something more complicated to identify both
            // and server in the real world.    
            pipeline.addLast(sslCtx.newHandler(ch.alloc(), HOST,PORT));

            // On top of the SSL handler, add the text line codec.
            pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
            pipeline.addLast(new StringDecoder());
            pipeline.addLast(new StringEncoder());
            // and then business logic.
            pipeline.addLast(new SecureChatClientHandler(mContext));
        }
    }
}

interface ReceiveMessage {
    abstract void receive(String msg);
}
