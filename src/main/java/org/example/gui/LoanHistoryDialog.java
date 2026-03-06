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

public class LoanHistoryDialog extends JDialog {

    private JTable loanTable;
    private DefaultTableModel tableModel;
    private LoanDAO loanDAO;
    private BookDAO bookDAO;

    public LoanHistoryDialog(JFrame parent, int userId) {

        super(parent, "Historia wypożyczeń", true);
        setSize(700, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        loanDAO = new LoanDAO();
        bookDAO = new BookDAO();

        String[] columns = {"Tytuł", "Data wypożyczenia", "Data zwrotu"};
        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int col) {

                return false;
            }
        };

        loanTable = new JTable(tableModel);
        loanTable.getTableHeader().setReorderingAllowed(false);
        add(new JScrollPane(loanTable), BorderLayout.CENTER);

        loadLoanHistory(userId);
    }

    private void loadLoanHistory(int userId) {

        tableModel.setRowCount(0);
        List<Loan> loans = loanDAO.getLoansForUser(userId);

        Map<Integer, String> bookTitlesCache = new HashMap<>();

        for (Loan loan : loans) {
            String title = bookTitlesCache.computeIfAbsent(loan.getBookId(), id -> {
                Book book = bookDAO.getBookById(id);
                return book != null ? book.getTitle() : "Nieznana książka";
            });

            tableModel.addRow(new Object[]{
                    title,
                    loan.getLoanDate().toString(),
                    loan.getReturnDate() != null ? loan.getReturnDate().toString() : "NIE ZWRÓCONO"
            });
        }
    }
}
