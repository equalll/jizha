package com.example.sony.jizha.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.sony.jizha.R;
import com.example.sony.jizha.Widget.BaseActivity;
import com.example.sony.jizha.adapter.FriendAdapter;
import com.example.sony.jizha.http.BaseResponse;
import com.example.sony.jizha.http.RequestListener;
import com.example.sony.jizha.model.Contact;
import com.example.sony.jizha.system.Constant;
import com.example.sony.jizha.utils.PreferencesUtils;
import com.example.sony.jizha.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

/**
 * Created with Android Studio
 * 项目名称：jizha
 * 类描述：好友搜索结果显示activity
 * 创建人：sony
 * 创建时间：2015/12/30 15:42
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version V1.0
 */
@ContentView(R.layout.activity_friend_search_result)
public class FriendSearchResultActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "FriendSearchResultActivity";
    @InjectView(R.id.listView)
    private ListView mListView;

    //好友搜索列表适配器
    private FriendAdapter mAdapter;

    //好友搜索列表
    private List<Contact> mFriends;

    //获取传递过来的关键字
    @InjectExtra(Constant.WORD)
    private String word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化actionBar
        initActionBarBasic();
        mListView.setOnItemClickListener(this);
        //查询好友列表
        queryFriends();
    }


    /**
     * 查询好友
     */
    private void queryFriends() {
        //put传值
        Map<String, String> params = new HashMap<String, String>();
        params.put("word", word);
        //params.put("token", PreferencesUtils.getString(this, Constant.ACCESS_TOKEN));

        //向服务端请求数据
        mHttpClient.post(Constant.API.URL_FRIENDS_QUERY, params, R.string.loading, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {

                if (response.isSuccess()) {
                    System.out.println("----------------->response:" + response.getData());
                    mFriends = response.getList(Contact.class);

                    if (mFriends != null) {
                        mAdapter = new FriendAdapter(FriendSearchResultActivity.this, mFriends);
                        mListView.setAdapter(mAdapter);
                    } else {
                        ToastUtils.show(FriendSearchResultActivity.this, R.string.empty_result);
                        finish();
                    }
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(FriendSearchResultActivity.this, R.string.error_result);
            }

            @Override
            public void onRequestFail(int code, String msg) {
                ToastUtils.show(FriendSearchResultActivity.this, R.string.error_result);
            }
        });
    }

    /**
     * listView的item的点击事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        Contact friend = mAdapter.getItem(position);
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(Constant.FRIEND, friend);
        intent.putExtra("from", 1);

        startActivity(intent);
    }
}
