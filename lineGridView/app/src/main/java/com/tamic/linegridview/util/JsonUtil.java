package com.tamic.linegridview.util;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by liuyongkui726 on 2016-09-02.
 */
public class JsonUtil {

    /**
     * 解析实体
     * @param jsonStr
     *          json字符串
     * @param entityClass
     *          实体类型
     * @param <T>
     *          实体对象
     * @return
     */
    public static <T> T parseObject(String jsonStr, Class<T> entityClass) {
        T ret = null;

        try {
            ret = JSON.parseObject(jsonStr, entityClass);
        } catch (Exception e) {
            Log.e("will", "parseObject-something Exception with:" + e.toString());
            e.printStackTrace();
        }

        return ret;
    }

    public static <T> T parseObject(String jsonStr, Type type) {
        T obj = null;
        try {
            obj = JSON.parseObject(jsonStr, type, Feature.AutoCloseSource);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }


    /** 解析成map
     * @param jsonStr
     * @param tf
     * @param <T>
     * @return
     */
    public static <T> T parseObject(String jsonStr, TypeReference<T> tf) {
        T obj = null;
        try {
            obj = JSON.parseObject(jsonStr, tf, Feature.AutoCloseSource);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }





    /**
     * 解析List
     * @param jsonStr
     * @param entityClass
     * @param <T>
     * @return
     */
    public static <T> List<T> parseList(String jsonStr, Class<T> entityClass) {
        List<T> ret = null;

        try {
            ret = JSON.parseArray(jsonStr, entityClass);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static String toJSONString(Object obj) {
        String ret = null;

        try {
            ret = JSON.toJSONString(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }
}

