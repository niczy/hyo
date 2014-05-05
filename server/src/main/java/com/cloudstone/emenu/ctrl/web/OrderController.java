/**
 * @(#)OrderController.java, Jul 29, 2013. 
 *
 */
package com.cloudstone.emenu.ctrl.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cloudstone.emenu.EmenuContext;
import com.cloudstone.emenu.constant.Const;
import com.cloudstone.emenu.data.Bill;
import com.cloudstone.emenu.data.Order;
import com.cloudstone.emenu.data.Table;
import com.cloudstone.emenu.exception.PreconditionFailedException;

/**
 * @author xuhongfeng
 */
@Controller
public class OrderController extends BaseWebController {

    @RequestMapping("/bill")
    public String payBill(HttpServletRequest req, HttpServletResponse resp,
                          @RequestParam("tableId") int tableId, ModelMap model) {
        EmenuContext context = newContext(req);
        Table table = tableLogic.get(context, tableId);
        if (table == null) {
            sendError(resp, 404);
            return null;
        }
        Order order = orderLogic.getOrder(context, table.getOrderId());
        if (order == null || order.isDeleted()) {
            throw new PreconditionFailedException("Can not find order");
        }
        model.put("order", orderWraper.wrap(context, order));
        return sendView("bill", req, resp, model);
    }


    @RequestMapping("/order")
    public String order(HttpServletRequest req, HttpServletResponse resp,
                        @RequestParam("orderId") int orderId, ModelMap model) {
        EmenuContext context = newContext(req);
        Order order = orderLogic.getOrder(context, orderId);
        if (order == null || order.isDeleted()) {
            throw new PreconditionFailedException("Can not find order");
        }
        model.put("order", orderWraper.wrap(context, order));
        if (order.getStatus() == Const.OrderStatus.PAYED) {
            Bill bill = orderLogic.getBillByOrderId(context, orderId);
            model.put("bill", bill);
        }
        return sendView("bill", req, resp, model);
    }
}
