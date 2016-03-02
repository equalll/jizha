/*
*RequestListener.java
*Created on 2014-9-25 上午10:45 by Ivan
*Copyright(c)2014 Guangzhou Onion Information Technology Co., Ltd.
*http://www.cniao5.com
*/
package com.example.sony.jizha.http;

/**
 * Created by Ivan on 14-9-25.
 * Copyright(c)2014 Guangzhou Onion Information Technology Co., Ltd.
 * http://www.cniao5.com
 */
public interface RequestListener {

    /**
     * 在请求之前调用的方法
     */
    public  void onPreRequest();

    /**
     * 请求成功调用
     * @param response
     */
    public  void onRequestSuccess(BaseResponse response);

    /**
     * 请求失败调用，致命错误
     * @param code
     * @param msg
     */
    public  void onRequestError(int code, String msg);

    /**
     * 服务器返回失败调用
     * @param code
     * @param msg
     */
    public  void onRequestFail(int code, String msg);
}
