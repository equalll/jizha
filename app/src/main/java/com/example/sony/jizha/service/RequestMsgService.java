package com.example.sony.jizha.service;

import android.content.Context;

import com.example.sony.jizha.dao.DaoSession;
import com.example.sony.jizha.dao.RequestMsgDao;
import com.example.sony.jizha.model.RequestMsg;
import com.example.sony.jizha.system.Constant;
import com.example.sony.jizha.system.JzApplication;
import com.example.sony.jizha.utils.PreferencesUtils;

import de.greenrobot.dao.query.QueryBuilder;

import java.util.List;

/**
 * Created with Android Studio
 * 项目名称：jizha
 * 类描述：添加好友请求服务
 * 创建人：sony
 * 创建时间：2015/12/30 15:42
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version V1.0
 */
public class RequestMsgService {

    //用户服务实体对象
    private static RequestMsgService instance;

    private DaoSession mDaoSession;

    private RequestMsgDao mRequestMsgDao;

    /**
     * 无参构造函数
     */
    private RequestMsgService() {
    }


    /**
     * 单例模式
     *
     * @return
     */
    public static RequestMsgService getInstance() {
        if (instance == null) {
            instance = new RequestMsgService();
            instance.mDaoSession = JzApplication.getInstance().getDaoSession();
            instance.mRequestMsgDao = instance.mDaoSession.getRequestMsgDao();
        }
        return instance;
    }

    /**
     * 保存添加好友请求信息
     *
     * @param requestMsg
     */
    public void save(RequestMsg requestMsg) {
        this.mRequestMsgDao.insertInTx(requestMsg);
    }

    /**
     * 通过memberid以及contactid查找添加好友请求信息列表
     *
     * @param memberid
     * @param friendid
     * @return
     */
    public List<RequestMsg> findFriendRequestMsg(long memberid, long friendid) {

        QueryBuilder<RequestMsg> queryBuilder = mRequestMsgDao.queryBuilder();
        queryBuilder.where(RequestMsgDao.Properties.Memberid.eq(memberid));
        queryBuilder.where(RequestMsgDao.Properties.Contactid.eq(friendid));

        return queryBuilder.list();
    }

    /**
     * 查找当前用户添加好友请求信息列表
     *
     * @param context 上下文
     * @return
     */
    public List<RequestMsg> findRequestMsg(Context context) {

        Long memberid = PreferencesUtils.getLong(context, Constant.MEMBER_ID);
        QueryBuilder<RequestMsg> queryBuilder = mRequestMsgDao.queryBuilder();
        queryBuilder.where(RequestMsgDao.Properties.Memberid.eq(memberid));
        queryBuilder.orderDesc(RequestMsgDao.Properties.Requesttime);

        return queryBuilder.list();
    }

    public int countUnHandleMsg(Context context) {

        Long memberid = PreferencesUtils.getLong(context, Constant.MEMBER_ID);
        QueryBuilder<RequestMsg> queryBuilder = mRequestMsgDao.queryBuilder();
        queryBuilder.where(RequestMsgDao.Properties.Memberid.eq(memberid));
        queryBuilder.where(RequestMsgDao.Properties.Status.eq(RequestMsg.STATUS_UN_HANDLE));

        return (int) queryBuilder.count();

    }


    /**
     * 更新添加好友请求信息
     *
     * @param msg
     */
    public void update(RequestMsg msg) {
        this.mRequestMsgDao.update(msg);
    }
}
