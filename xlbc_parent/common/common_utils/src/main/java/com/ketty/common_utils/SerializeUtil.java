package com.ketty.common_utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * 序列化工具
 * @author zhouhanfei 2021.12.2
 */
@Slf4j
public class SerializeUtil {

    public static byte[] serialize(Object o) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ObjectOutputStream outo = new ObjectOutputStream(out);
            outo.writeObject(o);
        } catch (IOException e) {
            log.error("serialize exception",e);
        }

        return out.toByteArray();
    }

    public static Object unSerialize(byte[] b) {
        ObjectInputStream oin;
        try {
            oin = new ObjectInputStream(new ByteArrayInputStream(b));
            try {
                return oin.readObject();
            } catch (ClassNotFoundException e) {
                log.error("unSerialize readObject exception",e);
                return null;
            }
        } catch (IOException e) {
            log.error("unSerialize exception",e);
            return null;
        }

    }

}
