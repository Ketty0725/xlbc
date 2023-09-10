package com.ketty.api_web;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.ketty.api_entity.Productshopcart;
import com.ketty.api_entity.User;
import com.ketty.api_entity.enums.LikedStateEnum;
import com.ketty.common_utils.HttpUtils;
import com.ketty.common_utils.IDKeyUtil;
import com.ketty.common_utils.RedisKeyUtils;
import com.ketty.provider_sms.SMSUtil;
import com.ketty.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @Auther: Ketty Allen
 * @Date:2023/1/20 - 11:49
 * @Description:com.ketty.common_base
 * @version: 1.0
 */
public class DemoTest {

    public static final String ZYstart = "<zy>";
    public static final String ZYend = "</zy>";
    public static final String FJstart = "<fj>";
    public static final String FJend = "</fj>";

    @Test
    public void test01() {
        String text = "hello<zy>白芷</zy>world";
        String subText = text.substring(text.indexOf("<zy>")+"<zy>".length(),text.indexOf("</zy>"));
        System.out.println(text.indexOf(subText));
    }

    @Test
    public void test02() {
        String text = "1、《注解伤寒论》：硬则气坚，咸味可以软之，旋覆之咸，以软痞硬；虚则气浮，重剂可以镇之，代赭之重，以镇虚逆；辛者散也，<zy>生姜</zy>、<zy>半夏</zy>之辛，以散虚痞；甘者缓也，<zy>人参</zy>、<zy>甘草</zy>、大枣之甘，以补胃弱。\\n2、《删补名医方论》引罗天益曰：方中以<zy>人参</zy>。<zy>甘草</zy>养正补虚；<zy>生姜</zy>、大枣和脾养胃，所以定中州者至矣；更以<zy>代赭石</zy>之重，使之敛浮镇逆；<zy>旋覆花</zy>之辛用以宣气涤饮；佐以<zy>人参</zy>以归气于下；佐<zy>半夏</zy>以蠲饮于上。浊降则痞硬可消，清升则噫气可除矣。\\n3、《医方考》：旋覆之咸，能软痞硬而下气；代赭之重，能镇心君而止噫；姜、夏之辛，所以散逆；参、草、大枣之甘，所以补虚。\\n4、《伤寒论三注》：<zy>旋覆花</zy>能消痰结软痞，治噫气；<zy>代赭石</zy>治反胃，除五脏血脉中热，健脾，乃痞而噫气者用之，谁曰不宜？于是佐以<zy>生姜</zy>之辛，可以开结也；<zy>半夏</zy>逐饮也；<zy>人参</zy>补正也；<fj>桂枝散</fj>邪也；<zy>甘草</zy>、大枣益胃也。余每借之以治反胃、噎食不降者，靡不神效。\\n5、《成方便读》：<zy>旋覆花</zy>能斡旋胸腹之气，软坚化痰；而以<zy>半夏</zy>之辛温散结者协助之；虚则气上逆，故以代赭之重以镇之；然治病必求其本，痞硬噫气等疾，皆由正虚而来，故必以<zy>人参</zy>、<zy>甘草</zy>补脾而安正，然后痰可消，结可除，且旋覆、<zy>半夏</zy>之功，益彰其效耳；用姜枣者，病因伤寒汗吐下后而得，则表气必伤，藉之以和营卫也。";
        /*String temp = text;
        while (temp.contains("<zy>")) {
            String subText = temp.substring(temp.indexOf(ZYstart)+ZYstart.length(),temp.indexOf(ZYend));
            temp = temp.replace(temp.substring(0,temp.indexOf(ZYend)+ZYend.length()),"");
            System.out.println(temp);
            System.out.println(subText);
            final String showText = text.replace(ZYstart,"").replace(ZYend,"");
            System.out.println(showText);
        }*/


        int begin = text.indexOf("<", 0) + 4;
        int end = text.indexOf("</", 0) + 5;
        String subText = text.substring(begin, end - 5);
        System.out.println(text.substring(begin-4,begin));

    }

    @Test
    public void test03() {
        String s = "1672502399000";
        long millis = Long.parseLong(s);
        String time = getRecentTimeSpanByNow(millis);
        System.out.println(time);
    }

    public static String getRecentTimeSpanByNow(final long millis) {
        final int SEC  = 1000;
        final int MIN  = 60000;
        final int HOUR = 3600000;

        long now = System.currentTimeMillis();
        long span = now - millis;

        Calendar from = Calendar.getInstance();
        from.setTime(new Date(millis));
        Calendar to = Calendar.getInstance();
        to.setTime(new Date(now));

        int fromYear = from.get(Calendar.YEAR);
        int fromDay = from.get(Calendar.DAY_OF_MONTH);

        int toYear = to.get(Calendar.YEAR);
        int toDay = to.get(Calendar.DAY_OF_MONTH);
        int year = toYear - fromYear;
        int day = toDay - fromDay;

        if (year < 1) {
            if (day < 1) {
                if (span < 1000) {
                    return "刚刚";
                } else if (span < MIN) {
                    return String.format(Locale.getDefault(), "%d秒前", span / SEC);
                } else if (span < HOUR) {
                    return String.format(Locale.getDefault(), "%d分钟前", span / MIN);
                } else {
                    return String.format(Locale.getDefault(), "%d小时前", span / HOUR);
                }
            } else if (day < 2) {
                return "昨天";
            } else if (day < 6) {
                return day + "天前";
            } else {
                return new SimpleDateFormat("MM-dd").format(millis);
            }
        } else {
            return new SimpleDateFormat("yyyy-MM-dd").format(millis);
        }
    }

