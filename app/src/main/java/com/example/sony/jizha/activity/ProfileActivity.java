package com.example.sony.jizha.activity;


import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sony.jizha.R;
import com.example.sony.jizha.Widget.BaseActivity;
import com.example.sony.jizha.Widget.CustomDialog;
import com.example.sony.jizha.http.BaseResponse;
import com.example.sony.jizha.http.RequestListener;
import com.example.sony.jizha.model.Contact;
import com.example.sony.jizha.model.Member;
import com.example.sony.jizha.model.Profile;
import com.example.sony.jizha.service.ContactService;
import com.example.sony.jizha.system.Constant;
import com.example.sony.jizha.system.JzApplication;
import com.example.sony.jizha.utils.ImageUtil;
import com.example.sony.jizha.utils.PreferencesUtils;
import com.example.sony.jizha.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

/**
 * Created with Android Studio
 * 项目名称：jizha
 * 类描述：用户详细信息activity
 * 创建人：sony
 * 创建时间：2015/12/31 16:09
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version V1.0
 */
@ContentView(R.layout.activity_profile)
public class ProfileActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ProfileActivity";

    //初始化联系人服务
    private ContactService mContactServer = ContactService.getInstance();

    //头像
    @InjectView(R.id.imageviewHead)
    private ImageView mImgHead;

    //姓名
    @InjectView(R.id.txtName)
    private TextView mTxtName;

    //账号
    @InjectView(R.id.txtEmail)
    private TextView mTxtEmail;

    //地址
    @InjectView(R.id.txtFrom)
    private TextView mTxtFrom;

    //年龄
    @InjectView(R.id.txtAge)
    private TextView mTxtAge;

    //公司
    @InjectView(R.id.txtCompany)
    private TextView mTxtCompany;

    //发送消息按钮
    @InjectView(R.id.btnMsgAt)
    private Button mBtnMsgAt;

    //好友添加按钮
    @InjectView(R.id.btnRequest)
    private Button mBtnRequest;

    //intent传递的值
    @InjectExtra(Constant.FRIEND)
    private Contact friend;

    //intent传递的值
    @InjectExtra("from")
    private int from;

    //自定义请求好友对话框
    private CustomDialog mSayHiDialog;

    //好友请求信息对话框
    private EditText mEtxtMsg;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //在actionBar上添加返回按钮
        initActionBarBasic();
        //展示好友基本信息
        displayMemeberInfo();
        //获取用户详细信息
        requestProfile();

        // 从搜索结果页面进来
        if (from == 1) {
            //获取memberid
            long memberid = PreferencesUtils.getLong(this, Constant.MEMBER_ID);
            //联系人的id与会员id相同，说明是本人查看详细信息

            Log.d(TAG, "---------------->friendid:" + friend.getContactid());

            if (friend.getContactid() == memberid) {
                mBtnMsgAt.setVisibility(View.GONE);
                mBtnRequest.setVisibility(View.GONE);
            }

            // 判断本地存不存在该好友，如果存在则是好友关系
            Contact tempFriend = mContactServer.getContact(this, friend.getContactid());
            // 非好友关系
            if (tempFriend == null) {
                //发送信息按钮隐藏
                mBtnMsgAt.setVisibility(View.GONE);
                //添加好友按钮显示
                mBtnRequest.setVisibility(View.VISIBLE);
            } else { // 好友关系
                //发送信息按钮显示
                mBtnMsgAt.setVisibility(View.VISIBLE);
                //添加好友按钮隐藏
                mBtnRequest.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 获取用户详细信息
     */
    private void requestProfile() {

        //传递的值
        Map<String, String> pramas = new HashMap<String, String>(2);
        pramas.put("memberid", friend.getContactid() + "");
        pramas.put("token", PreferencesUtils.getString(this, Constant.ACCESS_TOKEN));

        //通过memberid获取详细信息
        mHttpClient.post(Constant.API.URL_FRIEND_PROFILE, pramas, 0, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {

                if (response.isSuccess())
                    disPlayProfile(response.getObj(Profile.class));
                else
                    ToastUtils.show(ProfileActivity.this, R.string.load_fail);
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(ProfileActivity.this, R.string.load_fail);
            }

            @Override
            public void onRequestFail(int code, String msg) {
                ToastUtils.show(ProfileActivity.this, R.string.load_fail);
            }
        });

    }

    /**
     * 展示详细信息
     *
     * @param profile 将服务器端返回的数据解析成Profile对象
     */
    private void disPlayProfile(Profile profile) {
        //设置城市年龄等
        if (TextUtils.isEmpty(profile.getProvince()) && TextUtils.isEmpty(profile.getCity())) {
            mTxtFrom.setText(R.string.planet);
        } else {
            String from = profile.getProvince() + "-" + profile.getCity();
            mTxtFrom.setText(from);
        }
        mTxtAge.setText(profile.getAge() + "");
        mTxtCompany.setText(profile.getCompany());
    }

    /**
     * 展示会员基本信息
     */
    private void displayMemeberInfo() {
        //设置姓名
        mTxtName.setText(friend.getName());
        //设置账号
        mTxtEmail.setText("账号:" + friend.getEmail());
        //设置头像显示
        ImageUtil.displayImageUseDefOptions(friend.getHeadbig(), mImgHead);
    }

    /**
     * 发送信息按钮的点击事件，跳转到聊天界面
     *
     * @param view profileActivity
     */
    public void msgAt(View view) {
        Intent intent = new Intent(ProfileActivity.this, ChatActivity.class);
        intent.putExtra(Constant.FRIEND, friend);
        startActivity(intent);
    }


    /**
     * 添加好友按钮的点击事件
     *
     * @param view
     */
    public void requestClick(View view) {
        //创建请求对话框
        buildDialog();
        //弹出对话框
        mSayHiDialog.show();
    }

    /**
     * 创建请求好友对话框
     */
    private void buildDialog() {

        if (mSayHiDialog == null) {
            View view = getLayoutInflater().inflate(R.layout.dialog_friend_request, null);

            Button btnClose = (Button) view.findViewById(R.id.btnCancel);
            Button btnSend = (Button) view.findViewById(R.id.btnYes);
            mEtxtMsg = (EditText) view.findViewById(R.id.etxtMsg);

            btnClose.setOnClickListener(this);
            btnSend.setOnClickListener(this);

            //初始化好友请求请求对话框
            mSayHiDialog = new CustomDialog(this, view);
        }
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnCancel) {
            if (mSayHiDialog != null)
                mSayHiDialog.dismiss();
        } else if (v.getId() == R.id.btnYes) {
            //发送好友请求
            sendRequest();
        }
    }

    /**
     * 发送添加好友请求
     */
    private void sendRequest() {

        String msg = mEtxtMsg.getText().toString();
        //获取登陆用户信息
        Member member = JzApplication.getInstance().getmLoginMember();

        Log.d(TAG,"------------>member:" + member.toString());

        Map<String, String> params = new HashMap<String, String>();

        params.put("memberid", member.getId() + "");
        params.put("friendid", friend.getContactid() + "");
        params.put("msg", msg);
        params.put("token", member.getToken());

        //向服务端请求信息
        mHttpClient.post(Constant.API.URL_FRIENDS_REQUEST, params, R.string.sending, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {

                if (response.isSuccess()) {
                    ToastUtils.show(ProfileActivity.this, R.string.send_success);
                    mSayHiDialog.dismiss();
                } else
                    ToastUtils.show(ProfileActivity.this, R.string.send_fail);
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(ProfileActivity.this, R.string.send_fail);
            }

            @Override
            public void onRequestFail(int code, String msg) {
                ToastUtils.show(ProfileActivity.this, R.string.send_fail);
            }
        });
    }
}
