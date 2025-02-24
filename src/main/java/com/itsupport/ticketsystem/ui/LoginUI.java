package com.itsupport.ticketsystem.ui;

import com.itsupport.ticketsystem.utils.RestClient;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;

public class LoginUI extends JFrame {
    private static String loggedInUsername;
    private static String loggedInPassword;
    private static String loggedInRole;

    private static Long loggedInUserId;

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginUI() {
        System.out.println("LoginUI Window Opened");
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(e -> {
            System.out.println("Login Button Clicked");
            authenticateUser();
        });

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(new JLabel());
        add(loginButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static String getLoggedInRole() {
        return loggedInRole;
    }


    private void authenticateUser() {

        System.out.println("Login Button Clicked");

        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String jsonPayload = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";
        System.out.println("Sending login request: " + jsonPayload);

        try {
            String response = RestClient.sendPostRequest("api/users/login", jsonPayload);

            System.out.println("Login API Response: " + response);

            try {
                JSONObject jsonResponse = new JSONObject(response);

                if (jsonResponse.getString("status").equals("success")) {
                    loggedInUsername = jsonResponse.getString("username");
                    loggedInUserId = jsonResponse.getLong("id");
                    loggedInRole = jsonResponse.getString("role");

                    JOptionPane.showMessageDialog(this, "Login Successful!");
                    new TicketManagementUI();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (JSONException e) {
                JOptionPane.showMessageDialog(this, "Error parsing server response!", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to server", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static Long getLoggedInUserId() {
        return loggedInUserId;
    }


    public static String getLoggedInUsername() {
        return loggedInUsername;
    }

    public static String getLoggedInPassword() {
        return loggedInPassword;
    }
}
