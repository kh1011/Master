package com.yxdriver.app.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import zuo.biao.library.util.TimeUtil;

/**
 * Created by mac on 2017/11/16.
 * ////////////////////////////////////////////////////////////////////
 * //                            _ooOoo_                             //
 * //                           o8888888o                            //
 * //                           88" . "88                            //
 * //                           (| ^_^ |)                            //
 * //                           O\  =  /O                            //
 * //                        ____/`---'\____                         //
 * //                      .'  \\|     |//  `.                       //
 * //                     /  \\|||  :  |||//  \                      //
 * //                    /  _||||| -:- |||||-  \                     //
 * //                    |   | \\\  -  /// |   |                     //
 * //                    | \_|  ''\---/''  |   |                     //
 * //                    \  .-\__  `-`  ___/-. /                     //
 * //                  ___`. .'  /--.--\  `. . ___                   //
 * //                ."" '<  `.___\_<|>_/___.'  >'"".                //
 * //              | | :  `- \`.;`\ _ /`;.`/ - ` : | |               //
 * //              \  \ `-.   \_ __\ /__ _/   .-` /  /               //
 * //        ========`-.____`-.___\_____/___.-`____.-'========       //
 * //                             `=---='                            //
 * //        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^      //
 * //         佛祖保佑       永无BUG        永不修改                    //
 * ////////////////////////////////////////////////////////////////////
 */

public class DateUtils {
    private static long timeMills = 0;
    private static Date endDate;

    public static Date strToDate(String val) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(val);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param startTime 开始的事件
     * @return 返回距离发帖时间的时间差
     */
    public static String twoDateDistance(String startTime) {

        if (startTime.equals("")) {

            return "";
        }
        timeMills = Long.parseLong(startTime);
        Date startDate = new Date(timeMills);
        endDate = strToDate(getCurrentDate());
        if (startDate == null || endDate == null) {
            return null;
        }
        long timeLong = endDate.getTime() - startDate.getTime();
        if (timeLong <= 0) {
            return "刚刚";
        } else if (timeLong < 60 * 1000) {
            return timeLong / 1000 + "秒前";
        } else if (timeLong < 60 * 60 * 1000) {
            timeLong = timeLong / 1000 / 60;
            return timeLong + "分钟前";
        } else if (timeLong < 60 * 60 * 24 * 1000) {
            timeLong = timeLong / 60 / 60 / 1000;
            return timeLong + "小时前";
        } else if (timeLong < 60 * 60 * 24 * 1000 * 7) {
            timeLong = timeLong / 1000 / 60 / 60 / 24;
            return timeLong + "天前";
        } else if (timeLong < 60 * 60 * 24 * 1000 * 7 * 4) {
            timeLong = timeLong / 1000 / 60 / 60 / 24 / 7;
            return timeLong + "周前";
        } else {
            timeLong = timeLong / 1000 / 60 / 60 / 24;
            return timeLong + "天前";
        }
    }

    private static String getCurrentDate() {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return df.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
