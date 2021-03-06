package com.es.base.util;

import com.github.houbb.heaven.util.util.MapUtil;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class ClassUtils {

    public static String getClassVar(String className) {
        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }

    public static List<Field> getAllFieldList(Class clazz) {
        List<Field> fieldList = new ArrayList();

        for (Class tempClass = clazz; tempClass != null; tempClass = tempClass.getSuperclass()) {
            fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
        }

        Iterator var3 = fieldList.iterator();

        while (var3.hasNext()) {
            Field field = (Field) var3.next();
            field.setAccessible(true);
        }
        return fieldList;
    }

    public static Map<String, Field> getAllFieldLMap(Class clazz) {
        List<Field> fieldList = getAllFieldList(clazz);
        return MapUtil.toMap(fieldList, Field::getName);
    }

    public static Map<String, Object> beanToMap(Object bean) {
        try {
            Map<String, Object> map = new LinkedHashMap();
            List<Field> fieldList = getAllFieldList(bean.getClass());
            Iterator var3 = fieldList.iterator();

            while (var3.hasNext()) {
                Field field = (Field) var3.next();
                String fieldName = field.getName();
                Object fieldValue = field.get(bean);
                map.put(fieldName, fieldValue);
            }

            return map;
        } catch (IllegalAccessException var7) {
            throw new RuntimeException(var7);
        }
    }

    public static Map<String, String> beanToMapString(Object bean) {
        List<Field> fieldList = getAllFieldList(bean.getClass());
        Map<String, String> map = fieldList.stream()
                .filter(field -> !field.getName().equals("log"))
                .filter(field -> StringUtils.isNotBlank(getValueString(bean, field)))
                .collect(Collectors.toMap(field -> field.getName(),
                        field -> getValueString(bean, field),
                        (field1, field2) -> field2));

        return map;
    }

    private static String getValueString(Object bean, Field field) {
        try {
            return Optional.ofNullable(field.get(bean)).map(value -> (String) value).orElse("");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException var2) {
            throw new RuntimeException(var2);
        }
    }
}
