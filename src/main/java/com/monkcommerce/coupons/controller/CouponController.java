package com.monkcommerce.coupons.controller;

import com.monkcommerce.coupons.entity.Coupon;
import com.monkcommerce.coupons.service.CouponService;
import com.monkcommerce.coupons.dto.Cart;
import com.monkcommerce.coupons.dto.CartResponse;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/coupons")
public class CouponController {

    private final CouponService service;

    public CouponController(CouponService service) {
        this.service = service;
    }

    // ======= CREATE =======
    @PostMapping
    public Coupon createCoupon(@RequestBody Coupon coupon) {
        return service.createCoupon(coupon);
    }

    // ====== GET ALL =====
    @GetMapping
    public List<Coupon> getAllCoupons() {
        return service.getAllCoupons();
    }

    // ======= GET BY ID =====
    @GetMapping("/{id}")
    public Coupon getCoupon(@PathVariable Long id) {
        return service.getCoupon(id);
    }

    // === UPDATE ====
    @PutMapping("/{id}")
    public Coupon updateCoupon(
            @PathVariable Long id,
            @RequestBody Coupon coupon) {

        return service.updateCoupon(id, coupon);
    }

    // ==== DELETE ======
    @DeleteMapping("/{id}")
    public void deleteCoupon(@PathVariable Long id) {
        service.deleteCoupon(id);
    }

    // ===== APPLICABLE COUPONS =======
    @PostMapping("/applicable")
    public List<String> getApplicableCoupons(@RequestBody Cart cart) {

        List<Coupon> coupons = service.getAllCoupons();
        List<String> applicable = new ArrayList<>();

        for (Coupon coupon : coupons) {

            double discount = 0;

            if (coupon.getType().name().equals("CART_WISE")) {
                discount = service.calculateCartWiseDiscount(coupon, cart);
            }

            if (coupon.getType().name().equals("PRODUCT_WISE")) {
                discount = service.calculateProductWiseDiscount(coupon, cart);
            }

            if (coupon.getType().name().equals("BXGY")) {
                discount = service.calculateBxGyDiscount(coupon, cart);
            }

            if (discount > 0) {
                applicable.add("Coupon ID: " + coupon.getId()
                        + " Discount: " + discount);
            }
        }

        return applicable;
    }

    // ====== APPLY COUPON =======
    @PostMapping("/apply/{id}")
    public CartResponse applyCoupon(
            @PathVariable Long id,
            @RequestBody Cart cart) {

        return service.applyCoupon(id, cart);
    }
}