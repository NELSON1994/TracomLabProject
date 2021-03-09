package com.tracom.atlas.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashBoardWrapper {
    private String description;
    private String links;
    private Long total;
}
