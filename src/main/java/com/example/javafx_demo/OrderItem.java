package com.example.javafx_demo;

public class OrderItem {
    private final int no;
    private final int productId;
    private final String productName;
    private final String supplier;
    private final int quantity;
    private final double price;
    private final double amount;

    public OrderItem(int no, int productId, String productName, String supplier, int quantity, double price, double amount) {
        this.no = no;
        this.productId = productId;
        this.productName = productName;
        this.supplier = supplier;
        this.quantity = quantity;
        this.price = price;
        this.amount = amount;

    }

    public int getNo() {
        return no;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getSupplier() {
        return supplier;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getAmount() {
        return amount;
    }
}
