package com.example.javafx_demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.time.LocalDate;

public class Purchase {

    @FXML
    private TextField orderNoField, supplierNameField, addressField, telpField, productField, quantityField, priceField;
    @FXML
    private DatePicker dateField;
    @FXML
    private TableView<OrderItem> tableView;
    @FXML
    private TableColumn<OrderItem, Integer> noColumn;
    @FXML
    private TableColumn<OrderItem, Integer> productIdColumn; // Changed to Integer
    @FXML
    private TableColumn<OrderItem, String> productNameColumn, supplierColumn;
    @FXML
    private TableColumn<OrderItem, Integer> quantityColumn;
    @FXML
    private TableColumn<OrderItem, Double> priceColumn, amountColumn;

    private ObservableList<String> suppliers = FXCollections.observableArrayList();

    public void initialize() {
        // Initialize TableView columns
        noColumn.setCellValueFactory(new PropertyValueFactory<>("no"));
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        supplierColumn.setCellValueFactory(new PropertyValueFactory<>("supplier"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        loadSupplierData();
        loadTableData();
    }

    @FXML
    void handleEnterList(ActionEvent event) {
        try {
            // Get the product ID and supplier ID based on the selected product and supplier names
            int productId = getProductIDFromName(productField.getText());
            int supplierId = getSupplierIDFromName(supplierNameField.getText());
            String productName = productField.getText();
            String supplier = supplierNameField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());
            double amount = quantity * price;

            // Add new item to TableView
            OrderItem newItem = new OrderItem(tableView.getItems().size() + 1, productId, productName, supplier, quantity, price, amount);
            tableView.getItems().add(newItem);

            // Insert data into Purchase table
            boolean success = Database.insertPurchase(productId, productName, supplierId, supplier, quantity, price, amount);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Item added to Purchase table successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add item to Purchase table.");
            }

            // Clear input fields
            productField.clear();
            quantityField.clear();
            priceField.clear();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Quantity and Price must be valid numbers.");
        }
    }

    @FXML
    void handleNewData(ActionEvent event) {
        String orderNo = orderNoField.getText();
        String supplierName = supplierNameField.getText();
        String address = addressField.getText();
        String telp = telpField.getText();
        LocalDate orderDate = dateField.getValue();
        String orderDateStr = orderDate != null ? orderDate.toString() : "";

        // Insert the order into the database
        int orderId = Database.insertOrder(orderDateStr, 1, "Pending", calculateTotalAmount()); // Assuming customer_id is 1 for now, replace with actual logic

        if (orderId > 0) {
            boolean allItemsInserted = true;
            for (OrderItem item : tableView.getItems()) {
                boolean success = Database.insertOrderItem(orderId, item.getProductId(), item.getQuantity(), item.getPrice());
                if (!success) {
                    allItemsInserted = false;
                    break;
                }
            }

            if (allItemsInserted) {
                showAlert(Alert.AlertType.INFORMATION, "Order Saved", "Order has been saved successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Order Save Failed", "Failed to save some order items.");
            }
            loadTableData();
        } else {
            showAlert(Alert.AlertType.ERROR, "Order Save Failed", "Failed to save order.");
        }
    }

    private double calculateTotalAmount() {
        return tableView.getItems().stream().mapToDouble(OrderItem::getAmount).sum();
    }

    private void loadTableData() {
        // Clear existing data
        tableView.getItems().clear();

        try (Connection conn = Database.connect()) {
            String query = "SELECT * from purchase"; // Join to get product name
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            int rowNumber = 1;
            while (rs.next()) {
//                int orderId = rs.getInt("order_id");
                int productId = rs.getInt("product_id");
                String productName = rs.getString("product_name");
                String supplierName = rs.getString("supplier");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("price");
                double amount = rs.getDouble("amount");

                tableView.getItems().add(new OrderItem(rowNumber++, productId, productName, supplierName, quantity, price, amount)); // Pass product name for display
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void handleShowProductList(ActionEvent event) {
        ProductDialog.display(this::handleProductSelection);
    }

    private void handleProductSelection(String selectedProduct) {
        productField.setText(selectedProduct);

        // Load additional information like quantity and price from the database based on the selected product
        try (Connection conn = Database.connect()) {
            String query = "SELECT stock_quantity, price FROM Product WHERE name = '" + selectedProduct + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                int quantity = rs.getInt("stock_quantity");
                double price = rs.getDouble("price");
                quantityField.setText(String.valueOf(quantity));
                priceField.setText(String.valueOf(price));
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load product information: " + e.getMessage());
        }
    }

    private void loadSupplierData() {
        try (Connection conn = Database.connect()) {
            String query = "SELECT name FROM supplier"; // Query to fetch supplier names
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                suppliers.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load supplier data: " + e.getMessage());
        }
    }

    @FXML
    void handleShowSupplierList(ActionEvent event) {
        SupplierDialog.display(this::handleSupplierSelection);
    }

    private void handleSupplierSelection(String selectedSupplier) {
        supplierNameField.setText(selectedSupplier);

        // Load additional information like telephone and address from the database based on the selected supplier
        try (Connection conn = Database.connect()) {
            String query = "SELECT telp, address FROM supplier WHERE name = '" + selectedSupplier + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                String telp = rs.getString("telp");
                String address = rs.getString("address");
                telpField.setText(telp);
                addressField.setText(address);
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load supplier information: " + e.getMessage());
        }
    }

    private int getProductIDFromName(String productName) {
        // Implement logic to get product ID from database based on name
        try (Connection conn = Database.connect()) {
            String query = "SELECT product_id FROM Product WHERE name = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, productName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("product_id");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to retrieve product ID: " + e.getMessage());
        }
        return -1; // Return -1 if product ID not found or an error occurred
    }

    private int getSupplierIDFromName(String supplierName) {
        // Implement logic to get supplier ID from database based on name
        try (Connection conn = Database.connect()) {
            String query = "SELECT supplier_id FROM supplier WHERE name = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, supplierName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("supplier_id");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to retrieve supplier ID: " + e.getMessage());
        }
        return -1; // Return -1 if supplier ID not found or an error occurred
    }

}

