package ru.hse.tickbattle.views;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.FrameLayout;
import android.widget.GridLayout;

import ru.hse.tickbattle.objects.OnSelectBlockListener;

import java.util.ArrayList;

import ua.org.tenletters.widget.DiagonalScrollView;


public class GameMapView extends DiagonalScrollView {
    GridLayout grid;
    private float currentScale = 1f;
    private final ScaleGestureDetector scaleDetector;
    private ArrayList<BlockView> blockViews;
    int w, h;
    private final OnSelectBlockListener selectBlockListener;

    public GameMapView(Context context, OnSelectBlockListener selectBlockListener) {
        super(context);
        this.selectBlockListener = selectBlockListener;
        FrameLayout.LayoutParams mainLayoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.setLayoutParams(mainLayoutParams);

        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }


    public void init(int w, int h) {
        this.w = w;
        this.h = h;

        grid = new GridLayout(this.getContext());
        grid.setColumnCount(w);
        grid.setRowCount(h);
        grid.setUseDefaultMargins(true);

        blockViews = new ArrayList<>();

        for (int i = 0; i < h; i += 1) {
            for (int j = 0; j < w; j += 1) {
                BlockView blockView = new BlockView(grid.getContext(), selectBlockListener,  j, i);
                blockViews.add(blockView);
                grid.addView(blockView);
            }
        }
        addView(grid);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        scaleDetector.onTouchEvent(event);
        super.dispatchTouchEvent(event);
        return true;
    }

    public ArrayList<BlockView> getBlocks()  {
        return blockViews;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = Math.abs(detector.getScaleFactor());
            if (currentScale * scaleFactor < 0.3) return true;
            if (currentScale * scaleFactor > 1.5) return true;

            currentScale *= scaleFactor;
            grid.setScaleX(currentScale);
            grid.setScaleY(currentScale);
            return true;
        }
    }


    public BlockView getBlock(int x, int y) {
        return blockViews.get(y * w + x);
    }
}
