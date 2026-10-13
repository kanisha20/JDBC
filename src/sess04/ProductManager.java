package sess04;


import java.sql.*;
import java.util.Scanner;

public class ProductManager {

    static final String URL =
        "jdbc:mysql://localhost:3306/shopdb";
    static final String USER = "root";
    static final String PASSWORD = "";

    public static void main(String[] args) {

        try (Scanner sc = new Scanner(System.in);
             Connection con = DriverManager.getConnection(
                 URL, USER, PASSWORD)) {

            System.out.println("1. Display All Products");
            System.out.println("2. Search by Category");
            System.out.println("3. Pagination");
            System.out.println("4. Sort Products");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    displayAll(con);
                    break;
                case 2:
                    System.out.print("Enter category: ");
                    String category = sc.nextLine();
                    displayCategory(con, category);
                    break;
                case 3:
                    System.out.print("Enter page number: ");
                    int page = sc.nextInt();
                    displayPage(con, page);
                    break;
                case 4:
                    System.out.print("Sort by price or name: ");
                    String sort = sc.nextLine();
                    sortProducts(con, sort);
                    break;
                default:
                    System.out.println("Invalid choice!");
            }

        } catch (SQLException e) {
            System.out.println("Database error: "
                               + e.getMessage());
        }
    }

    static void displayAll(Connection con)
            throws SQLException {

        String sql = "SELECT name, price FROM products";

        try (PreparedStatement ps =
                 con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            printHeader();

            while (rs.next()) {
                System.out.printf("%-20s %10.2f%n",
                    rs.getString("name"),
                    rs.getDouble("price"));
            }
        }
    }

    static void displayCategory(Connection con,
                                String category)
            throws SQLException {

        String sql = "SELECT name, price FROM products "
                   + "WHERE category = ?";

        try (PreparedStatement ps =
                 con.prepareStatement(sql)) {

            ps.setString(1, category);

            try (ResultSet rs = ps.executeQuery()) {
                printHeader();

                while (rs.next()) {
                    System.out.printf("%-20s %10.2f%n",
                        rs.getString("name"),
                        rs.getDouble("price"));
                }
            }
        }
    }

    static void displayPage(Connection con, int page)
            throws SQLException {

        if (page < 1) {
            System.out.println("Invalid page number!");
            return;
        }

        int pageSize = 5;
        int offset = (page - 1) * pageSize;

        String sql = "SELECT name, price FROM products "
                   + "ORDER BY product_id LIMIT ? OFFSET ?";

        try (PreparedStatement ps =
                 con.prepareStatement(sql)) {

            ps.setInt(1, pageSize);
            ps.setInt(2, offset);

            try (ResultSet rs = ps.executeQuery()) {
                printHeader();
                boolean found = false;

                while (rs.next()) {
                    found = true;
                    System.out.printf("%-20s %10.2f%n",
                        rs.getString("name"),
                        rs.getDouble("price"));
                }

                if (!found) {
                    System.out.println("No products found.");
                }
            }
        }
    }

    static void sortProducts(Connection con, String sort)
            throws SQLException {

        String orderBy;

        if (sort.equalsIgnoreCase("price")) {
            orderBy = "price ASC";
        } else if (sort.equalsIgnoreCase("name")) {
            orderBy = "name ASC";
        } else {
            System.out.println("Enter price or name.");
            return;
        }

        String sql = "SELECT name, price FROM products "
                   + "ORDER BY " + orderBy;

        try (PreparedStatement ps =
                 con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            printHeader();

            while (rs.next()) {
                System.out.printf("%-20s %10.2f%n",
                    rs.getString("name"),
                    rs.getDouble("price"));
            }
        }
    }

    static void printHeader() {
        System.out.println("---------------------------------");
        System.out.printf("%-20s %10s%n",
                          "Product Name", "Price");
        System.out.println("---------------------------------");
    }
}

