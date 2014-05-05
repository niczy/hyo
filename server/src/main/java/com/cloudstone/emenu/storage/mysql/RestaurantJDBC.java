package com.cloudstone.emenu.storage.mysql;

import com.cloudstone.emenu.EmenuContext;
import com.cloudstone.emenu.data.Restaurant;
import com.cloudstone.emenu.storage.dao.RestaurantDAO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by charliez on 5/2/14.
 */
@Repository
public class RestaurantJDBC extends MysqlDB implements RestaurantDAO {

    private static enum Column {
        NAME("name");

        private Column(final String str) {
            this.str = str;
        }

        private final String str;

        @Override
        public String toString() {
            return str;
        }
    }

    private class RestaurantRowMapper implements RowMapper<Restaurant> {
        public Restaurant mapRow(ResultSet rs, int rowNum) throws SQLException {
            Restaurant rest = new Restaurant();
            Utils.mapRow(rs, rowNum, rest);
            return rest;
        }
    }


    public Restaurant get(EmenuContext context, int id) {
        return queryById(id, new RestaurantRowMapper());
    }

    public Restaurant add(EmenuContext context, Restaurant restaurant) {
        String SQL = "INSERT INTO restaurant (name) VALUES (?)";
        int id = jdbc.updateAndReturnKey(SQL, restaurant.getName());
        return get(context, id);
    }

    public List<Restaurant> getAll(EmenuContext context) {
        return queryAll(new RestaurantRowMapper());
    }

    public Restaurant update(EmenuContext context, Restaurant restaurant) {
        String SQL = "UPDATE restaurant SET name = ? WHERE id= ?";
        jdbc.update(SQL, restaurant.getName(), restaurant.getId());
        return get(context, restaurant.getId());
    }

    @Override
    public String getTableName() {
        return "restaurant";
    }
}
