package com.example.sony.jizha.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sony.jizha.R;
import com.example.sony.jizha.activity.ProfileActivity;
import com.example.sony.jizha.activity.RequestMsgActivity;
import com.example.sony.jizha.adapter.ContactAdapter;
import com.example.sony.jizha.model.Contact;
import com.example.sony.jizha.service.ContactService;
import com.example.sony.jizha.service.RequestMsgService;
import com.example.sony.jizha.system.Constant;
import com.example.sony.jizha.system.JzApplication;
import com.example.sony.jizha.utils.PreferencesUtils;
import com.example.sony.jizha.utils.SideBar;
import com.example.sony.jizha.utils.ToastUtils;

import java.util.List;

import roboguice.fragment.RoboFragment;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created with Android Studio
 * 项目名称：jizha
 * 类描述：联系人列表fragment
 * 创建人：sony
 * 创建时间：2015/12/30 15:42
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version V1.0
 */
@ContentView(R.layout.fragment_contactlist)
public class ContactListFragment extends RoboFragment implements AdapterView.OnItemClickListener {

    public static final String TAG = "ContactListFragment";

    //列表显示
    @InjectView(R.id.listViewContact)
    private StickyListHeadersListView mListView;

    //进度条
    @InjectView(R.id.progressBar)
    private ProgressBar mProgressBar;

    //侧边栏
    @InjectView(R.id.sideBar)
    private SideBar mSideBar;

    //联系人列表适配器
    private ContactAdapter mAdapter;
    //联系人服务
    private ContactService contactService;
    //从服务器端获取的联系人列表
    private List<Contact> mContacts;
    //添加好友请求文本数量
    private TextView mTxtBadge;
    //初始化好友请求服务
    private RequestMsgService mRequestService = RequestMsgService.getInstance();

    //在Fragment的生命周期中，onAttach()和onDetach()之间getActivity()函数才会返回正确的对象，否则的话返回null。
    //Android程序中Application、Service和Activity都实现了Context，
    // 但只有Application才能保证在程序运行期间一直存在并且具有唯一性，因此在程序中可以使用Application来获得Context而不用担心空指针。
    private Context context = JzApplication.getInstance();

    //接收同意添加好友请求的通知
    private ContactBroadcastReceiver receiver;

    //布局添加对象
    private LayoutInflater mInflater;

    /**
     * 构造函数
     */
    public ContactListFragment() {
        //初始化联系人服务实例
        contactService = ContactService.getInstance();
        //初始化布局添加对象
        mInflater = LayoutInflater.from(context);
    }

    /**
     * 循环判断获取联系人初始化完成没有，处理返回的信息
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //消息处理
            if (msg.what == Constant.SUCCESS) {
                //隐藏进度条
                mProgressBar.setVisibility(View.GONE);
                //将数据放入适配器
                mAdapter = new ContactAdapter(context, mContacts);
                mListView.setAdapter(mAdapter);
            }
        }
    };

    /**
     * ListView的item点击监听事件
     *
     * @param parent   adapter
     * @param view     fragment
     * @param position 位置
     * @param id       Serializable接口id
     */
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //根据位置获取联系人对象
        Contact profile = (Contact) parent.getAdapter().getItem(position);
        //跳转到详细信息界面
        Intent intent = new Intent(context, ProfileActivity.class);
        //intent传值
        intent.putExtra(Constant.FRIEND, profile);

        Log.d(TAG, "----------->profile:" + profile);

        intent.putExtra("from", 1);

        startActivity(intent);
    }

    /**
     * 判断联系人有没有加载完成
     */
    class LoadContactRunnable implements Runnable {
        @Override
        public void run() {
            //判断是否完成初始化数据
            boolean isInitFinish = PreferencesUtils.getBoolean(context, Constant.IS_DATA_INIT + PreferencesUtils.getLong(getActivity(), Constant.MEMBER_ID));

            Log.d(TAG, "---------------->isInitFinish:" + isInitFinish);
            //初始化联系人数据完成
            if (isInitFinish) {
                //获取联系人列表
                mContacts = contactService.findMemeberContacts(context);
                handler.sendEmptyMessage(Constant.SUCCESS);
            } else
                handler.postDelayed(new LoadContactRunnable(), 2000);
        }
    }

    /**
     * 刚创建fragment 时执行
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "------------->ContactFragment  Create");
    }

    /**
     * 创建view时执行
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //添加view
        View view = inflater.inflate(R.layout.fragment_contactlist, container, false);
        Log.d(TAG, "------------->ContactFragment View  Create");
        return view;
    }

    /**
     * 创建view完成之后执行
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //绑定item点击事件
        mListView.setOnItemClickListener(this);

        Log.d(TAG, "------------->ContactFragment View  Created");

        //执行消息处理
        handler.post(new LoadContactRunnable());

        //侧边栏改变的监听事件
        mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //获取侧边栏选择的字母
                int position = mAdapter.getPositionForSection(s.charAt(0));
                //根据字母显示listView
                mListView.setSelection(position);
            }
        });

        //初始化列表上布局
        initHeadView();
        //初始化添加好友请求信息显示
        initBadge();
    }

    /**
     * 开始fragment，注册通知
     */
    @Override
    public void onStart() {
        super.onStart();

        receiver = new ContactBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ACTION_CONTACT_UPDATE);
        filter.addAction(Constant.ACTION_MAKE_FRIEND_REQUEST);
        this.getActivity().registerReceiver(receiver, filter);
    }

    /**
     * fragment结束
     */
    @Override
    public void onStop() {
        super.onStop();
        this.getActivity().unregisterReceiver(receiver);
    }


    /**
     * 用于接收同意添加好友请求时添加的好友广播
     */
    class ContactBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constant.ACTION_CONTACT_UPDATE)) {
                //重新加载好友
                reloadContacts();
            } else if (intent.getAction().equals(Constant.ACTION_MAKE_FRIEND_REQUEST)) {
                initBadge();
            }
        }
    }


    /**
     * 重新加载好友
     */
    private void reloadContacts() {
        //重新查找好友列表
        mContacts = contactService.findMemeberContacts(getActivity());
        //清除数据
        mAdapter.clear();
        //添加数据
        mAdapter.addData(mContacts);
    }

    /**
     * 初始化图表数量显示
     */
    private void initBadge() {
        //查询未处理的添加好友请求
        int addFriendRequestCount = mRequestService.countUnHandleMsg(getActivity());
        if (addFriendRequestCount > 0) {
            mTxtBadge.setVisibility(View.VISIBLE);
            mTxtBadge.setText(addFriendRequestCount + "");
        } else
            mTxtBadge.setVisibility(View.GONE);
    }

    /**
     * 初始化好友列表显示
     */
    private void initHeadView() {
        View headView = mInflater.inflate(R.layout.template_contact_head, null);

        View newFriendView = headView.findViewById(R.id.layoutNewFriend);

        View layoutNiu = headView.findViewById(R.id.layoutNiu);

        //新的好友点击事件
        newFriendView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到添加好友请求列表界面
                Intent intent = new Intent(getActivity(), RequestMsgActivity.class);
                //跳转处理返回的结果
                startActivityForResult(intent, Constant.REQUEST_CODE);
            }
        });

        //技术大牛点击事件
        layoutNiu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ToastUtils.show(context, "功能未开放");
            }
        });

        //初始化数量显示
        mTxtBadge = (TextView) headView.findViewById(R.id.txtBadge);
        //列表添加布局
        mListView.addHeaderView(headView, null, false);
    }

    /**
     * 处理返回的结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //重新加载联系人列表
        reloadContacts();
        //初始化消息数量显示
        initBadge();
    }
}


