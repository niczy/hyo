/**
 * @(#)PrintComponentDb.java, Aug 13, 2013. 
 *
 */
package com.cloudstone.emenu.storage.sqlitedb;

import java.util.List;

import com.cloudstone.emenu.storage.dao.IPrintComponentDb;
import org.springframework.stereotype.Repository;

import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;
import com.cloudstone.emenu.EmenuContext;
import com.cloudstone.emenu.data.PrintComponent;
import com.cloudstone.emenu.storage.sqlitedb.util.ColumnDefBuilder;
import com.cloudstone.emenu.storage.sqlitedb.util.IdStatementBinder;
import com.cloudstone.emenu.storage.sqlitedb.util.InsertSqlBuilder;
import com.cloudstone.emenu.storage.sqlitedb.util.RowMapper;
import com.cloudstone.emenu.storage.sqlitedb.util.SelectSqlBuilder;
import com.cloudstone.emenu.storage.sqlitedb.util.StatementBinder;
import com.cloudstone.emenu.storage.sqlitedb.util.UpdateSqlBuilder;

/**
 * @author xuhongfeng
 */
@Repository
public class PrintComponentDb extends SQLiteDb implements IPrintComponentDb {
    private static final String TABLE_NAME = "printComponent";

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public List<PrintComponent> listAll(EmenuContext context) {
        return getAllInRestaurant(context, rowMapper);
    }

    @Override
    public void add(EmenuContext context, PrintComponent data) {
        data.setId(genId(context));
        data.setRestaurantId(context.getRestaurantId());
        executeSQL(context, SQL_INSERT, new PrintComponentBinder(data));
    }

    @Override
    public void update(EmenuContext context, PrintComponent data) {
        executeSQL(context, SQL_UPDATE, new UpdateBinder(data));
    }

    @Override
    public PrintComponent get(EmenuContext context, int id) {
        return queryOne(context, SQL_SELECT_BY_ID, new IdStatementBinder(id), rowMapper);
    }

    @Override
    protected void onCheckCreateTable(EmenuContext context) {
        checkCreateTable(context, TABLE_NAME, COL_DEF);
    }

    /* ---------- SQL ---------- */
    private static enum Column {
        ID("id"), NAME("name"), CONTENT("content"),
        CREATED_TIME("createdTime"), UPDATE_TIME("updateTime"),
        DELETED("deleted"), RESTAURANT_ID("restaurantId");

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
            .append(Column.CONTENT, DataType.TEXT, "NOT NULL")
            .append(Column.CREATED_TIME, DataType.INTEGER, "NOT NULL")
            .append(Column.UPDATE_TIME, DataType.INTEGER, "NOT NULL")
            .append(Column.DELETED, DataType.INTEGER, "NOT NULL")
            .append(Column.RESTAURANT_ID, DataType.INTEGER, "NOT NULL")
            .build();
    private static final String SQL_INSERT = new InsertSqlBuilder(TABLE_NAME, 7).build();
    private static final String SQL_SELECT_BY_ID = new SelectSqlBuilder(TABLE_NAME)
            .appendWhereId().build();

    private static class PrintComponentBinder implements StatementBinder {
        private final PrintComponent data;

        public PrintComponentBinder(PrintComponent data) {
            super();
            this.data = data;
        }

        @Override
        public void onBind(SQLiteStatement stmt) throws SQLiteException {
            stmt.bind(1, data.getId());
            stmt.bind(2, data.getName());
            stmt.bind(3, data.getContent());
            stmt.bind(4, data.getCreatedTime());
            stmt.bind(5, data.getUpdateTime());
            stmt.bind(6, data.isDeleted() ? 1 : 0);
            stmt.bind(7, data.getRestaurantId());
        }
    }

    private RowMapper<PrintComponent> rowMapper = new RowMapper<PrintComponent>() {
        @Override
        public PrintComponent map(SQLiteStatement stmt) throws SQLiteException {
            PrintComponent data = new PrintComponent();
            data.setId(stmt.columnInt(0));
            data.setName(stmt.columnString(1));
            data.setContent(stmt.columnString(2));
            data.setCreatedTime(stmt.columnInt(3));
            data.setUpdateTime(stmt.columnInt(4));
            data.setDeleted(stmt.columnInt(5) == 1);
            return data;
        }
    };

    private static final String SQL_UPDATE = new UpdateSqlBuilder(TABLE_NAME)
            .appendSetValue(Column.NAME)
            .appendSetValue(Column.CONTENT)
            .appendSetValue(Column.CREATED_TIME)
            .appendSetValue(Column.UPDATE_TIME)
            .appendSetValue(Column.DELETED)
            .appendWhereId()
            .build();

    private static class UpdateBinder implements StatementBinder {
        private final PrintComponent data;

        public UpdateBinder(PrintComponent data) {
            super();
            this.data = data;
        }

        @Override
        public void onBind(SQLiteStatement stmt) throws SQLiteException {
            stmt.bind(1, data.getName());
            stmt.bind(2, data.getContent());
            stmt.bind(3, data.getCreatedTime());
            stmt.bind(4, data.getUpdateTime());
            stmt.bind(5, data.isDeleted() ? 1 : 0);
            stmt.bind(6, data.getId());
        }

    }
}
