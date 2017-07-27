package com.timaimee.spo2hviewmpademo.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.timaimee.spo2hviewmpademo.MainActivity.LOW_DATA_VALUE_REALLY;
import static com.timaimee.spo2hviewmpademo.MainActivity.MIDDLE_VALUE;
import static com.timaimee.spo2hviewmpademo.MainActivity.LOW_DATA_VALUE_SHOW_FAKE;

/**
 * 对变化的区间,自定义的计算
 * Created by timaimee on 2017/6/29.
 */
public class Spo2hUtil {

    public static float changeToBig(float value) {
        float v = value;
        if (value < MIDDLE_VALUE) {
            v = (value - LOW_DATA_VALUE_SHOW_FAKE) / (MIDDLE_VALUE - LOW_DATA_VALUE_SHOW_FAKE) * (MIDDLE_VALUE - LOW_DATA_VALUE_REALLY) + LOW_DATA_VALUE_REALLY;
        }
        return v;
    }

    public static int changeToLow(float value) {
        float v = value;
        if (value < MIDDLE_VALUE) {
            v = (v - LOW_DATA_VALUE_REALLY) / (MIDDLE_VALUE - LOW_DATA_VALUE_REALLY) * (MIDDLE_VALUE - LOW_DATA_VALUE_SHOW_FAKE) + LOW_DATA_VALUE_SHOW_FAKE;
        }
        return getPositionDoubleUP(v);
    }

    public static int getPositionDoubleUP(double value) {
        BigDecimal bigObject = new BigDecimal(value);
        double v = bigObject.setScale(1, RoundingMode.HALF_UP).doubleValue();
        return (int) v;
    }

}
