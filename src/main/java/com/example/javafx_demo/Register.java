package com.example.javafx_demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class Register {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField phoneField; // Added phoneField

    @FXML
    private TextArea addressField; // Added addressField

    @FXML
    private Button signUpButton;

    @FXML
    private Label Login;
    @FXML
    private Button cancelButton;

    @FXML
    void handleSignUp(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText(); // Get phone number
        String address = addressField.getText(); // Get address

        if (Database.register(username, password, email, phone, address)) {
            showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "Account created for " + username);
        } else {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "Failed to create account.");
        }
    }

    @FXML
    void handleSignIn(javafx.scene.input.MouseEvent event) {
        loadLoginScene();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadLoginScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafx_demo/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) signUpButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        // Set limit for phone number length
        int phoneMaxLength = 10; // Change this to your desired maximum length
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Remove non-numeric characters
            phoneField.setText(newValue.replaceAll("[^\\d]", ""));
            // Limit the length of the phone number
            if (phoneField.getText().length() > phoneMaxLength) {
                String limitedText = phoneField.getText().substring(0, phoneMaxLength);
                phoneField.setText(limitedText);
            }
        });
    }
}
