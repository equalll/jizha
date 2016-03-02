package com.example.sony.jizha.service;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.sony.jizha.activity.ChatActivity;
import com.example.sony.jizha.dao.ChatMsgDao;
import com.example.sony.jizha.dao.DaoSession;
import com.example.sony.jizha.model.ChatMsg;
import com.example.sony.jizha.model.ChatMsgEx;
import com.example.sony.jizha.system.Constant;
import com.example.sony.jizha.system.JzApplication;
import com.example.sony.jizha.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created with Android Studio
 * 项目名称：jizha
 * 类描述：聊天信息的服务，并不在后台运行，只是方法而已
 * 创建人：sony
 * 创建时间：2016/1/1 15:59
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version V1.0
 */
public class ChatMsgService {

    private static final String TAG = "ChatMsgService";
    //聊天信息服务的实例
    private static ChatMsgService instance;
    //用于获取创建数据库dao层方法
    private DaoSession mDaoSession;
    //用于创建表
    private ChatMsgDao mChatMsgDao;

    //实例化联系人服务
    private ContactService mContactService = ContactService.getInstance();

    //初始化context
    private Context context = JzApplication.getInstance();

    /**
     * 构造函数
     */
    private ChatMsgService() {
    }


    /**
     * 获取实例，单例模式
     *
     * @return 聊天信息的服务实例
     */
    public static ChatMsgService getInstance() {
        if (instance == null) {
            instance = new ChatMsgService();
            instance.mDaoSession = JzApplication.getInstance().getDaoSession();
            instance.mChatMsgDao = instance.mDaoSession.getChatMsgDao();
        }
        return instance;
    }

    /**
     * 保存当前发送的消息到手机端数据库
     *
     * @param chatMsg 发送的消息
     * @return 成功返回0；失败返回1
     */
    public long save(ChatMsg chatMsg) {
        Log.d(TAG, "------------------>chatMsg  save");
        return this.mChatMsgDao.insert(chatMsg);
    }

    /**
     * 更新手机端数据库中保存的消息
     *
     * @param chatMsg
     */
    public void update(ChatMsg chatMsg) {
        this.mChatMsgDao.updateInTx(chatMsg);
    }

    /**
     * 通过contactid获取聊天信息列表
     *
     * @param contactid 聊天人的id
     * @param pageIndex 分页索引
     * @param pageSize  页的大小
     * @return 聊天信息列表
     */
    public List<ChatMsg> findChatMsgByContactId(Long contactid, int pageIndex, int pageSize) {


        QueryBuilder<ChatMsg> queryBuilder = mChatMsgDao.queryBuilder();
        queryBuilder.where(ChatMsgDao.Properties.Contactid.eq(contactid));

        int offset = pageIndex * pageSize;
        queryBuilder.offset(offset);
        queryBuilder.limit(pageSize);

        return queryBuilder.list();
    }

    /**
     * 查找未读的聊天消息
     *
     * @param context 上下文
     * @return 聊天消息状态为未读的消息的列表
     */
    public List<ChatMsg> findUnreadMsg(Context context) {

        Log.d(TAG, "----------->当前登录用户memberId:" + PreferencesUtils.getLong(context, Constant.MEMBER_ID));
        //通过当前用户id查询出未读的消息
        QueryBuilder<ChatMsg> queryBuilder = mChatMsgDao.queryBuilder();
        queryBuilder.where(ChatMsgDao.Properties.Memberid.eq(PreferencesUtils.getLong(context, Constant.MEMBER_ID)));
        queryBuilder.where(ChatMsgDao.Properties.Status.eq(ChatMsg.STATUS_UNREAD));
        //查询未读的消息
        List<ChatMsg> chatMsgs = queryBuilder.listLazy();

        return chatMsgs;
    }

