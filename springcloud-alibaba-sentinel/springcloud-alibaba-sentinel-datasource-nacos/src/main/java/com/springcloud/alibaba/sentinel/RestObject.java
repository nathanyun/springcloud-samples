package com.springcloud.alibaba.sentinel;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.net.URI;

@ToString
@Getter
@Setter
@Builder
public class RestObject implements Serializable {
    private URI requestURI;
    private Integer statusCode;
    private String statusMessage;
}
