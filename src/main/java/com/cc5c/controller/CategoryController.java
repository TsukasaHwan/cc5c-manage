package com.cc5c.controller;

import com.cc5c.api.CategoryControllerApi;
import com.cc5c.common.response.ResponseResult;
import com.cc5c.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryController implements CategoryControllerApi {
    @Autowired
    private CategoryService categoryService;

    @Override
    @GetMapping("/findAll")
    public ResponseResult findAll() {
        return categoryService.findAll();
    }
}
