package com.cc5c.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@TableName("tb_category")
public class Category implements Serializable {
    @TableId("category_id")
    private String categoryId;

    @TableId("category_name")
    private String categoryName;
}