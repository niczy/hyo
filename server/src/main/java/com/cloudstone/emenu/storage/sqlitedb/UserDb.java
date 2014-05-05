/**
 * @(#)UserDb.java, 2013-6-20. 
 *
 */
package com.cloudstone.emenu.storage.sqlitedb;

import java.util.List;

import com.cloudstone.emenu.storage.dao.IUserDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.almworks.sqlite4java.SQLiteException;
import com.almworks.sqlite4java.SQLiteStatement;
import com.cloudstone.emenu.EmenuContext;
import com.cloudstone.emenu.data.User;
import com.cloudstone.emenu.storage.sqlitedb.util.ColumnDefBuilder;
import com.cloudstone.emenu.storage.sqlitedb.util.IdStatementBinder;
import com.cloudstone.emenu.storage.sqlitedb.util.InsertSqlBuilder;
import com.cloudstone.emenu.storage.sqlitedb.util.RowMapper;
import com.cloudstone.emenu.storage.sqlitedb.util.SelectSqlBuilder;
import com.cloudstone.emenu.storage.sqlitedb.util.StatementBinder;
import com.cloudstone.emenu.storage.sqlitedb.util.UpdateSqlBuilder;
import com.cloudstone.emenu.util.RsaUtils;

/**
 * @author xuhongfeng
 */
@Repository
public class UserDb extends SQLiteDb implements IUserDb {
    private static final Logger LOG = LoggerFactory.getLogger(UserDb.class);

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Value("${admin.password.default}")
    private String DEFAULT_ADMIN_PASSWORD;

    private static final String TABLE_NAME = "user";

    private static enum Column {
        ID("id"), NAME("name"), PASSWORD("password"),
        TYPE("type"), REAL_NAME("realName"), COMMENT("comment"),
        CREATED_TIME("createdTime"), UPDATE_TIME("updatetime"), DELETED("deleted"),
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
            .append(Column.ID.toString(), DataType.INTEGER, "NOT NULL PRIMARY KEY")
            .append(Column.NAME.toString(), DataType.TEXT, "NOT NULL")
            .append(Column.PASSWORD.toString(), DataType.TEXT, "NOT NULL")
            .append(Column.TYPE.toString(), DataType.INTEGER, "NOT NULL")
            .append(Column.REAL_NAME.toString(), DataType.TEXT, "NOT NULL")
            .append(Column.COMMENT.toString(), DataType.TEXT, "DEFAULT ''")
            .append(Column.CREATED_TIME, DataType.INTEGER, "NOT NULL")
            .append(Column.UPDATE_TIME, DataType.INTEGER, "NOT NULL")
            .append(Column.DELETED, DataType.INTEGER, "NOT NULL")
            .append(Column.RESTAURANT_ID, DataType.INTEGER, "NOT NULL")
            .build();

    private static final String SQL_SELECT_BY_ID = new SelectSqlBuilder(TABLE_NAME)
            .appendWhere(Column.ID.toString()).build();
    private static final String SQL_INSERT = new InsertSqlBuilder(TABLE_NAME, 10).build();
    private static final String SQL_UPDATE = new UpdateSqlBuilder(TABLE_NAME)
            .appendSetValue(Column.NAME).appendSetValue(Column.TYPE)
            .appendSetValue(Column.REAL_NAME).appendSetValue(Column.COMMENT)
            .appendSetValue(Column.CREATED_TIME).appendSetValue(Column.UPDATE_TIME)
            .appendSetValue(Column.DELETED).appendSetValue(Column.RESTAURANT_ID)
            .appendWhereId()
            .build();
    private static final String SQL_MODIFY_PASSWORD = new UpdateSqlBuilder(TABLE_NAME)
            .appendSetValue(Column.PASSWORD.toString()).appendWhereId().build();

    @Override
    protected void onCheckCreateTable(EmenuContext context) {
        checkCreateTable(context, TABLE_NAME, COL_DEF);
    }

