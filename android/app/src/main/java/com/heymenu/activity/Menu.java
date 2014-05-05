package com.heymenu.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.heymenu.core.OnOrderChange;
import com.heymenu.core.OrderManager;
import com.heymenu.dialog.BackConfirmDialog;
import com.heymenu.dialog.DishDialog;
import com.heymenu.dialog.LogoutConfirmDialog;
import com.heymenu.dialog.OrderConfirmDialog;
import com.heymenu.util.DataUtil;
import com.heymenu.util.IntentConsts;
import com.heymenu.widget.AddDishButton;
import com.heymenu.widget.MenuGrid;
import com.heymenu.widget.MenuNav;
import com.heymenu.widget.OnOrderCountChangedListener;

import net.cloudmenu.emenu.R;
import net.cloudmenu.emenu.task.GetMenuTask;
import net.cloudmenu.emenu.utils.GlobalValue;
import net.cloudmenu.emenu.utils.MenuUtils;
import net.cloudmenu.emenu.utils.ProfileHolder;

import org.apache.commons.codec.binary.StringUtils;

import java.util.List;

import cn.buding.common.asynctask.HandlerMessageTask;
import cn.buding.common.widget.AsyncImageView;
import cn.com.cloudstone.menu.server.thrift.api.Goods;

/**
 * Created by nicholaszhao on 4/13/14.
 */
public class Menu extends Activity implements OnOrderCountChangedListener {

    protected cn.com.cloudstone.menu.server.thrift.api.Menu mMenu;
    protected List<MenuUtils.GoodsCategory> mGoodsCategories;
    private GetMenuTask mGetMenuTask;
    private MenuNav mMenuNav;
    private OrderManager orderManager;
    private String mCurrentTable;
    private TextView mOrderCount;
    private TextView mPlaceOrder;
    private TextView mTableName;
    private MenuGrid mMenuGrid;
    private ListView mMenuList;
    private MenuListAdapter mMenuListAdapter;
    private ViewGroup mContainer;
    private int mCurrentOrientation = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        mContainer = (ViewGroup) findViewById(R.id.container);
        mCurrentTable = getIntent().getStringExtra(IntentConsts.TABLE_ID);
        if (!ProfileHolder.getIns().isLogined(this)) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
        } else if (mCurrentTable == null) {
            Intent intent = new Intent(this, ListTables.class);
            startActivity(intent);
            finish();
            return;
        }
        mMenuGrid = (MenuGrid) findViewById(R.id.menu_grid);
        mMenuNav = (MenuNav) findViewById(R.id.category);
        mMenuGrid.setOnScrollListener(new AbsListView.OnScrollListener() {

            private int mLastScrollState = SCROLL_STATE_IDLE;
            private int mScrollState = SCROLL_STATE_IDLE;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE && mLastScrollState != SCROLL_STATE_IDLE) {
                    mMenuNav.onItemScrolled(mMenuGrid.getFirstVisiblePosition(), mMenuGrid.getLastVisiblePosition());
                }
                mLastScrollState = scrollState;
            }

            @Override
            public void onScroll(AbsListView view,
                                 int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        mMenuGrid.setOrderChangeListener(this);

        orderManager = new OrderManager();
        mMenuList = (ListView) findViewById(R.id.menu_list);
        mMenuListAdapter = new MenuListAdapter(this);
        mMenuNav.setOnCategoryChanged(
                new MenuNav.CategoryChanged() {
                    @Override
                    public void onCategoryChanged(int categoryIndex) {
                        if (categoryIndex == MenuNav.SPECIAL_INDEX) {
                            mMenuList.setVisibility(View.VISIBLE);
                            mMenuGrid.setVisibility(View.GONE);
                        } else {
                            mMenuList.setVisibility(View.GONE);
                            mMenuGrid.setVisibility(View.VISIBLE);
                            mMenuGrid.onCategoryChanged(categoryIndex);
                        }
                    }
                }
                );
        mMenuList.setAdapter(mMenuListAdapter);

        mOrderCount = (TextView) findViewById(R.id.order_count);
        mPlaceOrder = (TextView) findViewById(R.id.place_order);
        mTableName = (TextView) findViewById(R.id.table_name);
        mTableName.setText(mCurrentTable);
        findViewById(R.id.place_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderConfirmDialog confirmDialog = new OrderConfirmDialog(Menu.this, orderManager, mCurrentTable);
                confirmDialog
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                mMenuGrid.getAdapter().notifyDataSetChanged();
                                onOrderCountChanged(orderManager.getOrderCount());
                            }
                        });
                confirmDialog.show();
            }
        });
        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BackConfirmDialog(Menu.this).show();
            }
        });
        findViewById(R.id.sync).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData(true);
            }
        });
        initData(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.orientation == mCurrentOrientation) {
            return;
        }

        setRequestedOrientation(newConfig.orientation);
        mContainer.requestLayout();
        mCurrentOrientation = newConfig.orientation;
        Log.d("Menu", "layouting!!");
    }

    protected void initData(boolean forseRefresh) {
        if (mGetMenuTask != null && mGetMenuTask.getStatus() == AsyncTask.Status.RUNNING)
            return;
        mGetMenuTask = new GetMenuTask(this);
        mGetMenuTask.setForseRefresh(forseRefresh);
        mGetMenuTask.setCallback(new HandlerMessageTask.Callback() {
            @Override
            public void onSuccess(HandlerMessageTask task, Object t) {
                mMenu = mGetMenuTask.getResult();
                GlobalValue.getIns().setMenu(mMenu);
                orderManager.setMenu(mMenu);
                mMenuGrid.setOrderManager(orderManager);
                mMenuListAdapter.notifyDataSetChanged();
                mGoodsCategories = makeGoodsCategory();
                mMenuNav.setCategories(mGoodsCategories, orderManager.getSpecialGoodSize() > 0);
            }

            @Override
            public void onFail(HandlerMessageTask task, Object t) {
                Log.e("Menu", "failed to load" + t);
            }
        });
        mGetMenuTask.execute();
    }

    protected List<MenuUtils.GoodsCategory> makeGoodsCategory() {
        return MenuUtils.getMenuGoodsCategories(mMenu);
    }

    @Override
    public void onOrderCountChanged(int totalCount) {
        if (totalCount == 0) {
            mOrderCount.setVisibility(View.INVISIBLE);
            mPlaceOrder.setEnabled(false);
        } else {
            mOrderCount.setVisibility(View.VISIBLE);
            mOrderCount.setText("" + totalCount);
            mPlaceOrder.setEnabled(true);
        }
    }

    private class MenuListAdapter extends BaseAdapter {

        private Context mContext;
        public MenuListAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return orderManager.getSpecialGoodSize();
        }

        @Override
        public Object getItem(int position) {
            return orderManager.getSpecialGoodAt(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) (mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE));
                convertView = inflater.inflate(R.layout.goods_portait_large, parent, false);
            }
            Goods good = orderManager.getSpecialGoodAt(position);
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
                    orderManager.onOrderChange(goodId, count);
                    onOrderCountChanged(orderManager.getOrderCount());

                }
            });
            addDishButton.setCount(orderManager.getOrderCountForGood(good.getId()));
            View.OnClickListener onDishDetailClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DishDialog dishDialog =
                            new DishDialog(mContext, good, orderManager);
                    dishDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            addDishButton.setCount(orderManager.getOrderCountForGood(good.getId()));
                            onOrderCountChanged(orderManager.getOrderCount());
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
