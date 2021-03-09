package com.tracom.atlas.wrapper;

import lombok.Data;

import java.util.List;

@Data
public class DeliveryWrapper {
    private List<String> ids;
    private String deliveryStatus;
    private String location;
    private String deliveredBy;
}
