package org.example.gui;

import org.example.dao.UserDAO;
import org.example.models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginWindow() {

        setTitle("Logowanie do Biblioteki");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
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

        gbc.gridx = 1;
        gbc.gridy = 2;
        JButton loginButton = new JButton("Zaloguj");
        panel.add(loginButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        JButton registerButton = new JButton("Zarejestruj się");
        panel.add(registerButton, gbc);

        add(panel, BorderLayout.CENTER);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                logIn();
            }
        });

        registerButton.addActionListener(e -> {
            dispose();
            new RegisterWindow().setVisible(true);
        });
    }

    private void logIn() {

        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        UserDAO userDAO = new UserDAO();
        User user = userDAO.authenticate(username, password);

        if (user != null) {
            JOptionPane.showMessageDialog(this, "Zalogowano jako: " + user.getRole());

            dispose();

            if ("admin".equals(user.getRole())) {
                new AdminPanel().setVisible(true);
            } else {
                new StudentPanel(user).setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Nieprawidłowy login lub hasło", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }
}
