package com.yxdriver.app.utils;

/**
 * Created by mac on 2017/11/1.
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
// 费用计算工具类
public class CostUtils {
    /**
     * 专车费用计算
     *
     * @param kilometre
     * @return
     */
    public static String getZCMoney(double kilometre) {
        double money = 0.0;
        if (kilometre > 0 && kilometre <= 5) {
            money = 8.0;
        } else {
            money = 8.0 + (kilometre * 1.6);
        }
        return String.valueOf(money);
    }

    /**
     * 顺风车费用计算
     *
     * @param kilometre
     * @return
     */
    public static String getSFCMoney(double kilometre) {
        double money = 0.0;
        if (kilometre > 0 && kilometre <= 5.0) {
            money = 5.0;
        } else {
            money = 5.0 + (kilometre * 0.4);
        }
        return String.valueOf(money);
    }

    /**
     * 快递型货物费用计算
     *
     * @param kilometre 公里数
     * @param weight    货物重量
     * @return
     */
    public static String getKDMoney(double kilometre, double weight) {
        double money = 0.0;
        // 50公里以内，货物重量在两公斤内，5块钱；如果货物重量大于两公斤，则按照每公斤2块钱计算价格
        if (kilometre > 0.0 && kilometre <= 50.0) {
            if (weight < 0.0 && weight <= 2.0) {
                money = 5.0;
            } else {
                // 续重按照2块钱一公斤计算
                money = 5.0 + (doubleToInt(weight) - 2) * 2.0;
            }

        } else {
            // 超出50公里
            // 货物2公斤以内
            if (weight <= 2.0) {
                money = 5.0 + ((kilometre - 50.0) / 10) * 0.5;
            } else {
                // 货物两公斤以上
                money = 5.0 + (doubleToInt(weight) - 2) * 2.0 + ((kilometre - 50.0) / 10) * 0.5;
            }

        }
        return String.valueOf(money);
    }

    /**
     * 小型货运费用计算
     *
     * @param kilometre
     * @param weight
     * @return
     */
    public static String getLitleGoods(double kilometre, double weight) {
        double money = 0.0;
        // 5公里以内
        if (kilometre > 0.0 && kilometre <= 5.0) {
            // 货物在500千克以内，按照30块运费计算
            if (weight < 0.0 && weight <= 500.0) {
                money = 30.0;
            } else {
                // 货物超过500千克，按照38块钱计算
                money = 38.0;
            }

        } else {
            // 超出5公里
            if (weight > 0.0 && weight <= 500.0) {
                // 五百公斤以内，在起步价的基础上，按照每公斤3块钱计价
                money = 30.0 + (kilometre - 5.0) * 3.0;
            } else {
                // 超过五百公斤，按照38块钱的起步价，3块钱一公里的方式计价
                money = 38.0 + (kilometre - 5.0) * 3.0;
            }

        }
        return String.valueOf(money);
    }

    /**
     * 大型货物费用计算
     *
     * @param kilometre
     * @param weight
     * @return
     */
    public static String getBigGoods(double kilometre, double weight) {
        double money = 0.0;
        money = 48.0 + ((weight - 1000) / 500) * 10 + kilometre * ((weight - 1000) / 500) * 0.8;
        return String.valueOf(money);
    }

    /**
     * 不足一公斤按照一公斤计算
     *
     * @param val
     * @return
     */
    private static int doubleToInt(double val) {
        return (Double.valueOf(val)).intValue() + 1;
    }
}
