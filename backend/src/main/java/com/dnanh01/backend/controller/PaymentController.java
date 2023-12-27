package com.dnanh01.backend.controller;

import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.print.attribute.standard.JobOriginatingUserName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.dnanh01.backend.exception.OrderException;
import com.dnanh01.backend.exception.UserException;
import com.dnanh01.backend.model.Cart;
import com.dnanh01.backend.model.Order;
import com.dnanh01.backend.model.User;
import com.dnanh01.backend.repository.OrderRepository;
import com.dnanh01.backend.request.PaymentRequest;
import com.dnanh01.backend.response.PaymentDeliveredResponse;
import com.dnanh01.backend.response.PaymentResponse;
import com.dnanh01.backend.response.PaymentSubmitResponse;
import com.dnanh01.backend.service.CartServiceImplementation;
import com.dnanh01.backend.service.OrderService;
import com.dnanh01.backend.service.UserService;
import com.dnanh01.backend.service.VNPayService;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartServiceImplementation cartServiceImplementation;

    @Autowired
    private UserService userService;

    // b1
    @PostMapping("/submitOrder")
    @ResponseBody
    public ResponseEntity<PaymentSubmitResponse> submitOrder(
            HttpServletRequest request,
            @RequestHeader("Authorization") String jwt,
            @RequestBody PaymentRequest req)
            throws UserException {

        User user = userService.findUserProfileByJwt(jwt);
        Optional<Order> opt = orderRepository.findById(req.getCurrentOrderId());
        Order orderCreated = new Order();
        if (opt.isPresent()) {
            opt.get();
        }

        Cart cart = cartServiceImplementation.findUserCart(user.getId());
        BigDecimal total = new BigDecimal(cart.getTotalDiscountedPrice());

        String vnpayUrl = vnPayService.createOrder(total, order, baseUrl);

        return ResponseEntity.ok(vnpayUrl);
    }
    // sau bước 1
    // order id hien tai

    // FE (nhận về orderId, vnpayUrl)

    // /b2
    @GetMapping("/vnpay-payment")

    @ResponseBody
    public ResponseEntity<PaymentDeliveredResponse> vnpayPayment(
            HttpServletRequest request,
            @RequestParam("orderId") String ) {
        int paymentStatus = vnPayService.orderReturn(request);

        // gọi orderService để deliveredOrder

        if (paymentStatus == 1) {

            // gọi orderService để deliveredOrder để trừ hàng trong kho\
            // PaymentDeliveredResponse
            return ResponseEntity.ok("success");
        } else {
            // Payment failed, handle accordingly
            return ResponseEntity.ok("cancel");
        }
    }

    // có orderId, vnpayUrl từ b1
    // sau bước 2 
    // if(orderId.equ()) {
        // 
    }
}