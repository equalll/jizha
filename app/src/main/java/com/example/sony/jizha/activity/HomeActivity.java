package com.example.sony.jizha.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.sony.jizha.R;
import com.example.sony.jizha.Widget.BaseActivity;
import com.example.sony.jizha.fragment.ChatListFragment;
import com.example.sony.jizha.fragment.ContactListFragment;
import com.example.sony.jizha.fragment.MoreFragment;
import com.example.sony.jizha.utils.TabUtils;
import com.umeng.update.UmengUpdateAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio
 * 项目名称：jizha
 * 类描述：主页显示activity
 * 创建人：sony
 * 创建时间：2015/12/30 15:42
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version V1.0
 */
public class HomeActivity extends BaseActivity implements ActionBar.TabListener, ViewPager.OnPageChangeListener, ChatListFragment.ChatListener {

    private static final String TAG = "HomeActivity";
    //tab
    private List<Tab> mTabs;
    //标题
    private ActionBar mActionBar;
    //视图页
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //监测友盟的集成对不对,false为不检测。默认为true
        //UmengUpdateAgent.setUpdateCheckConfig(false);

        //设置非wifi下更新
        //UmengUpdateAgent.setUpdateOnlyWifi(false)

        //默认wifi情况在会下载友盟自动更新版本
        UmengUpdateAgent.update(this);


        setContentView(R.layout.activity_home);

        //实例化ViewPager
        mViewPager = (ViewPager) this.findViewById(R.id.pager);
        //初始化Tabs
        initTabs();
        //初始化ActionBar
        initActionBar();
    }

    /*
    * 初始化mTabs
    */
    private void initTabs() {
        mTabs = new ArrayList<Tab>(3);

        mTabs.add(new Tab(R.string.chat, ChatListFragment.class));
        mTabs.add(new Tab(R.string.contact, ContactListFragment.class));
        mTabs.add(new Tab(R.string.more, MoreFragment.class));
    }

    /**
     * 初始化ActionBar
     */
    private void initActionBar() {

        mActionBar = getSupportActionBar();
        mActionBar.setTitle(R.string.app_name);
        //activity中显示logo
        ////使左上角图标是否显示，如果设成false，则没有程序图标，仅仅就个标题，否则，显示应用程序图标
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setLogo(R.drawable.logo);
        mActionBar.setDisplayUseLogoEnabled(true);

        //setHomeButtonEnabled这个小于4.0版本是默认为true的。该方法的作用：决定左上角的图标是否可以点击。没有向左的小图标。
        // parameters  : true 图标可以点击  false 不可以点击。
        // setDisplayHomeAsUpEnabled，该方法是 决定左上角图标的右侧是否有向左的小箭头。
        // parameters  : true 有小箭头，并且图标可以点击  false 没有小箭头，并且图标不可以点击。
        mActionBar.setDisplayHomeAsUpEnabled(false);
        //设置导航的模式
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        //循环添加Tabs
        for (Tab t : mTabs) {
            ActionBar.Tab tab = mActionBar.newTab();

            //tab.setText(t.getTxt());
            //读取Tab,设置text以及数量,为0时不显示
            tab.setCustomView(TabUtils.renderTabView(this, t.getTxt(), 0));

            //添加tab的监听
            tab.setTabListener(this);
            mActionBar.addTab(tab);
        }
        // activity 中嵌套fragment时使用。Fragment里面嵌套Fragment 的话：一定要用getChildFragmentManager();
        mViewPager.setAdapter(new TabFragmentPagerAdapter(getSupportFragmentManager()));
        //添加viewPager的监听事件
        mViewPager.setOnPageChangeListener(this);

        //设置数字提示
        //TabUtils.updateTabBadge(mActionBar.getTabAt(0), 10);
        //TabUtils.updateTabBadge(mActionBar.getTabAt(1), 20);
    }

    //**********************ActionBar监听方法
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        //根据tab选择的位置，ViewPager随之切换
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    //*****************ViewPager监听方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //根据ViewPager滑动的位置，Tab随之切换
        mActionBar.selectTab(mActionBar.getTabAt(position));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    /**
     * 在tab中显示未读消息的数量
     *
     * @param count 未读消息的数量
     */
    @Override
    public void unReadMsgCount(int count) {

        Log.d(TAG, "-------------->ChatListener 执行:" + count);
        //更新第一个tab上的数量显示
        TabUtils.updateTabBadge(mActionBar.getTabAt(0), count);
    }

    /**
     * Tab实体
     */
    class Tab {

        private int txt;
        private Class fragment;

        /**
         * 构造函数
         *
         * @param txt      tab标题
         * @param fragment 片段
         */
        Tab(int txt, Class fragment) {
            this.txt = txt;
            this.fragment = fragment;
        }

        public int getTxt() {

            return txt;
        }

        public void setTxt(int txt) {
            this.txt = txt;
        }

        public Class getFragment() {
            return fragment;
        }

        public void setFragment(Class fragment) {
            this.fragment = fragment;
        }
    }

    /**
     * 自定义adapter
     */
    class TabFragmentPagerAdapter extends FragmentPagerAdapter {

        public TabFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            try {
                //根据选择的tab实例化对应的fragment
                return (Fragment) mTabs.get(position).getFragment().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }
    }


    //菜单


    /**
     * 创建菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
        return true;
    }

    /**
     * 菜单的选择事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add_friend) {
            startActivity(new Intent(this, FriendSearchActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}

