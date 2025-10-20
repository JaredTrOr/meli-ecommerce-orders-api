package com.meli.meli_ecommerce_orders_api.controller;

import com.meli.meli_ecommerce_orders_api.dto.CreateOrderRequest;
import com.meli.meli_ecommerce_orders_api.model.Order;
import com.meli.meli_ecommerce_orders_api.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * The type Order controller.
 */
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    /**
     * Instantiates a new Order controller.
     *
     * @param orderService the order service
     */
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Create order response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest request) {
        Order createdOrder = orderService.createOrder(request);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    /**
     * Gets all orders.
     *
     * @return the all orders
     */
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllActiveOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * Gets order by id.
     *
     * @param id the id
     * @return the order by id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable UUID id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    /**
     * Delete order response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID id) {
        orderService.softDeleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
