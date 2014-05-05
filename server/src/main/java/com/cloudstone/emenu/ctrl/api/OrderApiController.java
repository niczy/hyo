/**
 * @(#)OrderApiController.java, Jul 30, 2013. 
 *
 */
package com.cloudstone.emenu.ctrl.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cloudstone.emenu.EmenuContext;
import com.cloudstone.emenu.data.Bill;
import com.cloudstone.emenu.data.Order;
import com.cloudstone.emenu.data.PayType;
import com.cloudstone.emenu.data.vo.OrderVO;
import com.cloudstone.emenu.exception.BadRequestError;
import com.cloudstone.emenu.util.JsonUtils;

/**
 * @author xuhongfeng
 */
@Controller
public class OrderApiController extends BaseApiController {

    @RequestMapping(value = "/api/orders/{id:[\\d]+}", method = RequestMethod.DELETE)
    public void deleteOrder(@PathVariable("id") int orderId,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        EmenuContext context = newContext(request);
        orderLogic.deleteOrder(context, orderId);
        return;
    }

    @RequestMapping(value = "/api/orders/{id:[\\d]+}", method = RequestMethod.GET)
    public
    @ResponseBody
    OrderVO getOrder(@PathVariable(value = "id") int orderId,
                     HttpServletRequest request,
                     HttpServletResponse response) {
        EmenuContext context = newContext(request);
        Order order = orderLogic.getOrder(context, orderId);
        if (order == null) {
            sendError(response, HttpServletResponse.SC_NOT_FOUND);
        }
        return orderWraper.wrap(context, order);
    }

    @RequestMapping(value = "/api/orders", method = RequestMethod.GET)
    public
    @ResponseBody
    List<OrderVO> getDailyOrders(
            @RequestParam(value = "time", defaultValue = "0") long time,
            @RequestParam(value = "start", defaultValue = "0") long startTime,
            @RequestParam(value = "end", defaultValue = "0") long endTime,
            HttpServletRequest request,
            HttpServletResponse response) {
        EmenuContext context = newContext(request);
        if (time < 0 || startTime < 0 || endTime < 0 || startTime > endTime) {
            throw new BadRequestError();
        }
        List<Order> orders;
        if (time > 0) {
            orders = orderLogic.getDailyOrders(context, time);
        } else if (startTime > 0) {
            orders = orderLogic.getOrders(context, startTime, endTime);
        } else {
            time = System.currentTimeMillis();
            orders = orderLogic.getDailyOrders(context, time);
        }
        return orderWraper.wrap(context, orders);
    }

    @RequestMapping(value = "/api/pay-types", method = RequestMethod.GET)
    public
    @ResponseBody
    List<PayType> listPayTypes(HttpServletRequest request) {
        EmenuContext context = newContext(request);
        return orderLogic.listPayTypes(context);
    }

    @RequestMapping(value = "/api/bills", method = RequestMethod.POST)
    public
    @ResponseBody
    Bill payBill(@RequestBody String body,
                 HttpServletRequest req,
                 HttpServletResponse response) {
        EmenuContext context = newContext(req);
        Bill bill = JsonUtils.fromJson(body, Bill.class);
        return orderLogic.payBill(context, bill, getLoginUser(req));
    }

    @RequestMapping(value = "/api/orders/{id:[\\d]+}/dishes/{dishId:[\\d]+}/cancel", method = RequestMethod.PUT)
    public
    @ResponseBody
    OrderVO cancelDish(@PathVariable("id") int orderId,
                       @PathVariable("dishId") int dishId,
                       @RequestParam("count") int count,
                       HttpServletRequest request,
                       HttpServletResponse response) {
        EmenuContext context = newContext(request);
        Order order = orderLogic.cancelDish(context, orderId, dishId, count);
        return orderWraper.wrap(context, order);
    }

    @RequestMapping(value = "/api/orders/{id:[\\d]+}/dishes/{dishId:[\\d]+}/free", method = RequestMethod.PUT)
    public
    @ResponseBody
    OrderVO freeDish(@PathVariable("id") int orderId,
                     @PathVariable("dishId") int dishId,
                     @RequestParam("count") int count,
                     HttpServletRequest request,
                     HttpServletResponse response) {
        EmenuContext context = newContext(request);
        Order order = orderLogic.freeDish(context, orderId, dishId, count);
        return orderWraper.wrap(context, order);
    }

    @RequestMapping(value = "/api/orders", method = RequestMethod.POST)
    public
    @ResponseBody
    OrderVO submit(HttpServletRequest request
            , HttpServletResponse response
            , @RequestBody String body) {
        OrderVO order = JsonUtils.fromJson(body, OrderVO.class);
        EmenuContext context = newContext(request);
        if (order == null || order.getDishes() == null) {
            throw(new BadRequestError("no dishes in order"));
        }
        return orderLogic.submit(context, order, order.getDishes());
    }

    @RequestMapping(value = "/api/orders/{id:[\\d]+}", method = RequestMethod.PUT)
    public
    @ResponseBody
    OrderVO update(HttpServletRequest request
            , HttpServletResponse response
            , @PathVariable("id") int orderId
            , @RequestBody String body) {
        EmenuContext context = newContext(request);
        OrderVO order = JsonUtils.fromJson(body, OrderVO.class);
        return orderLogic.incrUpdate(context, orderId, order.getDishes());
    }

}