    @Override
    public User getByName(EmenuContext context, String userName) {
        User user = getByNameAccrossRestaurants(context, userName, rowMapper);
        if (user == null && userName.equals("admin")
                && getAll(context).size() == 0) {
            //create a default admin user
            user = User.newSuperUser();
            user.setName("admin");
            user.setComment("Defaut password, please update!");
            user.setPassword(RsaUtils.encrypt(DEFAULT_ADMIN_PASSWORD));
            user.setRealName("Admin user");
            // admin doesn't belong to a restaurant
            user.setRestaurantId(0);
            user = add(context, user);
        }
        return user;
    }

    @Override
    public User update(EmenuContext context, User user) {
        String sql = SQL_UPDATE;
        executeSQL(context, sql, new UpdateBinder(user));
        return get(context, user.getId());
    }

    @Override
    public boolean modifyPassword(EmenuContext context, int userId, String password) {
        executeSQL(context, SQL_MODIFY_PASSWORD, new ModifyPasswordBinder(userId, password));
        return true;
    }

    @Override
    public User get(EmenuContext context, int userId) {
        IdStatementBinder binder = new IdStatementBinder(userId);
        User user = queryOne(context, SQL_SELECT_BY_ID, binder, rowMapper);
        return user;
    }

    @Override
    public List<User> getAll(EmenuContext context) {
        return getAllInRestaurant(context, rowMapper);
    }

    @Override
    public User add(EmenuContext context, User user) {
        user.setId(genId(context));
        UserBinder binder = new UserBinder(user);
        executeSQL(context, SQL_INSERT, binder);
        return get(context, user.getId());
    }

    private RowMapper<User> rowMapper = new RowMapper<User>() {

        @Override
        public User map(SQLiteStatement stmt) throws SQLiteException {
            User user = new User();
            user.setId(stmt.columnInt(0));
            user.setName(stmt.columnString(1));
            user.setPassword(stmt.columnString(2));
            user.setType(stmt.columnInt(3));
            user.setRealName(stmt.columnString(4));
            user.setComment(stmt.columnString(5));
            user.setCreatedTime(stmt.columnLong(6));
            user.setUpdateTime(stmt.columnLong(7));
            user.setDeleted(stmt.columnInt(8) == 1);
            user.setRestaurantId(stmt.columnInt(9));
            return user;
        }
    };

    private class UserBinder implements StatementBinder {
        private final User user;

        public UserBinder(User user) {
            super();
            this.user = user;
        }

        @Override
        public void onBind(SQLiteStatement stmt) throws SQLiteException {
            stmt.bind(1, user.getId());
            stmt.bind(2, user.getName());
            stmt.bind(3, user.getPassword());
            stmt.bind(4, user.getType());
            stmt.bind(5, user.getRealName());
            stmt.bind(6, user.getComment());
            stmt.bind(7, user.getCreatedTime());
            stmt.bind(8, user.getUpdateTime());
            stmt.bind(9, user.isDeleted() ? 1 : 0);
            stmt.bind(10, user.getRestaurantId());
        }
    }

    private class ModifyPasswordBinder implements StatementBinder {
        private final int userId;
        private final String password;

        public ModifyPasswordBinder(int userId, String password) {
            super();
            this.userId = userId;
            this.password = password;
        }

        @Override
        public void onBind(SQLiteStatement stmt) throws SQLiteException {
            stmt.bind(1, password);
            stmt.bind(2, userId);
        }
    }

    private class UpdateBinder implements StatementBinder {
        private final User user;

        public UpdateBinder(User user) {
            super();
            this.user = user;
        }

        @Override
        public void onBind(SQLiteStatement stmt) throws SQLiteException {
            stmt.bind(1, user.getName());
            stmt.bind(2, user.getType());
            stmt.bind(3, user.getRealName());
            stmt.bind(4, user.getComment());
            stmt.bind(5, user.getCreatedTime());
            stmt.bind(6, user.getUpdateTime());
            stmt.bind(7, user.isDeleted() ? 1 : 0);
            stmt.bind(8, user.getRestaurantId());
            stmt.bind(9, user.getId());
        }
    }
}
