/*
*BaseRequest.java
*Created on 2014-9-25 上午10:38 by Ivan
*Copyright(c)2014 Guangzhou Onion Information Technology Co., Ltd.
*http://www.cniao5.com
*/
package com.example.sony.jizha.http;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by Ivan on 14-9-25.
 * Copyright(c)2014 Guangzhou Onion Information Technology Co., Ltd.
 * http://www.cniao5.com
 */
public class BaseRequest extends Request<BaseResponse> {


    public static final String TAG = "BaseRequest";

    private Response.Listener<BaseResponse> mListener;
    private Map<String, String> mParams;

    public BaseRequest(int method, String url, Map<String, String> params, Response.Listener listener, Response.ErrorListener Errorlistener) {
        super(method, url, Errorlistener);

        mListener = listener;
        this.mParams = params;
    }


    @Override
    protected Response<BaseResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));

            Log.d(TAG, "respone:" + jsonString);


            BaseResponse baseResponse = parseJson(jsonString);
            return Response.success(baseResponse, HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        return null;


    }

    @Override
    protected void deliverResponse(BaseResponse response) {

        mListener.onResponse(response);
    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }


    private BaseResponse parseJson(String json) {
        int status = 0;
        String msg = null;
        String data = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            status = jsonObject.getInt("status");
            msg = jsonObject.getString("msg");
            data = jsonObject.getString("data");


        } catch (JSONException e) {
            e.printStackTrace();
        }


        BaseResponse response = new BaseResponse();

        response.setStatus(status);
        response.setMsg(msg);
        response.setData(data);

        return response;


    }
}
