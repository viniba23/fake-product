package org.example.fake.service;

import java.io.ByteArrayOutputStream;
import java.util.Optional;

import org.example.fake.model.BlockchainProduct;
import org.example.fake.model.Product;
import org.example.fake.model.ProductQRCode;
import org.example.fake.repo.BlockchainProductRepository;
import org.example.fake.repo.ProductQRCodeRepository;
import org.example.fake.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@Service
public class QRCodeService {
	 @Autowired
	    private ProductRepository productRepo;

	    @Autowired
	    private BlockchainProductRepository blockchainRepo;

	    @Autowired
	    private ProductQRCodeRepository qrRepo;

	    public void generateQRCode(Long productId) throws Exception {

	        if (qrRepo.existsByProductId(productId)) {
	            throw new RuntimeException("QR Code already generated");
	        }

	        Product product = productRepo.findById(productId)
	                .orElseThrow(() -> new RuntimeException("Product not found"));

	        BlockchainProduct bc = blockchainRepo.findAll()
	                .stream()
	                .filter(b -> b.getProduct().getId().equals(productId))
	                .findFirst()
	                .orElseThrow(() -> new RuntimeException("Product not enrolled in blockchain"));

	        String qrData =
	                "PRODUCT_ID=" + product.getId() +
	                "|HASH=" + bc.getProductHash();

	        QRCodeWriter qrCodeWriter = new QRCodeWriter();
	        BitMatrix bitMatrix = qrCodeWriter.encode(qrData, BarcodeFormat.QR_CODE, 250, 250);

	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

	        ProductQRCode qr = new ProductQRCode();
	        qr.setProduct(product);
	        qr.setQrImage(outputStream.toByteArray());

	        qrRepo.save(qr);
	    }

	    public Optional<ProductQRCode> getQRCode(Long productId) {
	        return qrRepo.findByProductId(productId);
	    }

}
