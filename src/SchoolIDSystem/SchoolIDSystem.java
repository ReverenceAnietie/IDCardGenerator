package SchoolIDSystem;

import javax.swing.*;

public class SchoolIDSystem {
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }
        
        // Initialize database
        DatabaseConnection.initializeDatabase();
        
        // Start the application
        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}