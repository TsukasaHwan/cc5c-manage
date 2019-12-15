package com.cc5c.service;

import com.cc5c.common.response.CommonCode;
import com.cc5c.common.response.QueryResponseResult;
import com.cc5c.common.response.QueryResult;
import com.cc5c.common.response.ResponseResult;
import com.cc5c.entity.pojo.Category;
import com.cc5c.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    public ResponseResult findAll() {
        List<Category> categories = categoryMapper.selectList(null);
        QueryResult<Category> queryResult = new QueryResult<>();
        queryResult.setDataList(categories);
        queryResult.setRowCount(categories.size());
        return new QueryResponseResult(CommonCode.SUCCESS, queryResult);
    }
}
