package ru.hse.tickbattle;
import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {

    private GameThread gameThread;

    public GameSurface(Context context)  {
        super(context);
        this.setFocusable(true);
        this.getHolder().addCallback(this);
    }

    public void update()  {

    }

    @Override
    public void draw(Canvas canvas)  {
        super.draw(canvas);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        this.gameThread = new GameThread(this, holder);
        this.gameThread.setRunning(true);
        this.gameThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                this.gameThread.setRunning(false);
                this.gameThread.join();
            } catch(InterruptedException e)  {
                e.printStackTrace();
            }
            retry = true;
        }
    }

}