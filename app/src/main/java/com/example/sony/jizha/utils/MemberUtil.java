package com.example.sony.jizha.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.example.sony.jizha.activity.StartActivity;
import com.example.sony.jizha.model.Member;
import com.example.sony.jizha.system.Constant;
import com.example.sony.jizha.system.JzApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * 保存member信息工具类
 * Created by Sony on 2015/6/27.
 */
public class MemberUtil {

    //gson对象
    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public static void saveMember(Context context, Member member) {
        //保存member的信息
        PreferencesUtils.putString(context, Constant.MEMBER, gson.toJson(member));

        PreferencesUtils.putString(context, Constant.ACCESS_TOKEN, member.getToken());

        PreferencesUtils.putLong(context, Constant.MEMBER_ID, member.getId());
    }

    /**
     * 初始化用户,判断当前应用中登陆用户是否存在，不存在的话保存一个，防止系统进程杀死的时候收到消息
     * 如果收到消息的话，重新启动应用
     *
     * @param context
     */
    public static void initMember(Context context) {

        Member member = JzApplication.getInstance().getmLoginMember();
        if (member == null) {
            String memberJson = PreferencesUtils.getString(context, Constant.MEMBER);
            if (!TextUtils.isEmpty(memberJson)) {
                member = gson.fromJson(memberJson, Member.class);
                JzApplication.getInstance().setmLoginMember(member);

            } else
                context.startActivity(new Intent(context, StartActivity.class));
        }
    }


    /**
     * 清除保存的member信息
     *
     * @param context
     */
    public static void clearMember(Context context) {

        //重新保存信息，覆盖原先保存的信息
        PreferencesUtils.putString(context, Constant.ACCESS_TOKEN, null);
        PreferencesUtils.putLong(context, Constant.MEMBER_ID, -1);

        PreferencesUtils.putString(context, Constant.MEMBER, null);
        JzApplication.getInstance().setmLoginMember(null);
    }

    /**
     * 获取member信息
     *
     * @param context
     * @return
     */
    public static Member getMember(Context context) {
        //取出sharePreference保存的member数据
        String memberJson = PreferencesUtils.getString(context, Constant.MEMBER);
        //解析为对象
        if (!TextUtils.isEmpty(memberJson)) {
            return gson.fromJson(memberJson, Member.class);
        }
        return null;
    }
}
