/**
 * @(#)DishDb.java, 2013-7-7. 
 *
 */
package com.cloudstone.emenu.storage.sqlitedb;

import java.util.List;

import com.cloudstone.emenu.storage.dao.IDishDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;
import com.cloudstone.emenu.EmenuContext;
import com.cloudstone.emenu.data.Dish;
import com.cloudstone.emenu.data.IdName;
import com.cloudstone.emenu.storage.sqlitedb.util.ColumnDefBuilder;
import com.cloudstone.emenu.storage.sqlitedb.util.IdStatementBinder;
import com.cloudstone.emenu.storage.sqlitedb.util.InsertSqlBuilder;
import com.cloudstone.emenu.storage.sqlitedb.util.RowMapper;
import com.cloudstone.emenu.storage.sqlitedb.util.SelectSqlBuilder;
import com.cloudstone.emenu.storage.sqlitedb.util.SqlUtils;
import com.cloudstone.emenu.storage.sqlitedb.util.StatementBinder;
import com.cloudstone.emenu.storage.sqlitedb.util.UpdateSqlBuilder;

/**
 * @author xuhongfeng
 */
@Repository
public class DishDb extends SQLiteDb implements IDishDb {
    private static final Logger LOG = LoggerFactory.getLogger(DishDb.class);

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public List<IdName> getDishSuggestion(EmenuContext context) {
        return getIdNames(context);
    }

    @Override
    public void add(EmenuContext context, Dish dish) {
        dish.setId(genId(context));
        dish.setRestaurantId(context.getRestaurantId());
        DishBinder binder = new DishBinder(dish);
        executeSQL(context, SQL_INSERT, binder);
    }

    @Override
    public Dish getByName(EmenuContext context, String name) {
        return super.getByName(context, name, rowMapper);
    }

    @Override
    public Dish get(EmenuContext context, int dishId) {
        IdStatementBinder binder = new IdStatementBinder(dishId);
        Dish dish = queryOne(context, SQL_SELECT_BY_ID, binder, rowMapper);
        return dish;
    }

    @Override
    public List<Dish> getAll(EmenuContext context) {
        return getAllInRestaurant(context, rowMapper);
    }

    @Override
    public void update(EmenuContext context, Dish dish) {
        executeSQL(context, SQL_UPDATE, new UpdateBinder(dish));
    }

    @Override
    protected void onCheckCreateTable(EmenuContext context) {
        checkCreateTable(context, TABLE_NAME, COL_DEF);
    }

    /* ---------- inner class ---------- */
    private static final RowMapper<Dish> rowMapper = new RowMapper<Dish>() {

        @Override
        public Dish map(SQLiteStatement stmt) throws SQLiteException {
            Dish dish = new Dish();

            dish.setId(stmt.columnInt(0));
            dish.setName(stmt.columnString(1));
            dish.setPinyin(stmt.columnString(2));
            dish.setPrice(stmt.columnDouble(3));
            dish.setMemberPrice(stmt.columnDouble(4));
            dish.setUnit(stmt.columnInt(5));
            dish.setSpicy(stmt.columnInt(6));
            dish.setSpecialPrice(SqlUtils.intToBoolean(stmt.columnInt(7)));
            dish.setNonInt(SqlUtils.intToBoolean(stmt.columnInt(8)));
            dish.setDesc(stmt.columnString(9));
            dish.setImageId(stmt.columnString(10));
            dish.setStatus(stmt.columnInt(11));
            dish.setCreatedTime(stmt.columnLong(12));
            dish.setUpdateTime(stmt.columnLong(13));
            dish.setDeleted(stmt.columnInt(14) == 1);
            dish.setSoldout(stmt.columnInt(15) == 1);
            dish.setRestaurantId(stmt.columnInt(16));
            return dish;
        }
    };

    private static class DishBinder implements StatementBinder {
        private final Dish dish;

        public DishBinder(Dish dish) {
            super();
            this.dish = dish;
        }

        @Override
        public void onBind(SQLiteStatement stmt) throws SQLiteException {
            stmt.bind(1, dish.getId());
            stmt.bind(2, dish.getName());
            stmt.bind(3, dish.getPinyin());
            stmt.bind(4, dish.getPrice());
            stmt.bind(5, dish.getMemberPrice());
            stmt.bind(6, dish.getUnit());
            stmt.bind(7, dish.getSpicy());
            stmt.bind(8, SqlUtils.booleanToInt(dish.isSpecialPrice()));
            stmt.bind(9, SqlUtils.booleanToInt(dish.isNonInt()));
            stmt.bind(10, dish.getDesc());
            stmt.bind(11, dish.getImageId());
            stmt.bind(12, dish.getStatus());
            stmt.bind(13, dish.getCreatedTime());
            stmt.bind(14, dish.getUpdateTime());
            stmt.bind(15, dish.isDeleted() ? 1 : 0);
            stmt.bind(16, dish.isSoldout() ? 1 : 0);
            stmt.bind(17, dish.getRestaurantId());
        }
    }

