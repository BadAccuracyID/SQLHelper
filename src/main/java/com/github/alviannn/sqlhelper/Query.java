package com.github.alviannn.sqlhelper;

import com.github.alviannn.sqlhelper.utils.Closer;

import java.sql.*;

/**
 * query handler
 */
public class Query {

    private final String sqlQuery;
    private final SQLHelper helper;

    /**
     * constructor
     *
     * @param sqlQuery the sql query
     */
    public Query(String sqlQuery, SQLHelper helper) {
        this.sqlQuery = sqlQuery;
        this.helper = helper;
    }

    /**
     * executes the query
     *
     * @param params the parameter values
     * @throws SQLException if the query failed to be executed
     */
    public void execute(Object... params) throws SQLException {
        try (Closer closer = new Closer()) {
            Connection conn = helper.isHikari() ? closer.add(helper.getConnection()) : helper.getConnection();
            PreparedStatement statement = closer.add(conn.prepareStatement(sqlQuery));

            for (int i = 0; i < params.length; i++)
                statement.setObject(i + 1, params[i]);

            statement.execute();
        }
    }

    /**
     * executes the query
     *
     * @throws SQLException if the query failed to be executed
     */
    public void execute() throws SQLException {
        try (Closer closer = new Closer()) {
            Connection conn = helper.isHikari() ? closer.add(helper.getConnection()) : helper.getConnection();
            Statement statement = closer.add(conn.createStatement());

            statement.execute(sqlQuery);
        }
    }

    /**
     * fetches the SQL results, this can be used to fetch ResultSet too
     *
     * @param params the parameter values
     * @return the SQL results
     * @throws SQLException if the query failed to be executed or failed to fetch the SQL results
     */
    public Results getResults(Object... params) throws SQLException {
        Connection connection = helper.getConnection();
        PreparedStatement statement = connection.prepareStatement(sqlQuery);

        for (int i = 0; i < params.length; i++)
            statement.setObject(i + 1, params[i]);

        ResultSet set = statement.executeQuery();
        return new Results(connection, statement, set, helper.isHikari());
    }

    /**
     * fetches the SQL results, this can be used to fetch ResultSet too
     *
     * @return the SQL results
     * @throws SQLException if the query failed to be executed or failed to fetch the SQL results
     */
    public Results getResults() throws SQLException {
        Connection connection = helper.getConnection();
        Statement statement = connection.createStatement();

        ResultSet set = statement.executeQuery(sqlQuery);
        return new Results(connection, statement, set, helper.isHikari());
    }

    /**
     * fetches the SQL results, this can be used to fetch ResultSet too
     *
     * @return the SQL results
     * @throws SQLException if the query failed to be executed or failed to fetch the SQL results
     */
    public Results results(Object... params) throws SQLException {
        return this.getResults(params);
    }

    /**
     * fetches the SQL results, this can be used to fetch ResultSet too
     *
     * @return the SQL results
     * @throws SQLException if the query failed to be executed or failed to fetch the SQL results
     */
    public Results results() throws SQLException {
        return this.getResults();
    }

}
