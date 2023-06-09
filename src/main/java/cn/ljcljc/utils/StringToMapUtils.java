package cn.ljcljc.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 字符串转Map类
 * @author 李锦成
 * @version 1.0(2023.06)
 */

public class StringToMapUtils {

    /**
     * 字符串转Map
     * @param str 字符串
     * @return
     */
    public static Map<String,String> mapStringToMap(String str){
        str = str.substring(1, str.length()-1);
        String[] strs = str.split(",");
        Map<String,String> map = new HashMap<String, String>();
        for (String string : strs) {
            String key = string.split("=")[0];
            String value = string.split("=")[1];
            // 去掉头部空格
            String key1 = key.trim();
            String value1 = value.trim();
            map.put(key1, value1);
        }
        return map;
    }
}
