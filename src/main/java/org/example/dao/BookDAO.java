package org.example.dao;

import org.example.database.Database;
import org.example.models.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public List<Book> getAllBooks() {

        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("id"),
                        rs.getString("author_firstname"),
                        rs.getString("author_lastname"),
                        rs.getString("title"),
                        rs.getString("isbn"),
                        rs.getString("library_id"),
                        rs.getInt("quantity")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    public Book getBookById(int id) {

        String sql = "SELECT * FROM books WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Book(
                        rs.getInt("id"),
                        rs.getString("author_firstname"),
                        rs.getString("author_lastname"),
                        rs.getString("title"),
                        rs.getString("isbn"),
                        rs.getString("library_id"),
                        rs.getInt("quantity")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean addBook(Book book) {

        String sql = "INSERT INTO books (author_firstname, author_lastname, title, isbn, library_id, quantity) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getAuthorFirstName());
            stmt.setString(2, book.getAuthorLastName());
            stmt.setString(3, book.getTitle());
            stmt.setString(4, book.getIsbn());
            stmt.setString(5, book.getLibraryId());
            stmt.setInt(6, book.getQuantity());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateBook(Book book) {

        String sql = "UPDATE books SET author_firstname = ?, author_lastname = ?, title = ?, isbn = ?, library_id = ?, quantity = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getAuthorFirstName());
            stmt.setString(2, book.getAuthorLastName());
            stmt.setString(3, book.getTitle());
            stmt.setString(4, book.getIsbn());
            stmt.setString(5, book.getLibraryId());
            stmt.setInt(6, book.getQuantity());
            stmt.setInt(7, book.getId());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteBook(int id) {

        String sql = "DELETE FROM books WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
