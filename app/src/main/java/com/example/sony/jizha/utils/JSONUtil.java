package com.example.sony.jizha.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio
 * 项目名称：jizha
 * 类描述：解析json数据的工具类
 * 创建人：sony
 * 创建时间：2015/12/30 15:42
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version V1.0
 */
public class JSONUtil {

    //创建gson对象
    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public static Gson getGson() {
        return gson;
    }

    //使用Gson进行解析单个json
    public static <T> T getJson(String jsonString, Class<T> cls) {
        T t = null;
        try {
            t = gson.fromJson(jsonString, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }


    // 使用Gson进行解析列表json解析
    public static <T> List<T> getListJson(String jsonString, Class<T> cls) {
       /* List<T> list = new ArrayList<T>();
        try {
            list = gson.fromJson(jsonString, new TypeToken<List<T>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;*/
        ArrayList<T> mList = new ArrayList<T>();

        T t = null;
        boolean date = new JsonParser().parse(jsonString).isJsonNull();
        if (date) {
            t = getJson(jsonString, cls);
            mList.add(t);
        } else {
            try {
                JsonArray array = new JsonParser().parse(jsonString).getAsJsonArray();
                for (final JsonElement elem : array) {
                    mList.add(gson.fromJson(elem, cls));
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        return mList;
    }
}
