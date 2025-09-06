package SchoolIDSystem;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;

public class MainDashboard extends JFrame {
    private User currentUser;
    private JTextField fullNameField;
    private JTextField regNumberField;
    private JTextField departmentField;
    private JTextField facultyField;
    private JTextField phoneField;
    private JTextArea addressArea;
    private JTextField dobField;
    private JComboBox<String> genderCombo;
    private JLabel imageLabel;
    private String selectedImagePath;
    
    public MainDashboard(User user) {
        this.currentUser = user;
        setTitle("School ID Card System - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        initComponents();
        loadUserData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setPreferredSize(new Dimension(0, 80));
        
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getUsername() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 0));
        
        JPanel headerButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        headerButtonPanel.setOpaque(false);
        
        JButton aboutButton = new JButton("About");
        aboutButton.setPreferredSize(new Dimension(80, 35));
        aboutButton.addActionListener(e -> showAboutDialog());
        
        JButton generateIdButton = new JButton("Generate ID Card");
        generateIdButton.setPreferredSize(new Dimension(140, 35));
        generateIdButton.setBackground(new Color(41, 128, 185));
        generateIdButton.setForeground(new Color(52, 73, 94));
        generateIdButton.addActionListener(e -> generateIdCard());
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(80, 35));
        logoutButton.setBackground(new Color(41, 128, 185));
        logoutButton.setForeground(new Color(52, 73, 94));
        logoutButton.addActionListener(e -> logout());
        
        headerButtonPanel.add(aboutButton);
        headerButtonPanel.add(generateIdButton);
        headerButtonPanel.add(logoutButton);
        
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(headerButtonPanel, BorderLayout.EAST);
        
        // Main Content Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Profile Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Student Profile Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Left column
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridy++; formPanel.add(new JLabel("Registration Number:"), gbc);
        gbc.gridy++; formPanel.add(new JLabel("Department:"), gbc);
        gbc.gridy++; formPanel.add(new JLabel("Faculty:"), gbc);
        gbc.gridy++; formPanel.add(new JLabel("Phone Number:"), gbc);
        gbc.gridy++; formPanel.add(new JLabel("Date of Birth (DD/MM/YYYY):"), gbc);
        gbc.gridy++; formPanel.add(new JLabel("Gender:"), gbc);
        gbc.gridy++; formPanel.add(new JLabel("Address:"), gbc);
        
        // Middle column - Fields
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        fullNameField = new JTextField(20);
        formPanel.add(fullNameField, gbc);
        
        gbc.gridy++;
        regNumberField = new JTextField(20);
        regNumberField.setToolTipText("Format: 23/sc/co/062");
        formPanel.add(regNumberField, gbc);
        
        gbc.gridy++;
        departmentField = new JTextField(20);
        formPanel.add(departmentField, gbc);
        
        gbc.gridy++;
        facultyField = new JTextField(20);
        formPanel.add(facultyField, gbc);
        
        gbc.gridy++;
        phoneField = new JTextField(20);
        formPanel.add(phoneField, gbc);
        
        gbc.gridy++;
        dobField = new JTextField(20);
        formPanel.add(dobField, gbc);
        
        gbc.gridy++;
        genderCombo = new JComboBox<>(new String[]{"", "Male", "Female", "Other"});
        formPanel.add(genderCombo, gbc);
        
        gbc.gridy++;
        gbc.gridheight = 3;
        addressArea = new JTextArea(3, 20);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        JScrollPane addressScroll = new JScrollPane(addressArea);
        formPanel.add(addressScroll, gbc);
        
        // Right column - Image
        gbc.gridx = 2; gbc.gridy = 0; gbc.gridheight = 8;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBorder(BorderFactory.createTitledBorder("Profile Photo"));
        imagePanel.setPreferredSize(new Dimension(200, 280));
        
        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(150, 180));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setText("No Image");
        
        JButton uploadButton = new JButton("Upload Image");
        uploadButton.addActionListener(new ImageUploadListener());
        
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        imagePanel.add(uploadButton, BorderLayout.SOUTH);
        formPanel.add(imagePanel, gbc);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Save Profile");
        saveButton.setPreferredSize(new Dimension(120, 40));
        saveButton.setBackground(new Color(0, 102, 204));
        saveButton.setForeground(new Color(0, 102, 204));
        saveButton.addActionListener(new SaveProfileListener());
        
        JButton clearButton = new JButton("Clear Fields");
        clearButton.setPreferredSize(new Dimension(120, 40));
        clearButton.setBackground(new Color(149, 165, 166));
        clearButton.setForeground(new Color(149, 165, 166));
        clearButton.addActionListener(e -> clearFields());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(clearButton);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void loadUserData() {
        if (currentUser.getFullName() != null) {
            fullNameField.setText(currentUser.getFullName());
            regNumberField.setText(currentUser.getRegistrationNumber());
            departmentField.setText(currentUser.getDepartment());
            facultyField.setText(currentUser.getFaculty());
            phoneField.setText(currentUser.getPhoneNumber());
            addressArea.setText(currentUser.getAddress());
            
            if (currentUser.getDateOfBirth() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                dobField.setText(sdf.format(currentUser.getDateOfBirth()));
            }
            
            if (currentUser.getGender() != null) {
                genderCombo.setSelectedItem(currentUser.getGender());
            }
            
            if (currentUser.getProfileImagePath() != null && !currentUser.getProfileImagePath().isEmpty()) {
                loadImage(currentUser.getProfileImagePath());
            }
        }
    }
    
    private void loadImage(String imagePath) {
        try {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                BufferedImage img = ImageIO.read(imageFile);
                Image scaledImg = img.getScaledInstance(150, 180, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImg));
                imageLabel.setText("");
                selectedImagePath = imagePath;
            }
        } catch (IOException e) {
            System.err.println("Error loading image: " + e.getMessage());
        }
    }
    
    private class ImageUploadListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif"));
            
            if (fileChooser.showOpenDialog(MainDashboard.this) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                
                try {
                    // Create images directory if it doesn't exist
                    Path imagesDir = Paths.get("images");
                    if (!Files.exists(imagesDir)) {
                        Files.createDirectories(imagesDir);
                    }
                    
                    // Copy file to images directory
                    String fileName = currentUser.getUsername() + "_" + selectedFile.getName();
                    Path targetPath = imagesDir.resolve(fileName);
                    Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                    
                    // Load and display image
                    loadImage(targetPath.toString());
                    
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(MainDashboard.this, 
                        "Error uploading image: " + ex.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private class SaveProfileListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Validation
            if (fullNameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(MainDashboard.this, 
                    "Please enter your full name", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (regNumberField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(MainDashboard.this, 
                    "Please enter your registration number", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Update user object
            currentUser.setFullName(fullNameField.getText().trim());
            currentUser.setRegistrationNumber(regNumberField.getText().trim());
            currentUser.setDepartment(departmentField.getText().trim());
            currentUser.setFaculty(facultyField.getText().trim());
            currentUser.setPhoneNumber(phoneField.getText().trim());
            currentUser.setAddress(addressArea.getText().trim());
            currentUser.setGender((String) genderCombo.getSelectedItem());
            currentUser.setProfileImagePath(selectedImagePath);
            
            // Parse date of birth
            if (!dobField.getText().trim().isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date dob = sdf.parse(dobField.getText().trim());
                    currentUser.setDateOfBirth(dob);
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(MainDashboard.this, 
                        "Invalid date format. Please use DD/MM/YYYY", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            // Save to database
            if (AuthService.updateStudentProfile(currentUser)) {
                JOptionPane.showMessageDialog(MainDashboard.this, 
                    "Profile saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(MainDashboard.this, 
                    "Error saving profile. Please try again.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void clearFields() {
        fullNameField.setText("");
        regNumberField.setText("");
        departmentField.setText("");
        facultyField.setText("");
        phoneField.setText("");
        addressArea.setText("");
        dobField.setText("");
        genderCombo.setSelectedIndex(0);
        imageLabel.setIcon(null);
        imageLabel.setText("No Image");
        selectedImagePath = null;
    }
    
    private void generateIdCard() {
        if (currentUser.getFullName() == null || currentUser.getFullName().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please complete your profile before generating ID card", 
                "Profile Incomplete", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        new IDCardGenerator(currentUser).setVisible(true);
    }
    
    private void showAboutDialog() {
        String aboutText = """
            School ID Card Generator System
            
            This application allows students to:
            • Create and manage their profiles
            • Upload profile photos
            • Generate professional ID cards
            
            Features:
            • Secure user authentication
            • Profile management with image upload
            • Professional ID card generation
            • PostgreSQL database integration
            
            Developed by Your name using Java Swing and PostgreSQL
            """;
        
        JOptionPane.showMessageDialog(this, aboutText, "About", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", 
            "Logout Confirmation", JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            new LoginForm().setVisible(true);
            dispose();
        }
    }
}