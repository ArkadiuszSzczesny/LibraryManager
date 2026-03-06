package org.example.database;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class DatabaseSeeder {

    public static void seed() {

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT count(*) FROM users");
            if (rs.next() && rs.getInt(1) == 0) {
                System.out.println("Baza danych jest pusta. Uruchamiam seedowanie...");
                seedUsers(conn);
                seedBooks(conn);
                System.out.println("Seedowanie zakończone sukcesem!");
            }

        } catch (SQLException e) {
            System.err.println("Błąd podczas seedowania bazy: " + e.getMessage());
        }
    }

    private static void seedUsers(Connection conn) throws SQLException {

        String insertUserSql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertUserSql)) {

            pstmt.setString(1, "admin");
            pstmt.setString(2, BCrypt.hashpw("admin", BCrypt.gensalt()));
            pstmt.setString(3, "admin");
            pstmt.addBatch();

            pstmt.setString(1, "student");
            pstmt.setString(2, BCrypt.hashpw("student", BCrypt.gensalt()));
            pstmt.setString(3, "student");
            pstmt.addBatch();

            pstmt.executeBatch();
        }
    }

    private static void seedBooks(Connection conn) throws SQLException {

        String insertBookSql = "INSERT INTO books (author_firstname, author_lastname, title, isbn, library_id, quantity) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertBookSql)) {

            pstmt.setString(1, "Adam");
            pstmt.setString(2, "Mickiewicz");
            pstmt.setString(3, "Pan Tadeusz");
            pstmt.setString(4, "978-83-07-03350-0");
            pstmt.setString(5, "LIB-001");
            pstmt.setInt(6, 5);
            pstmt.addBatch();

            pstmt.setString(1, "Henryk");
            pstmt.setString(2, "Sienkiewicz");
            pstmt.setString(3, "Ogniem i mieczem");
            pstmt.setString(4, "978-83-08-04415-4");
            pstmt.setString(5, "LIB-002");
            pstmt.setInt(6, 3);
            pstmt.addBatch();

            pstmt.setString(1, "Bolesław");
            pstmt.setString(2, "Prus");
            pstmt.setString(3, "Lalka");
            pstmt.setString(4, "978-83-89-39502-0");
            pstmt.setString(5, "LIB-003");
            pstmt.setInt(6, 4);
            pstmt.addBatch();

            pstmt.setString(1, "Stanisław");
            pstmt.setString(2, "Lem");
            pstmt.setString(3, "Solaris");
            pstmt.setString(4, "978-83-08-05353-8");
            pstmt.setString(5, "LIB-004");
            pstmt.setInt(6, 2);
            pstmt.addBatch();

            pstmt.setString(1, "Andrzej");
            pstmt.setString(2, "Sapkowski");
            pstmt.setString(3, "Ostatnie życzenie");
            pstmt.setString(4, "978-83-75-90085-3");
            pstmt.setString(5, "LIB-005");
            pstmt.setInt(6, 7);
            pstmt.addBatch();

            pstmt.executeBatch();
        }
    }
}