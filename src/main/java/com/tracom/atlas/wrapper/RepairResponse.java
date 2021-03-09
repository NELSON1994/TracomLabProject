package com.tracom.atlas.wrapper;

import lombok.Data;

@Data
public class RepairResponse<T> {
    public int code;
    public String message;
    public T Content;
}
