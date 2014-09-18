package com.halo.app.ui.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by MoranDev on 9/15/2014.
 */
public class RobotoTextView extends TextView {
    public RobotoTextView(Context context) {
        super(context);
    }

    public RobotoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RobotoTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    private void style(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/roboto.ttf");
        setTypeface(tf);
    }
}
