package com.dnanh01.backend.response;

public class PaymentSubmitResponse {
    private String vnpayUrl;
    private String jwt;
    private Long orderId;

    public PaymentSubmitResponse() {
    }

    public PaymentSubmitResponse(String vnpayUrl, String jwt, Long orderId) {
        this.vnpayUrl = vnpayUrl;
        this.jwt = jwt;
        this.orderId = orderId;
    }

    public String getVnpayUrl() {
        return vnpayUrl;
    }

    public void setVnpayUrl(String vnpayUrl) {
        this.vnpayUrl = vnpayUrl;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

}
