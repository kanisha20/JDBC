
package ass03;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class session02 {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/spotify_clone";
        String username = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                url, username, password
            );

            System.out.println("Connection Successful");

            con.close();

        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver Not Found");
            System.out.println("Error: " + e.getMessage());

        } catch (SQLException e) {
            System.out.println("Database Connection Failed");
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
}
