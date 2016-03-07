package com.example.sony.jizha.message;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.example.sony.jizha.R;
import com.example.sony.jizha.activity.ChatActivity;
import com.example.sony.jizha.activity.HomeActivity;
import com.example.sony.jizha.activity.LoginActivity;
import com.example.sony.jizha.http.BaseResponse;
import com.example.sony.jizha.http.RequestListener;
import com.example.sony.jizha.http.VolleyHttpClient;
import com.example.sony.jizha.model.ChatMsg;
import com.example.sony.jizha.model.Contact;
import com.example.sony.jizha.model.Member;
import com.example.sony.jizha.model.RequestAnswerMsg;
import com.example.sony.jizha.model.RequestMsg;
import com.example.sony.jizha.service.ChatMsgService;
import com.example.sony.jizha.service.ContactService;
import com.example.sony.jizha.service.RequestMsgService;
import com.example.sony.jizha.system.Constant;
import com.example.sony.jizha.system.JzApplication;
import com.example.sony.jizha.utils.AppUtil;
import com.example.sony.jizha.utils.JSONUtil;
import com.example.sony.jizha.utils.MemberUtil;
import com.example.sony.jizha.utils.Pinyin4j;
import com.example.sony.jizha.utils.PreferencesUtils;
import com.example.sony.jizha.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with Android Studio
 * 项目名称：jizha
 * 类描述：处理返回成功的信息
 * 创建人：sony
 * 创建时间：2015/12/30 15:42
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version V1.0
 */
public class MessageHandler {

    private static final String TAG = "MessageHandler";
    //新建gson对象
    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    //网络请求
    private static VolleyHttpClient mHttpClient;

    //初始化聊天信息服务
    private ChatMsgService mChatMsgService = ChatMsgService.getInstance();

    //创建通知管理对象
    private NotificationManager mNotificationManager;

    //初始化联系人服务
    private ContactService mContactService = ContactService.getInstance();

    //初始化添加好友请求服务
    private RequestMsgService mRequestMsgService = RequestMsgService.getInstance();

    /**
     * 连接推送服务器成功之后做的事情
     *
     * @param context
     * @param userId
     * @param channelId
     */
    public static void bindSuccess(Context context, String userId, String channelId) {
        //保存userId  channelId
        PreferencesUtils.putString(context, Constant.BD_USERID, userId);
        PreferencesUtils.putString(context, Constant.BD_CHANNELID, channelId);

        System.out.println("---------------->MessageHandler" + channelId + " : " + PreferencesUtils.getString(context, Constant.BD_CHANNELID));

        //获取登陆时保存的信息
        String memberJson = PreferencesUtils.getString(context, Constant.MEMBER);
        String token = PreferencesUtils.getString(context, Constant.ACCESS_TOKEN);
        // 已经登录过，自动登录
        if (!TextUtils.isEmpty(memberJson) && !TextUtils.isEmpty(token)) {
            //将json数据转为对象
            Member member = gson.fromJson(memberJson, Member.class);
            if (member != null)
                //通过token登陆
                login(context, userId, channelId, member.getEmail(), token);
        }
    }

