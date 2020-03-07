package com.es.util;

import org.fluttercode.datafactory.impl.DataFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;

import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class DataUtils {

    public static final Random random = new Random();
    public static final DataFactory dataFactory = new DataFactory();

    public static <T> T mockObj(final Class<T> type) throws Exception {
        T obj = type.getDeclaredConstructor().newInstance();
        ClassUtils.getAllFieldList(type)
                .stream()
                .filter(field -> !Modifier.isFinal(field.getModifiers()))
                .forEach(field -> MockField.setValue(obj, field));
        return obj;
    }

    private DataUtils() {
    }

    public static String mockString() {
        return dataFactory.getRandomText(10, 20);
    }

    public static Integer mockInt() {
        return dataFactory.getNumberBetween(1, 10);
    }

    public static Long mockLong() {
        return Long.valueOf(dataFactory.getNumberBetween(1, 10));
    }

    public static Float mockFloat() {
        return (float) dataFactory.getNumberBetween(1, 100);
    }

    public static Float mockFloatBetween(Float begin, Float end) {
        BigDecimal bigDecimal = new BigDecimal(end - begin);
        BigDecimal point = new BigDecimal(Math.random());
        BigDecimal multiply = point.multiply(bigDecimal);
        BigDecimal result = multiply.add(new BigDecimal(begin)).setScale(2, BigDecimal.ROUND_FLOOR);
        return result.floatValue();
    }

    public static <T> List<T> mockList() throws Exception {
        return Arrays.asList();
    }

    public static <T> List<T> mockList(final Class<T> type) throws Exception {
        return Arrays.asList(mockObj(type), mockObj(type));
    }

    public static <T> List<T> mockList(final Class<T> type, int num) throws Exception {
        List list = new ArrayList(num);
        for (int i=0; i< num; i++) {
            list.add(mockObj(type));
        }
        return list;
    }

    public static <T> Page<T> mockPage() throws Exception {
        List<T> list = Arrays.asList();
        return PageableExecutionUtils.getPage(list, PageRequest.of(0, 10), list::size);
    }

    public static <T> Page<T> mockPage(final Class<T> type) throws Exception {
        List<T> list = Arrays.asList(mockObj(type), mockObj(type));
        return PageableExecutionUtils.getPage(list, PageRequest.of(0, 10), list::size);
    }

    public static <T> Page<T> mockPage(final Class<T> type, int num) throws Exception {
        List<T> list = mockList(type, num);
        return PageableExecutionUtils.getPage(list, PageRequest.of(0, 10), list::size);
    }
    /**
     * pageSize 每页数量以pageable中为准
     * page 从0开始
     *
     * @param list
     * @param pageable
     * @param <T>
     * @return
     */
    public static <T> Page<T> listToPage(List<T> list, Pageable pageable) {
        return getPage(list, pageable);
    }

    /**
     * List to Page
     */
    public static <T> Page<T> listToPage(List<T> list, Pageable pageable, long total) {
        return new PageImpl<>(list, pageable, total);
    }

    /**
     * pageSize 每页数量为15
     * page 从0开始
     *
     * @param list
     * @param page
     * @param <T>
     * @return
     */
    public static <T> Page<T> listToPage(List<T> list, int page) {
        PageRequest pageable = PageRequest.of(page, 15);
        return getPage(list, pageable);
    }

    private static <T> Page<T> getPage(List<T> list, Pageable pageable) {
        //第n页起始值
        int pageStart = pageable.getPageNumber() * pageable.getPageSize();
        //第n页期望结尾值
        int expectPageEnd = pageStart + pageable.getPageSize() - 1;
        return Optional.of(list.size())
                .filter(size -> size >= pageStart)
                .map(size -> getRealSubList(pageStart, expectPageEnd, list, pageable))
                .orElseGet(() -> getEmptySubList(list, pageable));
    }

    private static <T> PageImpl<T> getRealSubList(int pageStart, int expectPageEnd, List<T> list, Pageable pageable) {
        int realPageEnd = (list.size() > expectPageEnd) ? expectPageEnd + 1 : list.size();
        return new PageImpl<>(list.subList(pageStart, realPageEnd), pageable, list.size());
    }

    private static <T> PageImpl<T> getEmptySubList(List<T> list, Pageable pageable) {
        return new PageImpl<>(new ArrayList(), pageable, list.size());
    }

    public static BigDecimal getBigDecimal(float amount) {
        return new BigDecimal(amount + "");
    }

    public static float floatAdd(float x, float y) {
        return getBigDecimal(x)
                .add(getBigDecimal(y))
                .setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public static float floatSubtract(float x, float y) {
        return getBigDecimal(x)
                .subtract(getBigDecimal(y))
                .setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public static List buildList(Object... args) {
        return Arrays.stream(args).collect(Collectors.toList());
    }
}
