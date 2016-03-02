package com.example.sony.jizha.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.sony.jizha.R;
import com.example.sony.jizha.Widget.BaseActivity;
import com.example.sony.jizha.adapter.ChatAdapter;
import com.example.sony.jizha.model.ChatMsg;
import com.example.sony.jizha.model.Contact;
import com.example.sony.jizha.service.ChatMsgService;
import com.example.sony.jizha.system.Constant;
import com.example.sony.jizha.system.JzApplication;
import com.example.sony.jizha.utils.MemberUtil;
import com.example.sony.jizha.utils.ToastUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

/**
 * Created with Android Studio
 * 项目名称：jizha
 * 类描述：聊天界面activity
 * 创建人：sony
 * 创建时间：2016/1/1 13:08
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version V1.0
 */
@ContentView(R.layout.activity_chat)
public class ChatActivity extends BaseActivity {

    //聊天信息输入
    @InjectView(R.id.etxtMsg)
    private EditText mEtxtMsg;

    //发送按钮
    @InjectView(R.id.btnSend)
    private Button mBtnSend;

    //聊天内容的显示
    @InjectView(R.id.listView)
    private ListView mListView;

    //获取intent传递的当前聊天的好友
    @InjectExtra(Constant.FRIEND)
    private Contact friend;

    //聊天信息的适配器
    private ChatAdapter chatAdapter;
    //初始化context
    private Context context = JzApplication.getInstance();
    ///初始化聊天信息服务，用于读取聊天内容
    private ChatMsgService chatMsgService = ChatMsgService.getInstance();
    //创建聊天信息广播接收对象
    private ChatBroadcastReceiver receiver;

    /**
     * 创建activity
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化用户,判断当前应用中登陆用户是否存在，不存在的话保存一个，防止系统进程杀死的时候收到消息
        // 如果收到消息的话，重新启动应用
        MemberUtil.initMember(context);

        //设置actionBar的返回按钮
        initActionBarBasic();

        //初始化聊天适配器
        chatAdapter = new ChatAdapter(context, initChat(), friend);

        mListView.setAdapter(chatAdapter);

        //listView移动到最后一条
        mListView.setSelection(chatAdapter.getCount() - 1);
    }

    /**
     * 开始activity
     */
    @Override
    protected void onStart() {
        super.onStart();
        receiver = new ChatBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ACTION_MSG);

        //注册广播接收对象
        registerReceiver(receiver, filter);
    }

    /**
     * 点击返回按钮时，回调ChatFragment中的onActivityResult方法
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        setResult(100);
        this.finish();
    }

    /**
     * 触发actionBar的返回按钮
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //触发返回事件
            //onBackPressed();

            //统一返回按钮以及actionbar上返回按钮的返回到homeActivity的聊天信息显示列表界面
            navigateUpTo();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 结束activity
     */
    @Override
    protected void onStop() {
        super.onStop();
        //停止接收广播
        unregisterReceiver(receiver);
    }


    /**
     * 统一返回按钮以及actionbar上返回按钮的返回到homeActivity的聊天信息显示列表界面
     */
    private void navigateUpTo() {

        //        //获取到跳转至父Activity的Intent
        //        Intent upIntent = NavUtils.getParentActivityIntent(this);
        //        //父Activity和当前Activity是在同一个Task中的
        //        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
        //            // 如果不是在同一个Task中的，则需要借助TaskStackBuilder来创建一个新的Task
        //            TaskStackBuilder.create(this)
        //                    .addNextIntentWithParentStack(upIntent)
        //                    .startActivities();
        //        } else {
        //            //如果在同一TASK中 直接调用navigateUpTo()方法进行跳转
        //            upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        //            NavUtils.navigateUpTo(this, upIntent);
        //        }

        Intent intent = new Intent(this, HomeActivity.class);

        //FLAG_ACTIVITY_CLEAR_TOP：判断当前的任务栈顶是否存在相同的activity对象，如果存在，则直接使用，如果不存在，那么创建新的activity对象放入栈中
        //FLAG_ACTIVITY_NEW_TASK：在任务栈中会判断是否存在相同的activity，如果存在，那么会清除该activity之上的其他activity对象显示，如果不存在，则会创建一个新的activity放入栈顶
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }

    /**
     * 发送按钮的点击事件
     *
     * @param view
     */
    public void sendMsg(View view) {
        //构建聊天信息
        createMsg();
    }

    /**
     * 构建聊天信息
     */
    private void createMsg() {
        String msg = this.mEtxtMsg.getText().toString();
        if (TextUtils.isEmpty(msg)) {
            ToastUtils.show(this, "请输入消息");
            return;
        }
        ChatMsg chatMsg = new ChatMsg();

        chatMsg.setChatMsg(msg);
        //消息为发送出去的消息
        chatMsg.setIsreceive(ChatMsg.SEND);
        //消息发送的时间
        chatMsg.setChattime(new Date());
        //消息的类型
        chatMsg.setChattype(ChatMsg.MSG_TYPE_TEXT);
        //消息的发送状态一定为未发送，后期如果发送出去了，会更新该状态
        chatMsg.setStatus(ChatMsg.STATUS_NOSEND);
        //设置消息状态为未读
        //...
        chatMsg.setContactid(friend.getContactid());

        //往listView中添加聊天内容
        chatAdapter.addData(chatMsg);
        this.mEtxtMsg.setText("");

        //listView移动到最后一条
        mListView.setSelection(chatAdapter.getCount() - 1);
    }

    /**
     * 初始化数据
     *
     * @return 聊天信息列表
     */
    private List<ChatMsg> initChat() {
        return chatMsgService.findChatMsgByContactId(friend.getContactid(), 0, 1000);
    }


    /**
     * 聊天信息的广播接收，采用动态注册，静态注册的话要在xml中声明
     */
    class ChatBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //判断action是否是前边意图发出的
            if (intent.getAction().equals(Constant.ACTION_MSG)) {
                //拿到传过来的数据
                Serializable obj = intent.getSerializableExtra(Constant.EXTRA_MSG);
                //判断是否是聊天信息
                if (obj instanceof ChatMsg) {
                    ChatMsg chatMsg = (ChatMsg) obj;
                    // 正在聊天的朋友和发来消息的是否是同一个人
                    if (chatMsg.getContactid() == friend.getContactid()) {
                        chatAdapter.addData(chatMsg);
                        mListView.setSelection(chatAdapter.getCount() - 1);
                        //把消息更新成已读
                        //chatMsgService.updateUnreadChatMsg(ChatActivity.this, friend.getContactid());
                    }
                }
            }
        }
    }
}

