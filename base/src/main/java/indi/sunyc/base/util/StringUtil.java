package indi.sunyc.base.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by ChamIt-001 on 2017/5/10.
 */
public class StringUtil extends StringUtils {

    /**
     * 判断字符串非空
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj){
        if(obj != null && !isEmpty(obj.toString())){
            return false;
        }
        return true;
    }

    /**
     * 拼接字符串
     * @param strArray
     * @return
     */
    public static String concat(String... strArray){
        StringBuilder sb = new StringBuilder();
        for(String str:strArray){
            sb.append(str);
        }
        return sb.toString();
    }
}