    @Test
    public void test04() {
        System.out.println(IDKeyUtil.generateId());
    }

    @Test
    public void test05() {
        System.out.println(LikedStateEnum.LIKE.getCode());
        System.out.println(LikedStateEnum.LIKE.getMsg());
    }

    @Test
    public void test06() {
        String className = SSLConnectionSocketFactory.class.getName();
        String classNamePath = className.replace(".", "/") + ".class";
        URL is = SSLConnectionSocketFactory.class.getClassLoader().getResource(classNamePath);
        String path = is.getFile();
        path = StringUtils.replace(path, "%20", " ");
        System.out.println(StringUtils.removeStart(path, "/"));
    }

    @Test
    public void test07() {
        String uPhone = "19303028884";
        String captcha = String.valueOf((int)((Math.random()*9+1)*100000));
        SMSUtil.sendSMS(uPhone, captcha);
    }

    @Test
    public void test08() {
        User user = new User();
        user.setUId(1111L);
        user.setUName("1111");
        User user1 = usersBlankNum(User.class,user);
        System.out.println(user1.getUName());
    }

    public <U> U usersBlankNum(Class<U> clazz, U cla) {
        //将传过来的对象进行赋值处理，
        //此时u可用来代表传过来的对象（本示意中是Users）,
        //此时可以用u调用传过来对象的方法
        U u = clazz.cast(cla);
        //以下是验证此示意中实体类可被操作了
        //getDeclaredFields()：获得某个类的所有声明的字段，即包括public、private和proteced，但是不包括父类的申明字段。
        //.getClass()是一个对象实例的方法，只有对象实例才有这个方法，具体的类是没有的
        for (Field field : u.getClass().getDeclaredFields()) {
            //允许获取实体类private的参数信息 field.setAccessible(true);
            field.setAccessible(true);
            try {
                System.out.println(field.getName()+":::"+String.valueOf(field.get(u)));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return u;
    }

    @Test
    public void test09() {
        List<Productshopcart> dbList = new ArrayList<>();
        List<Productshopcart> rdList = new ArrayList<>();
        List<Productshopcart> list = new ArrayList<>();
        dbList.add(new Productshopcart(111L,222L,1));
        dbList.add(new Productshopcart(111L,333L,1));
        dbList.add(new Productshopcart(222L,444L,1));

        rdList.add(new Productshopcart(333L,222L,3));
        rdList.add(new Productshopcart(111L,222L,5));
        rdList.add(new Productshopcart(666L,222L,3));
        rdList.add(new Productshopcart(666L,333L,-1));

        for (Iterator<Productshopcart> iterator = rdList.iterator(); iterator.hasNext();) {
            Productshopcart bean = iterator.next();
            if (bean.getNumber() == -1) {
                iterator.remove();
            } else {
                dbList.removeIf(next -> next.getUserId().equals(bean.getUserId())
                        && next.getProductId().equals(bean.getProductId()));
            }
        }

        dbList.addAll(rdList);
        for (Productshopcart bean : dbList) {
            System.out.println(bean.toString());
        }
    }

    @Test
    public void test10() {
        float f = 1.227f;
        f = Math.round(f * 100) / 100f;
        System.out.println(f);
    }

    @Test
    public void test11() {
        String host = "https://ipcity.market.alicloudapi.com";
        String path = "/ip/city/query";
        String method = "GET";
        String appcode = "02fad517e42d4615a276231ccf8f64c1";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("coordsys", "WGS84");
        querys.put("ip", "113.214.207.225");

        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            if (response.getStatusLine().getStatusCode() == 200) {
                String json = EntityUtils.toString(response.getEntity());
                JSONObject object = JSONObject.parseObject(json);
                String country = String.valueOf(object.getJSONObject("data").getJSONObject("result").get("country"));
                String prov = String.valueOf(object.getJSONObject("data").getJSONObject("result").get("prov"));
                String city = String.valueOf(object.getJSONObject("data").getJSONObject("result").get("city"));
                String[] address = {country, prov, city};
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test12() {
        String json = "{\"msg\":\"成功\",\"success\":true,\"code\":200,\"data\":{\"orderNo\":\"220029511835364363\",\"result\":{\"continent\":\"亚洲\",\"owner\":\"华数传媒\",\"country\":\"中国\",\"lng\":\"120.749584\",\"adcode\":\"330400\",\"city\":\"嘉兴市\",\"timezone\":\"UTC+8\",\"isp\":\"华数传媒\",\"accuracy\":\"城市\",\"source\":\"数据挖掘\",\"asnumber\":\"24139\",\"areacode\":\"CN\",\"zipcode\":\"314000\",\"radius\":\"68.5715\",\"prov\":\"浙江省\",\"lat\":\"30.770355\"}}}";
        try {
            JSONObject object = JSONObject.parseObject(json).getJSONObject("data").getJSONObject("result");
            String country = String.valueOf(object.get("country"));
            String prov = String.valueOf(object.get("prov"));
            String city = String.valueOf(object.get("city"));

            System.out.println(country + "-" + prov + "-" + city);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test13() {
        String id = UUID.randomUUID().toString();
        System.out.println(id);
    }

    @Test
    public void test14() {
        long id = IDKeyUtil.generateId();
        System.out.println(id);
    }

}
