package com.quickcart.service;

import com.quickcart.dto.OrderItemResponse;
import com.quickcart.dto.OrderResponse;
import com.quickcart.entity.*;
import com.quickcart.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    // Manual constructor for dependency injection
    public OrderService(OrderRepository orderRepository,
                       OrderItemRepository orderItemRepository,
                       CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public OrderResponse placeOrder() {
        User user = getCurrentUser();
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        List<OrderItem> orderItems = cart.getItems().stream().map(item -> {
            OrderItem oi = new OrderItem();
            oi.setProduct(item.getProduct());
            oi.setQuantity(item.getQuantity());
            oi.setPrice(item.getProduct().getPrice());
            return oi;
        }).collect(Collectors.toList());

        BigDecimal total = orderItems.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setUser(user);
        order.setItems(orderItems);
        order.setTotal(total);
        order.setCreatedAt(LocalDateTime.now());

        order = orderRepository.save(order);
        for (OrderItem item : orderItems) {
            item.setOrder(order);
            orderItemRepository.save(item);
        }

        // Clear cart
        cartItemRepository.deleteAll(cart.getItems());
        cart.setItems(null);
        cartRepository.save(cart);

        return mapToResponse(order);
    }

    public List<OrderResponse> getOrderHistory() {
        User user = getCurrentUser();
        return orderRepository.findByUser(user).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private OrderResponse mapToResponse(Order order) {
        List<OrderItemResponse> items = order.getItems() == null ? List.of() :
                order.getItems().stream().map(item -> {
                    OrderItemResponse oir = new OrderItemResponse();
                    oir.setId(item.getId());
                    oir.setProductId(item.getProduct().getId());
                    oir.setProductName(item.getProduct().getName());
                    oir.setQuantity(item.getQuantity());
                    oir.setPrice(item.getPrice());
                    return oir;
                }).collect(Collectors.toList());
        return OrderResponse.builder()
                .id(order.getId())
                .items(items)
                .total(order.getTotal())
                .createdAt(order.getCreatedAt())
                .build();
    }
}