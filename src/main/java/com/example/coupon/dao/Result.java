package com.example.coupon.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Result {
    private int success;
    private int fail;
    private int request;
}
