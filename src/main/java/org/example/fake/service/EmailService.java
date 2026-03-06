package org.example.fake.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    
	 @Autowired
	    private JavaMailSender mailSender;

	    public void sendPasswordResetEmail(String toEmail, String otp) {
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(toEmail);
	        message.setSubject("Password Reset OTP");
	        message.setText("Your OTP for password reset is: " + otp + "\n\n" +
	                       "This OTP is valid for 15 minutes.\n" +
	                       "If you didn't request this, please ignore this email.");
	        mailSender.send(message);
	    }
	    
//	    public void sendPurchaseEmail(
//	            String toEmail,
//	            String productName,
//	            String productHash,
//	            double amount,
//	            String paymentMethod
//	    ) {
//
//	        SimpleMailMessage message = new SimpleMailMessage();
//
//	        message.setTo(toEmail);
//	        message.setSubject("Payment Successful - Product Purchase");
//
//	        message.setText(
//	                "Payment Successful!\n\n" +
//	                "Product Name: " + productName + "\n" +
//	                "Blockchain Hash: " + productHash + "\n" +
//	                "Payment Method: " + paymentMethod + "\n" +
//	                "Total Amount: ₹" + amount + "\n\n" +
//	                "Thank you for purchasing authentic products.\n" +
//	                "Fake Product Identification System"
//	        );
//
//	        mailSender.send(message);
//	    }
	    // NEW METHOD FOR PURCHASE EMAIL
	    public void sendPurchaseEmail(
	            String toEmail,
	            String customerName,
	            String productName,
	            String productHash,
	            double amount,
	            String paymentMethod,
	            String address
	    ) {

	        try {

	            MimeMessage message = mailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(message, true);

	            helper.setTo(toEmail);
	            helper.setSubject("Payment Successful - Product Detection System");

	            String html =
	                    "<h2 style='color:green;'>Payment Successful ✅</h2>" +

	                    "<p>Hello <b>" + customerName + "</b>,</p>" +

	                    "<p>Thank you for purchasing the product.</p>" +

	                    "<h3>Product Details</h3>" +

	                    "<b>Product Name:</b> " + productName + "<br>" +
	                    "<b>Payment Method:</b> " + paymentMethod + "<br>" +
	                    "<b>Total Amount:</b> ₹" + amount + "<br><br>" +

	                    "<h3>Delivery Address</h3>" +
	                    "<p>" + address + "</p>" +

	                    "<h3>Blockchain Hash</h3>" +
	                    "<p>" + productHash + "</p>" +

	                    "<br><br>" +
	                    "<p>This hash ensures product authenticity.</p>" +

	                    "<br>" +
	                    "<p>Regards,<br>" +
	                    "Fake Product Identification System</p>";

	            helper.setText(html, true);

	            mailSender.send(message);

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
}
