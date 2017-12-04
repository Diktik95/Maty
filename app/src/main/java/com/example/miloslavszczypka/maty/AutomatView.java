package com.example.miloslavszczypka.maty;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by miloslavszczypka on 04.12.17.
 */

public class AutomatView extends View {
    Paint Styl;

    public AutomatView(Context context) {
        super(context);
    }

    public AutomatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AutomatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Styl = new Paint();
        Styl.setColor(Color.rgb(0,0,255));
        Styl.setStrokeWidth(10);
    }

    protected void onDraw(Canvas canvas) {
        canvas.drawRect(50, 50, 100, 100, Styl);
    }
}
