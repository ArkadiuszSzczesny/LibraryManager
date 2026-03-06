package org.example.gui;

import org.example.dao.UserDAO;

import javax.swing.*;
import java.awt.*;

public class RegisterWindow extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JRadioButton adminRadio;
    private JRadioButton studentRadio;

    public RegisterWindow() {

        setTitle("Rejestracja");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Login:"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(20);
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Hasło:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        panel.add(passwordField, gbc);


        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Typ konta:"), gbc);

        gbc.gridx = 1;
        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        adminRadio = new JRadioButton("Admin");
        studentRadio = new JRadioButton("Student", true);
        ButtonGroup group = new ButtonGroup();
        group.add(adminRadio);
        group.add(studentRadio);
        rolePanel.add(adminRadio);
        rolePanel.add(studentRadio);
        panel.add(rolePanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        JButton registerButton = new JButton("Zarejestruj");
        panel.add(registerButton, gbc);

        gbc.gridy = 4;
        JButton backButton = new JButton("Powrót do logowania");
        panel.add(backButton, gbc);

        add(panel, BorderLayout.CENTER);

        registerButton.addActionListener(e -> registerUser());
        backButton.addActionListener(e -> {
            dispose();
            new LoginWindow().setVisible(true);
        });
    }

    private void registerUser() {

        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String role = adminRadio.isSelected() ? "admin" : "student";


        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Uzupełnij wszystkie pola", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        UserDAO userDAO = new UserDAO();
        if (userDAO.userExists(username)) {
            JOptionPane.showMessageDialog(this, "Użytkownik już istnieje", "Błąd", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = userDAO.registerUser(username, password, role);
        if (success) {
            JOptionPane.showMessageDialog(this, "Rejestracja zakończona sukcesem");
            dispose();
            new LoginWindow().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Błąd podczas rejestracji", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }
}
