package com.timaimee.spo2hviewmpademo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.timaimee.spo2hviewmpademo.util.DateUtil;
import com.timaimee.spo2hviewmpademo.util.Spo2hUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 要求画一个视图
 * 1.X轴由时间点构成，时间点由昨天的xx点到今天的xx点
 * 2.Y轴数值组成，并且在某一区间内需要进行特殊的变换
 * 3.要有markview的显示
 * 4.markiview要依据不同的数据显示不一样颜色的字体
 * 5.在基准值的位置要画一条特殊的线
 */
public class MainActivity extends AppCompatActivity {
    private Context mContext = MainActivity.this;
    @BindView(R.id.history_spo2h_chartview)
    LineChart mChartView;

    CustomMarkerView mMarkview;
    boolean mModelIs24 = false;


    private static final int HIGH_DATA_VALUE = 53;
    public static final float STAND_DATA_VALUE = 48f;
    public static final int MIDDLE_VALUE = 45;
    public static final int LOW_DATA_VALUE_REALLY = 43;
    public static final int LOW_DATA_VALUE_SHOW_FAKE = 10;

    //显示昨天的YESTERDAY_HOUR到今天的TODAY_HOUR的数据，第隔TIME_FLAG一个点
    public static final int YESTERDAY_HOUR = 13;
    public static final int TODAY_HOUR = 8;
    public static final int TIME_FLAG = 30;

