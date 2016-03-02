package com.example.sony.jizha.model;

/**
 * Created with Android Studio
 * 项目名称：jizha
 * 类描述：聊天信息显示实体，用于聊天列表的显示
 * 创建人：sony
 * 创建时间：2016/1/5 16:48
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version V1.0
 */
public class ChatMsgEx extends ChatMsg {

    //联系人信息
    private Contact contact;
    //未读消息数量
    private Integer unreadCount;

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Integer getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(Integer unreadCount) {
        this.unreadCount = unreadCount;
    }
}
