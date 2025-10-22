package com.meli.meli_ecommerce_orders_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meli.meli_ecommerce_orders_api.dto.CreateOrderRequest;
import com.meli.meli_ecommerce_orders_api.exceptions.OrderNotFoundException;
import com.meli.meli_ecommerce_orders_api.model.Order;
import com.meli.meli_ecommerce_orders_api.service.OrderService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;

/**
 * Integration test for the OrderController web layer.
 * This test loads ONLY the web layer and mocks the service layer.
 */
@WebMvcTest(OrderController.class)
class OrderControllerTest {

    /**
     * This static nested class provides the mock bean definition
     * to the test's ApplicationContext. This is the modern
     * replacement for the deprecated @MockBean.
     */
    @TestConfiguration
    static class ControllerTestConfig {
        @Bean
        public OrderService orderService() {
            return Mockito.mock(OrderService.class);
        }
    }

    // --- Injected Fields ---

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper; // For converting objects to JSON
    private final OrderService orderService; // This is the MOCK from our TestConfig

    /**
     * Use constructor injection to get the beans from the test context.
     */
    @Autowired
    OrderControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, OrderService orderService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.orderService = orderService;
    }

    @Test
    void testCreateOrder_Failure_InvalidInput() throws Exception {
        // 1. Arrange
        CreateOrderRequest badRequest = new CreateOrderRequest(); // An empty/invalid request
        // (This test assumes your CreateOrderRequest DTO has validation
        // annotations like @NotNull and your controller uses @Valid)

        // 2. Act & 3. Assert
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badRequest)))
                .andExpect(status().isBadRequest()); // Check for HTTP 400
    }

    @Test
    void testGetAllOrders_Success() throws Exception {
        // 1. Arrange
        Order order1 = new Order();
        order1.setId(UUID.randomUUID());
        List<Order> orders = List.of(order1);

        // Mock the service call
        when(orderService.getAllActiveOrders()).thenReturn(orders);

        // 2. Act & 3. Assert
        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk()) // Check for HTTP 200
                .andExpect(jsonPath("$", hasSize(1))) // Check that the JSON response is an array of size 1
                .andExpect(jsonPath("$[0].id", is(order1.getId().toString())));
    }

    @Test
    void testGetAllOrders_Success_EmptyList() throws Exception {
        // 1. Arrange
        when(orderService.getAllActiveOrders()).thenReturn(Collections.emptyList());

        // 2. Act & 3. Assert
        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0))); // Check that the JSON response is an empty array
    }

    @Test
    void testGetOrderById_Success() throws Exception {
        // 1. Arrange
        UUID orderId = UUID.randomUUID();
        Order order = new Order();
        order.setId(orderId);

        when(orderService.getOrderById(orderId)).thenReturn(order);

        // 2. Act & 3. Assert
        mockMvc.perform(get("/api/v1/orders/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(orderId.toString())));
    }

    @Test
    void testGetOrderById_Failure_NotFound() throws Exception {
        // 1. Arrange
        UUID nonExistentId = UUID.randomUUID();

        // Mock the service to throw an exception
        // ** IMPORTANT: This requires a @ControllerAdvice to work (see explanation)
        when(orderService.getOrderById(nonExistentId))
                .thenThrow(new OrderNotFoundException("Order not found"));

        // 2. Act & 3. Assert
        mockMvc.perform(get("/api/v1/orders/{id}", nonExistentId))
                .andExpect(status().isNotFound()); // Check for HTTP 404
    }

    @Test
    void testDeleteOrder_Success() throws Exception {
        // 1. Arrange
        UUID orderId = UUID.randomUUID();

        // For void methods, use doNothing()
        when(orderService.softDeleteOrder(orderId)).thenReturn(new Order());

        // 2. Act & 3. Assert
        mockMvc.perform(delete("/api/v1/orders/{id}", orderId))
                .andExpect(status().isNoContent()); // Check for HTTP 204

        // 3. (Optional) Verify the service method was called
        verify(orderService, times(1)).softDeleteOrder(orderId);
    }

    @Test
    void testDeleteOrder_Failure_NotFound() throws Exception {
        // 1. Arrange
        UUID nonExistentId = UUID.randomUUID();

        // Mock the service to throw an exception
        // ** IMPORTANT: This requires a @ControllerAdvice to work (see explanation)
        doThrow(new OrderNotFoundException("Order not found")) // Use your actual OrderNotFoundException
                .when(orderService).softDeleteOrder(nonExistentId);

        // 2. Act & 3. Assert
        mockMvc.perform(delete("/api/v1/orders/{id}", nonExistentId))
                .andExpect(status().isNotFound()); // Check for HTTP 404
    }
}