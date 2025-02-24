package com.itsupport.ticketsystem.ui;

import com.itsupport.ticketsystem.utils.RestClient;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class TicketManagementUI extends JFrame {
    private JTable ticketTable;
    private JButton btnCreate, btnUpdate, btnRefresh, btnAddComment, btnViewLogs;
    private String[][] ticketData;
    private String[] columns = {"ID", "Title", "Priority", "Category", "Status"};

    private JTextField txtSearchId;
    private JComboBox<String> cmbStatusFilter;
    private JButton btnSearch, btnFilter, btnReset;

    public TicketManagementUI() {
        setTitle("Ticket Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        boolean isITSupport = LoginUI.getLoggedInRole().equals("IT_SUPPORT");

        ticketData = new String[0][5];
        ticketTable = new JTable(ticketData, columns);
        JScrollPane scrollPane = new JScrollPane(ticketTable);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 0.7;
        gbc.fill = GridBagConstraints.BOTH;
        add(scrollPane, gbc);

        JPanel filterPanel = new JPanel(new GridBagLayout());
        GridBagConstraints filterGbc = new GridBagConstraints();
        filterGbc.insets = new Insets(5, 5, 5, 5);

        txtSearchId = new JTextField(10);
        cmbStatusFilter = new JComboBox<>(new String[]{"All", "NEW", "IN_PROGRESS", "RESOLVED"});
        btnSearch = new JButton("Search by ID");
        btnFilter = new JButton("Filter by Status");
        btnReset = new JButton("Reset");

        filterGbc.gridx = 0;
        filterGbc.gridy = 0;
        filterPanel.add(new JLabel("Ticket ID:"), filterGbc);
        filterGbc.gridx = 1;
        filterPanel.add(txtSearchId, filterGbc);
        filterGbc.gridx = 2;
        filterPanel.add(btnSearch, filterGbc);
        filterGbc.gridx = 3;
        filterPanel.add(new JLabel("Status:"), filterGbc);
        filterGbc.gridx = 4;
        filterPanel.add(cmbStatusFilter, filterGbc);
        filterGbc.gridx = 5;
        filterPanel.add(btnFilter, filterGbc);
        filterGbc.gridx = 6;
        filterPanel.add(btnReset, filterGbc);

        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.weighty = 0.1;
        add(filterPanel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        btnCreate = new JButton("Create Ticket");
        btnUpdate = new JButton("Update Status");
        btnRefresh = new JButton("Refresh List");
        btnAddComment = new JButton("Add Comment");
        btnViewLogs = new JButton("View Logs");

        buttonPanel.add(btnCreate);
        buttonPanel.add(btnRefresh);

        if (isITSupport) {
            buttonPanel.add(btnUpdate);
            buttonPanel.add(btnAddComment);
            buttonPanel.add(btnViewLogs);
        }

        gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.weighty = 0.2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(buttonPanel, gbc);

        btnCreate.addActionListener(e -> new CreateTicketUI());
        btnUpdate.addActionListener(e -> updateTicketStatus());
        btnRefresh.addActionListener(e -> fetchTickets());
        btnAddComment.addActionListener(e -> addCommentToTicket());
        btnViewLogs.addActionListener(e -> viewAuditLogs());
        btnSearch.addActionListener(e -> searchTicketById());
        btnFilter.addActionListener(e -> filterTicketsByStatus());
        btnReset.addActionListener(e -> resetSearchAndFilter());

        fetchTickets();
        setVisible(true);
    }

    private void fetchTickets() {
        try {
            String response = RestClient.sendGetRequest("api/tickets/byUsername?username=" + LoginUI.getLoggedInUsername());

            System.out.println("Tickets API Response: " + response);

            JSONArray ticketsArray = new JSONArray(response);
            ticketData = new String[ticketsArray.length()][5];

            for (int i = 0; i < ticketsArray.length(); i++) {
                JSONObject ticket = ticketsArray.getJSONObject(i);
                ticketData[i][0] = String.valueOf(ticket.getLong("id"));
                ticketData[i][1] = ticket.getString("title");
                ticketData[i][2] = ticket.getString("priority");
                ticketData[i][3] = ticket.getString("category");
                ticketData[i][4] = ticket.getString("status");
            }

            ticketTable.setModel(new javax.swing.table.DefaultTableModel(ticketData, columns));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching tickets!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void updateTicketStatus() {
        int selectedRow = ticketTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a ticket!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String ticketId = ticketData[selectedRow][0];
        String[] statuses = {"New", "In Progress", "Resolved"};
        String newStatus = (String) JOptionPane.showInputDialog(this, "Select new status:", "Update Status",
                JOptionPane.QUESTION_MESSAGE, null, statuses, statuses[0]);

        if (newStatus != null) {
            try {
                String jsonData = String.format(
                        "{\"status\": \"%s\", \"username\": \"%s\"}",
                        newStatus.toUpperCase().replace(" ", "_"),
                        LoginUI.getLoggedInUsername()
                );
                RestClient.sendPatchRequest("api/tickets/" + ticketId + "/status", jsonData);
                JOptionPane.showMessageDialog(this, "Status updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                fetchTickets();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error updating status!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addCommentToTicket() {
        int selectedRow = ticketTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a ticket!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String ticketId = ticketData[selectedRow][0];
        String comment = JOptionPane.showInputDialog(this, "Enter your comment:");

        if (comment == null || comment.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Comment cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String jsonData = String.format("{\"ticketId\": %s, \"comment\": \"%s\"}", ticketId, comment);
            RestClient.sendPostRequest("api/audit-logs", jsonData);
            JOptionPane.showMessageDialog(this, "Comment added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding comment!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewAuditLogs() {
        int selectedRow = ticketTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a ticket!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String ticketId = ticketData[selectedRow][0];

        try {
            System.out.println("Fetching Logs for Ticket ID: " + ticketId);
            String response = RestClient.sendGetRequest("api/audit-logs/" + ticketId);
            System.out.println("Logs Response: " + response);

            JSONArray logsArray = new JSONArray(response);
            StringBuilder logs = new StringBuilder("Audit Logs:\n");

            for (int i = 0; i < logsArray.length(); i++) {
                logs.append("- ").append(logsArray.getJSONObject(i).getString("action")).append("\n");
            }

            JOptionPane.showMessageDialog(this, logs.toString(), "Audit Logs", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching logs!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void searchTicketById() {
        String ticketId = txtSearchId.getText().trim();
        if (ticketId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Ticket ID!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String response = RestClient.sendGetRequest("api/tickets/search?ticketId=" + ticketId + "&username=" + LoginUI.getLoggedInUsername());

            System.out.println("Search Response: " + response);

            JSONArray ticketsArray = new JSONArray(response);
            if (ticketsArray.length() == 0) {
                JOptionPane.showMessageDialog(this, "No ticket found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            displayTickets(ticketsArray);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error searching ticket!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterTicketsByStatus() {
        String selectedStatus = (String) cmbStatusFilter.getSelectedItem();
        if (selectedStatus.equals("All")) {
            fetchTickets();
            return;
        }

        try {
            String response = RestClient.sendGetRequest("api/tickets/search?status=" + selectedStatus + "&username=" + LoginUI.getLoggedInUsername());

            System.out.println("Filter Response: " + response);

            JSONArray ticketsArray = new JSONArray(response);
            displayTickets(ticketsArray);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error filtering tickets!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayTickets(JSONArray ticketsArray) {
        ticketData = new String[ticketsArray.length()][5];

        for (int i = 0; i < ticketsArray.length(); i++) {
            JSONObject ticket = ticketsArray.getJSONObject(i);
            ticketData[i][0] = String.valueOf(ticket.getLong("id"));
            ticketData[i][1] = ticket.getString("title");
            ticketData[i][2] = ticket.getString("priority");
            ticketData[i][3] = ticket.getString("category");
            ticketData[i][4] = ticket.getString("status");
        }

        ticketTable.setModel(new javax.swing.table.DefaultTableModel(ticketData, columns));
    }

    private void resetSearchAndFilter() {
        txtSearchId.setText("");
        cmbStatusFilter.setSelectedIndex(0);
        fetchTickets();
    }


}