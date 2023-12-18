package com.dnanh01.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dnanh01.backend.dto.RevenueOrProfitStatsDto;
import com.dnanh01.backend.exception.OrderException;
import com.dnanh01.backend.request.SelectedTimeRequest;
import com.dnanh01.backend.service.OrderService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/admin/dashboard")
public class AdminDashboardController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/line-chart/selected-day")
    public ResponseEntity<List<RevenueOrProfitStatsDto>> getStatsForSelectedDayRevenueAndProfit(
            @RequestHeader("Authorization") String jwt,
            @RequestBody SelectedTimeRequest req) throws OrderException {
        List<RevenueOrProfitStatsDto> statsForSelectedDay = orderService.getStatsForSelectedDayRevenueAndProfit(req);
        return ResponseEntity.status(HttpStatus.OK).body(statsForSelectedDay);
    }

}
