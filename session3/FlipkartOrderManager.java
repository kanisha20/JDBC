package session3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FlipkartOrderManager {

    public static void updateOrderStatus(int orderId, String status) {

        String url = "jdbc:mysql://localhost:3306/flipkart_db";
        String username = "root";
        String password = "your_mysql_password";

        String sql = "UPDATE orders SET status = ? WHERE order_id = ?";

        try (
            Connection con = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, status);
            ps.setInt(2, orderId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Order status updated successfully!");
            } else {
                System.out.println("Order ID not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        updateOrderStatus(101, "Shipped");
    }
}
