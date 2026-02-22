package org.example.fake.service;

import java.util.List;

import org.example.fake.model.Purchase;
import org.example.fake.repo.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseService {
	@Autowired
    private PurchaseRepository purchaseRepo;

	public void savePurchase(Long userId,
            Long productId,
            int quantity,
            double totalAmount,
            String paymentMethod,
            String status,
            String customerName,
            String mobileNumber,
            String address,
            String paymentDetails) {

Purchase purchase = new Purchase();
purchase.setUserId(userId);
purchase.setProductId(productId);
purchase.setQuantity(quantity);
purchase.setTotalAmount(totalAmount);
purchase.setPaymentMethod(paymentMethod);
purchase.setStatus(status);

purchase.setCustomerName(customerName);
purchase.setMobileNumber(mobileNumber);
purchase.setAddress(address);
purchase.setPaymentDetails(paymentDetails);

purchaseRepo.save(purchase);
}

    public List<Purchase> getPurchasesByUser(Long userId) {
        return purchaseRepo.findByUserId(userId);
    }
    
    public List<Purchase> getAllPurchases() {
        return purchaseRepo.findAll();
    }
}
