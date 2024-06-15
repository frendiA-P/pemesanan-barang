package com.example.javafx_demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Consumer;

public class ProductDialog {

    public static void display(Consumer<String> onSelect) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Select Product");

        ListView<String> productListView = new ListView<>();
        ObservableList<String> products = FXCollections.observableArrayList();

        try (Connection conn = Database.connect()) {
            String query = "SELECT name FROM Product";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                products.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        productListView.setItems(products);

        Button selectButton = new Button("Select");
        selectButton.setOnAction(e -> {
            if (!productListView.getSelectionModel().isEmpty()) {
                String selectedProduct = productListView.getSelectionModel().getSelectedItem();
                onSelect.accept(selectedProduct);
                window.close();
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(productListView, selectButton);
        Scene scene = new Scene(layout, 200, 250);
        window.setScene(scene);
        window.showAndWait();
    }
}
