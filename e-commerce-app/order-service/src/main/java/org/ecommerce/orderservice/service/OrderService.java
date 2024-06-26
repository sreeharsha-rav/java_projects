package org.ecommerce.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ecommerce.orderservice.dto.OrderRequest;
import org.ecommerce.orderservice.dto.OrderResponse;
import org.ecommerce.orderservice.model.Order;
import org.ecommerce.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.ecommerce.orderservice.client.InventoryClient;

import java.util.Date;
import java.util.Optional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    private final InventoryClient inventoryClient;

    public OrderResponse createOrder(OrderRequest orderRequest) {
        try {
            // Check if item is in stock
            Boolean inStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());

            if (!inStock) {
                log.error("Item not in stock: {}", orderRequest.skuCode());
                return null;
            }

            // Map request to order
            Order order = Order.builder()
                    .orderNumber(UUID.randomUUID().toString())
                    .skuCode(orderRequest.skuCode())
                    .orderDate(new Date())
                    .price(orderRequest.price())
                    .quantity(orderRequest.quantity())
                    .build();

            // Save order
            Order savedOrder = orderRepository.save(order);
            log.info("Order created successfully: {}", savedOrder);

            // Return response
            return new OrderResponse(
                    savedOrder.getId(),
                    savedOrder.getOrderNumber(),
                    savedOrder.getSkuCode(),
                    savedOrder.getPrice(),
                    savedOrder.getQuantity(),
                    savedOrder.getOrderDate().toString());

        } catch (Exception e) {
            log.error("Error occurred while creating order: {}", e.getMessage());
            return null;
        }
    }

    public OrderResponse getOrderByID(Long id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isEmpty()) {
            log.error("Order not found: {}", id);
            return null;
        }

        return new OrderResponse(
                orderOptional.get().getId(),
                orderOptional.get().getOrderNumber(),
                orderOptional.get().getSkuCode(),
                orderOptional.get().getPrice(),
                orderOptional.get().getQuantity(),
                orderOptional.get().getOrderDate().toString());
    }

    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(order -> new OrderResponse(
                        order.getId(),
                        order.getOrderNumber(),
                        order.getSkuCode(),
                        order.getPrice(),
                        order.getQuantity(),
                        order.getOrderDate().toString()))
                .toList();
    }

    public void deleteOrder(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            orderRepository.delete(order.get());
            log.info("Order deleted successfully: {}", order.get());
        } else {
            log.error("Order not found: {}", id);
        }
    }

}
