package com.example.sony.jizha.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.webkit.WebView;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.example.sony.jizha.R;
import com.example.sony.jizha.system.Constant;
import com.example.sony.jizha.utils.ManifestUtil;
import com.example.sony.jizha.utils.PreferencesUtils;


/**
 * Created with Android Studio
 * 项目名称：jizha
 * 类描述：开始界面activity
 * 创建人：sony
 * 创建时间：2015/12/30 15:42
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version V1.0
 */
public class StartActivity extends Activity {

    //网页视图
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //应用向百度云推送服务器发送请求连接，请求成功，在PushMessageReceiver.onBind()中执行
        PushManager.startWork(this, PushConstants.LOGIN_TYPE_API_KEY, ManifestUtil.getMetaValue(this, Constant.BD_API_KEY));

        //实例化webView
        mWebView = (WebView) this.findViewById(R.id.webview);
        //设置webView纵向、横向滚动条为无
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);


        //让webview只显示一列，也就是自适应页面大小 不能左右滑动（4.4以上无效）
        //mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        //让页面适应手机屏幕的分辨率，完整的显示在屏幕上，可以放大缩小
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);

        //加载html
        mWebView.loadUrl(Constant.API.URL_START);

        //用线程延迟3秒跳转界面
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //跳转界面
                jump();
            }
        }, 3000);
    }

    /**
     * 应该检查会员信息，没有服务器端，直接检查与百度云的连接，有链接就跳，没有就登陆
     */
    private void jump() {
        //获取member信息，如果为空，说明没保存信息，也就是没登陆过
        String memberJson = PreferencesUtils.getString(this, Constant.MEMBER);
        if (TextUtils.isEmpty(memberJson)) {
            //跳转到登陆界面
            startActivity(new Intent(this, LoginActivity.class));
            this.finish();
        } else {
            //跳转到主界面
            startActivity(new Intent(this, HomeActivity.class));
            this.finish();
        }
    }
}
