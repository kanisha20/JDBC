package sess06;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlaylistFetcher {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/shopdb";
        String user = "root";
        String password = "";

        String sql = "SELECT playlist_name FROM playlists";

        try (Connection con =
                 DriverManager.getConnection(url, user, password);
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("Playlist Names");
            System.out.println("------------------------");

            while (rs.next()) {
                System.out.println(
                    rs.getString("playlist_name"));
            }

        } catch (SQLException e) {
            System.out.println(
                "Unable to fetch playlists. Please check your database connection.");
        }
    }
}

