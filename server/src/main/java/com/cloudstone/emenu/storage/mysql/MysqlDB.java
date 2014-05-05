package com.cloudstone.emenu.storage.mysql;

import com.cloudstone.emenu.EmenuContext;
import com.cloudstone.emenu.data.Restaurant;
import com.cloudstone.emenu.storage.dao.IDb;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by charliez on 5/2/14.
 */
public abstract class MysqlDB implements IDb {

    protected String TABLE_NAME = null;

    protected DataSource dataSource;

    protected JdbcTemplateWrapper jdbc;

    public class JdbcTemplateWrapper extends JdbcTemplate {

        public JdbcTemplateWrapper(DataSource dataSource) {
          super(dataSource);
        }

        public int updateAndReturnKey(final String sql, final Object... args) throws DataAccessException {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps =
                                connection.prepareStatement(sql, new String[]{"id"});
                        int idx = 1;
                        for (Object o: args) {
                            ps.setObject(idx, o);
                            ++idx;
                        }
                        return ps;
                    }
                },
                keyHolder
            );
            return keyHolder.getKey().intValue();
        }
    }

    protected <T> T queryById(int id, RowMapper<T> mapper) {
        return jdbc.queryForObject(
                "SELECT * FROM " + getTableName() + " WHERE id = ? AND deleted = 0",
                new Object[]{id}, mapper);
    }

    protected <T> List<T> queryAll(RowMapper<T> mapper) {
        return jdbc.query("SELECT * FROM " + getTableName() + " WHERE deleted = 0", mapper);
    }

    protected <T> List<T> queryAllByRestaurantId(int restaurantId, RowMapper<T> mapper) {
        return jdbc.query(
                "SELECT * FROM " + getTableName() + " WHERE deleted = 0 AND restaurant_id = ?",
                new Object[]{restaurantId}, mapper);
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbc = new JdbcTemplateWrapper(dataSource);
    }

    public int getMaxId(EmenuContext context) {
        throw new NotImplementedException();
    }

    public void delete(EmenuContext context, int id) {
        jdbc.update("UPDATE " + getTableName() + " SET deleted = 1 WHERE id = ?", id);
    }

    public void init() {
        throw new NotImplementedException();
    }

    public abstract String getTableName();

    public int count(EmenuContext context) {
        return jdbc.queryForInt("SELECT COUNT(*) FROM " + getTableName() + " WHERE deleted=0");
    }
}
