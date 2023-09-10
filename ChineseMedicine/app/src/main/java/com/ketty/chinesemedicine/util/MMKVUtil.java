package com.ketty.chinesemedicine.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.getkeepsafe.relinker.ReLinker;
import com.tencent.mmkv.MMKV;
import com.tencent.mmkv.MMKVLogLevel;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * MMKV封装工具类
 */
public class MMKVUtil {
    private volatile static MMKVUtil instance = null;
    private Context mContext;

    /**
     * 是否加密
     */
    private boolean encrypt;

    /**
     * 秘钥
     */
    private String cryptKey;

    /**
     * 是否迁移SharedPreferences旧数据
     */
    private boolean migrate;

    private MMKVUtil() {
    }

    public static MMKVUtil getInstance() {
        if (instance == null) {
            synchronized (MMKVUtil.class) {
                if (instance == null) {
                    instance = new MMKVUtil();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        mContext = context;
        String root = context.getFilesDir().getAbsolutePath() + "/mmkv";
        if (android.os.Build.VERSION.SDK_INT == 19) {
            MMKV.initialize(root, libName -> ReLinker.loadLibrary(context, libName));
        } else {
            MMKV.initialize(context);
        }
    }

    /**
     * 日志等级
     */
    public void setLogLevel(MMKVLogLevel level) {
        MMKV.setLogLevel(level);
    }

    /**
     * 是否开启加密解密
     *
     * @param encrypt  是否开启
     * @param cryptKey 秘钥
     */
    public void setEncrypt(boolean encrypt, String cryptKey) {
        this.encrypt = encrypt;
        this.cryptKey = cryptKey;
    }

    /**
     * 是否迁移旧数据
     */
    public void setMigrate(boolean migrate) {
        this.migrate = migrate;
    }

    public MMKV getMMKV() {
        return getMMKV(null);
    }

    public MMKV getMMKV(String name) {
        MMKV mmkv;
        if (TextUtils.isEmpty(name)) {
            mmkv = encrypt ? MMKV.defaultMMKV(MMKV.MULTI_PROCESS_MODE, cryptKey) : MMKV.defaultMMKV(MMKV.MULTI_PROCESS_MODE, null);
        } else {
            mmkv = encrypt ? MMKV.mmkvWithID(name, MMKV.MULTI_PROCESS_MODE, cryptKey) : MMKV.mmkvWithID(name, MMKV.MULTI_PROCESS_MODE);
        }
        if (migrate) {
            //迁移SharedPreferences旧数据
            SharedPreferences sharedPreferences;
            if (TextUtils.isEmpty(name)) {
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            } else {
                sharedPreferences = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
            }
            mmkv.importFromSharedPreferences(sharedPreferences);
            sharedPreferences.edit().clear().apply();
        }
        return mmkv;
    }

    //String Set类型---------------------------------------------------------------------------------
    public Set<String> getStringSet(String key) {
        return getMMKV().getStringSet(key, new HashSet<>());
    }

    public Set<String> getStringSet(String key, Set<String> defValues) {
        return getMMKV().getStringSet(key, defValues);
    }

    public Set<String> getStringSet(String kvName, String key, Set<String> defValues) {
        return getMMKV(kvName).getStringSet(key, defValues);
    }

    public void putStringSet(String key, Set<String> values) {
        getMMKV().putStringSet(key, values);
    }

    public void putStringSet(String kvName, String key, Set<String> values) {
        getMMKV(kvName).putStringSet(key, values);
    }

    //Double类型---------------------------------------------------------------------------------
    public void putDouble(String key, double value) {
        getMMKV().encode(key, value);
    }

    public double getDouble(String key) {
        return getMMKV().decodeDouble(key);
    }

    public double getDouble(String key, double defValue) {
        return getMMKV().decodeDouble(key, defValue);
    }

    public double getDouble(String kvName, String key, double defValue) {
        return getMMKV(kvName).decodeDouble(key, defValue);
    }

    //byte[]类型---------------------------------------------------------------------------------
    public void putByte(String key, byte[] value) {
        getMMKV().encode(key, value);
    }

    public byte[] getBytes(String key) {
        return getMMKV().decodeBytes(key);
    }

    public byte[] getBytes(String key, byte[] defValue) {
        return getMMKV().decodeBytes(key, defValue);
    }

    public byte[] getBytes(String kvName, String key, byte[] defValue) {
        return getMMKV(kvName).decodeBytes(key, defValue);
    }

    //String类型---------------------------------------------------------------------------------
    public String getString(String key) {
        return getMMKV().getString(key, "");
    }

    public String getString(String key, String defValue) {
        return getMMKV().getString(key, defValue);
    }

    public String getString(String kvName, String key, String defValue) {
        return getMMKV(kvName).getString(key, defValue);
    }

    public void putString(String key, String value) {
        getMMKV().putString(key, value);
    }

    public void putString(String kvName, String key, String value) {
        getMMKV(kvName).putString(key, value);
    }

    //Boolean类型-----------------------------------------------------------------------------------------------
    public boolean getBoolean(String key) {
        return getMMKV().getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return getMMKV().getBoolean(key, defValue);
    }

    public boolean getBoolean(String kvName, String key, boolean defValue) {
        return getMMKV(kvName).getBoolean(key, defValue);
    }

    public void putBoolean(String key, boolean value) {
        getMMKV().putBoolean(key, value);
    }

    public void putBoolean(String kvName, String key, boolean value) {
        getMMKV(kvName).putBoolean(key, value);
    }

    //Int类型-----------------------------------------------------------------------------------------------
    public void putInt(String key, int value) {
        getMMKV().putInt(key, value);
    }

    public void putInt(String kvName, String key, int value) {
        getMMKV(kvName).putInt(key, value);
    }

    public int getInt(String key) {
        return getMMKV().getInt(key, 0);
    }

    public int getInt(String key, int defValue) {
        return getMMKV().getInt(key, defValue);
    }

    public int getInt(String kvName, String key, int defValue) {
        return getMMKV(kvName).getInt(key, defValue);
    }

    //Float类型-----------------------------------------------------------------------------------------------
    public void putFloat(String key, float value) {
        getMMKV().putFloat(key, value);
    }

    public void putFloat(String kvName, String key, float value) {
        getMMKV(kvName).putFloat(key, value);
    }

    public float getFloat(String key) {
        return getMMKV().getFloat(key, 0f);
    }

    public float getFloat(String key, float defValue) {
        return getMMKV().getFloat(key, defValue);
    }

    public float getFloat(String kvName, String key, float defValue) {
        return getMMKV(kvName).getFloat(key, defValue);
    }

    //Long类型-----------------------------------------------------------------------------------------------
    public void putLong(String key, long value) {
        getMMKV().putLong(key, value);
    }

    public void putLong(String kvName, String key, long value) {
        getMMKV(kvName).putLong(key, value);
    }

    public long getLong(String key) {
        return getMMKV().getLong(key, 0L);
    }

    public long getLong(String key, long defValue) {
        return getMMKV().getLong(key, defValue);
    }

    public long getLong(String kvName, String key, long defValue) {
        return getMMKV(kvName).getLong(key, defValue);
    }

    //对象类型-----------------------------------------------------------------------------------------------
    /**
     * 保存数据
     * @param serializable 实现了序列化的对象
     */
    public void putObjectData(Serializable serializable) {
        getMMKV().putString(serializable.getClass().getName(), JsonHelper.parserObject2Json(serializable))
                .apply();
    }

    public void putObjectData(String kvName, Serializable serializable) {
        getMMKV(kvName).putString(serializable.getClass().getName(), JsonHelper.parserObject2Json(serializable))
                .apply();
    }

    /**
     * 读取数据
     * @param clazz 返回对象   User u = *.loadObjectData(User.class);
     * @return 序列化的对象
     */
    public <T> T getObjectData(Class<T> clazz) {
        String json = getMMKV().getString(clazz.getName(), "");
        if (!TextUtils.isEmpty(json)) {
            return JsonHelper.parserJson2Object(json, clazz);
        } else {
            return null;
        }
    }

    public <T> T getObjectData(String kvName, Class<T> clazz) {
        String json = getMMKV(kvName).getString(clazz.getName(), "");
        if (!TextUtils.isEmpty(json)) {
            return JsonHelper.parserJson2Object(json, clazz);
        } else {
            return null;
        }
    }

    /**
     * 删除指定对象信息
     * @param clazz  对象
     */
    public <T> void deleteAppointObjectData(Class<T> clazz) {
        getMMKV().remove(clazz.getName()).apply();
    }

    public <T> void deleteAppointObjectData(String kvName, Class<T> clazz) {
        getMMKV(kvName).remove(clazz.getName()).apply();
    }

    //其他方法-----------------------------------------------------------------------------------------------
    public void remove(String key) {
        getMMKV().remove(key);
    }

    public void remove(String kvName, String key) {
        getMMKV(kvName).remove(key);
    }

    public void clear() {
        getMMKV().clear();
    }

    public void clear(String kvName) {
        getMMKV(kvName).clear();
    }

    public boolean contains(String key) {
        return getMMKV().contains(key);
    }

    public boolean contains(String kvName, String key) {
        return getMMKV(kvName).contains(key);
    }

}
