package com.example.javafx_demo;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
public class Supplier {
    @FXML
    private TextField supplierIdField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField contactField;

    @FXML
    private TextField telpField;

    @FXML
    private TextArea addressArea;

    @FXML
    private Button saveButton;

    @FXML
    void initialize() {
        saveButton.setOnAction(event -> {
            String supplierId = supplierIdField.getText();
            String name = nameField.getText();
            String contact = contactField.getText();
            String telp = telpField.getText();
            String address = addressArea.getText();

            boolean success = Database.insertSupplier(supplierId, name, contact, telp, address);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Supplier successfully added to the database.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add supplier to the database.");
            }
        });
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
