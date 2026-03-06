package org.example;

import com.formdev.flatlaf.FlatDarkLaf;
import org.example.database.DatabaseSeeder;
import org.example.gui.LoginWindow;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        DatabaseSeeder.seed();

        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Nie udało się zainicjować motywu FlatLaf. Uruchamianie z domyślnym wyglądem.");
        }

        SwingUtilities.invokeLater(() -> {
            LoginWindow loginWindow = new LoginWindow();
            loginWindow.setVisible(true);
        });
    }
}