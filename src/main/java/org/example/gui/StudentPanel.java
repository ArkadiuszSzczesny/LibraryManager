package org.example.gui;

import org.example.dao.BookDAO;
import org.example.dao.LoanDAO;
import org.example.models.Book;
import org.example.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentPanel extends JFrame {

    private JTable bookTable;
    private DefaultTableModel tableModel;
    private BookDAO bookDAO;
    private LoanDAO loanDAO;
    private User loggedUser;

    public StudentPanel(User user) {

        this.loggedUser = user;
        this.bookDAO = new BookDAO();
        this.loanDAO = new LoanDAO();

        setTitle("Panel Studenta – Lista Książek");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTextField searchField = new JTextField(30);
        JButton searchButton = new JButton("Szukaj");
        JButton resetButton = new JButton("Wyczyść");

        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("Szukaj:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(resetButton);

        add(searchPanel, BorderLayout.NORTH);

        String[] columns = {
                "ID", "Imię autora", "Nazwisko autora", "Tytuł","ISBN", "Ilość"
        };
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {

                return false;
            }
        };
        bookTable = new JTable(tableModel);
        add(new JScrollPane(bookTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton borrowButton = new JButton("Wypożycz");
        JButton returnButton = new JButton("Zwróć książkę");
        JButton myLoansButton = new JButton("Moje wypożyczenia");
        JButton logoutButton = new JButton("Wyloguj");

        buttonPanel.add(borrowButton);
        buttonPanel.add(returnButton);
        buttonPanel.add(myLoansButton);
        buttonPanel.add(logoutButton);

        add(buttonPanel, BorderLayout.SOUTH);

        loadBooks();

        searchButton.addActionListener(e -> {
            String query = searchField.getText().trim();
            searchBooks(query);
        });

        resetButton.addActionListener(e -> {
            searchField.setText("");
            loadBooks();
        });

        borrowButton.addActionListener(e -> borrowBook());
        returnButton.addActionListener(e -> returnBook());
        myLoansButton.addActionListener(e -> new LoanHistoryDialog(this, loggedUser.getId()).setVisible(true));
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Czy na pewno chcesz się wylogować?",
                    "Potwierdzenie wylogowania",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginWindow().setVisible(true);
            }
        });


    }

    private void loadBooks() {

        tableModel.setRowCount(0);
        for (Book book : bookDAO.getAllBooks()) {
            tableModel.addRow(new Object[]{
                    book.getId(),
                    book.getAuthorFirstName(),
                    book.getAuthorLastName(),
                    book.getTitle(),
                    book.getIsbn(),
                    book.getQuantity()
            });
        }
    }

    private void searchBooks(String query) {

        tableModel.setRowCount(0);

        for (Book book : bookDAO.getAllBooks()) {
            if (book.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    book.getAuthorFirstName().toLowerCase().contains(query.toLowerCase()) ||
                    book.getAuthorLastName().toLowerCase().contains(query.toLowerCase())) {

                tableModel.addRow(new Object[]{
                        book.getId(),
                        book.getAuthorFirstName(),
                        book.getAuthorLastName(),
                        book.getTitle(),
                        book.getIsbn(),
                        book.getQuantity()
                });
            }
        }
    }

    private void borrowBook() {

        int row = bookTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Wybierz książkę do wypożyczenia.");
            return;
        }

        int bookId = (int) tableModel.getValueAt(row, 0);
        int quantity = (int) tableModel.getValueAt(row, 5);

        if (quantity <= 0) {
            JOptionPane.showMessageDialog(this, "Brak dostępnych egzemplarzy.");
            return;
        }

        if (loanDAO.hasActiveLoan(loggedUser.getId(), bookId)) {
            JOptionPane.showMessageDialog(this,
                    "Masz już wypożyczoną tę książkę!\nZwróć ją, aby móc wypożyczyć ponownie.",
                    "Niedozwolona operacja",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean success = loanDAO.borrowBook(loggedUser.getId(), bookId);
        if (success) {
            JOptionPane.showMessageDialog(this, "Książka została wypożyczona.");
            loadBooks();
        } else {
            JOptionPane.showMessageDialog(this, "Błąd wypożyczania.", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void returnBook() {

        new ReturnDialog(this, loggedUser.getId()).setVisible(true);
        loadBooks();
    }
}
