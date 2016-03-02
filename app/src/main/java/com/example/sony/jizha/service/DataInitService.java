package com.example.sony.jizha.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.sony.jizha.http.BaseResponse;
import com.example.sony.jizha.http.RequestListener;
import com.example.sony.jizha.http.VolleyHttpClient;
import com.example.sony.jizha.model.Contact;
import com.example.sony.jizha.system.Constant;
import com.example.sony.jizha.system.JzApplication;
import com.example.sony.jizha.utils.JSONUtil;
import com.example.sony.jizha.utils.Pinyin4j;
import com.example.sony.jizha.utils.PreferencesUtils;
import com.example.sony.jizha.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 初始化数据的服务，真正运行在后台中的服务
 * IntentService和service的区别：IntentService可以在intent方法中做异步任务，做完之后IntentService可以自动销毁，service不会自动销毁，还需要开异步线程做这个任务
 * Created by sony on 2015/9/12.
 */
public class DataInitService extends IntentService {

    public static final String TAG = "DataInitService";

    //初始化volley
    private VolleyHttpClient httpClient = new VolleyHttpClient(this);
    //初始化联系人服务
    private ContactService contactService = ContactService.getInstance();
    //获取context
    private Context context = JzApplication.getInstance();

    /**
     * 构造函数
     */
    public DataInitService() {
        super("DataInitService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //获取登陆会员的id
        final Long memberId = JzApplication.getInstance().getmLoginMember().getId();
        Log.d(TAG, "***********memberID is  " + memberId);

        //传值
        final Map<String, String> map = new HashMap<String, String>();
        map.put("memberid", memberId + "");

        //根据会员id获取联系人列表
        httpClient.post(Constant.API.URL_FRIENDS, map, 0, new RequestListener() {

            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                //将响应的信息转为list
                List<Contact> contacts = response.getList(Contact.class);

                Log.d(TAG, "**************resonseList = " + contacts);

                //判断list是否为空
                if (contacts != null) {
                    List<Contact> list = new ArrayList<Contact>(contacts.size());

                    //新建Contact对象
                    Contact c = null;
                    for (Contact contact : contacts) {
                        c = new Contact();
                        //将名字转为拼音
                        String pinyin = Pinyin4j.getPingYin(contact.getName(), true);
                        //转为大写
                        c.setPinyin(pinyin.toUpperCase());
                        c.setMemberid(memberId);
                        c.setContactid(contact.getId());
                        c.setId(null);//数据库中id为自增长

                        c.setEmail(contact.getEmail());
                        c.setName(contact.getName());
                        c.setHeadbig(contact.getHeadbig());
                        c.setHeadmid(contact.getHeadmid());
                        c.setHeadsmall(contact.getHeadsmall());
                        c.setRegistetime(contact.getRegistetime());
                        c.setCreatetime(contact.getCreatetime());

                        list.add(c);
                    }

                    Log.d(TAG, "------------->获取当前登录用户好友列表成功:" + list.size());
                    //保存到手机端的数据库
                    contactService.save(list);


                    //标记为已初始化信息，退出再登陆则不会初始化信息，有bug.加上memberid.每个用户的初始化都不一样，则都会初始化
                    PreferencesUtils.putBoolean(context, Constant.IS_DATA_INIT + memberId, true);
                }
            }

            @Override
            public void onRequestError(int code, String msg) {

            }

            @Override
            public void onRequestFail(int code, String msg) {

            }
        });
    }
}
