/**
 * @(#)BillDb.java, Aug 1, 2013. 
 *
 */
package com.cloudstone.emenu.storage.sqlitedb;

import java.util.List;

import com.cloudstone.emenu.storage.dao.IBillDb;
import org.springframework.stereotype.Repository;

import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;
import com.cloudstone.emenu.EmenuContext;
import com.cloudstone.emenu.data.Bill;
import com.cloudstone.emenu.data.vo.OrderVO;
import com.cloudstone.emenu.storage.sqlitedb.util.ColumnDefBuilder;
import com.cloudstone.emenu.storage.sqlitedb.util.IdStatementBinder;
import com.cloudstone.emenu.storage.sqlitedb.util.InsertSqlBuilder;
import com.cloudstone.emenu.storage.sqlitedb.util.RowMapper;
import com.cloudstone.emenu.storage.sqlitedb.util.SelectSqlBuilder;
import com.cloudstone.emenu.storage.sqlitedb.util.SqlUtils;
import com.cloudstone.emenu.storage.sqlitedb.util.StatementBinder;
import com.cloudstone.emenu.storage.sqlitedb.util.TimeStatementBinder;
import com.cloudstone.emenu.util.JsonUtils;
import com.cloudstone.emenu.util.StringUtils;

/**
 * @author xuhongfeng
 */
@Repository
public class BillDb extends SQLiteDb implements IBillDb {

    @Override
    public Bill getByOrderId(EmenuContext context, int orderId) {
        return queryOne(context, SQL_SELECT_BY_ORDER_ID, new OrderIdBinder(orderId), rowMapper);
    }

    @Override
    public void add(EmenuContext context, Bill bill) {
        bill.setId(genId(context));
        bill.setRestaurantId(context.getRestaurantId());
        long now = System.currentTimeMillis();
        bill.setCreatedTime(now);
        bill.setUpdateTime(now);
        executeSQL(context, SQL_INSERT, new BillBinder(bill));
    }

    @Override
    public List<Bill> listBills(EmenuContext context) {
        return getAllInRestaurant(context, rowMapper);
    }

    @Override
    public Bill get(EmenuContext context, int id) {
        IdStatementBinder binder = new IdStatementBinder(id);
        return queryOne(context, SQL_SELECT_BY_ID, binder, rowMapper);
    }

