package com.heymenu.core;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.cloudstone.menu.server.thrift.api.Goods;
import cn.com.cloudstone.menu.server.thrift.api.GoodsOrder;
import cn.com.cloudstone.menu.server.thrift.api.MenuPage;

/**
 * Created by nicholaszhao on 4/19/14.
 */
public class OrderManager implements OnOrderChange {

    List<Goods> mGoods = new ArrayList<Goods>();
    List<Goods> mSpecialGoods = new ArrayList<Goods>();
    Map<Integer, Integer> newOrder = new HashMap<Integer, Integer>();
    Map<Integer, Goods> mGoodsMap = new HashMap<Integer, Goods>();

    public void setMenu(cn.com.cloudstone.menu.server.thrift.api.Menu menu) {
        mGoods.clear();
        for (MenuPage page : menu.getPages()) {
            mGoods.addAll(page.getGoodsList());
            for (Goods good : page.getGoodsList()) {
                mGoodsMap.put(good.getId(), good);
            }
            for (Goods good : page.getGoodsList()) {
                if (good.onSales) {
                    mSpecialGoods.add(good);
                }
            }
        }
        Log.i("OrderManager", "Special Goods size " + mSpecialGoods.size());
    }

    public Goods getGoodAt(int index) {
        return mGoods.get(index);
    }

    public Goods getSpecialGoodAt(int index) {
        return mSpecialGoods.get(index);
    }

    public int getGoodSize() {
        return mGoods.size();
    }

    public int getSpecialGoodSize() {
        return mSpecialGoods.size();
    }

    public List<GoodsOrder> getCurrentOrder() {
        List<GoodsOrder> orders = new ArrayList<GoodsOrder>();
        for (Map.Entry<Integer, Integer> entry : newOrder.entrySet()) {
            if (entry.getValue() > 0) {
                GoodsOrder order = new GoodsOrder();
                Goods good = mGoodsMap.get(entry.getKey());
                order.id = good.getId();
                order.name = good.getName();
                order.number = entry.getValue();
                order.price = good.price;
                order.category = good.getCategory();
                orders.add(order);
            }
        }
        return orders;
    }

    @Override
    public void onOrderChange(int goodId, int count) {
        newOrder.put(goodId, count);
    }

    public void clearCurrentOrder() {
        newOrder.clear();
    }

    public int getOrderCountForGood(int id) {
        return newOrder.containsKey(id) ? newOrder.get(id) : 0;
    }

    public int getOrderCount() {
        int count = 0;
        for (Map.Entry<Integer, Integer> entry : newOrder.entrySet()) {
            count += entry.getValue();
        }
        return count;
    }
}
