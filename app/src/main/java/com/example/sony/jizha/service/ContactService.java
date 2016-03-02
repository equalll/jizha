package com.example.sony.jizha.service;

import android.content.Context;
import android.util.Log;

import com.example.sony.jizha.activity.ProfileActivity;
import com.example.sony.jizha.dao.ContactDao;
import com.example.sony.jizha.dao.DaoSession;
import com.example.sony.jizha.model.Contact;
import com.example.sony.jizha.system.Constant;
import com.example.sony.jizha.system.JzApplication;
import com.example.sony.jizha.utils.PreferencesUtils;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * 联系人服务，并不在后台运行，只是方法而已
 * Created by sony on 2015/12/15.
 */
public class ContactService {

    private static ContactService instance;
    private DaoSession mDaoSession;
    private ContactDao mContactDao;

    private ContactService() {

    }

    /**
     * 单例模式
     *
     * @return
     */
    public static ContactService getInstance() {
        if (instance == null) {
            instance = new ContactService();
            instance.mDaoSession = JzApplication.getInstance().getDaoSession();
            instance.mContactDao = instance.mDaoSession.getContactDao();
        }
        return instance;
    }

    /**
     * 保存从服务器中获取的好友列表
     *
     * @param contacts
     */
    public void save(List<Contact> contacts) {
        mContactDao.insertOrReplaceInTx(contacts);
    }

    /**
     * 保存单个联系人
     *
     * @param contact
     */
    public void save(Contact contact) {
        mContactDao.insert(contact);
    }


    /**
     * 从手机端数据库中查找联系人列表
     *
     * @param context
     * @return
     */
    public List<Contact> findMemeberContacts(Context context) {
        Long memberId = PreferencesUtils.getLong(context, Constant.MEMBER_ID);
        QueryBuilder<Contact> queryBuilder = mContactDao.queryBuilder();
        queryBuilder.where(ContactDao.Properties.Memberid.eq(memberId)).orderAsc(ContactDao.Properties.Pinyin);

        return queryBuilder.list();
    }

    /**
     * 从手机端数据库中获取单个联系人
     *
     * @param context   上下文
     * @param contactid 联系人id
     * @return
     */
    public Contact getContact(Context context, long contactid) {
        QueryBuilder<Contact> queryBuilder = mContactDao.queryBuilder();
        Long memberId = PreferencesUtils.getLong(context, Constant.MEMBER_ID);

        queryBuilder.where(ContactDao.Properties.Memberid.eq(memberId));
        queryBuilder.where(ContactDao.Properties.Contactid.eq(contactid));
        return queryBuilder.unique();
    }
}
