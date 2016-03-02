package com.example.sony.jizha.adapter;

import android.view.View;

import java.lang.reflect.Field;

/**
 * Created with Android Studio
 * 项目名称：jizha
 * 类描述：通过反射获取到页面上的全部属性，反射的字段可能是一个类（静态）字段或实例字段
 * 创建人：sony
 * 创建时间：2016/1/4 18:57
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version V1.0
 */
public class ViewInjector {

    public static synchronized void injectView(Object clz, View view) {

        // 通过反射获取到全部属性，反射的字段可能是一个类（静态）字段或实例字段
        Field[] fields = clz.getClass().getDeclaredFields();
        if (fields != null && fields.length > 0) {

            for (Field field : fields) {

                field.setAccessible(true);
                // 根据名字获取到ID的值
                int id = view.getResources().getIdentifier(field.getName(), "id", view.getContext().getPackageName());

                if (id > 0) {

                    View fieldView = view.findViewById(id);
                    try {
                        field.set(clz, fieldView);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                field.setAccessible(false);
            }
        }
    }
}
