package com.cc5c.common.request;

import lombok.Data;

import java.util.Date;

@Data
public class QueryPageRequest {
    private String categoryId;
    private String usePlace;
    private Date checkTime;
    private Short deviceStatus;
}
