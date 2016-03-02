package com.example.sony.jizha.utils;

/**
 * 网络请求属于耗时操作，需要在子线程中运行，但是主线程还要获取请求的数据，所以要使用java的回调机制
 * Created by sony on 2015/12/18.
 */
public interface HttpCallBackListener {
    /**
     * 请求完成
     *
     * @param response
     */
    void onFinish(String response);

    /**
     * 请求错误
     *
     * @param e
     */
    void onError(Exception e);
}
