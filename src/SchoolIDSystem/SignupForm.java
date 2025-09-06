package SchoolIDSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

public class SignupForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField emailField;
    
    public SignupForm() {
        setTitle("School ID Card System - Sign Up");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        
        initComponents();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setPreferredSize(new Dimension(450, 80));
        JLabel titleLabel = new JLabel("Create New Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 0, 5, 0);
        mainPanel.add(new JLabel("Username:"), gbc);
        
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 0);
        usernameField = new JTextField(20);
        usernameField.setPreferredSize(new Dimension(280, 30));
        mainPanel.add(usernameField, gbc);
        
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 0, 5, 0);
        mainPanel.add(new JLabel("Email:"), gbc);
        
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 15, 0);
        emailField = new JTextField(20);
        emailField.setPreferredSize(new Dimension(280, 30));
        mainPanel.add(emailField, gbc);
        
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 0, 5, 0);
        mainPanel.add(new JLabel("Password:"), gbc);
        
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 15, 0);
        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(280, 30));
        mainPanel.add(passwordField, gbc);
        
        gbc.gridy = 6;
        gbc.insets = new Insets(10, 0, 5, 0);
        mainPanel.add(new JLabel("Confirm Password:"), gbc);
        
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 20, 0);
        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setPreferredSize(new Dimension(280, 30));
        mainPanel.add(confirmPasswordField, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton signupButton = new JButton("Sign Up");
        signupButton.setPreferredSize(new Dimension(100, 35));
        signupButton.setBackground(new Color(0, 102, 204));
        signupButton.setForeground(new Color(0, 102, 204)); 
        signupButton.addActionListener(new SignupActionListener());
        
        JButton backButton = new JButton("Back to Login");
        backButton.setPreferredSize(new Dimension(120, 35));
        backButton.setBackground(new Color(0, 102, 204));
        backButton.setForeground(new Color(0, 102, 204));
        backButton.addActionListener(e -> {
            new LoginForm().setVisible(true);
            dispose();
        });
        
        buttonPanel.add(signupButton);
        buttonPanel.add(backButton);
        
        gbc.gridy = 8;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(buttonPanel, gbc);
        
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        
        pack();
        
        getRootPane().setDefaultButton(signupButton);
    }
    
    private class SignupActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            
            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(SignupForm.this, 
                    "Please fill in all fields", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (username.length() < 3) {
                JOptionPane.showMessageDialog(SignupForm.this, 
                    "Username must be at least 3 characters long", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(SignupForm.this, 
                    "Please enter a valid email address", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (password.length() < 6) {
                JOptionPane.showMessageDialog(SignupForm.this, 
                    "Password must be at least 6 characters long", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(SignupForm.this, 
                    "Passwords do not match", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                confirmPasswordField.setText("");
                return;
            }
            
            if (AuthService.registerUser(username, password, email)) {
                JOptionPane.showMessageDialog(SignupForm.this, 
                    "Account created successfully! You can now log in.", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
                new LoginForm().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(SignupForm.this, 
                    "Registration failed. Username or email might already exist.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
}