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
@TableName("tb_user")
public class User implements Serializable {
    @TableId("user_id")
    private String userId;
    @TableField("username")
    private String username;
    @TableField("real_name")
    private String realName;
    @TableField("password")
    private String password;
    @TableField("email")
    private String email;
    @TableField("login_ip")
    private String loginIp;
    @TableField("login_date")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date loginDate;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private Date createTime;
    @TableField("description")
    private String description;
    @TableField("status")
    private Integer status;
    @TableField("account_type")
    private Integer accountType;
    @TableField("parent_id")
    private String parentId;
}
