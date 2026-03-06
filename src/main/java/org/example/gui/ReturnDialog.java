package org.example.gui;

import org.example.dao.BookDAO;
import org.example.dao.LoanDAO;
import org.example.models.Book;
import org.example.models.Loan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReturnDialog extends JDialog {

    private JTable loanTable;
    private DefaultTableModel tableModel;
    private LoanDAO loanDAO;
    private BookDAO bookDAO;
    private int userId;

    public ReturnDialog(JFrame parent, int userId) {

        super(parent, "Zwróć książkę", true);
        this.userId = userId;
        this.loanDAO = new LoanDAO();
        this.bookDAO = new BookDAO();

        setSize(700, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        String[] columns = {"ID Wypożyczenia", "Tytuł", "Data wypożyczenia"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {

                return false;
            }
        };

        loanTable = new JTable(tableModel);
        loanTable.getTableHeader().setReorderingAllowed(false);

        loanTable.getColumnModel().getColumn(0).setMinWidth(0);
        loanTable.getColumnModel().getColumn(0).setMaxWidth(0);
        loanTable.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scrollPane = new JScrollPane(loanTable);
        add(scrollPane, BorderLayout.CENTER);

        JButton returnButton = new JButton("Zwróć wybraną książkę");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(returnButton);
        add(buttonPanel, BorderLayout.SOUTH);

        returnButton.addActionListener(e -> returnSelectedBook());

        loadCurrentLoans();
    }

    private void loadCurrentLoans() {

        tableModel.setRowCount(0);

        List<Loan> activeLoans = loanDAO.getLoansForUser(userId)
                .stream()
                .filter(loan -> loan.getReturnDate() == null)
                .toList();

        Map<Integer, String> bookTitlesCache = new HashMap<>();

        for (Loan loan : activeLoans) {
            String title = bookTitlesCache.computeIfAbsent(loan.getBookId(), id -> {
                Book book = bookDAO.getBookById(id);
                return book != null ? book.getTitle() : "Nieznana książka";
            });

            tableModel.addRow(new Object[]{
                    loan.getId(),
                    title,
                    loan.getLoanDate().toString()
            });
        }
    }

    private void returnSelectedBook() {

        int row = loanTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Wybierz wypożyczenie do zwrotu.");
            return;
        }

        int loanId = (int) tableModel.getValueAt(row, 0);

        boolean success = loanDAO.returnBook(loanId);
        if (success) {
            JOptionPane.showMessageDialog(this, "Książka została zwrócona.");
            loadCurrentLoans();
        } else {
            JOptionPane.showMessageDialog(this, "Nie udało się zwrócić książki.", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }
}
