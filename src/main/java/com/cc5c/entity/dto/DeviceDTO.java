package com.cc5c.entity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.util.Date;

@Data
public class DeviceDTO extends BaseRowModel {
    private String deviceId;

    private String categoryId;

    @ExcelProperty(value = "设备名称", index = 0)
    private String deviceName;

    @ExcelProperty(value = "设备型号", index = 1)
    private String deviceType;

    @ExcelProperty(value = "设备编号", index = 2)
    private String deviceNo;

    @ExcelProperty(value = "煤安编号", index = 3)
    private String coalSafetyNo;

    @ExcelProperty(value = "制造厂家", index = 4)
    private String producer;

    @ExcelProperty(value = "使用地点", index = 5)
    private String usePlace;

    @ExcelProperty(value = "使用单位", index = 6)
    private String useCompany;

    @ExcelProperty(value = "包机人", index = 7)
    private String users;

    @ExcelProperty(value = "设备资料情况", index = 8)
    private String deviceInfo;

    @ExcelProperty(value = "入井时间", index = 9, format = "yyyy/MM/dd")
    private Date intoWellTime;

    @ExcelProperty(value = "入井验收人及验收结果", index = 10)
    private String acceptorResult;

    @ExcelProperty(value = "安装时间", index = 11, format = "yyyy/MM/dd")
    private Date installTime;

    @ExcelProperty(value = "安装验收人及验收结果", index = 12)
    private String installResult;

    @ExcelProperty(value = "防爆检查时间", index = 13, format = "yyyy/MM/dd")
    private Date blastFindTime;

    @ExcelProperty(value = "防爆检查人", index = 14)
    private String blastFindPerson;

    @ExcelProperty(value = "防爆检查情况", index = 15)
    private String blastFindInfo;

    @ExcelProperty(value = "防爆检查问题整改情况及验收人", index = 16)
    private String blastInfoSeePerson;

    @ExcelProperty(value = "设备定期试验时间", index = 17, format = "yyyy/MM/dd")
    private Date deviceTestTime;

    @ExcelProperty(value = "试验人及试验结果", index = 18)
    private String testPerson;

    @ExcelProperty(value = "干燥剂更换时间", index = 19, format = "yyyy/MM/dd")
    private Date desiccantReplaceTime;

    @ExcelProperty(value = "更换人及更换结果", index = 20)
    private String changeResult;

    @ExcelProperty(value = "设备检修时间", index = 21, format = "yyyy/MM/dd")
    private Date checkTime;

    @ExcelProperty(value = "设备日常检修情况", index = 22)
    private String checkInfo;

    @ExcelProperty(value = "检修人及检修结果", index = 23)
    private String checkResult;

    @ExcelProperty(value = "设备事故记录", index = 24)
    private String deviceRecord;

    @ExcelProperty(value = "防爆合格证、完好牌、整定牌更换日期", index = 25, format = "yyyy/MM/dd")
    private Date changeTime;

    @ExcelProperty(value = "设备状态（在用/闲置/备用）", index = 26)
    private String deviceStatusStr;

    @ExcelProperty(value = "设备升井时间", index = 27, format = "yyyy/MM/dd")
    private Date deviceUpTime;

    @ExcelProperty(value = "备注", index = 28)
    private String remark;
}