    private static class UpdateBinder implements StatementBinder {
        private final Dish dish;

        public UpdateBinder(Dish dish) {
            super();
            this.dish = dish;
        }

        @Override
        public void onBind(SQLiteStatement stmt) throws SQLiteException {
            stmt.bind(1, dish.getName());
            stmt.bind(2, dish.getPinyin());
            stmt.bind(3, dish.getPrice());
            stmt.bind(4, dish.getMemberPrice());
            stmt.bind(5, dish.getUnit());
            stmt.bind(6, dish.getSpicy());
            stmt.bind(7, SqlUtils.booleanToInt(dish.isSpecialPrice()));
            stmt.bind(8, SqlUtils.booleanToInt(dish.isNonInt()));
            stmt.bind(9, dish.getDesc());
            stmt.bind(10, dish.getImageId());
            stmt.bind(11, dish.getStatus());
            stmt.bind(12, dish.getCreatedTime());
            stmt.bind(13, dish.getUpdateTime());
            stmt.bind(14, dish.isDeleted() ? 1 : 0);
            stmt.bind(15, dish.isSoldout() ? 1 : 0);
            stmt.bind(16, dish.getRestaurantId());
            stmt.bind(17, dish.getId());
        }
    }

    /* ---------- SQL ---------- */
    private static final String TABLE_NAME = "dish";

    private static enum Column {
        ID("id"), NAME("name"), PINYIN("pinyin"),
        PRICE("price"), MEMBER_PRICE("memberPrice"), UNIT("unit"), SPICY("spicy"),
        SPECIAL_PRICE("specialPrice"), NON_INT("nonInt"), DESC("desc"),
        IMAGE_ID("imageId"), STATUS("status"),
        CREATED_TIME("createdTime"), UPDATE_TIME("updateTime"), DELETED("deleted"),
        SOLDOUT("soldout"), RESTAURANT_ID("restaurantId");

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
            .append(Column.NAME, DataType.TEXT, "NOT NULL")
            .append(Column.PINYIN, DataType.TEXT, "NOT NULL")
            .append(Column.PRICE, DataType.REAL, "NOT NULL")
            .append(Column.MEMBER_PRICE, DataType.REAL, "NOT NULL")
            .append(Column.UNIT, DataType.INTEGER, "NOT NULL")
            .append(Column.SPICY, DataType.INTEGER, "NOT NULL")
            .append(Column.SPECIAL_PRICE, DataType.INTEGER, "NOT NULL")
            .append(Column.NON_INT, DataType.INTEGER, "NOT NULL")
            .append(Column.DESC, DataType.TEXT, "NOT NULL")
            .append(Column.IMAGE_ID, DataType.TEXT, "DEFAULT ''")
            .append(Column.STATUS, DataType.INTEGER, "DEFAULT ''")
            .append(Column.CREATED_TIME, DataType.INTEGER, "NOT NULL")
            .append(Column.UPDATE_TIME, DataType.INTEGER, "NOT NULL")
            .append(Column.DELETED, DataType.INTEGER, "NOT NULL")
            .append(Column.SOLDOUT, DataType.INTEGER, "NOT NULL")
            .append(Column.RESTAURANT_ID, DataType.INTEGER, "NOT NULL")
            .build();
    private static final String SQL_INSERT = new InsertSqlBuilder(TABLE_NAME, 17).build();
    private static final String SQL_SELECT_BY_ID = new SelectSqlBuilder(TABLE_NAME)
            .appendWhereId().build();
    private static final String SQL_UPDATE = new UpdateSqlBuilder(TABLE_NAME)
            .appendSetValue(Column.NAME)
            .appendSetValue(Column.PINYIN)
            .appendSetValue(Column.PRICE)
            .appendSetValue(Column.MEMBER_PRICE)
            .appendSetValue(Column.UNIT)
            .appendSetValue(Column.SPICY)
            .appendSetValue(Column.SPECIAL_PRICE)
            .appendSetValue(Column.NON_INT)
            .appendSetValue(Column.DESC)
            .appendSetValue(Column.IMAGE_ID)
            .appendSetValue(Column.STATUS)
            .appendSetValue(Column.CREATED_TIME)
            .appendSetValue(Column.UPDATE_TIME)
            .appendSetValue(Column.DELETED)
            .appendSetValue(Column.SOLDOUT)
            .appendSetValue(Column.RESTAURANT_ID)
            .appendWhereId()
            .build();
}
