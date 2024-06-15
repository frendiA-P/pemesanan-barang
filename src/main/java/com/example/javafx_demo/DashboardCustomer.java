package com.example.javafx_demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class DashboardCustomer {

    @FXML
    private Text welcomeText;

    @FXML
    public BorderPane mainPane;

    @FXML
    private Button homeButton;

    @FXML
    private Button orderButton;

    @FXML
    private Button customerButton;

    @FXML
    private Button logoutButton;

    @FXML
    private void initialize() {
        homeButton.setOnAction(event -> loadPage("DashboardCustomer.fxml"));
        orderButton.setOnAction(event -> loadPage("Order.fxml"));
        customerButton.setOnAction(event -> loadPage("Customer.fxml"));
        logoutButton.setOnAction(event -> loadPage("Login.fxml"));
    }

    private void loadPage(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            mainPane.setCenter(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUsername(String username) {
        welcomeText.setText("Selamat Datang," + username + "!");
    }
}
