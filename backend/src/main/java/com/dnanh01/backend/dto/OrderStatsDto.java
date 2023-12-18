package com.dnanh01.backend.dto;

public class OrderStatsDto {
    private String selectedTime;
    private Integer quantityOrder;

    public OrderStatsDto() {
    }

    public OrderStatsDto(String selectedTime, Integer quantityOrder) {
        this.selectedTime = selectedTime;
        this.quantityOrder = quantityOrder;
    }

    public String getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(String selectedTime) {
        this.selectedTime = selectedTime;
    }

    public Integer getQuantityOrder() {
        return quantityOrder;
    }

    public void setQuantityOrder(Integer quantityOrder) {
        this.quantityOrder = quantityOrder;
    }

}
