package org.example.fake.service;

import java.util.List;

import org.example.fake.model.Cart;
import org.example.fake.model.Product;
import org.example.fake.repo.CartRepository;
import org.example.fake.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepo;

    @Autowired
    private ProductRepository productRepo;

    // ADD TO CART
    public void addToCart(Long userId, Product product, int quantity) {

        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setProduct(product);
        cart.setQuantity(quantity);

        cartRepo.save(cart);
    }

    // GET USER CART ITEMS
    public List<Cart> getCartByUser(Long userId) {
        return cartRepo.findByUserId(userId);
    }

    // REMOVE FROM CART
    public void removeFromCart(Long cartId) {
        cartRepo.deleteById(cartId);
    }
    public Cart getCartById(Long cartId) {
        return cartRepo.findById(cartId).orElse(null);
    }
    
    public void removeFromCartByProduct(Long userId, Long productId) {

        List<Cart> cartItems = cartRepo.findByUserId(userId);

        for (Cart item : cartItems) {
            if (item.getProduct().getId().equals(productId)) {
                cartRepo.delete(item);
            }
        }
    }
    public void deleteCart(Long cartId) {
        cartRepo.deleteById(cartId);
    }
}