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

        //系统设置点击事件
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
            ToastUtils.show(context, "系统设置功能未开放");
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
                .titleDefault("关于我们")
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
}
