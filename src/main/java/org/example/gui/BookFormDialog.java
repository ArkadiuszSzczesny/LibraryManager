package org.example.gui;

import org.example.dao.BookDAO;
import org.example.models.Book;

import javax.swing.*;
import java.awt.*;

public class BookFormDialog extends JDialog {

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField titleField;
    private JTextField isbnField;
    private JTextField libraryIdField;
    private JSpinner quantitySpinner;
    private BookDAO bookDAO;
    private Book bookToEdit;

    public BookFormDialog(JFrame parent, Book bookToEdit) {

        super(parent, bookToEdit == null ? "Dodaj książkę" : "Edytuj książkę", true);
        this.bookToEdit = bookToEdit;
        this.bookDAO = new BookDAO();

        setSize(400, 400);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Imię autora:"), gbc);

        gbc.gridx = 1;
        firstNameField = new JTextField(20);
        add(firstNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Nazwisko autora:"), gbc);

        gbc.gridx = 1;
        lastNameField = new JTextField(20);
        add(lastNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Tytuł:"), gbc);

        gbc.gridx = 1;
        titleField = new JTextField(20);
        add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("ISBN:"), gbc);

        gbc.gridx = 1;
        isbnField = new JTextField(20);
        add(isbnField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("ID biblioteki:"), gbc);

        gbc.gridx = 1;
        libraryIdField = new JTextField(20);
        add(libraryIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Ilość egzemplarzy:"), gbc);

        gbc.gridx = 1;
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        add(quantitySpinner, gbc);

        gbc.gridx = 1;
        gbc.gridy++;
        JButton saveButton = new JButton(bookToEdit == null ? "Dodaj" : "Zapisz zmiany");
        add(saveButton, gbc);

        if (bookToEdit != null) {
            fillFormWithBookData();
        }

        saveButton.addActionListener(e -> saveBook());
    }

    private void fillFormWithBookData() {

        firstNameField.setText(bookToEdit.getAuthorFirstName());
        lastNameField.setText(bookToEdit.getAuthorLastName());
        titleField.setText(bookToEdit.getTitle());
        isbnField.setText(bookToEdit.getIsbn());
        libraryIdField.setText(bookToEdit.getLibraryId());
        quantitySpinner.setValue(bookToEdit.getQuantity());
    }

    private void saveBook() {

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String title = titleField.getText();
        String isbn = isbnField.getText();
        String libraryId = libraryIdField.getText();
        int quantity = (int) quantitySpinner.getValue();

        if (firstName.isEmpty() || lastName.isEmpty() || title.isEmpty() ||
                isbn.isEmpty() || libraryId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Wypełnij wszystkie pola.", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success;
        if (bookToEdit == null) {
            Book book = new Book(firstName, lastName, title, isbn, libraryId, quantity);
            success = bookDAO.addBook(book);
        } else {
            bookToEdit.setAuthorFirstName(firstName);
            bookToEdit.setAuthorLastName(lastName);
            bookToEdit.setTitle(title);
            bookToEdit.setIsbn(isbn);
            bookToEdit.setLibraryId(libraryId);
            bookToEdit.setQuantity(quantity);
            success = bookDAO.updateBook(bookToEdit);
        }

        if (success) {
            JOptionPane.showMessageDialog(this, "Książka zapisana pomyślnie.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Błąd podczas zapisu.", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }
}
