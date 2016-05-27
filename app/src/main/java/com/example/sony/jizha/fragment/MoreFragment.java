package com.example.sony.jizha.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sony.jizha.R;
import com.example.sony.jizha.Widget.CustomDialog;
import com.example.sony.jizha.activity.StartActivity;
import com.example.sony.jizha.model.Member;
import com.example.sony.jizha.system.JzApplication;
import com.example.sony.jizha.utils.ImageUtil;
import com.example.sony.jizha.utils.MemberUtil;
import com.example.sony.jizha.utils.ToastUtils;
import com.thefinestartist.finestwebview.FinestWebView;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * 更多列表
 * Created by sony on 2015/9/7.
 */
public class MoreFragment extends RoboFragment implements View.OnClickListener {


    @InjectView(R.id.imageviewHead)
    private ImageView mImgHead;

    @InjectView(R.id.txtName)
    private TextView mTxtName;

    @InjectView(R.id.txtEmail)
    private TextView mTxtEmail;

    @InjectView(R.id.viewSetting)
    private View mViewSetting;

    @InjectView(R.id.viewAboutus)
    private View mViewAboutus;

    @InjectView(R.id.btnLogout)
    private Button mBtnLogout;

    @InjectView(R.id.viewSetting)
    private RelativeLayout viewSetting;

    @InjectView(R.id.viewAboutus)
    private RelativeLayout viewAboutus;

    // 但只有Application才能保证在程序运行期间一直存在并且具有唯一性，因此在程序中可以使用Application来获得Context而不用担心空指针。
    private Context context = JzApplication.getInstance();

    //自定义添加好友请求对话框
    private CustomDialog mDialog;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_more, container, false);

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //显示当前用户信息
        displayProfile();
        //绑定退出登录按钮的点击事件
        mBtnLogout.setOnClickListener(this);

        //分享应用点击事件
        viewSetting.setOnClickListener(this);

        //关于我们点击事件
        viewAboutus.setOnClickListener(this);

    }

    /**
     * 点击事件的监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogout) {
            showConfirmDialog();
        }
        if (v.getId() == R.id.viewSetting) {
            showShare();
        }
        if (v.getId() == R.id.viewAboutus) {
            showUsDes();
        }
    }

    /**
     * 显示个人信息
     */
    private void showUsDes() {

        String path = "file:///android_asset/project_description.html";

        new FinestWebView.Builder(this.getActivity())
                .theme(R.style.FinestWebViewTheme_Light)
                .titleDefault("关于叽喳")
                .statusBarColorRes(R.color.white)
                .toolbarColorRes(R.color.white)
                .titleColorRes(R.color.black)
                .titleSize(40)
                .iconDefaultColorRes(R.color.finestBlack)
                .progressBarColorRes(R.color.finestBlack)
                .webViewDisplayZoomControls(true)
                .swipeRefreshColorRes(R.color.black)
                .menuColorRes(R.color.white)
                .menuTextColorRes(R.color.finestBlack)
                .dividerHeight(0)
                .gradientDivider(false)
                .setCustomAnimations(R.anim.activity_open_enter, R.anim.activity_open_exit, R.anim.activity_close_enter, R.anim.activity_close_exit)
                .show(path);
    }

    /**
     * 显示退出对话框
     */
    private void showConfirmDialog() {
        buildConfirmDialog().show();
    }

    /**
     * 创建一个退出的对话框
     *
     * @return
     */
    private CustomDialog buildConfirmDialog() {

        if (mDialog == null) {
            //添加布局
            View layoutView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_info, null);

            mDialog = new CustomDialog(getActivity(), layoutView);

            //初始化信息
            TextView txtMsg = (TextView) layoutView.findViewById(R.id.txtMsg);
            //设置信息
            txtMsg.setText(R.string.logout_confirm);

            //初始化按钮
            Button mBtnCancel = (Button) layoutView.findViewById(R.id.btnCancel);
            Button mBtnConfirm = (Button) layoutView.findViewById(R.id.btnConfirm);

            //绑定取消按钮的事件
            mBtnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });
            //绑定确认按钮的事件
            mBtnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //退出
                    logout();
                }
            });
        }
        return mDialog;
    }


    /**
     * 退出登录
     */
    private void logout() {
        //清除保存的member信息
        MemberUtil.clearMember(getActivity());
        //跳转到登陆界面
        startActivity(new Intent(getActivity(), StartActivity.class));

        //homeActivity结束
        getActivity().finish();
    }

    /**
     * 显示当前用户信息
     */
    private void displayProfile() {

        Member member = MemberUtil.getMember(getActivity());
        if (member != null) {
            mTxtName.setText(member.getName());
            mTxtEmail.setText(member.getEmail());
            ImageUtil.displayImageUseDefOptions(member.getHeadbig(), mImgHead);
        }
    }

    /**
     * 社会化分享方法
     */
    private void showShare() {
        ShareSDK.initSDK(context);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://pan.baidu.com/s/1o8aREVC");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我正在使用即时通讯应用--叽喳,你也来试试吧！！！");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://pan.baidu.com/s/1o8aREVC");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("功能比较完善可以体验一下");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://pan.baidu.com/s/1o8aREVC");

        // 启动分享GUI
        oks.show(context);
    }
}
