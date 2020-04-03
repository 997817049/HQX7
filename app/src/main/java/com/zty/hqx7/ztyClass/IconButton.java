package com.zty.hqx7.ztyClass;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class IconButton extends android.support.v7.widget.AppCompatRadioButton {
    public IconButton(Context context) {
        super(context);
    }

    public IconButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"files/icon/iconfont.ttf");
        setTypeface(typeface);
    }
}
