package com.cc5c.entity.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@TableName("tb_log")
public class Log implements Serializable {
    @TableId("log_id")
    private String logId;
    @TableField("user_id")
    private String userId;
    @TableField("category_id")
    private String categoryId;
    @TableField("description")
    private String description;
    @TableField("username")
    private String username;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private Date createTime;

    @TableField(exist = false)
    private String categoryName;
}
