package com.example.sony.jizha.fragment;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.sony.jizha.R;
import com.example.sony.jizha.activity.ChatActivity;
import com.example.sony.jizha.adapter.ChatListAdapter;
import com.example.sony.jizha.adapter.ChatListAdapter1;
import com.example.sony.jizha.model.ChatMsg;
import com.example.sony.jizha.model.ChatMsgEx;
import com.example.sony.jizha.service.ChatMsgService;
import com.example.sony.jizha.system.Constant;

import java.io.Serializable;
import java.util.List;

import roboguice.fragment.RoboFragment;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * 聊天列表
 * Created by sony on 2015/9/7.
 */
public class ChatListFragment extends RoboFragment implements AdapterView.OnItemClickListener {

    public static final String TAG = "ChatListFrament";

    //listView
    @InjectView(R.id.listView)
    private ListView mListView;

    //适配器对象对象
    private ChatListAdapter1 mAdapter;

    //聊天信息服务对象
    private ChatMsgService mChatMsgService;
    //聊天信息列表对象
    private List<ChatMsgEx> mChatMsgs;

    //聊天信息广播接受者对象
    private ChatBroadcastReceiver receiver;

    //聊天消息监听器兑现
    private ChatListener chatListener;

    //未读消息的数量
    private int unReadChatMsgCount;


    /**
     * Fragment和Activity建立关联的时候调用（获得activity的传递的值）
     *
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof ChatListener)
            chatListener = (ChatListener) activity;
        else
            throw new ClassCastException(activity.toString()
                    + " must implement ChatListener");
    }


    /**
     * 创建Fragment
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化聊天消息服务
        mChatMsgService = ChatMsgService.getInstance();
        //获取未读消息的数量
        unReadChatMsgCount = mChatMsgService.findUnreadMsg(this.getActivity()).size();

        Log.d(TAG, "-------------->unReadChatMsgCount:" + unReadChatMsgCount);
    }


    /**
     * 为Fragment创建视图（加载布局）时调用（给当前的fragment绘制UI布局，可以使用线程更新UI）
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chatlist, container, false);

        return view;
    }

    /**
     * fragment视图创建完成之后
     *
     * @param view
     * @param savedInstanceState
     */
    @Nullable
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //查询聊天的历史消息
        mChatMsgs = mChatMsgService.findHistoryChatMsg(getActivity());

        //初始化适配器
        mAdapter = new ChatListAdapter1(getActivity(), mChatMsgs);
        mListView.setAdapter(mAdapter);

        //listView的item点击事件
        mListView.setOnItemClickListener(this);

        //显示未读消息数量
        showTotalUnreadMsgCount(mChatMsgs);
    }


    /**
     * listViewde的item的点击事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //根据位置获取聊天信息的具体对象
        ChatMsgEx chatMsgEx = mAdapter.getItem(position);

        //点击之后跳转到聊天的界面 ChatActivity
        Intent intent = new Intent(getActivity(), ChatActivity.class);

        //将联系人的信息传递
        intent.putExtra(Constant.FRIEND, chatMsgEx.getContact());

        //从ChatActivity返回时需要更新ChatFragment中的信息显示，所以需要进行结果返回的处理
        startActivityForResult(intent, 10000);
    }


    /**
     * 从ChatActivity返回时需要更新ChatFragment中的信息显示，所以需要进行结果返回的处理
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //重新加载聊天信息
        loadMsg();
    }


    /**
     * fragment开始时执行
     */
    @Override
    public void onStart() {
        super.onStart();

        receiver = new ChatBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ACTION_MSG);

        getActivity().registerReceiver(receiver, filter);
    }


    /**
     * fragment结束时执行
     */
    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(receiver);
    }


    /**
     * 聊天信息的广播接收者
     */
    class ChatBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //判断是否HomeActivity传递出来的数据
            if (intent.getAction().equals(Constant.ACTION_MSG)) {
                Serializable obj = intent.getSerializableExtra(Constant.EXTRA_MSG);
                if (obj instanceof ChatMsg) {
                    //加载聊天信息
                    loadMsg();
                }
            }
        }
    }


    /**
     * 重新加载聊天信息
     */
    private void loadMsg() {

        //查询聊天的历史记录
        mChatMsgs = mChatMsgService.findHistoryChatMsg(getActivity());

        Log.d(TAG,"------------>historyChatMsgCount:"+mChatMsgs.size());

        //清除适配器中的数据
        mAdapter.clear();
        //往适配器添加数据
        mAdapter.addData(mChatMsgs);

        //显示未读信息数量
        showTotalUnreadMsgCount(mChatMsgs);
    }

    /**
     * 显示未读消息数量
     *
     * @param msgs
     */
    private void showTotalUnreadMsgCount(List<ChatMsgEx> msgs) {

        int count = 0;
        if (msgs != null && msgs.size() > 0) {
            for (ChatMsgEx msg : msgs) {
                msg.setUnreadCount(unReadChatMsgCount);
                count += msg.getUnreadCount();
            }
            chatListener.unReadMsgCount(count);
        }
    }


    /**
     * 消息监听器，用于更新未读消息数量，fragment与activity的交互
     */
    public interface ChatListener {
        public void unReadMsgCount(int count);
    }
}
