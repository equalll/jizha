/*
*BaseResponse.java
*Created on 2014-9-25 上午10:34 by Ivan
*Copyright(c)2014 Guangzhou Onion Information Technology Co., Ltd.
*http://www.cniao5.com
*/
package com.example.sony.jizha.http;

import android.text.TextUtils;

import com.example.sony.jizha.utils.JSONUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan on 14-9-25.
 * Copyright(c)2014 Guangzhou Onion Information Technology Co., Ltd.
 * http://www.cniao5.com
 */
public class BaseResponse {


//    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();


    public static final int SUCCESS=1;
    public static final int FAIL=0;

    private Integer status;

    private String msg;

    private String data;


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    public boolean isSuccess()
    {
        return  status == SUCCESS;
    }


    public <T> T getObj(Class<T> clz) {

        if (TextUtils.isEmpty(data))
            return null;

        return JSONUtil.getJson(data, clz);
//        return gson.fromJson(data, clz);
    }


    public <T> List<T> getList(Class<T> clz) {
        if (TextUtils.isEmpty(data))
            return null;

        List<T> list = new ArrayList<T>();

       // Type listType = type(List.class, clz);

//        list = gson.fromJson(data, listType);

        list = JSONUtil.getListJson(data,clz);

        return list;
    }


    static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }

        };
    }
}
