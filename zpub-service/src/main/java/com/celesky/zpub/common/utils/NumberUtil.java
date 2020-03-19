package com.celesky.zpub.common.utils;


/**
 * @desc:
 * @author: panqiong
 * @date: 2018/8/20
 */
public class NumberUtil {

    public static boolean numberCompare(double a,String operation,double b){
        boolean over=false;
        switch (operation) {
            case ">":
                if (a>b) {
                    over = true;
                }
                break;
            case ">=":
                if (a >= b) {
                    over = true;
                }
                break;
            case "<=":
                if (a <= b) {
                    over = true;
                }
                break;
            case "<":
                if (a < b) {
                    over = true;
                }
                break;
            case "=":
                if (a == b) {
                    over = true;
                }
                break;
            default:
                over = false;
        }
        return over;
    }





}
