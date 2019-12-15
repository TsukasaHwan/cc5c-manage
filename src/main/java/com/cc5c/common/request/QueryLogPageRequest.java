package com.cc5c.common.request;

import lombok.Data;

import java.util.Date;

@Data
public class QueryLogPageRequest {
    private String userId;
    private String categoryId;
    private Date insertTime;
}
