package com.example.javafx_demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order {

    @FXML
    private TextField orderNoField;
    @FXML
    private TextField customerNameField;
    @FXML
    private TextField addressField;
    @FXML
    private DatePicker dateField;
    @FXML
    private TextField productField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField statusField;
    @FXML
    private TableView<OrderItem> tableView;
    @FXML
    private TableColumn<OrderItem, Integer> noColumn;
    @FXML
    private TableColumn<OrderItem, Integer> productIdColumn;
    @FXML
    private TableColumn<OrderItem, String> productNameColumn;
    @FXML
    private TableColumn<OrderItem, String> supplierColumn;
    @FXML
    private TableColumn<OrderItem, Integer> quantityColumn;
    @FXML
    private TableColumn<OrderItem, Double> priceColumn;
    @FXML
    private TableColumn<OrderItem, Double> amountColumn;

    private ObservableList<OrderItem> orderItems;

    @FXML
    public void initialize() {
        orderItems = FXCollections.observableArrayList();
        tableView.setItems(orderItems);

        // Sesuaikan nama kolom dengan properti pada model OrderItem
        noColumn.setCellValueFactory(new PropertyValueFactory<>("no"));
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        supplierColumn.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        // Ambil data dari tabel customer_order dan tampilkan
        List<OrderItem> orders = getCustomerOrders();
        orderItems.addAll(orders);
    }

    private List<OrderItem> getCustomerOrders() {
        List<OrderItem> orders = new ArrayList<>();
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM customer_order")) {

            while (rs.next()) {
                // Sesuaikan pembuatan OrderItem dengan kolom-kolom di tabel customer_order
                OrderItem order = new OrderItem(
                        rs.getInt("order_id"),
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getString("customer_id"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getDouble("amount")
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }


    @FXML
    public void showCustomerDialog(ActionEvent event) {
        Dialog<Customer> dialog = new Dialog<>();
        dialog.setTitle("Select Customer");

        TableView<Customer> customerTable = new TableView<>();
        TableColumn<Customer, Integer> idColumn = new TableColumn<>("ID");
        TableColumn<Customer, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Customer, String> addressColumn = new TableColumn<>("Address");

        customerTable.getColumns().addAll(idColumn, nameColumn, addressColumn);
        ObservableList<Customer> customers = FXCollections.observableArrayList(getCustomers());
        customerTable.setItems(customers);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        VBox vbox = new VBox(customerTable);
        dialog.getDialogPane().setContent(vbox);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return customerTable.getSelectionModel().getSelectedItem();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(customer -> {
            customerNameField.setText(customer.getName());
            addressField.setText(customer.getAddress());
            // Store customer ID for later use
            customerNameField.setUserData(customer.getId());
        });
    }

    @FXML
    public void showProductDialog(ActionEvent event) {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Select Product");

        TableView<Product> productTable = new TableView<>();
        TableColumn<Product, Integer> idColumn = new TableColumn<>("ID");
        TableColumn<Product, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Product, Double> priceColumn = new TableColumn<>("Price");

        productTable.getColumns().addAll(idColumn, nameColumn, priceColumn);
        ObservableList<Product> products = FXCollections.observableArrayList(getProducts());
        productTable.setItems(products);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        VBox vbox = new VBox(productTable);
        dialog.getDialogPane().setContent(vbox);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return productTable.getSelectionModel().getSelectedItem();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(product -> {
            productField.setText(product.getName());
            priceField.setText(String.valueOf(product.getPrice()));
            // Store product ID for later use
            productField.setUserData(product.getId());
        });
    }

    public void handleEnterList(ActionEvent event) {
        try {
            int customerId = (int) customerNameField.getUserData();
            String customerName = customerNameField.getText();
            String address = addressField.getText();

            LocalDate orderLocalDate = dateField.getValue(); // Ambil nilai dari DatePicker
            String orderDate = null;

            // Periksa apakah orderLocalDate tidak null sebelum memanggil metode toString()
            if (orderLocalDate != null) {
                orderDate = orderLocalDate.toString();
            } else {
                // Jika null, tampilkan pesan kesalahan
                showAlert(Alert.AlertType.ERROR, "Order Failed", "Please select a date for the order.");
                return; // Keluar dari metode jika tanggal null
            }
            if (customerId != 0) {
                // Insert order dan dapatkan ID order yang dihasilkan
                int orderId = Database.insertOrder(null, customerId, "Pending", calculateTotalAmount());

                if (orderId != -1) {
                    for (OrderItem item : orderItems) {
                        int productId = (int) productField.getUserData(); // Mendapatkan ID produk dari UserData
                        String productName = productField.getText();
                        int quantity = Integer.parseInt(quantityField.getText());
                        double price = Double.parseDouble(priceField.getText());
                        double amount = item.getQuantity() * item.getPrice();

                        // Insert into customer_order
                        Database.insertCustomerOrder(
                                orderId,
                                productId,
                                productName,
                                customerId,
                                quantity,
                                price,
                                amount
                        );

                        // Insert into order_item
                        Database.insertOrderItem(orderId, productId, quantity, price);
                    }

                    // Refresh table view
                    tableView.refresh();

                    showAlert(Alert.AlertType.INFORMATION, "Order Placed", "Order has been placed successfully!");

                    // Clear order items after successful order placement
                    orderItems.clear();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Order Failed", "Failed to create order.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Order Failed", "Please select a customer before placing the order.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
        }
    }


    private List<Customer> getCustomers() {
        List<Customer> customers = new ArrayList<>();
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM customer")) {

            while (rs.next()) {
                customers.add(new Customer(rs.getInt("customer_id"), rs.getString("name"), rs.getString("address")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    private List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM product")) {

            while (rs.next()) {
                products.add(new Product(rs.getInt("product_id"), rs.getString("name"), rs.getDouble("price")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    private double calculateTotalAmount() {
        return orderItems.stream().mapToDouble(OrderItem::getAmount).sum();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

