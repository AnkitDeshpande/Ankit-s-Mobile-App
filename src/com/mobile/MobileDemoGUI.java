package com.mobile;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class MobileDemoGUI extends JFrame implements ActionListener {

    private JTextField modelNumberTextField;
    private JTextField companyNameTextField;
    private JTextField priceTextField;
    private JTextField manufactureDateTextField;
    private JTextArea outputTextArea;

    private Connection connect() {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost/day09", "root", "root");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    private void disconnect(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }

    private void addMobile() {
        String n = modelNumberTextField.getText();
        String name = companyNameTextField.getText();
        int price = Integer.parseInt(priceTextField.getText());
        String jDate = manufactureDateTextField.getText();

        String insert = "INSERT into mobile (model_no, company, price, mfgdate) values(?,?,?,?)";

        Connection connect = connect();

        try {
            PreparedStatement ps = connect.prepareStatement(insert);
            ps.setString(1, n);
            ps.setString(2, name);
            ps.setInt(3, price);
            ps.setString(4, jDate);

            if (ps.executeUpdate() > 0) {
                outputTextArea.append("Mobile Added successfully\n");
            } else {
                outputTextArea.append("Unable to add Mobile.\n");
            }

            disconnect(connect);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateMobile() {
        String n = modelNumberTextField.getText();
        String name = companyNameTextField.getText();
        int price = Integer.parseInt(priceTextField.getText());
        String jDate = manufactureDateTextField.getText();

        String upt = "update mobile set company=?, price=?, mfgdate=? where model_no = ?";

        Connection con = connect();

        try {
            PreparedStatement ps = con.prepareStatement(upt);

            ps.setString(1, name);
            ps.setInt(2, price);
            ps.setString(3, jDate);
            ps.setString(4, n);

            if (ps.executeUpdate() > 0) {
                outputTextArea.append("Mobile Updated successfully.\n");
            } else {
                outputTextArea.append("Unable to Update.\n");
            }

            disconnect(con);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void deleteMobile() {
        String n = modelNumberTextField.getText();

        Connection con = connect();

        String dlt = "Delete from mobile where model_no = ?";

        try {
            PreparedStatement ps = con.prepareStatement(dlt);
            ps.setString(1, n);

            if (ps.executeUpdate() > 0) {
                outputTextArea.append("Mobile Deleted Successfully.\n");
            } else {
                outputTextArea.append("Unable to delete.\n");
            }

            disconnect(con);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void viewMobiles() {
        Connection con = connect();

        String v = "Select * from mobile";

        try {
            PreparedStatement ps = con.prepareStatement(v);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                outputTextArea.append("No mobile found.\n");
            } else {
                do {
                    outputTextArea.append("Id = " + rs.getInt(1) + "\t");
                    outputTextArea.append("model Number = " + rs.getString(2) + "\t");
                    outputTextArea.append("company = " + rs.getString(3) + "\t");
                    outputTextArea.append("price  = " + rs.getInt(4) + "\t");
                    outputTextArea.append("MFG date  = " + rs.getString(5) + "\n");
                } while (rs.next());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        disconnect(con);
    }

    private void searchMobile() {
        String n = modelNumberTextField.getText();

        String srch = "Select * from mobile where model_no = ?";

        Connection con = connect();

        try {

            PreparedStatement ps = con.prepareStatement(srch);

            ps.setString(1, n);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                outputTextArea.append("No Mobile Found\n");
            } else {
                outputTextArea.append("Mobile Found:\n");
                outputTextArea.append("ID: " + rs.getInt("id") + "\n");
                outputTextArea.append("Model Number: " + rs.getString("model_no") + "\n");
                outputTextArea.append("Company: " + rs.getString("company") + "\n");
                outputTextArea.append("Price: " + rs.getDouble("price") + "\n");
                outputTextArea.append("Manufacturing Date: " + rs.getDate("MFGdate") + "\n");
            }
            disconnect(con);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public MobileDemoGUI() {
        setTitle("Mobile Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create the input panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.add(new JLabel("Model Number:"));
        modelNumberTextField = new JTextField();
        inputPanel.add(modelNumberTextField);
        inputPanel.add(new JLabel("Company Name:"));
        companyNameTextField = new JTextField();
        inputPanel.add(companyNameTextField);
        inputPanel.add(new JLabel("Price:"));
        priceTextField = new JTextField();
        inputPanel.add(priceTextField);
        inputPanel.add(new JLabel("Manufacture Date:"));
        manufactureDateTextField = new JTextField();
        inputPanel.add(manufactureDateTextField);

        // Create the button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add");
        addButton.addActionListener(this);
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(this);
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(this);
        JButton viewButton = new JButton("View");
        viewButton.addActionListener(this);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(searchButton);

        // Create the output text area
        outputTextArea = new JTextArea();
        outputTextArea.setEditable(false);

        // Add the components to the frame
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(new JScrollPane(outputTextArea), BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Add")) {
            addMobile();
        } else if (e.getActionCommand().equals("Update")) {
            updateMobile();
        } else if (e.getActionCommand().equals("Delete")) {
            deleteMobile();
        } else if (e.getActionCommand().equals("View")) {
            viewMobiles();
        } else if (e.getActionCommand().equals("Search")) {
            searchMobile();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MobileDemoGUI();
        });
    }
}
