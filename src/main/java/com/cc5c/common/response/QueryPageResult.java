package com.cc5c.common.response;

import lombok.Data;
import lombok.ToString;

/**
 * 分页查询响应对象
 *
 * @author 4you
 * @date 2019/7/10
 */
@Data
@ToString
public class QueryPageResult extends ResponseResult {
    private QueryResult queryResult;

    public QueryPageResult(ResultCode resultCode, QueryResult queryResult) {
        super(resultCode);
        this.queryResult = queryResult;
    }
}
