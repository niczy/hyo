package com.heymenu.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.heymenu.core.OnOrderChange;
import com.heymenu.core.OrderManager;
import com.heymenu.dialog.DishDialog;
import com.heymenu.util.DataUtil;

import net.cloudmenu.emenu.R;
import net.cloudmenu.emenu.utils.MenuUtils;

import cn.buding.common.widget.AsyncImageView;
import cn.com.cloudstone.menu.server.thrift.api.Goods;

/**
 * Created by nicholaszhao on 4/22/14.
 */
public class MenuGrid extends FrameLayout implements MenuNav.CategoryChanged {

    private MenuAdapter mAdapter;
    private GridView mGridView;
    private OnOrderCountChangedListener mOnOrderCountChangedListener;
    private boolean scrolledInitedByUser = false;
    public MenuGrid(Context context) {
        this(context, null);
    }

    public MenuGrid(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuGrid(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mAdapter = new MenuAdapter(context);
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.widget_menu_grid, this);
        mGridView = (GridView) findViewById(R.id.gridview);
        mGridView.setAdapter(mAdapter);
    }

    public void setOrderManager(OrderManager orderManager) {
        mAdapter.setOrderManager(orderManager);
        mAdapter.notifyDataSetChanged();
    }

    public void setOnScrollListener(AbsListView.OnScrollListener onScrollListener) {
        mGridView.setOnScrollListener(onScrollListener);
    }

    public void setOrderChangeListener(OnOrderCountChangedListener orderChangeListener) {
        this.mOnOrderCountChangedListener = orderChangeListener;
    }

    public BaseAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void onCategoryChanged(int startIndex) {

        if (getFirstVisiblePosition() == startIndex) {
            return;
        }
        int targetRow = startIndex / mGridView.getNumColumns();
        int firstVisiableRow = getFirstVisiblePosition()  / mGridView.getNumColumns() + 1;
        Log.d("MenuGrid", "fist visiable is " + getFirstVisiblePosition() + "target row is " + targetRow + " current row is " + firstVisiableRow);
        if (firstVisiableRow >= targetRow) {
            mGridView.smoothScrollToPosition(startIndex);
        } else {
            mGridView.smoothScrollToPosition(startIndex + getLastVisiblePosition() - getFirstVisiblePosition() - mGridView.getNumColumns());
        }
    }

    public int getFirstVisiblePosition() {
        return mGridView.getFirstVisiblePosition();
    }

    public int getLastVisiblePosition() {
        return mGridView.getLastVisiblePosition();
    }

    private class MenuAdapter extends BaseAdapter {
        private Context mContext;
        private OrderManager mOrderManager;

        public MenuAdapter(Context mContext) {
            this.mContext = mContext;
        }

        public void setOrderManager(OrderManager orderManager) {
            mOrderManager = orderManager;
        }

        @Override
        public int getCount() {
            if (mOrderManager == null) {
                return 0;
            }
            return mOrderManager.getGoodSize();
        }

        @Override
        public Object getItem(int position) {
            return mOrderManager.getGoodAt(position);
        }

        @Override
        public long getItemId(int position) {
            return mOrderManager.getGoodAt(position).getId();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) (mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE));
                convertView = inflater.inflate(R.layout.goods_portait, parent, false);
            }
            Goods good = mOrderManager.getGoodAt(position);
            return getDishView(convertView, good);
        }

        private View getDishView(View convertView, final Goods good) {
            String imageUrl = MenuUtils.getGoodPreviewImgUrl(mContext, good);
            AsyncImageView image = (AsyncImageView) convertView.findViewById(R.id.image);
            if (imageUrl != null) {
                image.postLoading(imageUrl);
            } else {
                image.setImageResource(R.drawable.image_missing_small);
            }
            final AddDishButton addDishButton = (AddDishButton) convertView.findViewById(R.id.add_dish_button);
            addDishButton.setGoodId(good.getId());
            addDishButton.setOnOrderChange(new OnOrderChange() {
                @Override
                public void onOrderChange(int goodId, int count) {
                    mOrderManager.onOrderChange(goodId, count);
                    if (mOnOrderCountChangedListener != null) {
                        mOnOrderCountChangedListener.onOrderCountChanged(mOrderManager.getOrderCount());
                    }
                }
            });
            addDishButton.setCount(mOrderManager.getOrderCountForGood(good.getId()));
            OnClickListener onDishDetailClickListener = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    DishDialog dishDialog =
                            new DishDialog(mContext, good, mOrderManager);
                    dishDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            addDishButton.setCount(mOrderManager.getOrderCountForGood(good.getId()));
                            if (mOnOrderCountChangedListener != null) {
                                mOnOrderCountChangedListener.onOrderCountChanged(mOrderManager.getOrderCount());
                            }
                        }
                    });
                    dishDialog.show();
                }
            };
            image.setOnClickListener(onDishDetailClickListener);
            TextView desc = (TextView) convertView.findViewById(R.id.desc);
            desc.setText(good.getIntroduction());
            desc.setOnClickListener(onDishDetailClickListener);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            name.setText(good.getName());
            TextView price = (TextView) convertView.findViewById(R.id.price);

            price.setText(DataUtil.calMoney(good.getPrice()));
            return convertView;
        }
    }
}
