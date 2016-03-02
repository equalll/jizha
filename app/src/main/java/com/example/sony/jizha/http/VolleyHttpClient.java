/*
*VolleyHttpClient.java
*Created on 2014-9-25 上午10:46 by Ivan
*Copyright(c)2014 Guangzhou Onion Information Technology Co., Ltd.
*http://www.cniao5.com
*/
package com.example.sony.jizha.http;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.sony.jizha.Widget.LoadingDialog;
import com.example.sony.jizha.utils.ToastUtils;

import java.util.Map;

/**
 * Created by Ivan on 14-9-25.
 * Copyright(c)2014 Guangzhou Onion Information Technology Co., Ltd.
 * http://www.cniao5.com
 */
public class VolleyHttpClient {


    private static VolleyHttpClient mInstance;

    private VolleySingleton volleySingleton;

    private LoadingDialog mDialig;

    private Context mContext;

    public static synchronized VolleyHttpClient getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyHttpClient(context);
        }
        mInstance.initDialog(context);
        return mInstance;
    }


    public VolleyHttpClient(Context context) {

        mContext = context;
        initDialog(context);
        volleySingleton = VolleySingleton.getInstance(context);

    }

    private void initDialog(Context context) {

        if (context instanceof Activity)
            mDialig = new LoadingDialog(context);
    }

    public void post(String url,
                     Map<String, String> params,
                     int loadingMsg,
                     final RequestListener listener) {

        request(Request.Method.POST, url, params, loadingMsg, listener);
    }


    public void get(String url,
                    int loadingMsg,
                    final RequestListener listener) {

        request(Request.Method.GET, url, null, loadingMsg, listener);
    }

    public void request(int method,
                        String url,
                        Map<String, String> params,
                        int loadingMsg,
                        final RequestListener listener) {


        if (listener != null)
            listener.onPreRequest();

        showLoading(loadingMsg);
        BaseRequest request = new BaseRequest(
                method,
                url,
                params,
                new Response.Listener<BaseResponse>() {
                    public void onResponse(BaseResponse response) {

                        dismissLoading();
                        if (listener != null) {
                            if (response.isSuccess())
                                listener.onRequestSuccess(response);
                            else
                                listener.onRequestFail(response.getStatus(), response.getMsg());
                        }
                    }
                },

                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {

                        try {
                            dismissLoading();
                            String errMsg = null;
                            int errCode = -1;
                            if (error == null) {

                                errMsg = "请求服务器出错，错误代码未知";
                                //                            errCode=-1;
                            } else {
                                errMsg = VolleyErrorHelper.getMessage(mContext, error);
                                errCode = error.networkResponse == null ? errCode : error.networkResponse.statusCode;
                            }

                            ToastUtils.show(mContext, errMsg);
                            if (listener != null) {
                                listener.onRequestError(errCode, errMsg);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
        );


        volleySingleton.addToRequestQueue(request);
    }


    private void showLoading(int msg) {

        if (msg > 0 && mDialig != null) {

            mDialig.setDialogText(msg);
            mDialig.showDialog();
        }
    }


    private void dismissLoading() {

        if (mDialig != null && mDialig.isShowing())
            mDialig.downDialog();
    }
}
