package se2203b.assignments.ifinance;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class GroupAdapter {
    Connection connection;
    public GroupAdapter(Connection conn, Boolean reset) throws SQLException {
        connection = conn;

        Statement stmt = connection.createStatement();
        if (reset) {
            try {
                // Remove tables if database tables have been created.
                // This will throw an exception if the tables do not exist
                stmt.execute("DROP TABLE AccountGroups");
            } catch (SQLException ex) {
                // No need to report an error.
                // The table simply did not exist.
            }
        }

        try {
            // Create the table
            stmt.execute("CREATE TABLE AccountGroups ("
                    + "iD INT NOT NULL PRIMARY KEY,"
                    + "fullName VARCHAR(30) NOT NULL,"
                    + "parent INT NOT NULL,"
                    + "element VARCHAR(30) NOT NULL)"
            );
            populateTable();
        } catch (SQLException ex) {
            // No need to report an error.
            // The table exists and may have some data.
        }
    }

    public void populateTable() throws SQLException {
        AccountCategory assetsAC = new AccountCategory("Assets", "Debit");
        AccountCategory liabilitiesAC = new AccountCategory("Liabilities", "Credit");
        AccountCategory incomeAC = new AccountCategory("Income", "Credit");
        AccountCategory expensesAC = new AccountCategory("Expenses", "Debit");

        Group assetsGP = new Group();
        assetsGP.setName("Assets");
        assetsGP.setElement(assetsAC);

        Group liabilitiesGP = new Group();
        liabilitiesGP.setName("Liabilities");
        liabilitiesGP.setElement(liabilitiesAC);

        Group incomeGP = new Group();
        incomeGP.setName("Income");
        incomeGP.setElement(incomeAC);

        Group expensesGP = new Group();
        expensesGP.setName("Expenses");
        expensesGP.setElement(expensesAC);

        Group id1 = new Group(1, "Fixed assets", assetsGP, assetsAC);
        Group id2 = new Group(2, "Investments", assetsGP, assetsAC);
        Group id3 = new Group(3, "Branch/divisions", assetsGP, assetsAC);
        Group id4 = new Group(4, "Cash in hand", assetsGP, assetsAC);
        Group id5 = new Group(5, "Bank accounts", assetsGP, assetsAC);
        Group id6 = new Group(6, "Deposits (assets)", assetsGP, assetsAC);
        Group id7 = new Group(7, "Advances (assets)", assetsGP, assetsAC);

        Group id8 = new Group(8, "Capital account", liabilitiesGP, liabilitiesAC);
        Group id9 = new Group(9, "Long term loans", liabilitiesGP, liabilitiesAC);
        Group id10 = new Group(10, "Current liabilities", liabilitiesGP, liabilitiesAC);
        Group id11 = new Group(11, "Reserves and surplus", liabilitiesGP, liabilitiesAC);

        Group id12 = new Group(12, "Sales account", incomeGP, incomeAC);

        Group id13 = new Group(13, "Purchase account", expensesGP, expensesAC);
        Group id14 = new Group(14, "Expenses (direct)", expensesGP, expensesAC);
        Group id15 = new Group(15, "Expenses (indirect)", expensesGP, expensesAC);

        Group id16 = new Group(16, "Secured loans", id9, liabilitiesAC);
        Group id17 = new Group(17, "Unsecured loans", id9, liabilitiesAC);

        Group id18 = new Group(18, "Duties taxes payable", id10, liabilitiesAC);
        Group id19 = new Group(19, "Provisions", id10, liabilitiesAC);
        Group id20 = new Group(20, "Sundry creditors", id10, liabilitiesAC);
        Group id21 = new Group(21, "Bank od & limits", id10, liabilitiesAC);

        insertGroup(id1);
        insertGroup(id2);
        insertGroup(id3);
        insertGroup(id4);
        insertGroup(id5);
        insertGroup(id6);
        insertGroup(id7);
        insertGroup(id8);
        insertGroup(id9);
        insertGroup(id10);
        insertGroup(id11);
        insertGroup(id12);
        insertGroup(id13);
        insertGroup(id14);
        insertGroup(id15);
        insertGroup(id16);
        insertGroup(id17);
        insertGroup(id18);
        insertGroup(id19);
        insertGroup(id20);
        insertGroup(id21);
    }

    public void insertGroup(Group input) throws SQLException {
        Statement stmt = connection.createStatement();

        if (input.getParent().equals(null))
        {
            stmt.executeUpdate("INSERT INTO AccountGroups (iD, fullName, parent, element) "
                    + "VALUES ("
                    + input.getID() + ", '"
                    + input.getName() + "', "
                    + null + ", '"
                    + input.getElement().getName() + "')"
            );
        }
        else
        {
            stmt.executeUpdate("INSERT INTO AccountGroups (iD, fullName, parent, element) "
                    + "VALUES ("
                    + input.getID() + ", '"
                    + input.getName() + "', "
                    + input.getParent().getID() + ", '"
                    + input.getElement().getName() + "')"
            );
        }
    }

    public void insertGroup(Group input, Group parent) throws SQLException {
        Statement stmt = connection.createStatement();

        stmt.executeUpdate("INSERT INTO AccountGroups (iD, fullName, parent, element) "
                + "VALUES ("
                + input.getID() + ", '"
                + input.getName() + "', "
                + parent.getID() + ", '"
                + input.getElement().getName() + "')"
        );
    }

    public ArrayList<Integer> returnID() throws SQLException {
        Statement stmt = connection.createStatement();
        ArrayList<Integer> output = new ArrayList<>();
        ResultSet result;
        result = stmt.executeQuery("SELECT iD FROM AccountGroups");

        while (result.next())
        {
            output.add(result.getInt(1));
        }

        return output;
    }

    public Group returnGroup(int id) throws SQLException {
        Statement stmt = connection.createStatement();

        AccountCategory assetsAC = new AccountCategory("Assets", "Debit");
        AccountCategory liabilitiesAC = new AccountCategory("Liabilities", "Credit");
        AccountCategory incomeAC = new AccountCategory("Income", "Credit");
        AccountCategory expensesAC = new AccountCategory("Expenses", "Debit");

        ResultSet result;
        Group parentGroup = new Group();
        Group tempGroup = new Group();

        int resultID = 0;
        String name = "";
        int parentID = 0;
        String element = "";

        result = stmt.executeQuery("SELECT * FROM AccountGroups WHERE iD = " + id);

        while (result.next())
        {
            resultID = result.getInt("iD");
            name = result.getString("fullName");
            parentID = result.getInt("parent");
            element = result.getString("element");
        }

        if (parentID > 0)
        {
            parentGroup = returnGroup(parentID);
        }
        if (parentID == 0)
        {
            Group assetsGP = new Group();
            assetsGP.setName("Assets");
            assetsGP.setElement(expensesAC);

            Group liabilitiesGP = new Group();
            liabilitiesGP.setName("Liabilities");
            liabilitiesGP.setElement(expensesAC);

            Group incomeGP = new Group();
            incomeGP.setName("Income");
            incomeGP.setElement(expensesAC);

            Group expensesGP = new Group();
            expensesGP.setName("Expenses");
            expensesGP.setElement(expensesAC);

            if (element.equals("Assets"))
            {
                parentGroup = assetsGP;
            }
            else if (element.equals("Liabilities"))
            {
                parentGroup = liabilitiesGP;
            }
            else if (element.equals("Income"))
            {
                parentGroup = incomeGP;
            }
            else if (element.equals("Expenses"))
            {
                parentGroup = expensesGP;
            }
        }

        tempGroup.setID(resultID);
        tempGroup.setName(name);
        tempGroup.setParent(parentGroup);
        if (element.equals("Assets"))
        {
            tempGroup.setElement(assetsAC);
        }
        else if (element.equals("Liabilities"))
        {
            tempGroup.setElement(liabilitiesAC);
        }
        else if (element.equals("Income"))
        {
            tempGroup.setElement(incomeAC);
        }
        else if (element.equals("Expenses"))
        {
            tempGroup.setElement(expensesAC);
        }
        return tempGroup;
    }

    public int getMax() throws SQLException {
        Statement stmt = connection.createStatement();
        int output = 0;
        ResultSet result;

        result = stmt.executeQuery("SELECT MAX(iD) FROM AccountGroups");

        while (result.next())
        {
            output = result.getInt(1);
        }

        return output + 1;
    }

    public void deleteGroup(int id) throws SQLException {
        Statement stmt = connection.createStatement();

        stmt.executeUpdate("DELETE FROM AccountGroups WHERE iD = " + id);
    }

    public void modifyGroup(int id, String newName) throws SQLException {
        Statement stmt = connection.createStatement();

        stmt.executeUpdate("UPDATE AccountGroups SET fullName = '" + newName + "' WHERE iD = " + id);
    }
}
