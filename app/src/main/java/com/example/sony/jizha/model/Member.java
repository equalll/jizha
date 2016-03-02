package com.example.sony.jizha.model;

import com.example.sony.jizha.Widget.BaseModel;

import java.io.Serializable;

/**
 * 会员实体
 * Created by sony on 2015/9/9.
 */
public class Member implements BaseModel {
    //流水号
    private Long id;
    //邮箱
    private String email;
    //姓名
    private String name;
    //token信息
    private String token;
    //大头像
    private String headbig;
    //中头像
    private String headmid;
    //小头像
    private String headsmall;


    public Member() {
    }

    public Member(String name) {
        this.name = name;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getHeadbig() {
        return headbig;
    }

    public void setHeadbig(String headbig) {
        this.headbig = headbig;
    }

    public String getHeadmid() {
        return headmid;
    }

    public void setHeadmid(String headmid) {
        this.headmid = headmid;
    }

    public String getHeadsmall() {
        return headsmall;
    }

    public void setHeadsmall(String headsmall) {
        this.headsmall = headsmall;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", token='" + token + '\'' +
                ", headbig='" + headbig + '\'' +
                ", headmid='" + headmid + '\'' +
                ", headsmall='" + headsmall + '\'' +
                '}';
    }
}
