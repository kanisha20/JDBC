package session3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class session03 {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/music_db";
        String username = "root";
        String password = "";

        String sql = "INSERT INTO playlist_songs (song_id, title, artist) VALUES (?, ?, ?)";

        try (
            Connection con = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, 3);
            ps.setString(2, "Shape of You");
            ps.setString(3, "Ed Sheeran");

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Song added successfully!");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