    /**
     * 通过token登陆
     *
     * @param context
     * @param userid
     * @param channelid
     * @param email
     * @param token
     */
    private static void login(final Context context, String userid, String channelid, String email, String token) {

        //实例化volley
        mHttpClient = new VolleyHttpClient(context);

        Map<String, String> map = new HashMap<String, String>(4);
        map.put("email", email);
        map.put("token", token);
        map.put("userId", userid);
        map.put("channelid", channelid);

        //post方式请求
        mHttpClient.post(Constant.API.URL_LOGIN_TOKEN, map, 0, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                Member member = response.getObj(Member.class);

                //保存信息到Application
                JzApplication.getInstance().setmLoginMember(member);
                //保存member对象
                MemberUtil.saveMember(context, member);
            }

            @Override
            public void onRequestError(int code, String msg) {
                //认证失败，重新登陆
                authError(context);
            }

            @Override
            public void onRequestFail(int code, String msg) {
                authError(context);
            }
        });
    }

    /**
     * token登陆失败，重新登陆
     * <p/>
     * activity的启动模式：4种
     * <p/>
     * 1、standard：默认模式：每次启动都会创建一个新的activity对象，放到目标任务栈中
     * <p/>
     * 2、singleTop：判断当前的任务栈顶是否存在相同的activity对象，如果存在，则直接使用，如果不存在，那么创建新的activity对象放入栈中
     * <p/>
     * 3、singleTask：在任务栈中会判断是否存在相同的activity，如果存在，那么会清除该activity之上的其他activity对象显示，如果不存在，则会创建一个新的activity放入栈顶
     * <p/>
     * 4、singleIntance：会在一个新的任务栈中创建activity，并且该任务栈种只允许存在一个activity实例，其他调用该activity的组件会直接使用该任务栈种的activity对象
     * <p/>
     * intent flags属性来指定启动模式：Intent.FLAG_ACTIVITY_NEW_TASK      相当于singleTask
     * <p/>
     * Intent. FLAG_ACTIVITY_CLEAR_TOP相当于singleTop
     *
     * @param context
     */
    private static void authError(Context context) {
        ToastUtils.show(context, R.string.auth_error);
        //跳转到登陆界面
        Intent intent = new Intent(context, LoginActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


    /// 聊天的那些事

    /**
     * 处理传输的数据，解析消息的类型，处理消息
     *
     * @param context 上下文
     * @param msg     传输的json数据
     */
    public void handleMsg(Context context, String msg) {

        int msgType = -1;

        try {
            JSONObject jsonObject = new JSONObject(msg);
            msgType = jsonObject.getInt("msgType");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        switch (msgType) {
            //聊天信息
            case Message.MSG_TYPE_CHAT:
                handleChatMsg(context, msg);
                break;
            //好友请求信息
            case Message.MSG_TYPE_ADD_FRIEND_REQUEST:
                handleAddFiendRequestMsg(context, msg);
                break;
            //回复好友添加请求信息
            case Message.MSG_TYPE_ADD_FRIEND_ANSWER:
                handleAnswerRequestMsg(context, msg);
                break;
        }
    }

    /**
     * 处理聊天消息
     *
     * @param context
     * @param msg
     */
    public void handleChatMsg(Context context, String msg) {

        int msgType = -1;

        try {
            //解析传输的数据
            JSONObject jsonObject = new JSONObject(msg);
            msgType = jsonObject.getJSONObject("data").getInt("msgType");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        switch (msgType) {
            //根据传输的信息类型处理数据
            case ChatBaseMessage.MSG_TYPE_TEXT:
                handleChatTextMsg(context, msg);
                break;

            case ChatBaseMessage.MSG_TYPE_IMG:
                //
                break;
        }
    }

    /**
     * 处理文字信息
     *
     * @param context 上下文
     * @param msg     传输的json信息
     */
    private void handleChatTextMsg(Context context, String msg) {

        //将json解析为对象
        Message<ChatTextMessage> clientMsg = gson.fromJson(msg, new TypeToken<Message<ChatTextMessage>>() {
        }.getType());

        //创建文字聊天对象
        ChatTextMessage textMsg = clientMsg.getData();

        //创建聊天信息对象
        ChatMsg chatMsg = new ChatMsg();
        chatMsg.setChattime(new Date(textMsg.getChatTime()));
        chatMsg.setChatMsg(textMsg.getMsg());
        chatMsg.setStatus(ChatMsg.STATUS_UNREAD); // 未读
        chatMsg.setIsreceive(ChatMsg.RECEVIER);
        chatMsg.setChattype(ChatMsg.MSG_TYPE_TEXT);
        chatMsg.setContactid(textMsg.getFromMemberId());
        chatMsg.setMemberid(textMsg.getToMemberId());

        mChatMsgService.save(chatMsg);


        //发送广播，通知好友新信息
        notityUser(context, chatMsg);
    }

    /**
     * 处理回复好友请求信息
     *
     * @param context
     * @param msg
     */
    private void handleAnswerRequestMsg(Context context, String msg) {
        //解析服务器返回的数据
        Message<RequestAnswerMsg> clientMsg = gson.fromJson(msg, new TypeToken<Message<RequestAnswerMsg>>() {
        }.getType());

        RequestAnswerMsg answerMsg = clientMsg.getData();

        // 同意添加好友
        if (answerMsg.getType() == RequestAnswerMsg.TYPE_AGREE) {

            long loginMemberId = PreferencesUtils.getLong(context, Constant.MEMBER_ID);

            //创建联系人对象
            Contact contact = new Contact();

            contact.setMemberid(loginMemberId);
            contact.setContactid(answerMsg.getContactid());
            contact.setPinyin(Pinyin4j.getPingYin(answerMsg.getContactname(), true).toUpperCase());
            contact.setCreatetime(new Date());
            contact.setEmail(answerMsg.getContactemail());
            contact.setName(answerMsg.getContactname());
            contact.setHeadbig(answerMsg.getContactheadbig());
            contact.setHeadmid(answerMsg.getContactheadmid());
            contact.setHeadsmall(answerMsg.getContactheadsmall());

            //保存联系人
            mContactService.save(contact);

            //发送通知用户
            context.sendBroadcast(new Intent(Constant.ACTION_CONTACT_UPDATE));
        } else {
            //拒绝。。。
        }
    }

    /**
     * 处理添加好友请求信息
     *
     * @param context
     * @param msg
     */
    private void handleAddFiendRequestMsg(Context context, String msg) {
        //解析服务端返回的消息
        Message<RequestMsg> clientMsg = gson.fromJson(msg, new TypeToken<Message<RequestMsg>>() {
        }.getType());

        RequestMsg requestMsg = clientMsg.getData();

        if (requestMsg != null) {
            long loginMemberId = PreferencesUtils.getLong(context, Constant.MEMBER_ID);

            // 如果存在了请求则不处理
            List<RequestMsg> msgs = mRequestMsgService.findFriendRequestMsg(loginMemberId, requestMsg.getContactid());
            if (msgs != null && msgs.size() > 0)
                return;

            requestMsg.setStatus(RequestMsg.STATUS_UN_HANDLE);
            requestMsg.setMemberid(loginMemberId);

            mRequestMsgService.save(requestMsg);

            // 发送通知用户
            context.sendBroadcast(new Intent(Constant.ACTION_MAKE_FRIEND_REQUEST));
        }
    }

    /**
     * 通知好友发送的信息
     *
     * @param context 上下文
     * @param chatMsg 聊天的信息
     */
    private void notityUser(Context context, ChatMsg chatMsg) {
        //不在聊天界面或者主页
        if (!isTopActivity(context)) {
            notification(context, chatMsg);
        }
        //界面显示在主页或者聊天界面
        sendBroadCast(context, chatMsg);
    }

    /**
     * 判断是否是主页以及聊天界面，用以更新聊天信息的显示
     *
     * @param context
     * @return
     */
    private boolean isTopActivity(Context context) {
        //获取activity的名称
        String packageName = "com.example.sony.jizha.activity.HomeActivity";
        String chatActivityName = "com.example.sony.jizha.activity.ChatActivity";
        //初始化activity的管理
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //获取activity的运行任务信息
        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        //该activity在运行
        if (tasksInfo.size() > 0) {
            //应用程序位于堆栈的顶层
            if (packageName.equals(tasksInfo.get(0).topActivity.getClassName()) || chatActivityName.equals(tasksInfo.get(0).topActivity.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 不在聊天界面或者主页时，通知聊天信息
     *
     * @param context 上下文
     * @param chatMsg 聊天信息
     */
    private void notification(Context context, ChatMsg chatMsg) {

        //初始化通知管理对象
        if (mNotificationManager == null)
            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //创建通知管理组件
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        //查找未读的聊天消息
        List<ChatMsg> msgs = mChatMsgService.findUnreadMsg(context);

        Log.d(TAG, "------------->unread meg:" + msgs.size());

        //创建意图对象
        Intent intent = null;
        // 多条未读消息的处理
        if (msgs != null && msgs.size() > 1) {
            ////设置通知标题
            builder.setTicker("收到" + (msgs.size() > 99 ? "99+" : msgs.size()) + "条新消息");
            //设置内容info
            builder.setContentInfo(msgs.size() + "条");
            //设置内容题目
            builder.setContentTitle("有" + (msgs.size() > 99 ? "99+" : msgs.size()) + "条未读消息");
            //设置内容文本信息
            builder.setContentText("点击查看");

            //  3.设置意图对象，有多条消息时跳转到主界面
            intent = new Intent(context, HomeActivity.class);

        } else {
            //根据聊天好友的id获取联系人信息
            Contact friend = mContactService.getContact(context, chatMsg.getContactid());

            //设置通知标题
            builder.setTicker("收到新消息");
            //设置内容info
            builder.setContentInfo("1条");
            //设置内容题目
            builder.setContentTitle(friend.getName());
            //设置内容文本信息
            builder.setContentText(chatMsg.getChatMsg());

            //  只有一条消息时跳转到聊天界面
            intent = new Intent(context, ChatActivity.class);
            //传递好友信息
            intent.putExtra(Constant.FRIEND, friend);
        }

        //设置通知显示的小图标
        builder.setSmallIcon(R.drawable.ic_launcher);
        //设置通知时间
        builder.setWhen(System.currentTimeMillis());
        //默认点击对应的notification对象后，该对象消失
        builder.setAutoCancel(true);
        //默认是没有声音提醒的，得加上 这个 Notification.DEFAULT_SOUND
        builder.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND);


        //挂起意图
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,                 // The Context in which this PendingIntent should start the activity.
                0,                                  // request code
                intent,                             // Intent of the activity to be launched.
                0); // 待补充...

        builder.setContentIntent(pendingIntent);
        //  4.得到一个notification对象(根据builder预设置信息)
        Notification notification = builder.build();

        //  5.唤醒notification对象
        //将该notification发送到状态条上，如果id相同且没有消失，则直接更新该notification对象信息
        // 否则创建一个Notification实例对象

        mNotificationManager.notify(0,       // int id 应用唯一值
                notification);  // Notification notification 不得设置为null
    }

    /**
     * 界面显示在主页或者聊天界面时，直接通知并显示信息
     *
     * @param context 上下文
     * @param chatMsg 聊天信息
     */
    private void sendBroadCast(Context context, ChatMsg chatMsg) {

        Intent intent = new Intent();
        //设置action
        intent.setAction(Constant.ACTION_MSG);
        //传递聊天信息
        intent.putExtra(Constant.EXTRA_MSG, chatMsg);
        //设置本应用能收到广播，其他应用收不到
        intent.setPackage(AppUtil.getAppInfo(context).getPkgName());


        //发送广播
        context.sendBroadcast(intent);
    }
}
