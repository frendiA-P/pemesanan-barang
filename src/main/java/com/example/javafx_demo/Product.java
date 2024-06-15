package com.example.javafx_demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Product {
    private int id;
    private String name;
    private double price;

    // No-argument constructor
    public Product() {
    }

    public Product(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    @FXML
    private TextField productIdField, productNameField, priceField, quantityField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    void handleSave(ActionEvent event) {
        if (productIdField == null || productNameField == null || priceField == null || quantityField == null || descriptionArea == null) {
            showErrorAlert("Error", "Form fields are not initialized.");
            return;
        }

        String productId = productIdField.getText();
        String productName = productNameField.getText();
        String description = descriptionArea.getText();
        double price = Double.parseDouble(priceField.getText());
        int stock_quantity = Integer.parseInt(quantityField.getText());

        boolean success = Database.insertProduct(productId, productName, description, (int) price, stock_quantity);
        if (success) {
            showInformationAlert("Success", "Product added successfully.");
            clearFields();
        } else {
            showErrorAlert("Error", "Failed to add product to the database.");
        }
    }

    private void clearFields() {
        productIdField.clear();
        productNameField.clear();
        descriptionArea.clear();
        quantityField.clear();
        priceField.clear();
    }

    private void showInformationAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
