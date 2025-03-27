package com.example.coupon.service;

import com.example.coupon.dao.Coupon;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class BuyService {

    private final ConcurrentHashMap<Long, Long> buyList = new ConcurrentHashMap<>();


    public void buy(final Long productId) {
        Long count = buyList.getOrDefault(productId, 0L);
        buyList.put(productId, count + 1);
    }

}
