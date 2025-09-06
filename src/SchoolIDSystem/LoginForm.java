package SchoolIDSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private User currentUser;
    
    public LoginForm() {
        setTitle("School ID Card System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        
        initComponents();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setPreferredSize(new Dimension(400, 80));
        JLabel titleLabel = new JLabel("School ID Card System", SwingConstants.CENTER);
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
        usernameField.setPreferredSize(new Dimension(250, 30));
        mainPanel.add(usernameField, gbc);
        
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 0, 5, 0);
        mainPanel.add(new JLabel("Password:"), gbc);
        
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 20, 0);
        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(250, 30));
        mainPanel.add(passwordField, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 35));
        loginButton.setBackground(new Color(0, 102, 204));
        loginButton.setForeground(new Color(0, 102, 204));
        loginButton.addActionListener(new LoginActionListener());
        
        JButton signupButton = new JButton("Sign Up");
        signupButton.setPreferredSize(new Dimension(100, 35));
        signupButton.setBackground(new Color(0, 102, 204));
        signupButton.setForeground(new Color(0, 102, 204));
        signupButton.addActionListener(e -> {
            new SignupForm().setVisible(true);
            dispose();
        });
        
        buttonPanel.add(loginButton);
        buttonPanel.add(signupButton);
        
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(buttonPanel, gbc);
        
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        
        pack();
        
        getRootPane().setDefaultButton(loginButton);
    }
    
    private class LoginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(LoginForm.this, 
                    "Please enter both username and password", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            User user = AuthService.loginUser(username, password);
            if (user != null) {
                currentUser = user;
                JOptionPane.showMessageDialog(LoginForm.this, 
                    "Login successful! Welcome, " + username, 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
                new MainDashboard(user).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(LoginForm.this, 
                    "Invalid username or password", 
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
            }
        }
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
}