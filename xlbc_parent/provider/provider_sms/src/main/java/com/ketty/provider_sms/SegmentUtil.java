package com.ketty.provider_sms;

import java.util.Arrays;

public class SegmentUtil {
    private static final String[] cmcc = {
            "134","135","136","137","138","139",
            "147","150","151","152","157","158",
            "159","165","170","172","178","182",
            "183","184","187","188","195","197",
            "198"
    };
    private static final String[] cnc = {
            "130","131","132","145","155","156",
            "166","167","171","175","176","185",
            "186","196"
    };
    private static final String[] ct = {
            "133","153","162","173","177","180",
            "181","189","190","191","193","199"
    };
    private static final String[] bctv = {"192"};

    public static boolean compare(String phone) {
        String str = phone.replaceAll("\\s*","").substring(0,3);
        boolean res1 = Arrays.asList(cmcc).contains(str);
        boolean res2 = Arrays.asList(cnc).contains(str);
        boolean res3 = Arrays.asList(ct).contains(str);
        boolean res4 = Arrays.asList(bctv).contains(str);
        if (res1 || res2 || res3 || res4) {
            return true;
        } else {
            return false;
        }
    }
}
