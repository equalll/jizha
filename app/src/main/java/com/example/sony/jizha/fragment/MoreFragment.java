package com.example.sony.jizha.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sony.jizha.R;
import com.example.sony.jizha.Widget.CustomDialog;
import com.example.sony.jizha.activity.StartActivity;
import com.example.sony.jizha.model.Member;
import com.example.sony.jizha.utils.ImageUtil;
import com.example.sony.jizha.utils.MemberUtil;

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
