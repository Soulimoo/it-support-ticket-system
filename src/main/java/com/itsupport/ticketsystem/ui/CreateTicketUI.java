package com.itsupport.ticketsystem.ui;

import com.itsupport.ticketsystem.utils.RestClient;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class CreateTicketUI extends JFrame {
    private JTextField txtTitle;
    private JTextArea txtDescription;
    private JComboBox<String> cmbPriority, cmbCategory;
    private JButton btnSubmit;

    public CreateTicketUI() {
        setTitle("Create New Ticket");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel lblTitle = new JLabel("Title:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(lblTitle, gbc);

        txtTitle = new JTextField(20);
        gbc.gridx = 1;
        add(txtTitle, gbc);

        JLabel lblDescription = new JLabel("Description:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(lblDescription, gbc);

        txtDescription = new JTextArea(5, 20);
        gbc.gridx = 1;
        add(new JScrollPane(txtDescription), gbc);

        JLabel lblPriority = new JLabel("Priority:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(lblPriority, gbc);

        cmbPriority = new JComboBox<>(new String[]{"Low", "Medium", "High"});
        gbc.gridx = 1;
        add(cmbPriority, gbc);

        JLabel lblCategory = new JLabel("Category:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(lblCategory, gbc);

        cmbCategory = new JComboBox<>(new String[]{"Network", "Hardware", "Software", "Other"});
        gbc.gridx = 1;
        add(cmbCategory, gbc);

        btnSubmit = new JButton("Submit");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(btnSubmit, gbc);

        btnSubmit.addActionListener(e -> submitTicket());

        setVisible(true);
    }

    private void submitTicket() {
        String title = txtTitle.getText().trim();
        String description = txtDescription.getText().trim().replaceAll("[\t\n\r]", " ");  // ✅ Remove tabs/newlines
        String priority = ((String) cmbPriority.getSelectedItem()).toUpperCase();
        String category = ((String) cmbCategory.getSelectedItem()).toUpperCase();

        if (title.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String jsonData = String.format(
                "{\"title\": \"%s\", \"description\": \"%s\", \"priority\": \"%s\", \"category\": \"%s\", \"status\": \"NEW\", \"user\": {\"id\": %d}}",
                title, description, priority, category, LoginUI.getLoggedInUserId()
        );

        System.out.println("Final Ticket Payload: " + jsonData);

        try {
            System.out.println("Sending POST request to /api/tickets with data: " + jsonData);
            String response = RestClient.sendPostRequest("api/tickets", jsonData);
            System.out.println("Create Ticket Response: " + response);

            JOptionPane.showMessageDialog(this, "Ticket created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error creating ticket!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
