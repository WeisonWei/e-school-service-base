package com.es.base.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.function.BiConsumer;


@Slf4j
public enum MockField {
    ID("ID", MockField::setRandomId),
    TIME("TIME", MockField::setRandomTime),
    STRING("STRING", MockField::setRandomString),
    INTEGER("INTEGER", MockField::setNumberBetween),
    LONG("LONG", MockField::setRandomLong),
    FLOAT("FLOAT", MockField::setRandomFloat),
    ENUM("ENUM", MockField::setEnum);

    @Getter
    private String type;

    @Getter
    private BiConsumer<Object, Field> bitConsumer;


    MockField(String type, BiConsumer<Object, Field> bitConsumer) {
        this.type = type;
        this.bitConsumer = bitConsumer;
    }

    public static void setValue(Object object, Field field) {
        String name = field.getName();
        String classTypeName;
        switch (name) {
            case "id":
            case "pid":
            case "oid":
            case "uid":
            case "cid":
            case "mid":
                classTypeName = "ID";
                break;
            case "ctime":
            case "utime":
            case "date":
                classTypeName = "TIME";
                break;
            default:
                classTypeName = field.getType().isEnum() ? "ENUM" : field.getType().getSimpleName().toUpperCase();
        }

        Arrays.stream(MockField.values())
                .filter(mockField -> mockField.getType().equals(classTypeName))
                .findAny()
                .ifPresent(mockField -> mockField.bitConsumer.accept(object, field));
    }

    public static void setRandomId(Object object, Field field) {
        try {
            field.set(object, SnowFlake.genId());
        } catch (IllegalAccessException e) {
        }
    }

    public static void setRandomTime(Object object, Field field) {
        try {
            field.set(object, System.currentTimeMillis());
        } catch (IllegalAccessException e) {
        }
    }


    private static void setEnum(Object object, Field field) {
        try {
            Enum[] invoke = (Enum[]) Class.forName(field.getType().getName())
                    .getMethod("values")
                    .invoke(null, null);
            field.set(object, invoke[0]);
        } catch (IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
        }
    }

    private static void setRandomLong(Object object, Field field) {
        try {
            field.set(object, Long.valueOf(DataUtils.dataFactory.getNumberBetween(1, 10)));
        } catch (IllegalAccessException e) {
        }
    }
    private static void setRandomFloat(Object object, Field field) {
        try {
            field.set(object, (float) DataUtils.dataFactory.getNumberBetween(1, 10));
        } catch (IllegalAccessException e) {
        }
    }

    private static void setNumberBetween(Object object, Field field) {
        try {
            field.set(object, DataUtils.dataFactory.getNumberBetween(1, 10));
        } catch (IllegalAccessException e) {
        }
    }
    private static void setRandomString(Object object, Field field) {
        try {
            field.set(object, DataUtils.dataFactory.getRandomText(10, 20));
        } catch (IllegalAccessException e) {
        }
    }

}
