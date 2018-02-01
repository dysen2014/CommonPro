package com.dysen.commom_library.views;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;

public class TextViewMarquee extends android.support.v7.widget.AppCompatTextView {

	public TextViewMarquee(Context context) {
		super(context);
		this.setEllipsize(TruncateAt.MARQUEE);
		this.setSingleLine(true);
	}
	
	public TextViewMarquee(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setEllipsize(TruncateAt.MARQUEE);
		this.setSingleLine(true);
	}

	public TextViewMarquee(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setEllipsize(TruncateAt.MARQUEE);
		this.setSingleLine(true);
	}
	

	@Override
    public boolean isFocused() {
        return true;
    }

}
