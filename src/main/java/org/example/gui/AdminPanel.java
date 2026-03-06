package org.example.gui;

import org.example.dao.BookDAO;
import org.example.models.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminPanel extends JFrame {

    private JTable bookTable;
    private DefaultTableModel tableModel;
    private BookDAO bookDAO;

    public AdminPanel() {

        setTitle("Panel Administratora – Zarządzanie Książkami");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        bookDAO = new BookDAO();

        String[] columnNames = {
                "ID", "Imię autora", "Nazwisko autora", "Tytuł", "ISBN", "ID biblioteki", "Ilość"
        };
        tableModel = new DefaultTableModel(columnNames, 0);
        bookTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);

        JButton addButton = new JButton("Dodaj");
        JButton editButton = new JButton("Edytuj");
        JButton deleteButton = new JButton("Usuń");
        JButton refreshButton = new JButton("Odśwież");
        JButton logoutButton = new JButton("Wyloguj");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(logoutButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadBooks();

        addButton.addActionListener(e -> openBookForm(null));
        editButton.addActionListener(e -> editSelectedBook());
        deleteButton.addActionListener(e -> deleteSelectedBook());
        refreshButton.addActionListener(e -> loadBooks());
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
        List<Book> books = bookDAO.getAllBooks();

        for (Book book : books) {
            tableModel.addRow(new Object[]{
                    book.getId(),
                    book.getAuthorFirstName(),
                    book.getAuthorLastName(),
                    book.getTitle(),
                    book.getIsbn(),
                    book.getLibraryId(),
                    book.getQuantity()
            });
        }
    }

    private void openBookForm(Book bookToEdit) {

        BookFormDialog dialog = new BookFormDialog(this, bookToEdit);
        dialog.setVisible(true);
        loadBooks();
    }

    private void editSelectedBook() {

        int row = bookTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Wybierz książkę do edycji.");
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);
        Book book = bookDAO.getBookById(id);
        openBookForm(book);
    }

    private void deleteSelectedBook() {

        int row = bookTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Wybierz książkę do usunięcia.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Czy na pewno chcesz usunąć książkę?", "Potwierdzenie", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        int id = (int) tableModel.getValueAt(row, 0);
        boolean success = bookDAO.deleteBook(id);

        if (success) {
            JOptionPane.showMessageDialog(this, "Książka usunięta.");
            loadBooks();
        } else {
            JOptionPane.showMessageDialog(this, "Błąd podczas usuwania.", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }
}
