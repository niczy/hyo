package com.heymenu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.heymenu.core.OnOrderChange;

import net.cloudmenu.emenu.R;

/**
 * Created by nicholaszhao on 4/16/14.
 */
public class AddDishButtonForDialog extends FrameLayout {

    private final View addAndSubView;
    private final TextView mCountView;
    private int count = 0;
    private int mGoodId;
    private OnOrderChange mOnOrderChange;
    public AddDishButtonForDialog(Context context) {
        this(context, null);
    }

    public AddDishButtonForDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.widget_add_button_in_dialog, this);
        addAndSubView = findViewById(R.id.add_and_sub);
        mCountView = (TextView) findViewById(R.id.count);
        findViewById(R.id.add).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeOrder(1);
            }
        });
        findViewById(R.id.sub).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeOrder(-1);
            }
        });
    }

    private void changeOrder(int delta) {
        if (count != 0 || delta > 0) {
            count += delta;
            mCountView.setText("" + count);
        }
        if (mOnOrderChange != null) {
            mOnOrderChange.onOrderChange(mGoodId, count);
        }
    }

    public void setGoodId(int goodId) {
        mGoodId = goodId;
    }

    public void setOnOrderChange(OnOrderChange onOrderChange) {
        this.mOnOrderChange = onOrderChange;
    }

    public void setCount(int count) {
        this.count = count;
        changeOrder(0);
    }
}
