package com.itsupport.ticketsystem.ui;

import javax.swing.*;
import java.awt.*;

public class MainUI extends JFrame {
    private JButton btnLogin;
    private JButton btnExit;

    public MainUI() {
        setTitle("IT Support Ticket System");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lblTitle = new JLabel("Welcome to IT Support System");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(lblTitle, gbc);

        btnLogin = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(btnLogin, gbc);

        btnExit = new JButton("Exit");
        gbc.gridx = 1;
        add(btnExit, gbc);

        btnLogin.addActionListener(e -> new LoginUI());
        btnExit.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainUI::new);
    }
}
