package com.heymenu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.heymenu.core.OnOrderChange;

import net.cloudmenu.emenu.R;

import org.w3c.dom.Text;

/**
 * Created by nicholaszhao on 4/16/14.
 */
public class AddDishButton extends FrameLayout implements View.OnClickListener {

    private final View emptyView;
    private final View addAndSubView;
    private final TextView mCountView;
    private int count = 0;
    private int mGoodId;
    private OnOrderChange mOnOrderChange;
    public AddDishButton(Context context) {
        this(context, null);
    }

    public AddDishButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.widget_add_button, this);
        emptyView = findViewById(R.id.empty);
        addAndSubView = findViewById(R.id.add_and_sub);
        mCountView = (TextView) findViewById(R.id.count);
        OnClickListener onEmptyClickedListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeOrder(1);
            }
        };
        emptyView.setOnClickListener(
                onEmptyClickedListener
        );
        findViewById(R.id.add_button).setOnClickListener(onEmptyClickedListener);

        findViewById(R.id.add).setOnClickListener(this);
        findViewById(R.id.sub).setOnClickListener(this);
    }

    private void changeOrder(int delta) {
        count += delta;
        if (count == 0) {
            emptyView.setVisibility(VISIBLE);
            addAndSubView.setVisibility(GONE);
        } else {
            mCountView.setText("" + count);
            emptyView.setVisibility(GONE);
            addAndSubView.setVisibility(VISIBLE);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                changeOrder(1);
                break;
            case R.id.sub:
                changeOrder(-1);
                break;
        }
    }
}
