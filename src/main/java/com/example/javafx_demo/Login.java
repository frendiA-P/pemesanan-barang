package com.example.javafx_demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class Login {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField visiblePasswordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label signUpButton;

    @FXML
    private Button show;

    private boolean passwordVisible = false;

    @FXML
    void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordVisible ? visiblePasswordField.getText() : passwordField.getText();

        String role = Database.login(username, password);
        if (role != null) {
            showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + username);
            loadDashboardScene(username, role);
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
        }
    }

    @FXML
    public void handleSignUp(javafx.scene.input.MouseEvent event) {
        loadRegisterScene();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadDashboardScene(String username, String role) {
        String dashboardPath;
        switch (role) {
            case "Customer":
                dashboardPath = "/com/example/javafx_demo/DashboardCustomer.fxml";
                break;
            case "Supplier":
                dashboardPath = "/com/example/javafx_demo/DashboardSupplier.fxml";
                break;
            case "Admin":
            default:
                dashboardPath = "/com/example/javafx_demo/DashboardAdmin.fxml";
                break;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(dashboardPath));
            Parent root = loader.load();

            // Get the controller and set the username
            if (role.equals("Customer")) {
                DashboardCustomer dashboardCustomerController = loader.getController();
                dashboardCustomerController.setUsername(username);
            } else if (role.equals("Supplier")) {
                DashboardSupplier dashboardSupplierController = loader.getController();
                dashboardSupplierController.setUsername(username);
            } else {
                DashboardAdmin dashboardAdminController = loader.getController();
                dashboardAdminController.setUsername(username);
            }

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadRegisterScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafx_demo/Register.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) signUpButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Register");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        if (visiblePasswordField != null) {
            passwordField.textProperty().bindBidirectional(visiblePasswordField.textProperty());
        }
    }

    @FXML
    void togglePasswordVisibility(ActionEvent event) {
        passwordVisible = !passwordVisible;
        if (passwordVisible) {
            visiblePasswordField.setText(passwordField.getText());
            visiblePasswordField.setVisible(true);
            visiblePasswordField.setManaged(true);
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            show.setText("Hide");
        } else {
            passwordField.setText(visiblePasswordField.getText());
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            visiblePasswordField.setVisible(false);
            visiblePasswordField.setManaged(false);
            show.setText("Show");
        }
    }
}
