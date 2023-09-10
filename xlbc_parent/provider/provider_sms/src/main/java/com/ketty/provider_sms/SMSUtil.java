package com.ketty.provider_sms;

import com.ketty.provider_sms.util.CHttpPost;
import com.ketty.provider_sms.util.ConfigManager;
import com.ketty.provider_sms.util.Message;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SMSUtil {
    private static final String userid = "";
    private static final String password = "";
    private static final String ipAddress1 = "api01.monyun.cn:7901";
    private static final String ipAddress2 = "api02.monyun.cn:7901";
    private static final String ipAddress3 = null;
    private static final String ipAddress4 = null;
    // 日期格式定义
    private static SimpleDateFormat sdf	= new SimpleDateFormat("MMddHHmmss");

    public static void sendSMS(String phone, String captcha) {
        //设置IP
        ConfigManager.setIpInfo(ipAddress1, ipAddress2, ipAddress3, ipAddress4);

        //密码是否加密 true：密码加密;false：密码不加密
        ConfigManager.IS_ENCRYPT_PWD = true;
        boolean isEncryptPwd = ConfigManager.IS_ENCRYPT_PWD;

        // 单条发送
        singleSend(phone, captcha, isEncryptPwd);

        //清除所有IP (此处为清除IP示例代码，如果需要修改IP，请先清除IP，再设置IP)
        ConfigManager.removeAllIpInfo();

    }

    /**
     *
     * @description  单条发送
     * @param phone  手机号码
     * @param captcha 验证码
     * @param isEncryptPwd 密码是否加密   true：密码加密;false：密码不加密
     */
    public static void singleSend(String phone, String captcha, boolean isEncryptPwd) {
        try {
            // 参数类
            Message message = new Message();
            // 实例化短信处理对象
            CHttpPost cHttpPost = new CHttpPost();

            // 设置账号   将 userid转成大写,以防大小写不一致
            message.setUserid(userid.toUpperCase());

            //判断密码是否加密。
            //密码加密，则对密码进行加密
            if (isEncryptPwd) {
                // 设置时间戳
                String timestamp = sdf.format(Calendar.getInstance().getTime());
                message.setTimestamp(timestamp);

                // 对密码进行加密
                String encryptPwd = cHttpPost.encryptPwd(message.getUserid(),password, message.getTimestamp());
                // 设置加密后的密码
                message.setPwd(encryptPwd);
            } else {
                // 设置密码
                message.setPwd(password);
            }

            // 设置手机号码 此处只能设置一个手机号码
            message.setMobile(phone);
            // 设置内容
            String content = new String("您的验证码是"+captcha+"，在30分钟内有效。如非本人操作请忽略本短信。");
            message.setContent(content);

            // 返回的平台流水编号等信息
            StringBuffer msgId = new StringBuffer();
            // 返回值
            int result = -310099;
            // 发送短信
            result = cHttpPost.singleSend(message, msgId);
            // result为0:成功;非0:失败
            if (result == 0) {
                System.out.println("单条发送提交成功！");
                System.out.println(msgId.toString());
            } else {
                System.out.println("单条发送提交失败,错误码：" + result);
            }
        } catch (Exception e) {
            //异常处理
            e.printStackTrace();
        }
    }

}
