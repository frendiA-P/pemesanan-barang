package com.example.javafx_demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class DashboardAdmin {

    @FXML
    private Text welcomeText;

    @FXML
    public BorderPane mainPane;

    @FXML
    private Button homeButton;



    @FXML
    private Button manageStockButton;

    @FXML
    private Button manageUserButton;

    @FXML
    private Button manageInventoryButton;

    @FXML
    private Button logoutButton;

    @FXML
    private void initialize() {
        homeButton.setOnAction(event -> loadPage("DashboardAdmin.fxml"));
        manageStockButton.setOnAction(event -> loadPage("khusbarang.fxml"));
        manageUserButton.setOnAction(event -> loadPage("Purchase.fxml"));
        manageInventoryButton.setOnAction(event -> loadPage("Inventory.fxml"));
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
