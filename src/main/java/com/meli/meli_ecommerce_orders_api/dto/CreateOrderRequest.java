package com.meli.meli_ecommerce_orders_api.dto;

import java.util.List;
import java.util.UUID;

/**
 * The type Create order request.
 */
public class CreateOrderRequest {
    private UUID createdBy;
    private List<OrderLineItemRequest> items;

    /**
     * Gets created by.
     *
     * @return the created by
     */
    public UUID getCreatedBy() { return createdBy; }

    /**
     * Sets created by.
     *
     * @param createdBy the created by
     */
    public void setCreatedBy(UUID createdBy) { this.createdBy = createdBy; }

    /**
     * Gets items.
     *
     * @return the items
     */
    public List<OrderLineItemRequest> getItems() { return items; }

    /**
     * Sets items.
     *
     * @param items the items
     */
    public void setItems(List<OrderLineItemRequest> items) { this.items = items; }
}
