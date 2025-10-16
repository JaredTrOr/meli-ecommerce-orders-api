package com.meli.meli_ecommerce_orders_api.dto;

import java.util.List;
import java.util.UUID;

public class CreateOrderRequest {
    private UUID createdBy;
    private List<OrderLineItemRequest> items;

    public UUID getCreatedBy() { return createdBy; }
    public void setCreatedBy(UUID createdBy) { this.createdBy = createdBy; }
    public List<OrderLineItemRequest> getItems() { return items; }
    public void setItems(List<OrderLineItemRequest> items) { this.items = items; }
}
