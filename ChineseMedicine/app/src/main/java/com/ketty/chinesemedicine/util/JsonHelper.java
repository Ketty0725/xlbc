package com.ketty.chinesemedicine.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Json Sting和Object相互转换工具类
 */
public class JsonHelper {
    public JsonHelper() {
    }

    private static volatile Gson gson = null;

    private static Gson getInstance() {
        if (gson == null) {
            synchronized(Gson.class) {
                if (gson == null) {
                    gson = new GsonBuilder()
                            .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
                            .setNumberToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
                            .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
                            .setDateFormat("yyyy-MM-dd HH:mm:ss")
                            .create();
                }
            }
        }
        return gson;
    }

    public static <T> T parserJson2Object(String json, Class<T> classOfT) {
        T bean = null;
        if (!"".equals(json) && classOfT != null) {
            try {
                bean = getInstance().fromJson(json, classOfT);
            } catch (Exception var) {
                bean = null;
                LogUtils.e("====== parserJson2Object:Exception ======" + var.toString());
            }
        }

        return bean;
    }

    public static <T> String parserObject2Json(T entity) {
        String result = "";
        if (entity != null) {
            try {
                result = getInstance().toJson(entity);
            } catch (Exception var) {
                LogUtils.e("====== parserObject2Json:Exception ======" + var.toString());
            }
        }

        return result;
    }

    public static <T> ArrayList<T> parserJson2List(String json, Type typeOfT) {
        ArrayList beans = null;
        if (!"".equals(json) && typeOfT != null) {
            try {
                beans = (ArrayList) getInstance().fromJson(json, typeOfT);
            } catch (Exception var) {
                LogUtils.e("====== parserJson2List:Exception ======" + var.toString());
                beans = null;
            }
        }

        return beans;
    }

    public static <T> HashMap<String, T> parserJson2Map(String json, Type typeOfT) {
        HashMap beans = null;
        if (!"".equals(json) && typeOfT != null) {
            try {
                beans = (HashMap) getInstance().fromJson(json, typeOfT);
            } catch (Exception var) {
                LogUtils.e("====== parserJson2Map:Exception ======" + var.toString());
                beans = null;
            }
        }

        return beans;
    }

    public static <T> String parserList2Json(List<T> list, Type typeOfT) {
        String result = "";
        if (list != null && typeOfT != null) {
            try {
                result = getInstance().toJson(list, typeOfT);
            } catch (Exception var) {
                LogUtils.e("====== parserList2Json:Exception ======" + var.toString());
            }
        }

        return result;
    }

    public static JSONObject getJSONObject(String json) {
        JSONObject object = null;

        try {
            JSONTokener jsonParser = new JSONTokener(json.toString());
            object = (JSONObject) jsonParser.nextValue();
        } catch (Throwable var) {
            LogUtils.e("====== getJSONObject:Exception ======" + var.toString());
        }

        return object;
    }

    public static String getJSONValueByKey(String json, String key) {
        String value = "";
        JSONObject obj = getJSONObject(json);
        if (obj != null) {
            try {
                value = obj.getString(key);
            } catch (Throwable var) {
                LogUtils.e("====== getJSONValueByKey:Exception ======" + var.toString());
            }
        }

        return value;
    }
}
