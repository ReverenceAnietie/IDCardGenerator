package SchoolIDSystem;

import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthService {
    
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
    
    public static boolean registerUser(String username, String password, String email) {
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, hashPassword(password));
            stmt.setString(3, email);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Registration failed: " + e.getMessage());
            return false;
        }
    }
    
    public static User loginUser(String username, String password) {
        String sql = """
            SELECT u.id, u.username, u.email, u.created_at,
                   s.full_name, s.registration_number, s.department, s.faculty,
                   s.phone_number, s.address, s.date_of_birth, s.gender, s.profile_image_path
            FROM users u 
            LEFT JOIN students s ON u.id = s.user_id 
            WHERE LOWER(u.username) = LOWER(?) AND u.password = ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, hashPassword(password));
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                user.setFullName(rs.getString("full_name"));
                user.setRegistrationNumber(rs.getString("registration_number"));
                user.setDepartment(rs.getString("department"));
                user.setFaculty(rs.getString("faculty"));
                user.setPhoneNumber(rs.getString("phone_number"));
                user.setAddress(rs.getString("address"));
                user.setDateOfBirth(rs.getDate("date_of_birth"));
                user.setGender(rs.getString("gender"));
                user.setProfileImagePath(rs.getString("profile_image_path"));
                return user;
            }
            
        } catch (SQLException e) {
            System.err.println("Login failed: " + e.getMessage());
        }
        
        return null;
    }
    
    public static boolean updateStudentProfile(User user) {
        String checkSql = "SELECT COUNT(*) FROM students WHERE user_id = ?";
        String insertSql = """
            INSERT INTO students (user_id, full_name, registration_number, department, faculty, 
                                phone_number, address, date_of_birth, gender, profile_image_path) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        String updateSql = """
            UPDATE students SET full_name = ?, registration_number = ?, department = ?, faculty = ?,
                              phone_number = ?, address = ?, date_of_birth = ?, gender = ?, profile_image_path = ?
            WHERE user_id = ?
        """;
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check if student record exists
            boolean exists = false;
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, user.getId());
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    exists = rs.getInt(1) > 0;
                }
            }
            
            PreparedStatement stmt;
            if (exists) {
                stmt = conn.prepareStatement(updateSql);
                stmt.setString(1, user.getFullName());
                stmt.setString(2, user.getRegistrationNumber());
                stmt.setString(3, user.getDepartment());
                stmt.setString(4, user.getFaculty());
                stmt.setString(5, user.getPhoneNumber());
                stmt.setString(6, user.getAddress());
                stmt.setDate(7, user.getDateOfBirth() != null ? new java.sql.Date(user.getDateOfBirth().getTime()) : null);
                stmt.setString(8, user.getGender());
                stmt.setString(9, user.getProfileImagePath());
                stmt.setInt(10, user.getId());
            } else {
                stmt = conn.prepareStatement(insertSql);
                stmt.setInt(1, user.getId());
                stmt.setString(2, user.getFullName());
                stmt.setString(3, user.getRegistrationNumber());
                stmt.setString(4, user.getDepartment());
                stmt.setString(5, user.getFaculty());
                stmt.setString(6, user.getPhoneNumber());
                stmt.setString(7, user.getAddress());
                stmt.setDate(8, user.getDateOfBirth() != null ? new java.sql.Date(user.getDateOfBirth().getTime()) : null);
                stmt.setString(9, user.getGender());
                stmt.setString(10, user.getProfileImagePath());
            }
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Profile update failed: " + e.getMessage());
            return false;
        }
    }
}