package sess06;


import java.sql.*;

public class ProductMetadata {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/shopdb";
        String user = "root";
        String password = "";

        String sql = "SELECT * FROM products";

        try (Connection con =
                 DriverManager.getConnection(url, user, password);
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            ResultSetMetaData metadata = rs.getMetaData();

            int columnCount = metadata.getColumnCount();

            System.out.printf("%-20s %-20s%n",
                              "Column Name", "Data Type");
            System.out.println("----------------------------------------");

            for (int i = 1; i <= columnCount; i++) {
                System.out.printf("%-20s %-20s%n",
                    metadata.getColumnName(i),
                    metadata.getColumnTypeName(i));
            }

        } catch (SQLException e) {
            System.out.println(
                "Unable to retrieve product table information.");
        }
    }
}
