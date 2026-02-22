package org.example.fake.controller;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.example.fake.model.Cart;
import org.example.fake.model.Product;
import org.example.fake.model.Purchase;
import org.example.fake.model.User;
import org.example.fake.repo.ProductQRCodeRepository;
import org.example.fake.service.CartService;
import org.example.fake.service.ProductService;
import org.example.fake.service.PurchaseService;
import org.example.fake.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class UserProductController {
	@Autowired
    private ProductService productService;
	
	@Autowired
	private UserService userService;
	@Autowired
	private PurchaseService purchaseService;
	@Autowired
	private CartService cartService;
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {

        User user = userService.findByEmail(auth.getName());
        model.addAttribute("user", user);
        model.addAttribute("products", productService.getAllProducts());

        return "user-dashboard";
    }

//    @GetMapping("/products/view/{id}")
//    public String viewProduct(@PathVariable Long id, Model model) {
//        model.addAttribute("product", productService.getProductById(id));
//        return "user-product-view";
//    }
    @Autowired
    private ProductQRCodeRepository qrRepo;

    @GetMapping("/products/view/{id}")
    public String viewProduct(@PathVariable Long id, Model model) {

        Product product = productService.getProductById(id);

        if (product == null) {
            return "redirect:/user/dashboard";
        }

        model.addAttribute("product", product);

        // Load QR if exists
        qrRepo.findByProductId(id).ifPresent(qr -> {
            model.addAttribute("qrImage",
                    Base64.getEncoder().encodeToString(qr.getQrImage()));
        });

        return "user-product-view";
    }

    @GetMapping("/products/purchase/{id}")
    public String showPurchasePage(@PathVariable Long id, Model model) {

        Product product = productService.getProductById(id);

        if (product == null) {
            return "redirect:/user/dashboard";
        }

        model.addAttribute("product", product);	

        return "user-product-purchase";
    }
    
    @PostMapping("/buy-now")
    public String buyNow(@RequestParam Long productId,
                         @RequestParam int quantity,
                         Model model) {

        Product product = productService.getProductById(productId);

        double totalAmount = product.getPrice() * quantity;

        model.addAttribute("product", product);
        model.addAttribute("quantity", quantity);
        model.addAttribute("totalAmount", totalAmount);

        return "user-payment";
    }
    @PostMapping("/process-payment")
    public String processPayment(
            @RequestParam Long productId,
            @RequestParam int quantity,
            @RequestParam double totalAmount,
            @RequestParam String paymentMethod,
            @RequestParam String customerName,
            @RequestParam String mobileNumber,
            @RequestParam String address,
            Authentication auth,
            Model model) {

        User user = userService.findByEmail(auth.getName());

        Product product = productService.getProductById(productId);

        // ❌ Check stock availability
        if (product.getQuantity() < quantity) {
            model.addAttribute("error", "Only " + product.getQuantity() + " items available!");
            return "redirect:/user/products/purchase/" + productId;
        }

        // ✅ Reduce stock
        product.setQuantity(product.getQuantity() - quantity);
        productService.saveProduct(product);

        // ✅ Save purchase
        purchaseService.savePurchase(
                user.getId(),
                productId,
                quantity,
                totalAmount,
                paymentMethod,
                "SUCCESS",
                customerName,
                mobileNumber,
                address,
                ""
        );

        model.addAttribute("message", "Payment Successful ✅");

        return "user-payment-success";
    }
    @GetMapping("/my-purchases")
    public String myPurchases(Authentication auth, Model model) {

        User user = userService.findByEmail(auth.getName());

        List<Purchase> purchases =
                purchaseService.getPurchasesByUser(user.getId());

        Map<Long, Product> productMap = new HashMap<>();

        for (Purchase p : purchases) {
            Product product = productService.getProductById(p.getProductId());
            productMap.put(p.getProductId(), product);
        }

        model.addAttribute("purchases", purchases);
        model.addAttribute("productMap", productMap);

        return "user-my-purchases";
    }
    
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam int quantity,
                            Authentication auth) {

        User user = userService.findByEmail(auth.getName());
        Product product = productService.getProductById(productId);

        // ❌ Not enough stock
        if (product.getQuantity() < quantity) {
            return "redirect:/user/products/purchase/" + productId;
        }

        // ✅ Reduce stock immediately
        product.setQuantity(product.getQuantity() - quantity);
        productService.saveProduct(product);

        // ✅ Add to cart
        cartService.addToCart(user.getId(), product, quantity);

        return "redirect:/user/cart";
    }
    
    @GetMapping("/cart")
    public String viewCart(Authentication auth, Model model) {

        User user = userService.findByEmail(auth.getName());

        model.addAttribute("cartItems",
                cartService.getCartByUser(user.getId()));

        return "user-cart";
    }
    
//    @PostMapping("/cart/remove")
//    public String removeCart(@RequestParam Long cartId) {
//
//        cartService.removeFromCart(cartId);
//
//        return "redirect:/user/cart";
//    }
//    
    @PostMapping("/cart/buy")
    public String buyFromCart(@RequestParam Long cartId,
                              Model model) {

        Cart cartItem = cartService.getCartById(cartId);

        if (cartItem == null) {
            return "redirect:/user/cart";
        }

        Product product = cartItem.getProduct();
        int quantity = cartItem.getQuantity();

        double totalAmount = product.getPrice() * quantity;

        model.addAttribute("product", product);
        model.addAttribute("quantity", quantity);
        model.addAttribute("totalAmount", totalAmount);

        return "user-payment";
    }
    
    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Long cartId) {

        Cart cartItem = cartService.getCartById(cartId);

        if (cartItem != null) {

            Product product = cartItem.getProduct();

            // Return stock
            product.setQuantity(product.getQuantity() + cartItem.getQuantity());
            productService.saveProduct(product);

            cartService.deleteCart(cartId);
        }

        return "redirect:/user/cart";
    }
}
