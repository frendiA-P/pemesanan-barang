package com.example.javafx_demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class DashboardSupplier {

    @FXML
    private Text welcomeText;

    @FXML
    public BorderPane mainPane;

    @FXML
    private Button homeButton;

    @FXML
    private Button purchaseButton;

    @FXML
    private Button productButton;

    @FXML
    private Button supplierButton;

    @FXML
    private Button inventoryButton;

    @FXML
    private Button logoutButton;

    @FXML
    private void initialize() {
        homeButton.setOnAction(event -> loadPage("DashboardSupplier.fxml"));
        purchaseButton.setOnAction(event -> loadPage("Purchase.fxml"));
        productButton.setOnAction(event -> loadPage("Product.fxml"));
        supplierButton.setOnAction(event -> loadPage("Supplier.fxml"));
        inventoryButton.setOnAction(event -> loadPage("Inventory.fxml"));
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
