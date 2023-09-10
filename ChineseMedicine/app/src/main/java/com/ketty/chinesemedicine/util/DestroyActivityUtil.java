package com.ketty.chinesemedicine.util;

import android.app.Activity;

import com.google.android.exoplayer2.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DestroyActivityUtil {

    private static Map<String, Activity> destoryMap = new HashMap<>();

    //将Activity添加到队列中
    public static void addDestoryActivityToMap(Activity activity, String activityName) {
        destoryMap.put(activityName, activity);
    }

    //根据名字销毁制定Activity
    public static void destoryActivity(String activityName) {
        Set<String> keySet = destoryMap.keySet();
        Log.i("keySet", String.valueOf(keySet.size()));
        if (keySet.size() > 0) {
            for (String key : keySet) {
                if (activityName.equals(key)) {
                    destoryMap.get(key).finish();
                }
            }
        }
    }

}
