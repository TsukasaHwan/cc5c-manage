package com.cc5c.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cc5c.common.request.QueryLogPageRequest;
import com.cc5c.common.response.CommonCode;
import com.cc5c.common.response.QueryPageResult;
import com.cc5c.common.response.QueryResult;
import com.cc5c.common.response.ResponseResult;
import com.cc5c.entity.pojo.Category;
import com.cc5c.entity.pojo.Log;
import com.cc5c.mapper.CategoryMapper;
import com.cc5c.mapper.LogMapper;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogService {
    @Autowired
    private LogMapper logMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    public ResponseResult list(int pageIndex, int pageSize, QueryLogPageRequest queryLogPageRequest) {
        if (pageIndex <= 0) {
            pageIndex = 1;
        }
        if (pageSize <= 0) {
            pageSize = 10;
        }
        QueryWrapper<Log> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(queryLogPageRequest.getCategoryId())) {
            wrapper.lambda().eq(Log::getCategoryId, queryLogPageRequest.getCategoryId());
        }
        if (StringUtils.isNotBlank(queryLogPageRequest.getUserId())) {
            wrapper.lambda().eq(Log::getUserId, queryLogPageRequest.getUserId());
        }
        if (queryLogPageRequest.getInsertTime() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String timeStr = sdf.format(queryLogPageRequest.getInsertTime());
            String startDate = timeStr + " 00:00:00";
            String endDate = timeStr + " 23:59:59";
            wrapper.lambda().between(Log::getCreateTime, startDate, endDate);
        }
        wrapper.lambda().orderByDesc(Log::getCreateTime);
        PageHelper.startPage(pageIndex, pageSize);
        List<Log> logList = logMapper.selectList(wrapper);
        QueryResult<Log> queryResult = new QueryResult<>(logList);
        List<Log> dataList = queryResult.getDataList();
        dataList = dataList.stream().peek(log -> {
            String categoryId = log.getCategoryId();
            Category category = categoryMapper.selectById(categoryId);
            log.setCategoryName(category.getCategoryName());
        }).collect(Collectors.toList());
        queryResult.setDataList(dataList);
        return new QueryPageResult(CommonCode.SUCCESS, queryResult);
    }
}
