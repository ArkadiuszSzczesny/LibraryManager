package org.example.dao;

import org.example.database.Database;
import org.example.models.Loan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {

    public boolean hasActiveLoan(int userId, int bookId) {

        String sql = "SELECT 1 FROM loans WHERE user_id = ? AND book_id = ? AND return_date IS NULL";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean borrowBook(int userId, int bookId) {

        if (hasActiveLoan(userId, bookId)) {
            return false;
        }

        String insertLoan = "INSERT INTO loans (user_id, book_id, loan_date) VALUES (?, ?, ?)";
        String updateQuantity = "UPDATE books SET quantity = quantity - 1 WHERE id = ? AND quantity > 0";

        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement loanStmt = conn.prepareStatement(insertLoan);
                    PreparedStatement bookStmt = conn.prepareStatement(updateQuantity)
            ) {

                bookStmt.setInt(1, bookId);
                int updated = bookStmt.executeUpdate();

                if (updated == 0) {
                    conn.rollback();
                    return false;
                }

                loanStmt.setInt(1, userId);
                loanStmt.setInt(2, bookId);
                loanStmt.setString(3, LocalDate.now().toString());
                loanStmt.executeUpdate();

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean returnBook(int loanId) {

        String updateLoan = "UPDATE loans SET return_date = ? WHERE id = ? AND return_date IS NULL";
        String updateQuantity = """
            UPDATE books SET quantity = quantity + 1
            WHERE id = (SELECT book_id FROM loans WHERE id = ?)
        """;

        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement returnStmt = conn.prepareStatement(updateLoan);
                    PreparedStatement updateBookStmt = conn.prepareStatement(updateQuantity)
            ) {
                returnStmt.setString(1, LocalDate.now().toString());
                returnStmt.setInt(2, loanId);

                int updated = returnStmt.executeUpdate();
                if (updated == 0) {
                    conn.rollback();
                    return false;
                }

                updateBookStmt.setInt(1, loanId);
                updateBookStmt.executeUpdate();

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Loan> getLoansForUser(int userId) {

        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans WHERE user_id = ? ORDER BY loan_date DESC";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Loan loan = new Loan(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("book_id"),
                        LocalDate.parse(rs.getString("loan_date")),
                        rs.getString("return_date") != null ? LocalDate.parse(rs.getString("return_date")) : null
                );
                loans.add(loan);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return loans;
    }
}
