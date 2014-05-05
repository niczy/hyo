package com.cloudstone.emenu.storage.mysql;

import com.cloudstone.emenu.EmenuContext;
import com.cloudstone.emenu.data.Restaurant;
import com.cloudstone.emenu.data.User;
import com.cloudstone.emenu.storage.dao.IUserDb;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by charliez on 5/3/14.
 */
@Repository
public class UserJDBC extends MysqlDB implements IUserDb {

    private static enum Column {
        NAME("name"), PASSWORD("password"), TYPE("type"), REAL_NAME("real_name"),
        COMMENT("comment"), RESTAURANT_ID("restaurant_id");

        private Column(final String str) {
            this.str = str;
        }

        private final String str;

        @Override
        public String toString() {
            return str;
        }
    }

    private class UserRowMapper implements RowMapper<User> {
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User u = new User();
            Utils.mapRow(rs, rowNum, u);
            u.setType(rs.getInt(Column.TYPE.toString()));
            u.setRealName(rs.getString(Column.REAL_NAME.toString()));
            u.setComment(rs.getString(Column.COMMENT.toString()));
            u.setRestaurantId(rs.getInt(Column.RESTAURANT_ID.toString()));
            u.setPassword(rs.getString(Column.PASSWORD.toString()));
            return u;
        }
    }

    public User getByName(EmenuContext context, String userName) {
        return jdbc.queryForObject("SELECT * FROM " + getTableName() + " WHERE name = ? AND deleted = 0", new Object[]{userName}, new UserRowMapper());
    }

    public User get(EmenuContext context, int userId) {
        return queryById(userId, new UserRowMapper());
    }

    public List<User> getAll(EmenuContext context) {
        return queryAllByRestaurantId(context.getRestaurantId(), new UserRowMapper());
    }

    public User add(EmenuContext context, User user) {
        String SQL = "INSERT INTO " + getTableName() +
                "(name, password, type, real_name, comment, restaurant_id) VALUES (?,?,?,?,?,?)";
        int id = jdbc.updateAndReturnKey(SQL,
                user.getName(),
                user.getPassword(),
                user.getType(),
                user.getRealName(),
                user.getComment(),
                user.getRestaurantId());
        return get(context, id);
    }

    public User update(EmenuContext context, User user) {
        String SQL = "UPDATE " + getTableName() +
                " SET name = ?, password = ?, type = ?, real_name = ?, " +
                "comment = ?," +
                "restaurant_id = ?, deleted = ? WHERE id = ?";
        jdbc.update(SQL,
                user.getName(),
                user.getPassword(),
                user.getType(),
                user.getRealName(),
                user.getComment(),
                user.getRestaurantId(),
                user.isDeleted(),
                user.getId());
        return queryById(user.getId(), new UserRowMapper());
    }

    public boolean modifyPassword(EmenuContext context, int userId, String password) {
        String SQL = "UPDATE " + getTableName() +
                " SET password = ? WHERE id = ?";
        jdbc.update(SQL, password, userId);
        return true;
    }

    @Override
    public String getTableName() {
        return "users";
    }
}
