package com.example.sony.jizha.model;


import java.util.Date;

/**
 * Created with Android Studio
 * 项目名称：jizha
 * 类描述：添加好友请求回复实体
 * 创建人：sony
 * 创建时间：2016/1/4 18:57
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version V1.0
 */
public class RequestAnswerMsg {

    //同意请求
    public static final int TYPE_AGREE = 2;
    //拒绝请求
    public static final int TYPE_REFUSE = 3;

    private long contactid;
    private long requestid;
    private String answermsg;

    private Date answerTime;
    private String contactemail;
    private String contactname;
    private String contactheadsmall;
    private String contactheadmid;
    private String contactheadbig;
    private int type;

    public RequestAnswerMsg() {
    }

    public long getContactid() {
        return contactid;
    }

    public void setContactid(long contactid) {
        this.contactid = contactid;
    }

    public long getRequestid() {
        return requestid;
    }

    public void setRequestid(long requestid) {
        this.requestid = requestid;
    }

    public String getContactemail() {
        return contactemail;
    }

    public void setContactemail(String contactemail) {
        this.contactemail = contactemail;
    }

    public String getContactname() {
        return contactname;
    }

    public void setContactname(String contactname) {
        this.contactname = contactname;
    }

    public String getContactheadsmall() {
        return contactheadsmall;
    }

    public void setContactheadsmall(String contactheadsmall) {
        this.contactheadsmall = contactheadsmall;
    }

    public String getContactheadmid() {
        return contactheadmid;
    }

    public void setContactheadmid(String contactheadmid) {
        this.contactheadmid = contactheadmid;
    }

    public String getContactheadbig() {
        return contactheadbig;
    }

    public void setContactheadbig(String contactheadbig) {
        this.contactheadbig = contactheadbig;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(Date answerTime) {
        this.answerTime = answerTime;
    }

    public String getAnswermsg() {
        return answermsg;
    }

    public void setAnswermsg(String answermsg) {
        this.answermsg = answermsg;
    }
}
