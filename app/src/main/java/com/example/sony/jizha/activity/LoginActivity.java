package com.example.sony.jizha.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.sony.jizha.R;
import com.example.sony.jizha.Widget.BaseActivity;
import com.example.sony.jizha.Widget.ClearEditText;
import com.example.sony.jizha.http.BaseResponse;
import com.example.sony.jizha.http.RequestListener;
import com.example.sony.jizha.model.Member;
import com.example.sony.jizha.service.DataInitService;
import com.example.sony.jizha.system.Constant;
import com.example.sony.jizha.system.JzApplication;
import com.example.sony.jizha.utils.HttpCallBackListener;
import com.example.sony.jizha.utils.HttpUtils;
import com.example.sony.jizha.utils.MemberUtil;
import com.example.sony.jizha.utils.PreferencesUtils;
import com.example.sony.jizha.utils.ToastUtils;

import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;


/**
 * 登陆界面
 *
 * @ContentView(R.layout.activity_login)代替setContentView()
 * @InjectView(R.id.etxtEmail)private EditText mEtxtEmail;代替FindViewById()
 * Created by sony on 2015/9/9.
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";
    @InjectView(R.id.etxtEmail)
    private ClearEditText mEtxtEmail;

    @InjectView(R.id.etxtPwd)
    private ClearEditText mEtxtPwd;

    @InjectView(R.id.btnLogin)
    private Button mBtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化登陆按钮不可用,文字提示正在连接服务器
        mBtnLogin.setEnabled(false);
        mBtnLogin.setText(R.string.connectioning);

        //一直调用handler的post方法检测连接,调用子线程
        handler.post(new ConnectionCheckRunnable());
    }

    /**
     * Handler在子线程中处理发出的信息
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                //设置登陆按钮可用，提示信息为登陆
                mBtnLogin.setEnabled(true);
                mBtnLogin.setText(R.string.login);
            }
        }
    };

    /**
     * 检测连接，从本次获取保存的channelid
     */
    class ConnectionCheckRunnable implements Runnable {

        @Override
        public void run() {
            //客户端登陆时，开始与推送服务器连接，连接成功之后，保存相关信息。
            // 登陆时在这里获取保存的chanelId
            String channelid = PreferencesUtils.getString(LoginActivity.this, Constant.BD_CHANNELID);
            if (!TextUtils.isEmpty(channelid)) {
                handler.sendEmptyMessage(1);
            } else
                //2秒钟以后执行ConnectionCheckRunnable子线程
                handler.postDelayed(new ConnectionCheckRunnable(), 2000);
        }
    }

    /**
     * 获取两个EditText的值，执行登陆
     */
    public void login(View view) {
        String email = this.mEtxtEmail.getText().toString();
        String pwd = this.mEtxtPwd.getText().toString();

        if (TextUtils.isEmpty(email)) {
            //为空时左右晃动提示
            mEtxtEmail.setShakeAnimation();
            return;
        }

        if (TextUtils.isEmpty(pwd)) {
            //为空时左右晃动提示
            mEtxtPwd.setShakeAnimation();
            return;
        }

        doLogin(email, pwd);
    }

    /**
     * 登陆方法
     *
     * @param email 邮箱
     * @param pwd   密码
     */
    public void doLogin(String email, String pwd) {

        Map<String, String> map = new HashMap<String, String>(4);
        map.put("email", email);
        map.put("pwd", pwd);
        map.put("userId", PreferencesUtils.getString(this, Constant.BD_USERID));
        map.put("channelid", PreferencesUtils.getString(this, Constant.BD_CHANNELID));

        Log.d(TAG, "loginMember is " + map.get("email") + " : " + map.get("pwd") + " : " + map.get("userId") + " : " + map.get("channelid"));


        //post方式请求
        mHttpClient.post(Constant.API.URL_LOGIN, map, R.string.loading, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                Member member = response.getObj(Member.class);
                //保存Member信息
                saveMember(member);
                Log.d(TAG, "loginMember is " + member);
                //跳转到主界面
                jump(member.getId());
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(LoginActivity.this, msg);
            }

            @Override
            public void onRequestFail(int code, String msg) {
                ToastUtils.show(LoginActivity.this, msg);
            }
        });
    }

    /**
     * 页面跳转
     */
    private void jump(long memberid) {
        //登陆成功之后，开启服务
        startInitDataService(memberid);
        //跳转到主界面
        startActivity(new Intent(this, HomeActivity.class));

        this.finish();
    }

    /**
     * 开始初始化数据的服务
     */
    private void startInitDataService(long memberid) {
        //根据memberid判断数据是否初始化
        boolean isDataInit = PreferencesUtils.getBoolean(this, Constant.IS_DATA_INIT+memberid, false);
        Log.d(TAG, "isDataInit is " + isDataInit);
        // 如果没有初始化过数据就开启一天服务加载数据
        if (!isDataInit) {
            Log.d(TAG, "**************初始化信息服务开启**************************");
            startService(new Intent(this, DataInitService.class));
        }
    }

    /**
     * 保存会员登陆信息
     *
     * @param member
     */
    private void saveMember(Member member) {
        //保存信息到Application
        JzApplication.getInstance().setmLoginMember(member);
        //保存信息到preference
        MemberUtil.saveMember(this, member);
    }
}
