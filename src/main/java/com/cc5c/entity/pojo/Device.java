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
@TableName("tb_device")
public class Device implements Serializable {
    @TableId("device_id")
    private String deviceId;

    @TableField("category_id")
    private String categoryId;

    @TableField("device_name")
    private String deviceName;

    @TableField("device_type")
    private String deviceType;

    @TableField("device_no")
    private String deviceNo;

    @TableField("coal_safety_no")
    private String coalSafetyNo;

    @TableField("producer")
    private String producer;

    @TableField("use_place")
    private String usePlace;

    @TableField("into_well_time")
    @JSONField(format = "yyyy-MM-dd")
    private Date intoWellTime;

    @TableField("acceptor_result")
    private String acceptorResult;

    @TableField("install_time")
    @JSONField(format = "yyyy-MM-dd")
    private Date installTime;

    @TableField("install_result")
    private String installResult;

    @TableField("desiccant_replace_time")
    @JSONField(format = "yyyy-MM-dd")
    private Date desiccantReplaceTime;

    @TableField("change_result")
    private String changeResult;

    @TableField("check_time")
    @JSONField(format = "yyyy-MM-dd")
    private Date checkTime;

    @TableField("check_result")
    private String checkResult;

    @TableField("change_time")
    @JSONField(format = "yyyy-MM-dd")
    private Date changeTime;

    @TableField("device_status")
    private Short deviceStatus;

    @TableField("device_up_time")
    @JSONField(format = "yyyy-MM-dd")
    private Date deviceUpTime;

    @TableField("remark")
    private String remark;

    @TableField("users")
    private String users;

    @TableField("use_company")
    private String useCompany;

    @TableField("blast_find_time")
    @JSONField(format = "yyyy-MM-dd")
    private Date blastFindTime;

    @TableField("blast_find_person")
    private String blastFindPerson;

    @TableField("blast_find_info")
    private String blastFindInfo;

    @TableField("device_info")
    private String deviceInfo;

    @TableField("device_test_time")
    @JSONField(format = "yyyy-MM-dd")
    private Date deviceTestTime;

    @TableField("test_person")
    private String testPerson;

    @TableField("blast_info_see_person")
    private String blastInfoSeePerson;

    @TableField("device_record")
    private String deviceRecord;

    @TableField("check_info")
    private String checkInfo;

    @TableField("insert_time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date insertTime;

    @TableField(exist = false)
    private String deviceStatusStr;

    public String getDeviceStatusStr() {
        if (deviceStatus == 0) {
            deviceStatusStr = "在用";
        } else if (deviceStatus == 1) {
            deviceStatusStr = "闲置";
        } else if (deviceStatus == 2){
            deviceStatusStr = "备用";
        } else {
            deviceStatusStr = "";
        }
        return deviceStatusStr;
    }

    public void setDeviceStatusStr(String deviceStatusStr) {
        this.deviceStatusStr = deviceStatusStr;
    }
}