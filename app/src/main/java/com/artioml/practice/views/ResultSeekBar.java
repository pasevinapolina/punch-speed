package com.artioml.practice.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.SeekBar;

import java.util.ArrayList;

/**
 * Created by Polina P on 17.02.2017.
 */

public class ResultSeekBar extends SeekBar {

    private ArrayList<ProgressItem> mProgressItemsList;
    private Paint progressPaint;
    private Rect progressRect;

    public ResultSeekBar(Context context) {
        super(context);
    }

    public ResultSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ResultSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void initData(ArrayList<ProgressItem> progressItemsList) {
        setEnabled(false);
        progressPaint = new Paint();
        progressRect = new Rect();
        this.mProgressItemsList = progressItemsList;
    }

    public void onDraw(Canvas canvas) {
        if (mProgressItemsList.size() > 0) {
            int progressBarWidth = getWidth();
            int progressBarHeight = getHeight();
            int thumbOffset = getThumbOffset();
            int lastProgressX = 0;
            int progressItemWidth, progressItemRight;

            for (int i = 0; i < mProgressItemsList.size(); i++) {
                ProgressItem progressItem = mProgressItemsList.get(i);
                progressPaint.setColor(ContextCompat.getColor(getContext(),
                        progressItem.getColor()));
                progressItemWidth = (int) (progressItem.getProgressItemPercentage()
                        * progressBarWidth / 100);

                progressItemRight = lastProgressX + progressItemWidth;

                // for last item give right to progress item to the width
                if (i == mProgressItemsList.size() - 1
                        && progressItemRight != progressBarWidth) {
                    progressItemRight = progressBarWidth;
                }
                //thumbOffset / 2
                progressRect.set(lastProgressX, 0,
                        progressItemRight, progressBarHeight / 2 - thumbOffset / 6);
                canvas.drawRect(progressRect, progressPaint);
                lastProgressX = progressItemRight;
            }
            super.onDraw(canvas);
        }
    }
}
