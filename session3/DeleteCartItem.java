package session3;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteCartItem {

    public static void deleteCartItem(int cartItemId) {

        String url = "jdbc:mysql://localhost:3306/flipkart_db";
        String username = "root";
        String password = "your_mysql_password";

        String sql = "DELETE FROM cart_items WHERE cart_item_id = ?";

        try (
            Connection con = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, cartItemId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Cart item deleted successfully!");
            } else {
                System.out.println("Cart item not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        deleteCartItem(101);
    }
}
