package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String DB_URL = "jdbc:sqlite:biblioteka.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            initTables();
        } catch (Exception e) {
            System.err.println("Błąd inicjalizacji bazy danych: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {

        return DriverManager.getConnection(DB_URL);
    }

    private static void initTables() {

        String createUsersTable = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL,
                role TEXT NOT NULL CHECK(role IN ('admin', 'student'))
            );
        """;

        String createBooksTable = """
            CREATE TABLE IF NOT EXISTS books (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                author_firstname TEXT NOT NULL,
                author_lastname TEXT NOT NULL,
                title TEXT NOT NULL,
                isbn TEXT NOT NULL UNIQUE,
                library_id TEXT NOT NULL UNIQUE,
                quantity INTEGER NOT NULL
            );
        """;

        String createLoansTable = """
            CREATE TABLE IF NOT EXISTS loans (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                book_id INTEGER NOT NULL,
                loan_date TEXT NOT NULL,
                return_date TEXT,
                FOREIGN KEY (user_id) REFERENCES users(id),
                FOREIGN KEY (book_id) REFERENCES books(id)
            );
        """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(createUsersTable);
            stmt.execute(createBooksTable);
            stmt.execute(createLoansTable);

        } catch (SQLException e) {
            System.err.println("Błąd przy tworzeniu tabel: " + e.getMessage());
        }
    }
}