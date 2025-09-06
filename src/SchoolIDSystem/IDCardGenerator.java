package SchoolIDSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import javax.imageio.ImageIO;

public class IDCardGenerator extends JFrame implements Printable {
    private User student;
    private JPanel idCardPanel;
    private BufferedImage profileImage;
    
    public IDCardGenerator(User student) {
        this.student = student;
        setTitle("ID Card Generator");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        
        loadProfileImage();
        initComponents();
        setLocationRelativeTo(null);
    }
    
    private void loadProfileImage() {
        if (student.getProfileImagePath() != null && !student.getProfileImagePath().isEmpty()) {
            try {
                File imageFile = new File(student.getProfileImagePath());
                if (imageFile.exists()) {
                    profileImage = ImageIO.read(imageFile);
                }
            } catch (IOException e) {
                System.err.println("Error loading profile image: " + e.getMessage());
            }
        }
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setPreferredSize(new Dimension(500, 60));
        JLabel headerLabel = new JLabel("Student ID Card", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        
        // ID Card Panel
        createIdCardPanel();
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton printButton = new JButton("Print ID Card");
        printButton.setPreferredSize(new Dimension(120, 35));
        printButton.setBackground(new Color(0, 102, 204));
        printButton.setForeground(new Color(0, 102, 204));
        printButton.addActionListener(new PrintActionListener());
        
        JButton saveButton = new JButton("Save as Image");
        saveButton.setPreferredSize(new Dimension(120, 35));
        saveButton.setBackground(new Color(0, 102, 204));
        saveButton.setForeground(new Color(0, 102, 204));
        saveButton.addActionListener(new SaveImageActionListener());
        
        JButton closeButton = new JButton("Close");
        closeButton.setPreferredSize(new Dimension(100, 35));
        closeButton.setBackground(new Color(149, 165, 166));
        closeButton.setForeground(new Color(149, 165, 166));
        closeButton.addActionListener(e -> dispose());
        
        buttonPanel.add(printButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(closeButton);
        
        add(headerPanel, BorderLayout.NORTH);
        add(idCardPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        pack();
    }
    
    private void createIdCardPanel() {
        idCardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawIdCard(g);
            }
        };
        
        idCardPanel.setPreferredSize(new Dimension(400, 250));
        idCardPanel.setBackground(Color.WHITE);
        idCardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20),
            BorderFactory.createLineBorder(Color.BLACK, 2)
        ));
    }
    
    private void drawIdCard(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int cardWidth = 360;
        int cardHeight = 210;
        int x = 20;
        int y = 20;
        
        // Card background
        g2d.setColor(new Color(240, 248, 255));
        g2d.fillRect(x, y, cardWidth, cardHeight);
        
        // Header background
        g2d.setColor(new Color(41, 128, 185));
        g2d.fillRect(x, y, cardWidth, 50);
        
        // School name
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics fm = g2d.getFontMetrics();
        String schoolName = "UNIVERSITY OF UYO";
        int textWidth = fm.stringWidth(schoolName);
        g2d.drawString(schoolName, x + (cardWidth - textWidth) / 2, y + 20);
        
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        fm = g2d.getFontMetrics();
        String subtitle = "STUDENT IDENTIFICATION CARD";
        textWidth = fm.stringWidth(subtitle);
        g2d.drawString(subtitle, x + (cardWidth - textWidth) / 2, y + 40);
        
        // Profile image
        int imageX = x + 20;
        int imageY = y + 70;
        int imageWidth = 80;
        int imageHeight = 100;
        
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(imageX, imageY, imageWidth, imageHeight);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(imageX, imageY, imageWidth, imageHeight);
        
        if (profileImage != null) {
            Image scaledImage = profileImage.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
            g2d.drawImage(scaledImage, imageX, imageY, null);
        } else {
            g2d.setColor(Color.DARK_GRAY);
            g2d.setFont(new Font("Arial", Font.PLAIN, 10));
            fm = g2d.getFontMetrics();
            String noPhoto = "No Photo";
            textWidth = fm.stringWidth(noPhoto);
            g2d.drawString(noPhoto, imageX + (imageWidth - textWidth) / 2, imageY + imageHeight / 2);
        }
        
        // Student information
        int infoX = imageX + imageWidth + 20;
        int infoY = y + 80;
        int lineHeight = 18;
        
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 11));
        
        g2d.drawString("NAME:", infoX, infoY);
        g2d.setFont(new Font("Arial", Font.PLAIN, 11));
        g2d.drawString(student.getFullName() != null ? student.getFullName().toUpperCase() : "", infoX + 50, infoY);
        
        infoY += lineHeight;
        g2d.setFont(new Font("Arial", Font.BOLD, 11));
        g2d.drawString("REG NO:", infoX, infoY);
        g2d.setFont(new Font("Arial", Font.PLAIN, 11));
        g2d.drawString(student.getRegistrationNumber() != null ? student.getRegistrationNumber() : "", infoX + 50, infoY);
        
        infoY += lineHeight;
        g2d.setFont(new Font("Arial", Font.BOLD, 11));
        g2d.drawString("DEPT:", infoX, infoY);
        g2d.setFont(new Font("Arial", Font.PLAIN, 11));
        g2d.drawString(student.getDepartment() != null ? student.getDepartment().toUpperCase() : "", infoX + 50, infoY);
        
        infoY += lineHeight;
        g2d.setFont(new Font("Arial", Font.BOLD, 11));
        g2d.drawString("FACULTY:  ", infoX, infoY);
        g2d.setFont(new Font("Arial", Font.PLAIN, 11));
        g2d.drawString(student.getFaculty() != null ? student.getFaculty().toUpperCase() : "", infoX + 50, infoY);
        
        infoY += lineHeight;
        g2d.setFont(new Font("Arial", Font.BOLD, 11));
        g2d.drawString("PHONE:", infoX, infoY);
        g2d.setFont(new Font("Arial", Font.PLAIN, 11));
        g2d.drawString(student.getPhoneNumber() != null ? student.getPhoneNumber() : "", infoX + 50, infoY);
        
        // Issue date
        infoY += lineHeight + 10;
        g2d.setFont(new Font("Arial", Font.BOLD, 9));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        g2d.drawString("ISSUE DATE: " + sdf.format(new java.util.Date()), infoX, infoY);
        
        // Bottom border line
        g2d.setColor(new Color(41, 128, 185));
        g2d.fillRect(x, y + cardHeight - 10, cardWidth, 10);
        
        // Card border
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, y, cardWidth, cardHeight);
    }
    
    private class PrintActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintable(IDCardGenerator.this);
            
            if (job.printDialog()) {
                try {
                    job.print();
                    JOptionPane.showMessageDialog(IDCardGenerator.this,
                        "ID Card sent to printer successfully!",
                        "Print Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (PrinterException ex) {
                    JOptionPane.showMessageDialog(IDCardGenerator.this,
                        "Print failed: " + ex.getMessage(),
                        "Print Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private class SaveImageActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File(student.getRegistrationNumber() + "_IDCard.png"));
            
            if (fileChooser.showSaveDialog(IDCardGenerator.this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                
                BufferedImage image = new BufferedImage(400, 250, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = image.createGraphics();
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, 400, 250);
                drawIdCard(g2d);
                g2d.dispose();
                
                try {
                    ImageIO.write(image, "PNG", file);
                    JOptionPane.showMessageDialog(IDCardGenerator.this,
                        "ID Card saved as image successfully!",
                        "Save Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(IDCardGenerator.this,
                        "Error saving image: " + ex.getMessage(),
                        "Save Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    @Override
    public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
        if (page > 0) {
            return NO_SUCH_PAGE;
        }
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());
        
        // Scale to fit page
        double scaleX = pf.getImageableWidth() / 400.0;
        double scaleY = pf.getImageableHeight() / 250.0;
        double scale = Math.min(scaleX, scaleY);
        g2d.scale(scale, scale);
        
        drawIdCard(g2d);
        
        return PAGE_EXISTS;
    }
}