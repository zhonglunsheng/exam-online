package com.exam.online.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhonglunsheng
 * @Description
 * @create 2019-02-14 23:11
 */
public class CommonUtil {

    /**
     * 字符串转list
     * @param ids
     * @return
     */
    public static List<Integer> strToList(String ids) {
        String[] id = ids.split(",");
        List<Integer> idList = new ArrayList<>();
        for (String index:
                id) {
            idList.add(Integer.parseInt(index));
        }
        return idList;
    }

    /**
     * 整形数组转字符串
     * @param array
     * @return
     */
    public static String arrayForIntToStr(int[] array) {
        String[] str= new String[array.length];
        for (int i = 0; i < array.length; i++) {
            str[i] = array[i]+"";
        }
        return Arrays.toString(str).replaceAll("[\\[\\]\\s]", "");
    }
}
