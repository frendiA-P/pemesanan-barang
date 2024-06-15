package com.example.javafx_demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class test extends Application {
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        Label itemLabel = new Label("Item:");
        TextField itemTextField = new TextField();

        Label quantityLabel = new Label("Quantity:");
        TextField quantityTextField = new TextField();

        Button purchaseButton = new Button("Purchase");
        statusLabel = new Label();

        purchaseButton.setOnAction(e -> {
            String item = itemTextField.getText();
            int quantity = Integer.parseInt(quantityTextField.getText());
            purchaseItem(item, quantity);
        });

        VBox root = new VBox(10);
        root.getChildren().addAll(itemLabel, itemTextField, quantityLabel, quantityTextField, purchaseButton, statusLabel);

        Scene scene = new Scene(root, 300, 200);

        primaryStage.setTitle("Inventory Management App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void purchaseItem(String item, int quantity) {
        // Implement your purchase logic here
        // For simplicity, let's just display the purchase status
        statusLabel.setText(quantity + " " + item + "(s) purchased successfully.");
    }

    public static void main(String[] args) {
        launch(args);
    }
}