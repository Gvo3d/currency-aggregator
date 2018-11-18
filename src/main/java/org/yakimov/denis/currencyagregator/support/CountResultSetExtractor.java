package org.yakimov.denis.currencyagregator.support;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CountResultSetExtractor implements ResultSetExtractor<Integer> {
    @Override
    public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
        if (rs.next()) {
            return rs.getInt(1);
        } else {
            return 0;
        }
    }
}
