package com.example.sony.jizha.message;

/**
 * Created with Android Studio
 * 项目名称：jizha
 * 类描述：聊天的文字信息
 * 创建人：sony
 * 创建时间：2016/1/1 13:08
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version V1.0
 */
public class ChatTextMessage extends ChatBaseMessage {

    protected Long id;
    //发送人的id
    protected Long fromMemberId;
    //接收人的id
    protected Long toMemberId;
    //信息
    private String msg;
    //信息类型
    private int msgType;
    //聊天时间
    private Long chatTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFromMemberId() {
        return fromMemberId;
    }

    public void setFromMemberId(Long fromMemberId) {
        this.fromMemberId = fromMemberId;
    }

    public Long getToMemberId() {
        return toMemberId;
    }

    public void setToMemberId(Long toMemberId) {
        this.toMemberId = toMemberId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public Long getChatTime() {
        return chatTime;
    }

    public void setChatTime(Long chatTime) {
        this.chatTime = chatTime;
    }
}
