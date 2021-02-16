
package com.nexenio.bleindoorpositioningdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;

public class CustomView extends View implements Runnable {
    final static HomeActivity homeactivity = new HomeActivity();
    private static int signal = homeactivity.signal;
    private static long targetFrameInterval = 1000L / 30L;           // 프레임 속도 30
    private long frame = 30L;
    private int x = 150;
    private int y = 870;
    private Paint paint = new Paint();


    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Thread thread = new Thread(this);
        thread.start();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        Paint paint = new Paint();
        paint.setColor(Color.argb(80, 255, 0, 0));
        canvas.drawCircle(x, y, 30, paint);
    }


    @Override
    public void run() {
        int c = 0;
        while (true) {
            long frameStartTime = System.currentTimeMillis();
            signal = homeactivity.signal;
            long frameEndTime = System.currentTimeMillis();
            long delta = frameEndTime - frameStartTime;
            if (signal == 1) {
                if (c == 0) {
                    for (int i = 0; i < 50; i++) {
                        try {
                            Thread.sleep(targetFrameInterval - delta);
                            y -= 4;
                            Log.d("d", "fff");
                            c++;
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    CustomView.this.invalidate();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                else if (c == 50)
                    for (int i = 0; i < 50; i++) {
                        try {
                            Thread.sleep(targetFrameInterval - delta);
                            x += 3;
                            Log.d("d", "fff");
                            c++;
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    CustomView.this.invalidate();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                else if (c == 100) {
                    try {
                        Thread.sleep(10000);
                        signal = 2;
                        c = 0;
                        x=150;
                        y=850;
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }


            }

            if (signal == 2) {
                if (c == 0) {
                    for (int i = 0; i < 100; i++) {
                        try {
                            Thread.sleep(targetFrameInterval - delta);
                            y -= 7;
                            Log.d("d", "fff");
                            c++;
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    CustomView.this.invalidate();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (c == 100)
                    for (int i = 0; i < 100; i++) {

                        try {
                            Thread.sleep(targetFrameInterval - delta);
                            x += 11;
                            Log.d("d", "fff");
                            c++;
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    CustomView.this.invalidate();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                else if (c == 200)
                    for (int i = 0; i < 80; i++) {
                        try {
                            Thread.sleep(targetFrameInterval - delta);
                            y += 6;
                            Log.d("d", "fff");
                            c++;
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    CustomView.this.invalidate();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            c = 0;
                        }



                    }

            }
        }
    }
}



