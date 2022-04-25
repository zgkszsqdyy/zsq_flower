package com.utils;

import org.apache.commons.beanutils.BeanUtils;

import java.util.Map;

public class beanUtils {
    // 把 Map 中的值注入到对应的 JavaBean 属性中。
    public static <T> T copyParamToBean(Map value , T bean ){
        try {
            //System.out.println("注入之前：" + bean);
/**
 * 把所有请求的参数都注入到 user 对象中
 */
            BeanUtils.populate(bean, value);
            //System.out.println("注入之后：" + bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }

    /**
     * 将字符串转换成为 int 类型的数据
     * @param strInt
     * @param defaultValue
     * @return
     */
    public static Integer parseInt(String strInt,Integer defaultValue) {
        try {
            return Integer.parseInt(strInt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }
}

