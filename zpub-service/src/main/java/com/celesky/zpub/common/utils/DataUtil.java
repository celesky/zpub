package com.celesky.zpub.common.utils;

import com.google.common.collect.Lists;
import com.zuzuche.msa.base.util.JsonUtil;
import com.zuzuche.msa.base.util.StringUtil;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lizhifeng
 */
public class DataUtil {
    private static final Logger logger = LoggerFactory.getLogger(DataUtil.class);

    public static String getAllPostParam(HttpServletRequest request) {
        Map<String, String[]> postParam = request.getParameterMap();
        StringBuilder sb = new StringBuilder("");
        for (String key : postParam.keySet()) {
            String[] values = postParam.get(key);
            for (int i = 0; i < values.length; i++) {
                String value = values[i];
                sb.append(key).append("=").append(value).append("&");
            }
        }

        return sb.toString();
    }

    public static Map<String, String> getRequestMapParam(HttpServletRequest request) {
        Map<String, String> resultMap = new HashMap<>(16);
        Map<String, String[]> postParam = request.getParameterMap();
        for (String key : postParam.keySet()) {
            String[] values = postParam.get(key);
            for (int i = 0; i < values.length; i++) {
                String value = values[i];
                if (!resultMap.containsKey(key)) {
                    resultMap.put(key, value);
                } else {
                    logger.info("key=" + key + ", value=" + value + " is exist, exist value is " + resultMap.get(key));
                }
            }
        }
        return resultMap;
    }

    public static String getRequestJsonParam(HttpServletRequest request) {
        Map<String, String> requestMapParam = getRequestMapParam(request);
        String resultStr = "";
        try {
            resultStr = JsonUtil.objToPrettyStr(requestMapParam);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultStr;
    }

    public static String getRequstAddress(HttpServletRequest request) {
        String result = "";
        String reqMethod = request.getMethod();
        if (RequestMethod.POST.name().equalsIgnoreCase(reqMethod)) {
            result = request.getRequestURL() + "?" + getAllPostParam(request);
        }
        if (RequestMethod.GET.name().equalsIgnoreCase(reqMethod)) {
            result = request.getQueryString() != null ?
                    request.getRequestURL()
                            .append("?")
                            .append(request.getQueryString())
                            .toString() :
                    request.getRequestURL()
                            .toString();
        }
        return result;
    }


    /**
     * 将逗号隔开的字符串转为List
     *
     * @param ids
     * @return
     */
    public static List<String> asList(String ids) {
        return asList(ids, ",");
    }

    public static List<Integer> asIntegerList(String ids) {
        return asIntegerList(ids, ",");
    }


    public static List<Integer> asIntegerList(String ids, String delimeter) {
        String[] arr = ids.split(delimeter);
        List<Integer> list = Lists.newArrayList();

        for(String a: arr) {
            list.add(NumberUtils.toInt(a));
        }
        return list;
    }


    public static List<String> asList(String ids, String delimeter) {
        String[] arr = ids.split(delimeter);
        return Lists.newArrayList(arr);
    }

    public static String join(List<String> arr) {
        StringBuilder liststr = new StringBuilder();

        for (int i = 0; i < arr.size(); i++) {
            liststr.append(arr.get(i));
            if (i < arr.size() - 1) {
                liststr.append(",");
            }

        }
        return liststr.toString();
    }


    public static String getOracleSQLIn(List<?> ids, int count, String field) {
        count = Math.min(count, 1000);
        int len = ids.size();
        int size = len % count;
        if (size == 0) {
            size = len / count;
        } else {
            size = (len / count) + 1;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            int fromIndex = i * count;
            int toIndex = Math.min(fromIndex + count, len);
            String productId = StringUtil.defaultIfEmpty(StringUtil.join(ids.subList(fromIndex, toIndex), "','"), "");
            if (i != 0) {
                builder.append(" or ");
            }
            builder.append(field).append(" in ('").append(productId).append("') ");
        }

        return StringUtil.defaultIfEmpty(builder.toString(), field + " in ('') ");
    }
}
