package com.tracom.atlas.wrapper;

import lombok.Data;

@Data
public class ErrorResponse <T>{
    public int statusCode;
    public T message;
    public Long timestamp;
}
