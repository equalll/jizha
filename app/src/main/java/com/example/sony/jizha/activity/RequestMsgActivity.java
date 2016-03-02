package com.example.sony.jizha.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.sony.jizha.R;
import com.example.sony.jizha.Widget.BaseActivity;
import com.example.sony.jizha.adapter.RequestMsgAdapter;
import com.example.sony.jizha.model.RequestMsg;
import com.example.sony.jizha.service.RequestMsgService;
import com.example.sony.jizha.system.Constant;

import java.util.List;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;


/**
 * Created with Android Studio
 * 项目名称：jizha
 * 类描述：添加好友请求列表显示activity
 * 创建人：sony
 * 创建时间：2016/1/4 18:57
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version V1.0
 */
@ContentView(R.layout.activity_request_msg)
public class RequestMsgActivity extends BaseActivity {

    @InjectView(R.id.listView)
    private ListView mListView;

    //添加好友请求列表数据
    private List<RequestMsg> mDatas;

    //添加好友请求列表适配器
    private RequestMsgAdapter mAdapter;

    //初始化添加好友请求服务
    private RequestMsgService mRequestService = RequestMsgService.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化actionBar
        initActionBarBasic();
        //查找添加好友的请求
        mDatas = mRequestService.findRequestMsg(this);
        //初始化添加好友请求适配器
        mAdapter = new RequestMsgAdapter(this, mDatas);

        mListView.setAdapter(mAdapter);
    }

    /**
     * 点击actionBar的返回按钮
     */
    @Override
    public void onBackPressed() {
        setResult(Constant.RESULT_CODE);
        this.finish();
    }
}
