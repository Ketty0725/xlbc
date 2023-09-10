package com.ketty.common_utils;

import com.alibaba.fastjson2.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: Ketty Allen
 * @Date:2023/3/7 - 21:53
 * @Description:com.ketty.common_utils
 * @version: 1.0
 */
public class IPAddressUtil {
    private static final String host = "https://ipcity.market.alicloudapi.com";
    private static final String path = "/ip/city/query";
    private static final String method = "GET";
    private static final String appcode = "";

    public static String getIPAddresses(String ip) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("coordsys", "WGS84");
        querys.put("ip", ip);

        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            if (response.getStatusLine().getStatusCode() == 200) {
                String json = EntityUtils.toString(response.getEntity());
                JSONObject object = JSONObject.parseObject(json).getJSONObject("data").getJSONObject("result");
                String country = object.get("country") == null ? "" : String.valueOf(object.get("country"));
                String prov = object.get("prov") == null ? "" : String.valueOf(object.get("prov"));
                String city = object.get("city") == null ? "" : String.valueOf(object.get("city"));
                return country + "-" + prov + "-" + city;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
