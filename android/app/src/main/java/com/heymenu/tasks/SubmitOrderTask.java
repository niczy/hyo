package com.heymenu.tasks;

import android.content.Context;
import android.util.Log;

import net.cloudmenu.emenu.R;
import net.cloudmenu.emenu.net.RPCHelper;
import net.cloudmenu.emenu.task.TBaseTask;
import net.cloudmenu.emenu.utils.ProfileHolder;

import org.apache.thrift.TException;
import org.apache.thrift.TServiceClient;

import java.util.List;

import cn.buding.common.exception.ECode;
import cn.com.cloudstone.menu.server.thrift.api.AException;
import cn.com.cloudstone.menu.server.thrift.api.GoodsOrder;
import cn.com.cloudstone.menu.server.thrift.api.HasInvalidGoodsException;
import cn.com.cloudstone.menu.server.thrift.api.IOrderService;
import cn.com.cloudstone.menu.server.thrift.api.Order;
import cn.com.cloudstone.menu.server.thrift.api.TableEmptyException;
import cn.com.cloudstone.menu.server.thrift.api.UnderMinChargeException;
import cn.com.cloudstone.menu.server.thrift.api.UserNotLoginException;

/**
 * Created by nicholaszhao on 4/17/14.
 */
public class SubmitOrderTask extends TBaseTask {
    private List<GoodsOrder> mOrders;
    private final String mTableId;
    private static final int ECODE_INVALIDE_GOODS_SECOND_TIME = 102;

    public SubmitOrderTask(Context context, List<GoodsOrder> orders, String tableId) {
        super(context);
        mOrders = orders;
        mTableId = tableId;
        setShowProgessDialog(true);
        setCodeMsg(ECode.SUCCESS, mContext.getString(R.string.activity_order_complete));
        setCodeMsg(ECODE_INVALIDE_GOODS_SECOND_TIME, "Invalid state.");
    }

    @Override
    protected TServiceClient getClient() throws TException {
        return RPCHelper.getOrderService(mContext);
    }

    @Override
    protected Object process(TServiceClient client) throws TException,
            AException {
        IOrderService.Client iclient = (IOrderService.Client) client;
        try {
            String sid = ProfileHolder.getIns().getCurrentSid(mContext);
            Log.d("Order", "table id is " + mTableId);
            Order order = new Order();
            order.setTableId(mTableId);
            order.setGoods(mOrders);
            iclient.submitOrder(sid, order);
            return ECode.SUCCESS;
        } catch (Exception e) {
            Log.w("Order", "Submit order failed.", e);
            return e;
        }
    }

    @Override
    protected void processResult(Object result) {
        if (result instanceof UnderMinChargeException) {
            UnderMinChargeException e = (UnderMinChargeException) result;
            showResultMessage("The minimal order is $" + e.getMinCharge()
                    + "Please continue your order.");
        } else if (result instanceof HasInvalidGoodsException) {
            showResultMessage("Some dishes are sold order, please update.");
        } else {
            super.processResult(result);
        }
    }

}