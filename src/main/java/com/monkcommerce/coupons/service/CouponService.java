package com.monkcommerce.coupons.service;

import com.monkcommerce.coupons.entity.Coupon;
import com.monkcommerce.coupons.repository.CouponRepository;
import com.monkcommerce.coupons.dto.Cart;
import com.monkcommerce.coupons.dto.CartItem;
import com.monkcommerce.coupons.dto.CartResponse;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponService {

    private final CouponRepository repository;

    public CouponService(CouponRepository repository) {
        this.repository = repository;
    }

    // ================== CREATE ==================
    public Coupon createCoupon(Coupon coupon) {
        return repository.save(coupon);
    }

    // ================== GET ALL ==================
    public List<Coupon> getAllCoupons() {
        return repository.findAll();
    }

    // ================== GET BY ID ==================
    public Coupon getCoupon(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found"));
    }

    // ================== DELETE ==================
    public void deleteCoupon(Long id) {
        repository.deleteById(id);
    }

    // ================== CART-WISE DISCOUNT ==================
    public double calculateCartWiseDiscount(Coupon coupon, Cart cart) {

        double total = cart.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        if (coupon.getThreshold() != null &&
                total > coupon.getThreshold()) {

            return total * (coupon.getDiscount() / 100);
        }

        return 0;
    }

    // ================== PRODUCT-WISE DISCOUNT ==================
    public double calculateProductWiseDiscount(Coupon coupon, Cart cart) {

        double discount = 0;

        for (CartItem item : cart.getItems()) {

            if (coupon.getProductId() != null &&
                    coupon.getProductId().equals(item.getProductId())) {

                discount += item.getPrice() * item.getQuantity()
                        * (coupon.getDiscount() / 100);
            }
        }

        return discount;
    }

    // ================== BXGY DISCOUNT ==================
    public double calculateBxGyDiscount(Coupon coupon, Cart cart) {

        if (coupon.getBuyQuantity() == null ||
            coupon.getGetQuantity() == null ||
            coupon.getProductId() == null ||
            coupon.getGetProductId() == null) {
            return 0;
        }

        int buyCount = 0;
        double freeItemPrice = 0;

        for (CartItem item : cart.getItems()) {

            // Count buy items
            if (item.getProductId().equals(coupon.getProductId())) {
                buyCount += item.getQuantity();
            }

            // Find free product price
            if (item.getProductId().equals(coupon.getGetProductId())) {
                freeItemPrice = item.getPrice();
            }
        }

        int possibleFreeItems = buyCount / coupon.getBuyQuantity();
        int freeQuantity = possibleFreeItems * coupon.getGetQuantity();

        return freeQuantity * freeItemPrice;
    }

    // ================== APPLY COUPON ==================
    public CartResponse applyCoupon(Long couponId, Cart cart) {

        Coupon coupon = getCoupon(couponId);

        double totalPrice = cart.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        double discount = 0;

        if (coupon.getType().name().equals("CART_WISE")) {
            discount = calculateCartWiseDiscount(coupon, cart);
        }

        if (coupon.getType().name().equals("PRODUCT_WISE")) {
            discount = calculateProductWiseDiscount(coupon, cart);
        }

        if (coupon.getType().name().equals("BXGY")) {
            discount = calculateBxGyDiscount(coupon, cart);
        }

        CartResponse response = new CartResponse();
        response.setItems(cart.getItems());
        response.setTotalPrice(totalPrice);
        response.setTotalDiscount(discount);
        response.setFinalPrice(totalPrice - discount);

        return response;
    }
}