    /**
     * 接收通知以后将未读的消息更新状态为已读
     *
     * @param chatActivity
     * @param contactid
     */
    public void updateUnreadChatMsg(ChatActivity chatActivity, long contactid) {

        QueryBuilder<ChatMsg> queryBuilder = mChatMsgDao.queryBuilder();

        queryBuilder.where(ChatMsgDao.Properties.Memberid.eq(PreferencesUtils.getLong(context, Constant.MEMBER_ID)));
        queryBuilder.where(ChatMsgDao.Properties.Contactid.eq(contactid));
        queryBuilder.where(ChatMsgDao.Properties.Status.eq(ChatMsg.STATUS_UNREAD));
        //查出未读消息
        List<ChatMsg> chatMsgs = queryBuilder.listLazy();

        //如果有未读消息
        if (chatMsgs != null && chatMsgs.size() > 0) {
            for (ChatMsg chatMsg : chatMsgs) {
                //将未读消息的状态设置为已读
                chatMsg.setStatus(ChatMsg.STATUS_READED);
                //更新消息状态
                this.mChatMsgDao.updateInTx(chatMsg);
            }
        }
    }

    /**
     * 查询历史聊天信息
     *
     * @param context 上下文
     * @return
     */
    public List<ChatMsgEx> findHistoryChatMsg(Context context) {
        long memberid = PreferencesUtils.getLong(context, Constant.MEMBER_ID);

        Cursor cursor = mChatMsgDao.getDatabase().query(
                true,  // 是否去除重复行
                ChatMsgDao.TABLENAME, // 表名
                new String[]{
                        ChatMsgDao.Properties.Id.columnName,
                        ChatMsgDao.Properties.Contactid.columnName,
                        ChatMsgDao.Properties.ChatMsg.columnName,
                        ChatMsgDao.Properties.Chattime.columnName,
                        ChatMsgDao.Properties.Chattype.columnName,
                        ChatMsgDao.Properties.Status.columnName,
                        ChatMsgDao.Properties.Isreceive.columnName,
                },//要查询的列
                ChatMsgDao.Properties.Memberid.columnName + "=?",//查询条件
                new String[]{memberid + ""},//查询条件的参数
                ChatMsgDao.Properties.Contactid.columnName, // 根据联系人ID 分组
                null, //对分组的结果进行限制
                ChatMsgDao.Properties.Chattime.columnName + " desc", // 按照时间倒序
                null//分页查询限制
        );

        List<ChatMsgEx> chatMsgExes = new ArrayList<ChatMsgEx>();

        while (cursor.moveToNext()) {

            ChatMsgEx chatMsg = new ChatMsgEx();
            chatMsg.setId(cursor.getLong(0));
            chatMsg.setContactid(cursor.getLong(1));
            chatMsg.setChatMsg(cursor.getString(2));
            chatMsg.setChattime(new java.util.Date(cursor.getLong(3)));
            chatMsg.setChattype(cursor.getInt(4));
            chatMsg.setStatus(cursor.getInt(5));
            chatMsg.setIsreceive(cursor.getInt(6));
            // 获取朋友实例
            //chatMsg.setContact(this.mContactDao.load(chatMsg.getContactid())); // 有Bug，应该是根据contact id 查找联系人，而load 方法是根据id 查找

            chatMsg.setContact(this.mContactService.getContact(context, chatMsg.getContactid()));


            // 未读消息
            chatMsg.setUnreadCount(countContactUnreadMsg(context, chatMsg.getContactid()));

            Log.d(TAG,"---------------->unrReadMsg:"+chatMsg.getUnreadCount());
            chatMsgExes.add(chatMsg);
        }

        cursor.close();

        return chatMsgExes;

    }


    /**
     * 统计联系人未读消息数量
     *
     * @param context   上下文
     * @param contactId 联系人id
     * @return
     */
    public int countContactUnreadMsg(Context context, long contactId) {

        QueryBuilder<ChatMsg> queryBuilder = this.mChatMsgDao.queryBuilder();

        queryBuilder.where(ChatMsgDao.Properties.Memberid.eq(PreferencesUtils.getLong(context, Constant.MEMBER_ID)));
        queryBuilder.where(ChatMsgDao.Properties.Contactid.eq(contactId));
        queryBuilder.where(ChatMsgDao.Properties.Status.eq(ChatMsg.STATUS_UNREAD));

        return (int) queryBuilder.count();
    }
}
