package sess04;


import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class sess05 {

    static final String URL =
        "jdbc:mysql://localhost:3306/shopdb";
    static final String USER = "root";
    static final String PASSWORD = "your_password";

    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {

        try (Scanner sc = new Scanner(System.in)) {

            System.out.println("1. Insert a playlist song");
            System.out.println("2. Insert multiple movies");
            System.out.println("3. Batch orders with rollback");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            try {
                switch (choice) {

                    case 1:
                        addSong(1, "Perfect", "Ed Sheeran");
                        break;

                    case 2:
                        List<String> movieNames =
                            Arrays.asList(
                                "Avatar",
                                "Titanic",
                                "Inception"
                            );

                        addMultipleMovies(movieNames);
                        break;

                    case 3:
                        addMultipleOrders();
                        break;

                    default:
                        System.out.println("Invalid choice!");
                }

            } catch (SQLException e) {
                System.out.println("Operation failed: "
                                   + e.getMessage());
            }

        }
    }

    static void addSong(int id, String songName, String artist)
            throws SQLException {

        String sql =
            "INSERT INTO playlist (id, song_name, artist) "
            + "VALUES (?, ?, ?)";

        try (Connection con = getConnection()) {

            con.setAutoCommit(false);

            try (PreparedStatement ps =
                     con.prepareStatement(sql)) {

                ps.setInt(1, id);
                ps.setString(2, songName);
                ps.setString(3, artist);

                ps.executeUpdate();

                con.commit();

                System.out.println(
                    "Song inserted and transaction committed!");

            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }

    static void addMultipleMovies(List<String> movieNames)
            throws SQLException {

        String sql =
            "INSERT INTO movies (movie_name) VALUES (?)";

        try (Connection con = getConnection()) {

            con.setAutoCommit(false);

            try (PreparedStatement ps =
                     con.prepareStatement(sql)) {

                for (String movie : movieNames) {
                    ps.setString(1, movie);
                    ps.addBatch();
                }

                int[] results = ps.executeBatch();

                con.commit();

                System.out.println(
                    results.length + " movies inserted successfully!");

            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }
    static void addMultipleOrders() throws SQLException {

        String sql =
            "INSERT INTO orders "
            + "(order_id, product_id, product_name, quantity) "
            + "VALUES (?, ?, ?, ?)";

        try (Connection con = getConnection()) {

            con.setAutoCommit(false);

            try (PreparedStatement ps =
                     con.prepareStatement(sql)) {

                ps.setInt(1, 101);
                ps.setInt(2, 501);
                ps.setString(3, "Keyboard");
                ps.setInt(4, 1);
                ps.addBatch();

                ps.setInt(1, 102);
                ps.setInt(2, 502);
                ps.setString(3, "Mouse");
                ps.setInt(4, 2);
                ps.addBatch();

                ps.setInt(1, 103);
                ps.setInt(2, 501);
                ps.setString(3, "Monitor");
                ps.setInt(4, 1);
                ps.addBatch();

                ps.executeBatch();
                con.commit();

                System.out.println(
                    "All orders inserted successfully!");

            } catch (SQLException e) {

                con.rollback();

                System.out.println(
                    "An order failed. All changes rolled back.");
                System.out.println(
                    "Reason: " + e.getMessage());
            }
        }
    }
}

