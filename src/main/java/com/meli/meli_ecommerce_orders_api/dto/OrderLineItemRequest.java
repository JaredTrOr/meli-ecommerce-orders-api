package com.meli.meli_ecommerce_orders_api.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO representing a single item to be included in a new order request.
 * This class is used to transfer data from the client to the service layer.
 */
public class OrderLineItemRequest {
    private UUID productId;
    private String productName;
    private int quantity;
    private BigDecimal pricePerUnit;

    // Getters and Setters for all fields are required for JSON deserialization
    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public BigDecimal getPricePerUnit() { return pricePerUnit; }
    public void setPricePerUnit(BigDecimal pricePerUnit) { this.pricePerUnit = pricePerUnit; }
}
