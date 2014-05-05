package com.heymenu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.heymenu.core.OnOrderChange;
import com.heymenu.core.OrderManager;
import com.heymenu.tasks.SubmitOrderTask;
import com.heymenu.util.DataUtil;
import com.heymenu.widget.AddDishButtonForDialog;

import net.cloudmenu.emenu.R;
import net.cloudmenu.emenu.utils.GlobalValue;

import java.util.List;

import cn.buding.common.asynctask.HandlerMessageTask;
import cn.com.cloudstone.menu.server.thrift.api.Goods;
import cn.com.cloudstone.menu.server.thrift.api.GoodsOrder;
import cn.com.cloudstone.menu.server.thrift.api.Order;

/**
 * Created by nicholaszhao on 4/17/14.
 */
public class OrderConfirmDialog extends Dialog {

    private OrderManager mOrderManager;
    private ListView mListView;
    private List<GoodsOrder> mCurrentOrder;
    private final String mCurrentTable;
    private final TextView mTotal;
    private final TextView mSubTotal;
    private final TextView mTax;

    public OrderConfirmDialog(Context context, OrderManager orderManager, String currentTable) {
        super(context, R.style.Theme_Dialog);
        setContentView(R.layout.dialog_order_confirm);
        mOrderManager = orderManager;
        mCurrentTable = currentTable;
        mCurrentOrder = mOrderManager.getCurrentOrder();
        mListView = (ListView) findViewById(R.id.list);
        BaseAdapter orderAdapter = new OrderAdapter(context, mCurrentOrder);
        mListView.setAdapter(orderAdapter);
        mTotal = (TextView) findViewById(R.id.total_values);
        mSubTotal = (TextView) findViewById(R.id.subtotal_price);
        mTax = (TextView) findViewById(R.id.tax_value);
        findViewById(R.id.order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitOrder();
            }
        });
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        updateTotalPrice(mCurrentOrder);
    }

    private void submitOrder() {
        SubmitOrderTask task = new SubmitOrderTask(getContext(), mCurrentOrder, mCurrentTable);
        task.setCallback(new HandlerMessageTask.Callback() {
            @Override
            public void onSuccess(HandlerMessageTask task, Object t) {
                mOrderManager.clearCurrentOrder();
                dismiss();
            }

            @Override
            public void onFail(HandlerMessageTask task, Object t) {

            }
        });
        task.execute();
    }

    private void updateTotalPrice(List<GoodsOrder> orders) {
        double subTotal = 0;
        for (GoodsOrder goodOrder : orders) {
            subTotal += goodOrder.getNumber() * goodOrder.getPrice();
        }
        mSubTotal.setText(DataUtil.calMoney(subTotal));
        double tax = 0.0875 * subTotal;
        mTax.setText(DataUtil.calMoney(tax));
        mTotal.setText(DataUtil.calMoney(subTotal + tax));
    }

    private class OrderAdapter extends BaseAdapter {

        private List<GoodsOrder> mOrders;
        private Context mContext;

        public OrderAdapter(Context context, List<GoodsOrder> orders) {
            mContext = context;
            mOrders = orders;
        }

        @Override
        public int getCount() {
            return mOrders == null ? 0 : mOrders.size();
        }

        @Override
        public Object getItem(int position) {
            return mOrders.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mOrders.get(position).getId();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) (mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE));
                convertView = inflater.inflate(R.layout.item_order_confirm, parent, false);
            }
            final GoodsOrder order = mOrders.get(position);
            TextView price = (TextView) convertView.findViewById(R.id.price);
            price.setText(DataUtil.calMoney(order.getPrice() * order.getNumber()));
            AddDishButtonForDialog addButton = (AddDishButtonForDialog) convertView.findViewById(R.id.qty);
            addButton.setGoodId(order.getId());
            addButton.setOnOrderChange(new OnOrderChange() {
                @Override
                public void onOrderChange(int goodId, int count) {
                    order.number = count;
                    notifyDataSetChanged();
                    mOrderManager.onOrderChange(goodId, count);
                    updateTotalPrice(mOrders);
                }
            });
            addButton.setCount((int) order.getNumber());
            TextView name = (TextView) convertView.findViewById(R.id.name);
            name.setText(order.getName());

            return convertView;
        }
    }
}
