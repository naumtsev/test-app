package ru.hse.tickbattle;


import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

    private boolean running;
    private SurfaceHolder surfaceHolder;

    public GameThread(SurfaceHolder surfaceHolder)  {
        this.surfaceHolder= surfaceHolder;
    }

    @Override
    public void run()  {
        while (running) {

        }
    }

    public void setRunning(boolean running)  {
        this.running = running;
    }
}