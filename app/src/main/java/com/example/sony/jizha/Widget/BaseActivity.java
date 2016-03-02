package com.example.sony.jizha.Widget;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;


import com.example.sony.jizha.http.VolleyHttpClient;
import com.example.sony.jizha.system.Constant;

import roboguice.activity.RoboActionBarActivity;

/**
 * 基础activity，继承RoboActionBarActivity，实现注解功能，初始化volley
 * Created by sony on 2015/9/7.
 */
public class BaseActivity extends RoboActionBarActivity {

    //实例化volley请求
    protected VolleyHttpClient mHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //实例化VolleyHttpClient对象
        mHttpClient = VolleyHttpClient.getInstance(this);
    }

    /**
     * 初始化actionbar，在actionBar上添加返回按钮
     */
    protected  void initActionBarBasic(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * 设置actionBar返回按钮的选择事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            setResult(Constant.RESULT_CODE);
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
