package se2203b.assignments.ifinance;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AccountCategoryAdapter {

    Connection connection;
    public AccountCategoryAdapter(Connection conn, Boolean reset) throws SQLException {
        Connection connection = conn;

        Statement stmt = connection.createStatement();
        if (reset) {
            try {
                // Remove tables if database tables have been created.
                // This will throw an exception if the tables do not exist
                stmt.execute("DROP TABLE AccountCategory");
            } catch (SQLException ex) {
                // No need to report an error.
                // The table simply did not exist.
            }
        }

        try {
            // Create the table
            stmt.execute("CREATE TABLE AccountCategory ("
                    + "categoryName VARCHAR(255) NOT NULL,"
                    + "categoryType VARCHAR(255) NOT NULL)"
            );
            populateTable();
        } catch (SQLException ex) {
            // No need to report an error.
            // The table exists and may have some data.
        }
    }

    public void populateTable() throws SQLException {
        insertAccount("Assets", "Debit");
        insertAccount("Liabilities", "Credit");
        insertAccount("Income", "Credit");
        insertAccount("Expenses", "Debit");
    }

    public void insertAccount(String name, String type) throws SQLException {
        Statement stmt = connection.createStatement();

        stmt.executeUpdate("INSERT INTO AccountCategory (categoryName, categoryType) "
                + "VALUES ('"
                + name + "', '"
                + type + "')"
        );
    }
}