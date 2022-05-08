package com.ctg.mes.config.common.util;

import com.google.common.primitives.Primitives;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author 林澄南
 * @date 2019/7/22 11:32
 */
public class InjectUtils {

    /**
     * 新建一个对象，该对象的属性值自动根据contents来设置
     * @param object 要填充的对象
     * @param contents "field=value"的list，field不区分大小写，用来设置对应的属性值，如果对象的class不存在该field则跳过
     */
    public static void injectField(Object object, List<String> contents) {
        injectField(object, contents, "=");
    }


    /**
     * 新建一个对象，该对象的属性值自动根据contents来设置
     * @param object 要填充的对象
     * @param contents "field" + separator + "value"的list，field不区分大小写，用来设置对应的属性值，如果对象的class不存在该field则跳过
     * @param separator field和value的分隔符
     */
    public static void injectField(Object object, List<String> contents, String separator) {
        for (String content : contents) {
            String[] arr = content.split(separator);
            try {
                Field field = ReflectionUtils.findField(object.getClass(), arr[0]);
                if (field != null) {
                    field.setAccessible(true);
                    field.set(object, Primitives.wrap(field.getType()).getConstructor(String.class).newInstance(arr[1]));
                }
            } catch (Exception ignored) {
            }
        }
    }
}
