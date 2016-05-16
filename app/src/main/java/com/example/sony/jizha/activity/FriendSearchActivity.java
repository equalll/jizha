package com.example.sony.jizha.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sony.jizha.R;
import com.example.sony.jizha.Widget.BaseActivity;
import com.example.sony.jizha.system.Constant;
import com.example.sony.jizha.system.JzApplication;
import com.example.sony.jizha.utils.ToastUtils;

import org.w3c.dom.Text;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * Created with Android Studio
 * 项目名称：jizha
 * 类描述：好友搜索activity
 * 创建人：sony
 * 创建时间：2015/12/30 15:42
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version V1.0
 */
@ContentView(R.layout.activity_friend_search)
public class FriendSearchActivity extends BaseActivity implements TextView.OnEditorActionListener {

    @InjectView(R.id.etxtWord)
    private EditText mEtxtWord;

    @InjectView(R.id.searchByContact)
    private TextView searchByContact;

    @InjectView(R.id.searchByQQ)
    private TextView searchByQQ;

    @InjectView(R.id.searchByScan)
    private TextView searchByScan;

    // 但只有Application才能保证在程序运行期间一直存在并且具有唯一性，因此在程序中可以使用Application来获得Context而不用担心空指针。
    private Context context = JzApplication.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化actionBar
        initActionBarBasic();
        //文本框的动作监听事件
        mEtxtWord.setOnEditorActionListener(this);


    }


    /**
     * 菜单的选择事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * 查询的点击事件
     *
     * @param v
     */
    public void query(View v) {
        queryFriends();
    }

    /**
     * 通过通讯录查询好友
     *
     * @param v
     */
    public void searchByContact(View v) {
        ToastUtils.show(context,"通过通讯录查询好友功能未开放");
    }

    /**
     * 通过QQ查询好友
     *
     * @param v
     */
    public void searchByQQ(View v) {
        ToastUtils.show(context, "通过QQ查询好友功能未开放");
    }

    /**
     * 通过二维码查询好友
     *
     * @param v
     */
    public void searchByScan(View v) {
        ToastUtils.show(context, "通过二维码查询好友功能未开放");
    }

    /**
     * 查询好友列表
     */
    private void queryFriends() {

        String word = mEtxtWord.getText().toString().trim();

        if (TextUtils.isEmpty(word)) {
            ToastUtils.show(this, R.string.search_friends);
            return;
        }

        //跳转到结果查询结果返回的activity
        Intent intent = new Intent(this, FriendSearchResultActivity.class);
        //将关键字传递过去
        intent.putExtra(Constant.WORD, word);
        startActivity(intent);
    }

    /**
     * 文本框的监听事件
     * @param v
     * @param actionId
     * @param event
     * @return
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if (actionId == EditorInfo.IME_ACTION_SEARCH
                || actionId == EditorInfo.IME_ACTION_DONE
                || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode()
                && KeyEvent.ACTION_DOWN == event.getAction())) {
            //处理事件
            queryFriends();
            return true;
        }
        return false;
    }
}
