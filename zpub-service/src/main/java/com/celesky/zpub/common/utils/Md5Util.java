/**
 * 
 */
package com.celesky.zpub.common.utils;

import java.security.MessageDigest;

/** 
 * 采用MD5加密解密 
 * @author tfq 
 * @datetime 2011-10-13 
 */  
public class Md5Util {  
  
    /*** 
     * MD5加码 生成32位md5码 
     */  
    public static String string2MD5(String s, String charset) {  
    	try {
    		charset = charset == null ? "utf-8" : charset;
    		
            byte[] btInput = s.getBytes(charset);
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < md.length; i++) {
                int val = ((int) md[i]) & 0xff;
                if (val < 16){
                	sb.append("0");
                }
                sb.append(Integer.toHexString(val));
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        } 
  
    }  
  
    /** 
     * 加密解密算法 执行一次加密，两次解密 
     */   
    public static String convertMD5(String inStr){  
  
        char[] a = inStr.toCharArray();  
        for (int i = 0; i < a.length; i++){  
            a[i] = (char) (a[i] ^ 't');  
        }  
        String s = new String(a);  
        return s;  
    }

    /**
     * 测试主函数
      */
    public static void main(String args[]) {
    	String o = "accessId=crm-ivr&agentno=8888&bu=1&channel=语音&inquiretype=投诉-市场推广1-积分疑义&isrecommend=true&istransfer=false&message=2132132112323333333333333333&method=1&needfollow=true&num=-1745421048&numtype=1&replayurl=http://10.17.2.8:8080/xiaoniu_online/getAudioPage?srId=K2016081810029&satisfaction=0&signType=MD5&touchTime=2016-08-18 10:27:43&type=1&v=1.09AF7F46A-EA52-4AA3-B8C3-9FD484C2AF12";
    	String o1 ="accessId=crm-ivr&agentno=8888&bu=1&channel=语音&inquiretype=投诉-市场推广1-积分疑义&isrecommend=true&istransfer=false&message=2132132112323333333333333333&method=1&needfollow=true&num=-1745421048&numtype=1&replayurl=http://10.17.2.8:8080/xiaoniu_online/getAudioPage?srId=K2016081810029&satisfaction=0&signType=MD5&touchTime=2016-08-18 10:27:43&type=1&v=1.09AF7F46A-EA52-4AA3-B8C3-9FD484C2AF12";
       
    	System.out.println(o.equals(o1));
    	
    	String s = o1;
        
        System.out.println();
        System.out.println("原始：" + s);  
        System.out.println("MD5后：" + string2MD5(s, null));  
        System.out.println("加密的：" + convertMD5(s));  
        System.out.println("解密的：" + convertMD5(convertMD5(s)));  
  
    }  
}  
