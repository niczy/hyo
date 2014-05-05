package com.cloudstone.emenu.storage.mysql;

import com.cloudstone.emenu.data.IdName;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by charliez on 5/3/14.
 */
public class Utils {

    public static void mapRow(ResultSet rs, int rowNum, IdName result) throws SQLException {
        result.setName(rs.getString("name"));
        result.setId(rs.getInt("id"));
        result.setCreatedTime(rs.getTimestamp("time_created").getTime());
        result.setUpdateTime(rs.getTimestamp("time_updated").getTime());
    }
}
