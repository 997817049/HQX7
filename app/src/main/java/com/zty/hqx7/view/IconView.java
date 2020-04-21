package com.zty.hqx7.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class IconView extends android.support.v7.widget.AppCompatTextView {
    public IconView(Context context) {
        super(context);
    }

    public IconView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "files/icon/iconfont.ttf");
        setTypeface(typeface);
    }
}
