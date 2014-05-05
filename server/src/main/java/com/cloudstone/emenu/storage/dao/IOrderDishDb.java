/**
 * @(#)IOrderDishDb.java, Jul 28, 2013. 
 *
 */
package com.cloudstone.emenu.storage.dao;

import java.util.List;

import com.cloudstone.emenu.EmenuContext;
import com.cloudstone.emenu.data.OrderDish;

/**
 * @author xuhongfeng
 */
public interface IOrderDishDb {
    public void add(EmenuContext context, OrderDish data);

    public void update(EmenuContext context, OrderDish data);

    public void delete(EmenuContext context, int orderId, int dishId);

    public List<OrderDish> listOrderDish(EmenuContext context, int orderId);

    public OrderDish getOrderDish(EmenuContext context, int orderId, int dishId);
}
