package com.quickcart.service;

import com.quickcart.dto.CartItemRequest;
import com.quickcart.dto.CartItemResponse;
import com.quickcart.dto.CartResponse;
import com.quickcart.entity.Cart;
import com.quickcart.entity.CartItem;
import com.quickcart.entity.Product;
import com.quickcart.entity.User;
import com.quickcart.repository.CartItemRepository;
import com.quickcart.repository.CartRepository;
import com.quickcart.repository.ProductRepository;
import com.quickcart.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    // Manual constructor for dependency injection
    public CartService(CartRepository cartRepository,
                      CartItemRepository cartItemRepository,
                      ProductRepository productRepository,
                      UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public CartResponse getCart() {
        User user = getCurrentUser();
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
        return mapToResponse(cart);
    }

    @Transactional
    public CartResponse addItem(CartItemRequest request) {
        User user = getCurrentUser();
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem item = null;
        if (cart.getItems() != null) {
            for (CartItem i : cart.getItems()) {
                if (i.getProduct().getId().equals(product.getId())) {
                    item = i;
                    break;
                }
            }
        }

        if (item != null) {
            item.setQuantity(item.getQuantity() + request.getQuantity());
            cartItemRepository.save(item);
        } else {
            item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(request.getQuantity());
            cartItemRepository.save(item);
            if (cart.getItems() != null) {
                cart.getItems().add(item);
            }
        }
        cartRepository.save(cart);
        return mapToResponse(cart);
    }

    @Transactional
    public CartResponse updateItem(Long itemId, int quantity) {
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        if (quantity <= 0) {
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
        return getCart();
    }

    @Transactional
    public CartResponse removeItem(Long itemId) {
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        cartItemRepository.delete(item);
        return getCart();
    }

    private CartResponse mapToResponse(Cart cart) {
        List<CartItemResponse> items = cart.getItems() == null ? List.of() :
            cart.getItems().stream().map(item -> {
                CartItemResponse cir = new CartItemResponse();
                cir.setId(item.getId());
                cir.setProductId(item.getProduct().getId());
                cir.setProductName(item.getProduct().getName());
                cir.setQuantity(item.getQuantity());
                cir.setPrice(item.getProduct().getPrice());
                return cir;
            }).collect(Collectors.toList());

        BigDecimal total = items.stream()
            .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponse.builder()
            .id(cart.getId())
            .items(items)
            .total(total)
            .build();
    }
}