    public static final int DATA_COUNT_SHOW = (24 - YESTERDAY_HOUR) * 60 / TIME_FLAG + (TODAY_HOUR) * 60 / TIME_FLAG + 1;
    public static final int DATA_COUNT_ALL = 24 * 60 / TIME_FLAG;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mModelIs24 = DateFormat.is24HourFormat(mContext);
        initChartView();
        updateChartView(getChartData());
    }

    private List<Map<String, Float>> getChartData() {
        List<Map<String, Float>> chartData = new ArrayList<>(DATA_COUNT_ALL);
        for (int i = 0; i < DATA_COUNT_ALL; i++) {
            float value;
            value = new Random().nextFloat() * 15 + 35;
            if (i == 15 || i == 25 || i == 35) {
                value = 35;
            }
            if (value <= MIDDLE_VALUE) {
                value = Spo2hUtil.changeToBig(value);
            }
            Map<String, Float> item = getFloatMap(value, i * TIME_FLAG, isYesterday(i));
            chartData.add(item);
        }
        return chartData;
    }

    /**
     * 判断是否是昨天的数据
     *
     * @param tenItemX
     * @return
     */
    private boolean isYesterday(int tenItemX) {
        //6==60/10
        return tenItemX >= YESTERDAY_HOUR * 60 / TIME_FLAG ? true : false;
    }

    private Map<String, Float> getFloatMap(float value, int time, boolean isYesterday) {
        Map<String, Float> hashMap = new HashMap<>();
        /**day的参数主要用于Hashmap排序*/
        if (isYesterday) {
            //表示昨天
            hashMap.put("day", -1f);
        } else {
            //表示当天
            hashMap.put("day", 0f);
        }
        hashMap.put("time", (float) time);
        hashMap.put("value", value);
        return hashMap;
    }

    private void initChartView() {
        mMarkview = new CustomMarkerView(this, R.layout.activity_markerview, mModelIs24);
        setChartView();
    }


    @BindString(R.string.nodata)
    String mStrNodata;
    @BindColor(R.color.background)
    int mSpo2hBackColor;

    private void setChartView() {
        mChartView.setDrawGridBackground(false);
        mChartView.setNoDataText(mStrNodata);
        mChartView.getDescription().setEnabled(false);
        mChartView.setTouchEnabled(true);
        mChartView.setExtraRightOffset(5f);
        mChartView.setDragEnabled(true);
        mChartView.setPinchZoom(false);
        mChartView.setBackgroundColor(mSpo2hBackColor);
        mChartView.setMarker(mMarkview);
        setXview();
        setYview();
        mChartView.animateX(1000);
        Legend l = mChartView.getLegend();
        l.setForm(Legend.LegendForm.NONE);
        l.setYOffset(-5);
        mChartView.setData(new LineData());
    }

    private void setXview() {
        XAxis xAxis = mChartView.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(8f);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(6, true);
        xAxis.setAvoidFirstLastClipping(false);
        xAxis.setCenterAxisLabels(false);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float xPosition, AxisBase axis) {
                int valueInt = (int) xPosition;
                valueInt = (valueInt * TIME_FLAG + YESTERDAY_HOUR * 60) % (24 * 60);
                return DateUtil.getSpo2hTimeString(valueInt, mModelIs24);
            }
        });
    }

    private void setYview() {
        LimitLine spo2hLowLine = new LimitLine(STAND_DATA_VALUE, " ");
        spo2hLowLine.setLineWidth(1f);
        spo2hLowLine.enableDashedLine(10f, 1f, 0f);
        spo2hLowLine.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        spo2hLowLine.setTextSize(10f);
        spo2hLowLine.setTextColor(Color.RED);

        YAxis leftAxis = mChartView.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(spo2hLowLine);
        leftAxis.setAxisMaximum(HIGH_DATA_VALUE);
        leftAxis.setLabelCount(5, true);
        leftAxis.setAxisMinimum(LOW_DATA_VALUE_REALLY);
        leftAxis.setGranularity(3f);
        leftAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                int valueInt = (int) v;
                if (valueInt == LOW_DATA_VALUE_REALLY) {
                    valueInt = LOW_DATA_VALUE_SHOW_FAKE;
                }
                return String.valueOf(valueInt);
            }
        });
        leftAxis.setDrawAxisLine(false);
        leftAxis.setGridColor(Color.WHITE);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawLimitLinesBehindData(false);
        mChartView.getAxisRight().setEnabled(false);
    }


    @BindColor(R.color.white_80)
    int setColor;

    private LineDataSet createSet() {
        LineDataSet set1 = new LineDataSet(null, "");
        set1.setDrawIcons(false);
        set1.setColor(setColor);
        set1.setCircleColor(Color.WHITE);
        set1.setDrawCircles(false);
        set1.setCubicIntensity(0.2f);
        set1.setDrawHighlightIndicators(false);
        set1.setHighlightLineWidth(2f);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setLineWidth(2f);
        set1.setCircleRadius(1f);
        set1.setDrawCircleHole(false);
        set1.setFormLineWidth(1f);
        set1.setDrawValues(false);
        set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set1.setFormSize(8.f);
        return set1;

    }

    private void updateChartView(final List<Map<String, Float>> listData) {

        LineData data = mChartView.getData();
        ILineDataSet set = data.getDataSetByIndex(0);
        if (set != null) {
            data.removeDataSet(0);
        }
        set = createSet();
        data.addDataSet(set);
        float spo2hValues[] = new float[DATA_COUNT_SHOW + 1];
        float times[] = new float[DATA_COUNT_SHOW + 1];
        for (int i = 0; i < listData.size(); i++) {
            float spo2hValue = listData.get(i).get("value");
            float time = listData.get(i).get("time");
            int xPosition = ((int) time + 1440 - YESTERDAY_HOUR * 60) % 1440 / TIME_FLAG;
            if (xPosition < DATA_COUNT_SHOW) {
                //只取 22：00-7：59的数据
                if (spo2hValues[xPosition] == 0) {
                    spo2hValues[xPosition] = spo2hValue;
                    times[xPosition] = time;
                } else if (spo2hValue < spo2hValues[xPosition]) {
                    spo2hValues[xPosition] = spo2hValue;
                    times[xPosition] = time;
                }
            }
        }
        for (int i = 0; i < spo2hValues.length; i++) {
            if (spo2hValues[i] < 1) {
                continue;
            } else {
                Entry e = new Entry(i, spo2hValues[i]);
                e.setData(times[i]);
                data.addEntry(e, 0);
            }
        }
        mChartView.setData(data);
        data.notifyDataChanged();
        mChartView.animateX(1000);
        mChartView.fitScreen();
        mChartView.notifyDataSetChanged();
        mChartView.invalidate();
    }

}
