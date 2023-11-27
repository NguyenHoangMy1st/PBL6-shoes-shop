package com.dnanh01.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dnanh01.backend.exception.CartItemException;
import com.dnanh01.backend.exception.ProductException;
import com.dnanh01.backend.exception.UserException;
import com.dnanh01.backend.model.Cart;
import com.dnanh01.backend.model.User;
import com.dnanh01.backend.request.AddItemRequest;
import com.dnanh01.backend.response.ApiResponse;
import com.dnanh01.backend.service.CartService;
import com.dnanh01.backend.service.UserService;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ResponseEntity<Cart> findUserCart(
            @RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserProfileByJwt(jwt);
        Cart cart = cartService.findUserCart(user.getId());
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PutMapping("/add")
    public ResponseEntity<ApiResponse> addItemToCart(
            @RequestBody AddItemRequest req,
            @RequestHeader("Authorization") String jwt) throws UserException, ProductException, CartItemException {
        User user = userService.findUserProfileByJwt(jwt);
        cartService.addCartItem(user.getId(), req);

        ApiResponse res = new ApiResponse();
        res.setMessage("Item added to cart!");
        res.setStatus(true);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}