    @Override
    public List<Bill> getBillsByTime(EmenuContext context, long startTime, long endTime) {
        TimeStatementBinder binder = new TimeStatementBinder(startTime, endTime, context.getRestaurantId());
        return query(context, SQL_SELECT_BY_TIME, binder, rowMapper);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected void onCheckCreateTable(EmenuContext context) {
        checkCreateTable(context, TABLE_NAME, COL_DEF);
    }

    private static final String TABLE_NAME = "bill";

    private static enum Column {
        ID("id"), ORDER_ID("orderId"), COST("cost"), DISCOUNT("discount"),
        TIP("tip"), INVOICE("invoice"), INVOICE_PRICE("invoicePrice"), DISCOUNT_DISH_IDS("discountDishIds"),
        PAY_TYPE("payType"), REMARKS("remarks"), ORDER("`order`"), COUPONS("coupons"),
        VIPID("vipId"), VIPCOST("vipCost"),
        CREATED_TIME("createdTime"), UPDATE_TIME("update_time"), DELETED("deleted"),
        RESTAURANT_ID("restaurantId");

        private final String str;

        private Column(String str) {
            this.str = str;
        }

        @Override
        public String toString() {
            return str;
        }
    }

    private static final String COL_DEF = new ColumnDefBuilder()
            .append(Column.ID, DataType.INTEGER, "NOT NULL PRIMARY KEY")
            .append(Column.ORDER_ID, DataType.INTEGER, "NOT NULL")
            .append(Column.COST, DataType.REAL, "NOT NULL")
            .append(Column.DISCOUNT, DataType.REAL, "NOT NULL")
            .append(Column.TIP, DataType.REAL, "NOT NULL")
            .append(Column.INVOICE, DataType.INTEGER, "NOT NULL")
            .append(Column.INVOICE_PRICE, DataType.REAL, "NOT NULL")
            .append(Column.DISCOUNT_DISH_IDS, DataType.TEXT, "NOT NULL")
            .append(Column.PAY_TYPE, DataType.INTEGER, "NOT NULL")
            .append(Column.REMARKS, DataType.TEXT, "NOT NULL")
            .append(Column.ORDER, DataType.TEXT, "NOT NULL")
            .append(Column.COUPONS, DataType.REAL, "NOT NULL")
            .append(Column.VIPID, DataType.INTEGER, "NOT NULL")
            .append(Column.VIPCOST, DataType.REAL, "NOT NULL")
            .append(Column.CREATED_TIME, DataType.INTEGER, "NOT NULL")
            .append(Column.UPDATE_TIME, DataType.INTEGER, "NOT NULL")
            .append(Column.DELETED, DataType.INTEGER, "NOT NULL")
            .append(Column.RESTAURANT_ID, DataType.INTEGER, "NOT NULL")
            .build();
    private static final String SQL_INSERT = new InsertSqlBuilder(TABLE_NAME, 18).build();

    private static class BillBinder implements StatementBinder {
        private final Bill bill;

        public BillBinder(Bill bill) {
            super();
            this.bill = bill;
        }

        @Override
        public void onBind(SQLiteStatement stmt) throws SQLiteException {
            String archive = "";
            if (bill.getOrder() != null) {
                archive = JsonUtils.toJson(bill.getOrder());
            }
            stmt.bind(1, bill.getId());
            stmt.bind(2, bill.getOrderId());
            stmt.bind(3, bill.getCost());
            stmt.bind(4, bill.getDiscount());
            stmt.bind(5, bill.getTip());
            stmt.bind(6, bill.isInvoice() ? 1 : 0);
            stmt.bind(7, bill.getInvoicePrice());
            stmt.bind(8, SqlUtils.idsToStr(bill.getDiscountDishIds()));
            stmt.bind(9, bill.getPayType());
            stmt.bind(10, bill.getRemarks());
            stmt.bind(11, archive);
            stmt.bind(12, bill.getCoupons());
            stmt.bind(13, bill.getVipId());
            stmt.bind(14, bill.getVipCost());
            stmt.bind(15, bill.getCreatedTime());
            stmt.bind(16, bill.getUpdateTime());
            stmt.bind(17, bill.isDeleted() ? 1 : 0);
            stmt.bind(18, bill.getRestaurantId());
        }
    }

    private static final RowMapper<Bill> rowMapper = new RowMapper<Bill>() {

        @Override
        public Bill map(SQLiteStatement stmt) throws SQLiteException {
            Bill bill = new Bill();
            bill.setId(stmt.columnInt(0));
            bill.setOrderId(stmt.columnInt(1));
            bill.setCost(stmt.columnDouble(2));
            bill.setDiscount(stmt.columnDouble(3));
            bill.setTip(stmt.columnDouble(4));
            bill.setInvoice(stmt.columnInt(5) == 1);
            bill.setInvoicePrice(stmt.columnDouble(6));
            bill.setDiscountDishIds(SqlUtils.strToIds(stmt.columnString(7)));
            bill.setPayType(stmt.columnInt(8));
            bill.setRemarks(stmt.columnString(9));
            String archive = stmt.columnString(10);
            if (!StringUtils.isBlank(archive)) {
                OrderVO order = JsonUtils.fromJson(archive, OrderVO.class);
                bill.setOrder(order);
            }
            bill.setCoupons(stmt.columnDouble(11));
            bill.setVipId(stmt.columnInt(12));
            bill.setVipCost(stmt.columnDouble(13));
            bill.setCreatedTime(stmt.columnLong(14));
            bill.setUpdateTime(stmt.columnLong(15));
            bill.setDeleted(stmt.columnInt(16) == 1);

            return bill;
        }
    };
    private static final String SQL_SELECT_BY_ID = new SelectSqlBuilder(TABLE_NAME)
            .appendWhereId().build();
    private static final String SQL_SELECT_BY_ORDER_ID = new SelectSqlBuilder(TABLE_NAME)
            .appendWhere(Column.ORDER_ID).build();
    private static final String SQL_SELECT_BY_TIME = new SelectSqlBuilder(TABLE_NAME)
            .append(" WHERE createdTime>? ")
            .append(" AND createdTime<?")
            .append(" AND restaurantId=?").build();

    public static class OrderIdBinder implements StatementBinder {
        private final int orderId;

        public OrderIdBinder(int orderId) {
            super();
            this.orderId = orderId;
        }

        @Override
        public void onBind(SQLiteStatement stmt) throws SQLiteException {
            stmt.bind(1, orderId);
        }
    }
}
