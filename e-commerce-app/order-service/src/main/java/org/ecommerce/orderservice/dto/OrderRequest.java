package org.ecommerce.orderservice.dto;

public record OrderRequest(
        String orderNumber,
        String skuCode,
        double price,
        int quantity) {
}
