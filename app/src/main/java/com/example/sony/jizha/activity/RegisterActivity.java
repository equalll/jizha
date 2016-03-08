package com.example.sony.jizha.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.sony.jizha.R;
import com.example.sony.jizha.Widget.BaseActivity;
import com.example.sony.jizha.Widget.ClearEditText;
import com.example.sony.jizha.http.BaseResponse;
import com.example.sony.jizha.http.RequestListener;
import com.example.sony.jizha.model.Member;
import com.example.sony.jizha.system.Constant;
import com.example.sony.jizha.utils.PreferencesUtils;
import com.example.sony.jizha.utils.ToastUtils;


import java.util.HashMap;
import java.util.Map;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_register)
public class RegisterActivity extends BaseActivity {

    private static final String TAG = "RegisterActivity";

    @InjectView(R.id.etxtEmail)
    private ClearEditText mEtxtEmail;

    @InjectView(R.id.etxtPwd)
    private ClearEditText mEtxtPwd;

    @InjectView(R.id.suretxtPwd)
    private ClearEditText mSureEtxtPwd;

    @InjectView(R.id.btnRegister)
    private Button mBtnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 注册监听事件
     *
     * @param view
     */
    public void register(View view) {
        final String pwd = this.mEtxtPwd.getText().toString();
        String surePwd = this.mSureEtxtPwd.getText().toString();
        final String email = this.mEtxtEmail.getText().toString();

        if (!pwd.equals(surePwd)) {
            ToastUtils.show(this, "两次密码输入不一致!!!");
            return;
        }

        Map<String, String> map = new HashMap<String, String>(4);
        map.put("email", email);
        map.put("pwd", pwd);

        mHttpClient.post(Constant.API.URL_REGISTER, map, R.string.now_activity_register, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                Log.d(TAG, "------------->注册response：" + response);
                if (response.isSuccess()) {
                    ToastUtils.show(RegisterActivity.this, "注册成功，请登录");
                    //跳转到登陆界面
                    jump();
                } else {
                    ToastUtils.show(RegisterActivity.this, "两次密码输入不一致!!!");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(RegisterActivity.this, msg);
            }

            @Override
            public void onRequestFail(int code, String msg) {
                ToastUtils.show(RegisterActivity.this, msg);
            }
        });
    }

    /**
     * 跳转到登陆界面
     */
    private void jump() {
        //跳转到主界面
        startActivity(new Intent(this, LoginActivity.class));
    }
}
