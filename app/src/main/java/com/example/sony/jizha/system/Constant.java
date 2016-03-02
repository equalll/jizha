package com.example.sony.jizha.system;

public class Constant {

    //请求code
    public static final int REQUEST_CODE = 1000;
    //结果code
    public static final int RESULT_CODE = 100;
    //成功
    public static final int SUCCESS = 1;
    //失败
    public static final int FAIL = 0;

    //身份认证，通过token登陆时使用
    public static final String FLAG_AUTH = "flag_auth";
    //好友
    public static final String FRIEND = "friend";
    //会员
    public static final String MEMBER = "member";
    public static final String WORD = "word";
    //会员id
    public static final String MEMBER_ID = "member_id";
    //token
    public static final String ACCESS_TOKEN = "access_token";
    //连接百度云推送时的userid
    public static final String BD_USERID = "bd_userid";
    //连接百度云推送时的channelid
    public static final String BD_CHANNELID = "bd_channelid";
    //百度apikey，开放平台注册时提供
    public static final String BD_API_KEY = "bd_api_key";

    //意图为发送信息
    public static final String ACTION_MSG = "com.example.sony.jizha.ACTION_CHART";
    //意图更新联系人信息
    public static final String ACTION_CONTACT_UPDATE = "com.example.sony.jizha.ACTION_CONTACT_UPDATE";
    //意图发送好友请求
    public static final String ACTION_MAKE_FRIEND_REQUEST = "com.example.sony.jizha.ACTION_MAKE_FRIEND_REQUEST";

    public static final String EXTRA_MSG="com.example.sony.jizha.CHART_MSG";

    //手机端数据库名称
    public static final String DB_NAME = "jizha.db";
    //判断是否初始化数据，第一次登陆时或者授权过期时，通过service后台获取联系人数据
    public static final String IS_DATA_INIT = "is_data_init_";

    /**
     * 服务器一些接口
     */
    public static class API {
        //服务器地址
        public static final String URL_BASE = "http://115.51.124.174:8080/imserver/";
        //手机开始界面webView中显示的界面
        public static final String URL_START = URL_BASE + "start_android.html";

        //通过邮箱和密码登录
        public static final String URL_LOGIN = URL_BASE + "login";
        //通过保存的token 信息登陆
        public static final String URL_LOGIN_TOKEN = URL_BASE + "login/token";
        //通过会员id获取好友
        public static final String URL_FRIENDS = URL_BASE + "friend/my";
        //好友详细信息
        public static final String URL_FRIEND_PROFILE = URL_BASE + "friend/profile";
        //通过关键字查询会员列表
        public static final String URL_FRIENDS_QUERY = URL_BASE + "friend/query";
        //添加好友请求
        public static final String URL_FRIENDS_REQUEST = URL_BASE + "friend/request";

        //向好友发送消息
        public static final String URL_CHART_SEND = URL_BASE + "chart/friend/send";
        //回复添加好友请求
        public static final String URL_FRIENDS_REQUEST_ANSWER = URL_BASE + "friend/request/answer";
        //public static final String URL_LOGIN_TOKEN=URL_BASE+"login/token";
    }
}
