package com.example.sony.jizha.message;

/**
 * Created with Android Studio
 * 项目名称：jizha
 * 类描述：传输的实体
 * 创建人：sony
 * 创建时间：2016/1/1 13:08
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version V1.0
 */
public class Message<T> {


    //消息类型
    public static final int MSG_TYPE_CHAT = 1;
    //添加好友请求
    public static final int MSG_TYPE_ADD_FRIEND_REQUEST = 2;
    //添加好友请求回复
    public static final int MSG_TYPE_ADD_FRIEND_ANSWER = 3;
    //好友信息更新
    public static final int MSG_TYPE_FRIEND_INFO_UPDATE = 4;
    //朋友圈回复
    public static final int MSG_TYPE_FRIEND_RECOMMEND = 5;
    //好友推荐
    public static final int MSG_TYPE_FRIEND_MOMENT = 6;

    private Long id;

    //消息类型：文本等
    private int msgType;

    //具体的信息
    private T data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
