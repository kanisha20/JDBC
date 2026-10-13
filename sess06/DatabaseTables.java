package sess06;


import java.sql.*;

public class DatabaseTables {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/shopdb";
        String user = "root";
        String password = "";

        try (Connection con =
                 DriverManager.getConnection(url, user, password)) {

            DatabaseMetaData dbmd = con.getMetaData();

            try (ResultSet rs = dbmd.getTables(
                    con.getCatalog(),
                    null,
                    "%",
                    new String[]{"TABLE"})) {

                System.out.println("Tables in shopdb:");
                System.out.println("------------------------");

                while (rs.next()) {
                    System.out.println(
                        rs.getString("TABLE_NAME"));
                }
            }

        } catch (SQLException e) {
            System.out.println(
                "Unable to retrieve database table names.");
        }
    }
}
