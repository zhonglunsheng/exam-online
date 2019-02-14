package com.exam.online.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhonglunsheng
 * @Description
 * @create 2019-02-14 23:11
 */
public class CommonUtil {

    public static List<Integer> StrToList(String ids){
        String[] id = ids.split(",");
        List<Integer> idList = new ArrayList<>();
        for (String index:
                id) {
            idList.add(Integer.parseInt(index));
        }
        return idList;
    }
}
