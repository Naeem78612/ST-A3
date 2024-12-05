import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginApp extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;

    // Updated DB URL with database name
    private static final String DB_URL = "jdbc:mysql://localhost:3306/user_database";  // Specify your database name here
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123456";

    public LoginApp() {
        setTitle("Login Screen");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));

        // Email Label and Text Field
        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        // Password Label and Password Field
        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        // Login Button
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginAction());
        panel.add(loginButton);

        add(panel);
    }

    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword()); // Password is ignored for validation

            String userName = authenticateUser(email, password);
            if (userName != null) {
                JOptionPane.showMessageDialog(null, "Welcome, " + userName + "!", "Login Successful", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "User not found or incorrect password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

//
public String authenticateUser(String email, String password) {
    String userName = null;
    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
        System.out.println("Authenticating email: " + email);
        String query = "SELECT name FROM users WHERE email = ? AND password = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, email);
        stmt.setString(2, password); // Password validation

        // Debugging: print the query
        System.out.println("Executing query: " + query);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            userName = rs.getString("name");
        } else {
            System.out.println("User not found or incorrect password");
        }
        rs.close();
        stmt.close();
    } catch (SQLException ex) {
        System.out.println("SQL Error: " + ex.getMessage());
    } catch (Exception e) {
        e.printStackTrace();
    }
    return userName;
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginApp loginApp = new LoginApp();
            loginApp.setVisible(true);
        });
    }
}