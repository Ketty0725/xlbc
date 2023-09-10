package com.ketty.common_utils;

/**
 * @Auther: Ketty Allen
 * @Date:2023/2/2 - 23:48
 * @Description:com.ketty.common_utils
 * @version: 1.0
 */
public class RedisKeyUtils {
    //保存用户点赞数据的key
    public static final String MAP_KEY_USER_LIKED = "MAP_USER_LIKED";
    //保存对象被点赞数量的key
    public static final String MAP_KEY_OBJECT_LIKED_COUNT = "MAP_OBJECT_LIKED_COUNT";
    //保存用户收藏数据的key
    public static final String MAP_KEY_USER_COLLECT = "MAP_KEY_USER_COLLECT";
    //保存对象被收藏数量的key
    public static final String MAP_KEY_OBJECT_COLLECT_COUNT = "MAP_KEY_OBJECT_COLLECT_COUNT";
    //保存用户加入购物车的商品数据的key
    public static final String MAP_KEY_ADD_SHOP_CART = "MAP_KEY_ADD_SHOP_CART";
    //保存用户浏览记录数据的key
    public static final String MAP_KEY_BROWSE_HISTORY = "MAP_KEY_BROWSE_HISTORY";

    public static String getHashKey(Object key1, Object key2) {
        StringBuilder builder = new StringBuilder();
        builder.append(key1);
        builder.append("::");
        builder.append(key2);
        return builder.toString();
    }

    public static String getHashKey(Object key1, Object key2, Object key3) {
        StringBuilder builder = new StringBuilder();
        builder.append(key1);
        builder.append("::");
        builder.append(key2);
        builder.append("::");
        builder.append(key3);
        return builder.toString();
    }
}
