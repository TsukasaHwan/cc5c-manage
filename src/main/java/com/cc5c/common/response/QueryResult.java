package com.cc5c.common.response;

import com.github.pagehelper.PageInfo;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 分页数据封装
 *
 * @author 4you
 * @date 2019/7/10
 */
@Data
@ToString
public class QueryResult<T> {
    /**
     * 返回数据
     */
    private List<T> dataList;
    /**
     * 总页数
     */
    private int pageCount;
    /**
     * 当前页
     */
    private int pageNum;
    /**
     * 页大小
     */
    private int pageSize;
    /**
     * 总页数
     */
    private long rowCount;

    public QueryResult(List<T> list) {
        PageInfo<T> pageInfo = new PageInfo<>(list);
        this.dataList = pageInfo.getList();
        this.pageCount = pageInfo.getPages();
        this.pageNum = pageInfo.getPageNum();
        this.pageSize = pageInfo.getPageSize();
        this.rowCount = pageInfo.getTotal();
    }

    public QueryResult() {}
}
