package com.example.javafx_demo;

import java.sql.*;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/uas_pbo";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static boolean register(String username, String password, String email, String phone, String address) {
        String query = "INSERT INTO user (Username, Password, Email, Phone, Address) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            pstmt.setString(4, phone);
            pstmt.setString(5, address);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String login(String username, String password) {
        String query = "SELECT Role FROM user WHERE Username = ? AND Password = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Role");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int insertOrder(Object o, int customerId, String status, double totalAmount) {
        int orderId = -1;
        String query = "INSERT INTO `order` (order_date, customer_id, status, total_amount) VALUES (CURDATE(), ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, customerId);
            pstmt.setString(2, status); // Menyertakan nilai untuk kolom 'status'
            pstmt.setDouble(3, totalAmount);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    orderId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderId;
    }

    public static void insertCustomerOrder(int orderId, int productId, String productName, int customerId, int quantity, double price, double amount) {
        String query = "INSERT INTO `customer_order` (order_id, product_id, product_name, customer_id, quantity, price, amount) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, orderId);
            pstmt.setInt(2, productId);
            pstmt.setString(3, productName);
            pstmt.setInt(4, Integer.parseInt(String.valueOf(customerId)));
            pstmt.setInt(5, quantity);
            pstmt.setDouble(6, price);
            pstmt.setDouble(7, amount);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Method to insert an order item into the order_item table
    public static boolean insertOrderItem(int orderId, int productId, int quantity, double price) {
        String sql = "INSERT INTO order_item (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            pstmt.setInt(2, productId);
            pstmt.setInt(3, quantity);
            pstmt.setDouble(4, price);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean insertProduct(String productId, String productName, String description,double price, int stock_quantity) {
        String query = "INSERT INTO Product (product_id, name, description, price, stock_quantity) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, productId);
            pstmt.setString(2, productName);
            pstmt.setString(3, description);
            pstmt.setDouble(4, price);
            pstmt.setInt(5, stock_quantity);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean insertSupplier(String supplierId, String name, String contact, String telp, String address) {
        String query = "INSERT INTO supplier (supplier_id, name, contact, telp, address) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, supplierId);
            pstmt.setString(2, name);
            pstmt.setString(3, contact);
            pstmt.setString(4, telp);
            pstmt.setString(5, address);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean insertCustomer(String customerId, String name, String email, String phone, String address) {
        String query = "INSERT INTO customer (customer_id, name, email, phone, address) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, customerId);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.setString(4, phone);
            pstmt.setString(5, address);

            int rowsInserted =  pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean insertPurchase(int productId, String productName, int supplierId, String supplierName, int quantity, double price, double amount) {
        String query = "INSERT INTO purchase (Product_ID, Product_name, supplier_id ,supplier, quantity, price, Amount) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, productId);
            pstmt.setString(2, productName);
            pstmt.setInt(3, supplierId);
            pstmt.setString(4, supplierName);
            pstmt.setInt(5, quantity);
            pstmt.setDouble(6, price);
            pstmt.setDouble(7, amount);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}