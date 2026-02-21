# Coupons Management API – Monk Commerce Backend Task

##  Overview

This project implements a RESTful API to manage and apply discount coupons for an e-commerce platform.

The system supports:

- Cart-wise coupons
- Product-wise coupons
- Buy X Get Y (BXGY) coupons
- Fetching applicable coupons
- Applying a specific coupon to a cart

The system is designed to allow easy addition of new coupon types in the future.

---

## Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- H2 In-Memory Database
- Maven

---

##  Architecture

Layered Architecture:

- Controller Layer – Handles API requests
- Service Layer – Contains business logic
- Repository Layer – Database access
- DTO Layer – Request/Response objects

---

##  Implemented Coupon Types

###  Cart-wise Coupons

- Condition: Cart total exceeds a defined threshold
- Discount: Percentage applied on entire cart total

**Example:**
10% discount on cart total above 100

---

###  Product-wise Coupons

- Condition: Specific product exists in cart
- Discount: Percentage applied only to that product

**Example:**
20% discount on Product ID 1

---

###  Buy X Get Y (BXGY)

- Condition: Buy X quantity of Product A
- Reward: Get Y quantity of Product B free
- Supports repetition logic
- Discount calculated based on free product price

**Example:**
Buy 2 of Product 1 → Get 1 of Product 2 free

---

// Applicable Coupons API

**Endpoint:**
POST /coupons/applicable

Returns:
- All applicable coupons
- Calculated discount for each coupon

---

// Apply Coupon API

**Endpoint:**
POST /coupons/apply/{id}

Returns:
- Cart items
- Total price
- Total discount
- Final price after applying coupon

---

// Edge Cases Considered

- Null checks for coupon parameters
- Coupon not found handling
- No discount if conditions not met
- Multiple coupons evaluated independently
- Zero quantity handling
- Invalid cart returns zero discount
- Safe division handling in BXGY

---

##  Assumptions

- Only one coupon can be applied at a time
- Prices are always positive
- No tax calculations included
- Cart is passed in request body (not persisted)
- Coupon stacking is not supported
- No user authentication implemented

---

##  Unimplemented Cases (Due to Time Constraints)

- Category-based coupons
- Coupon stacking
- Maximum discount cap
- Expiration date validation
- User-specific coupons
- Coupon usage limits
- Inventory validation
- Multiple BXGY product arrays
- Concurrency handling
- Database persistence beyond in-memory

---

##  Future Improvements

- Implement Strategy Pattern for better extensibility
- Add global exception handling
- Add validation layer
- Add coupon expiration enforcement
- Add priority rules between coupons
- Add unit tests
- Add caching (Redis)
- Persist cart data
- Add coupon usage tracking

---

##  How to Run

```bash
mvn clean install
mvn spring-boot:run