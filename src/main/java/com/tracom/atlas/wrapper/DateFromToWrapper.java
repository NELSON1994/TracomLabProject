package com.tracom.atlas.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class DateFromToWrapper {
    private Date from;
    private Date to;
}
