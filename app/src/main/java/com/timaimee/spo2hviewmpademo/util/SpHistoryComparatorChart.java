package com.timaimee.spo2hviewmpademo.util;

import java.util.Comparator;
import java.util.Map;

/**
 * Created by timaimee on 2017/6/30.
 */
public class SpHistoryComparatorChart implements Comparator<Map<String, Float>> {
    @Override
    public int compare(Map<String, Float> lhs, Map<String, Float> rhs) {
//l-r>0 升序
        float lday = lhs.get("day");
        float rday = rhs.get("day");
        float ltime = lhs.get("time");
        float rtime = rhs.get("time");
        if (lday == rday) {
            return (int)(ltime - rtime);
        } else {
            return (int)(lday - rday);
        }


    }
}
