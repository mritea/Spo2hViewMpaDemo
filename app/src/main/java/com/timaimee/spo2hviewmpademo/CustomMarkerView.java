package com.timaimee.spo2hviewmpademo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.timaimee.spo2hviewmpademo.util.DateUtil;
import com.timaimee.spo2hviewmpademo.util.Spo2hUtil;

import static com.timaimee.spo2hviewmpademo.MainActivity.STAND_DATA_VALUE;

/**
 * Created by timaimee on 2017/6/26.
 */
public class CustomMarkerView extends MarkerView {
    int mSpo2hBackColor, textOneColor;
    private TextView tvContent;
    private TextView tvTime;
    private boolean isTooLow = false;
    private boolean is24Modle = false;

    public CustomMarkerView(Context context, int layoutResource, boolean is24Modle) {
        super(context, layoutResource);
        this.is24Modle = is24Modle;
        mSpo2hBackColor = context.getResources().getColor(R.color.background);
        textOneColor = context.getResources().getColor(R.color.text_second);
        tvContent = (TextView) findViewById(R.id.tvContent);
        tvTime = (TextView) findViewById(R.id.tvTime);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        if (e instanceof CandleEntry) {
            CandleEntry ce = (CandleEntry) e;
            tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true));
        } else {
            float timeValue = (float) e.getData();
            String timeStr = DateUtil.getSpo2hTimeString((int) timeValue, is24Modle);
            float y = e.getY();
            y = Spo2hUtil.changeToLow(y);
            if (y <= STAND_DATA_VALUE) {
                isTooLow = true;
                tvContent.setTextColor(Color.RED);
                tvTime.setTextColor(textOneColor);
            } else {
                tvTime.setTextColor(textOneColor);
                tvContent.setTextColor(textOneColor);
                isTooLow = false;
            }
            tvContent.setText(Utils.formatNumber(y, 0, true) + "%");
            tvTime.setText(timeStr);
        }

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {

        MPPointF mpPointF;
        if (isTooLow) {
            mpPointF = new MPPointF(-(getWidth() / 2), -getHeight() * 3 / 2);
        } else {
            mpPointF = new MPPointF(-(getWidth() / 2), getHeight() / 2);
        }
        return mpPointF;
    }

    @Override
    public void draw(Canvas canvas, float posX, float posY) {
        super.draw(canvas, posX, posY);
        Paint circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.WHITE);
        circlePaint.setStrokeWidth(3f);
        canvas.drawCircle(posX, posY, 12, circlePaint);
        if (isTooLow) {
            circlePaint.setColor(Color.RED);
        } else {
            circlePaint.setColor(mSpo2hBackColor);

        }
        canvas.drawCircle(posX, posY, 8, circlePaint);
    }


}
