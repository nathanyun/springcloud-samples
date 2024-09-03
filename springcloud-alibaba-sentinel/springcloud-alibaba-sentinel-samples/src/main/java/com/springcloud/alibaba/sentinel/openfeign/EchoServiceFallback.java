package com.springcloud.alibaba.sentinel.openfeign;

public class EchoServiceFallback implements EchoService{

    @Override
    public String echo(String str) {
        return "Echo Service Fallback message";
    }
}
