package com.example.javafx_demo;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
public class Customer {
    private int id;
    private String name;
    private String address;

    @FXML
    private TextField customerIdField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextArea addressArea;
    @FXML
    private Button saveButton;

    // No-argument constructor
    public Customer() {
    }
    public Customer(int id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
    @FXML
    void initialize() {
        saveButton.setOnAction(event -> {
            String customerId = customerIdField.getText();
            String name = nameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String address = addressArea.getText();

            boolean success = Database.insertCustomer(customerId, name, email, phone, address);
            if (success) {
                showAlert("Success", "Customer successfully added to the database.");
            } else {
                showAlert("Error", "Failed to add customer to the database.");
            }
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
