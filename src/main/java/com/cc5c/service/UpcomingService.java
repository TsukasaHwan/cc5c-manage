package com.cc5c.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cc5c.common.exception.ExceptionCast;
import com.cc5c.common.response.CommonCode;
import com.cc5c.common.response.QueryPageResult;
import com.cc5c.common.response.QueryResult;
import com.cc5c.common.response.ResponseResult;
import com.cc5c.common.response.code.UserCode;
import com.cc5c.entity.pojo.*;
import com.cc5c.mapper.CategoryMapper;
import com.cc5c.mapper.DeviceHistoryMapper;
import com.cc5c.mapper.DeviceMapper;
import com.cc5c.mapper.LogMapper;
import com.cc5c.utils.UUIDUtil;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class UpcomingService {
    @Autowired
    private DeviceHistoryMapper deviceHistoryMapper;
    @Autowired
    private LogMapper logMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DeviceMapper deviceMapper;

    public ResponseResult findPage(int pageIndex, int pageSize) {
        if (pageIndex <= 0) {
            pageIndex = 1;
        }
        if (pageSize <= 0) {
            pageSize = 10;
        }
        QueryWrapper<DeviceHistory> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(DeviceHistory::getIsUpcoming, 0);
        PageHelper.startPage(pageIndex, pageSize);
        List<DeviceHistory> deviceHistories = deviceHistoryMapper.selectList(wrapper);
        QueryResult<DeviceHistory> queryResult = new QueryResult<>(deviceHistories);
        return new QueryPageResult(CommonCode.SUCCESS, queryResult);
    }

    public DeviceHistory findOne(String deviceHistoryId) {
        if (StringUtils.isBlank(deviceHistoryId)) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        DeviceHistory deviceHistory = deviceHistoryMapper.selectById(deviceHistoryId);
        if (deviceHistory == null || deviceHistory.getIsUpcoming().equals(1)) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        return deviceHistory;
    }

    public ResponseResult edit(DeviceHistory deviceHistory) {
        DeviceHistory one = findOne(deviceHistory.getDeviceHistoryId());
        if (one == null) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        one.setIsUpcoming(Short.valueOf("1"));
        deviceHistoryMapper.updateById(one);
        Device device = deviceMapper.selectById(one.getDeviceId());
        return addLog(deviceHistory, device);
    }

    public ResponseResult delete(String deviceHistoryId) {
        DeviceHistory one = findOne(deviceHistoryId);
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (user == null) {
            return new ResponseResult(UserCode.UN_LOGIN);
        }
        if (one != null) {
            deviceHistoryMapper.deleteById(one.getDeviceHistoryId());
            deleteLog(one, user);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    private ResponseResult addLog(DeviceHistory deviceHistory, Device oldDevice) {
        Log log = new Log();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (user == null) {
            return new ResponseResult(UserCode.UN_LOGIN);
        }
        log.setLogId(UUIDUtil.getUUID());
        log.setCategoryId(oldDevice.getCategoryId());
        String oldCategoryId = oldDevice.getCategoryId();
        Category oldCategory = categoryMapper.selectById(oldCategoryId);
        StringBuilder sb = new StringBuilder("修改类目");
        sb.append(oldCategory.getCategoryName()).append("待办事项").append("：");
        String categoryId = deviceHistory.getCategoryId();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (!oldDevice.getCategoryId().equals(deviceHistory.getCategoryId())) {
            Category category = categoryMapper.selectById(categoryId);
            sb.append("设备所属类别从")
                    .append(StringUtils.isBlank(oldCategory.getCategoryName()) ? "无" : oldCategory.getCategoryName())
                    .append("变更为").append(StringUtils.isBlank(category.getCategoryName()) ? "无" : category.getCategoryName()).append(",");
        }
        if (StringUtils.isNotBlank(deviceHistory.getDeviceName()) && (oldDevice.getDeviceName() == null || !oldDevice.getDeviceName().equals(deviceHistory.getDeviceName()))) {
            sb.append("设备名称从")
                    .append(StringUtils.isBlank(oldDevice.getDeviceName()) ? "无" : oldDevice.getDeviceName()).append("变更为")
                    .append(deviceHistory.getDeviceName()).append(",");
        }
        if (StringUtils.isNotBlank(deviceHistory.getDeviceType()) && (oldDevice.getDeviceType() == null || !oldDevice.getDeviceType().equals(deviceHistory.getDeviceType()))) {
            sb.append("设备型号从")
                    .append(StringUtils.isBlank(oldDevice.getDeviceType()) ? "无" : oldDevice.getDeviceType()).append("变更为")
                    .append(deviceHistory.getDeviceType()).append(",");
        }
        if (StringUtils.isNotBlank(deviceHistory.getDeviceNo()) && (oldDevice.getDeviceNo() == null || !oldDevice.getDeviceNo().equals(deviceHistory.getDeviceNo()))) {
            sb.append("设备编号从")
                    .append(StringUtils.isBlank(oldDevice.getDeviceNo()) ? "无" : oldDevice.getDeviceNo()).append("变更为")
                    .append(deviceHistory.getDeviceNo()).append(",");
        }
        if (StringUtils.isNotBlank(deviceHistory.getCoalSafetyNo()) && (oldDevice.getCoalSafetyNo() == null || !oldDevice.getCoalSafetyNo().equals(deviceHistory.getCoalSafetyNo()))) {
            sb.append("煤安编号从")
                    .append(StringUtils.isBlank(oldDevice.getCoalSafetyNo()) ? "无" : oldDevice.getCoalSafetyNo()).append("变更为")
                    .append(deviceHistory.getCoalSafetyNo()).append(",");
        }
        if (StringUtils.isNotBlank(deviceHistory.getProducer()) && (oldDevice.getProducer() == null || !oldDevice.getProducer().equals(deviceHistory.getProducer()))) {
            sb.append("制造厂家从")
                    .append(StringUtils.isBlank(oldDevice.getProducer()) ? "无" : oldDevice.getProducer()).append("变更为")
                    .append(deviceHistory.getProducer()).append(",");
        }
        if (StringUtils.isNotBlank(deviceHistory.getUsePlace()) && (oldDevice.getUsePlace() == null || !oldDevice.getUsePlace().equals(deviceHistory.getUsePlace()))) {
            sb.append("使用地点从")
                    .append(StringUtils.isBlank(oldDevice.getUsePlace()) ? "无" : oldDevice.getUsePlace()).append("变更为")
                    .append(deviceHistory.getUsePlace()).append(",");
        }
        if (StringUtils.isNotBlank(deviceHistory.getUseCompany()) && (oldDevice.getUseCompany() == null || !oldDevice.getUseCompany().equals(deviceHistory.getUseCompany()))) {
            sb.append("使用单位从")
                    .append(StringUtils.isBlank(oldDevice.getUseCompany()) ? "无" : oldDevice.getUseCompany()).append("变更为")
                    .append(deviceHistory.getUseCompany()).append(",");
        }
        if (StringUtils.isNotBlank(deviceHistory.getUsers()) && (oldDevice.getUsers() == null || !oldDevice.getUsers().equals(deviceHistory.getUsers()))) {
            sb.append("包机人从")
                    .append(StringUtils.isBlank(oldDevice.getUsers()) ? "无" : oldDevice.getUsers()).append("变更为")
                    .append(deviceHistory.getUsers()).append(",");
        }
        if (StringUtils.isNotBlank(deviceHistory.getDeviceInfo()) && (oldDevice.getDeviceInfo() == null || !oldDevice.getDeviceInfo().equals(deviceHistory.getDeviceInfo()))) {
            sb.append("设备资料情况从")
                    .append(StringUtils.isBlank(oldDevice.getDeviceInfo()) ? "无" : oldDevice.getDeviceInfo()).append("变更为")
                    .append(deviceHistory.getDeviceInfo()).append(",");
        }
        if (deviceHistory.getIntoWellTime() != null && (oldDevice.getIntoWellTime() == null || oldDevice.getIntoWellTime().getTime() != deviceHistory.getIntoWellTime().getTime())) {
            sb.append("入井时间从")
                    .append(oldDevice.getIntoWellTime() == null ? "无" : sdf.format(oldDevice.getIntoWellTime())).append("变更为")
                    .append(sdf.format(deviceHistory.getIntoWellTime())).append(",");
        }
        if (StringUtils.isNotBlank(oldDevice.getAcceptorResult()) && (oldDevice.getAcceptorResult() == null || !oldDevice.getAcceptorResult().equals(deviceHistory.getAcceptorResult()))) {
            sb.append("入井验收人及验收结果从")
                    .append(StringUtils.isBlank(oldDevice.getAcceptorResult()) ? "无" : oldDevice.getAcceptorResult()).append("变更为")
                    .append(deviceHistory.getAcceptorResult()).append(",");
        }
        if (deviceHistory.getInstallTime() != null && (oldDevice.getInstallTime() == null || oldDevice.getInstallTime().getTime() != deviceHistory.getInstallTime().getTime())) {
            sb.append("安装时间从")
                    .append(oldDevice.getInstallTime() == null ? "无" : sdf.format(oldDevice.getInstallTime())).append("变更为")
                    .append(sdf.format(deviceHistory.getInstallTime())).append(",");
        }
        if (StringUtils.isNotBlank(deviceHistory.getInstallResult()) && (oldDevice.getInstallResult() == null || !oldDevice.getInstallResult().equals(deviceHistory.getInstallResult()))) {
            sb.append("安装验收人及验收结果从")
                    .append(StringUtils.isBlank(oldDevice.getInstallResult()) ? "无" : oldDevice.getInstallResult()).append("变更为")
                    .append(deviceHistory.getInstallResult()).append(",");
        }
        if (deviceHistory.getBlastFindTime() != null && (oldDevice.getBlastFindTime() == null || oldDevice.getBlastFindTime().getTime() != deviceHistory.getBlastFindTime().getTime())) {
            sb.append("防爆检查时间从")
                    .append(oldDevice.getBlastFindTime() == null ? "无" : sdf.format(oldDevice.getBlastFindTime())).append("变更为")
                    .append(sdf.format(deviceHistory.getBlastFindTime())).append(",");
        }
        if (StringUtils.isNotBlank(deviceHistory.getBlastFindPerson()) && (oldDevice.getBlastFindPerson() == null || !oldDevice.getBlastFindPerson().equals(deviceHistory.getBlastFindPerson()))) {
            sb.append("防爆检查人从")
                    .append(StringUtils.isBlank(oldDevice.getBlastFindPerson()) ? "无" : oldDevice.getBlastFindPerson()).append("变更为")
                    .append(deviceHistory.getBlastFindPerson()).append(",");
        }
        if (StringUtils.isNotBlank(deviceHistory.getBlastFindInfo()) && (oldDevice.getBlastFindInfo() == null || !oldDevice.getBlastFindInfo().equals(deviceHistory.getBlastFindInfo()))) {
            sb.append("防爆检查情况从")
                    .append(StringUtils.isBlank(oldDevice.getBlastFindInfo()) ? "无" : oldDevice.getBlastFindInfo()).append("变更为")
                    .append(deviceHistory.getBlastFindInfo()).append(",");
        }
        if (StringUtils.isNotBlank(deviceHistory.getBlastInfoSeePerson()) && (oldDevice.getBlastInfoSeePerson() == null || !oldDevice.getBlastInfoSeePerson().equals(deviceHistory.getBlastInfoSeePerson()))) {
            sb.append("防爆检查问题整改情况及验收人从")
                    .append(StringUtils.isBlank(oldDevice.getBlastInfoSeePerson()) ? "无" : oldDevice.getBlastInfoSeePerson()).append("变更为")
                    .append(deviceHistory.getBlastInfoSeePerson()).append(",");
        }
        if (deviceHistory.getDeviceTestTime() != null && (oldDevice.getDeviceTestTime() == null || oldDevice.getDeviceTestTime().getTime() != deviceHistory.getDeviceTestTime().getTime())) {
            sb.append("设备定期试验时间从")
                    .append(oldDevice.getDeviceTestTime() == null ? "无" : sdf.format(oldDevice.getDeviceTestTime())).append("变更为")
                    .append(sdf.format(deviceHistory.getDeviceTestTime())).append(",");
        }
        if (StringUtils.isNotBlank(deviceHistory.getTestPerson()) && (oldDevice.getTestPerson() == null || !oldDevice.getTestPerson().equals(deviceHistory.getTestPerson()))) {
            sb.append("试验人及试验结果从")
                    .append(StringUtils.isBlank(oldDevice.getTestPerson()) ? "无" : oldDevice.getTestPerson()).append("变更为")
                    .append(deviceHistory.getTestPerson()).append(",");
        }
        if (deviceHistory.getDesiccantReplaceTime() != null && (oldDevice.getDesiccantReplaceTime() == null || oldDevice.getDesiccantReplaceTime().getTime() != deviceHistory.getDesiccantReplaceTime().getTime())) {
            sb.append("干燥剂更换时间从")
                    .append(oldDevice.getDesiccantReplaceTime() == null ? "无" : sdf.format(oldDevice.getDesiccantReplaceTime())).append("变更为")
                    .append(sdf.format(deviceHistory.getDesiccantReplaceTime())).append(",");
        }
        if (deviceHistory.getCheckTime() != null && (oldDevice.getCheckTime() == null || oldDevice.getCheckTime().getTime() != deviceHistory.getCheckTime().getTime())) {
            sb.append("设备检修时间从")
                    .append(oldDevice.getCheckTime() == null ? "无" : sdf.format(oldDevice.getCheckTime())).append("变更为")
                    .append(sdf.format(deviceHistory.getCheckTime())).append(",");
        }
        if (StringUtils.isNotBlank(deviceHistory.getCheckInfo()) && (oldDevice.getCheckInfo() == null || !oldDevice.getCheckInfo().equals(deviceHistory.getCheckInfo()))) {
            sb.append("设备日常检修情况从")
                    .append(StringUtils.isBlank(oldDevice.getCheckInfo()) ? "无" : oldDevice.getCheckInfo()).append("变更为")
                    .append(deviceHistory.getCheckInfo()).append(",");
        }
        if (StringUtils.isNotBlank(deviceHistory.getDeviceRecord()) && (oldDevice.getDeviceRecord() == null || !oldDevice.getDeviceRecord().equals(deviceHistory.getDeviceRecord()))) {
            sb.append("设备事故记录从")
                    .append(StringUtils.isBlank(oldDevice.getDeviceRecord()) ? "无" : oldDevice.getDeviceRecord()).append("变更为")
                    .append(deviceHistory.getDeviceRecord()).append(",");
        }
        if (!oldDevice.getDeviceStatus().equals(deviceHistory.getDeviceStatus())) {
            String statusStr = "";
            if (deviceHistory.getDeviceStatus() == 0) {
                statusStr = "在用";
            } else if (deviceHistory.getDeviceStatus() == 1) {
                statusStr = "闲置";
            } else if (deviceHistory.getDeviceStatus() == 2) {
                statusStr = "备用";
            }
            sb.append("设备状态从").append(oldDevice.getDeviceStatusStr()).append("变更为").append(statusStr).append(",");
        }
        if (deviceHistory.getDeviceUpTime() != null && (oldDevice.getDeviceUpTime() == null || oldDevice.getDeviceUpTime().getTime() != deviceHistory.getDeviceUpTime().getTime())) {
            sb.append("设备升井时间从")
                    .append(oldDevice.getDeviceUpTime() == null ? "无" : sdf.format(oldDevice.getDeviceUpTime())).append("变更为")
                    .append(sdf.format(deviceHistory.getDeviceUpTime())).append(",");
        }
        if (StringUtils.isNotBlank(deviceHistory.getRemark()) && (oldDevice.getRemark() == null || !oldDevice.getRemark().equals(deviceHistory.getRemark()))) {
            sb.append("备注从")
                    .append(oldDevice.getRemark() == null || StringUtils.isBlank(oldDevice.getRemark()) ? "无" : oldDevice.getRemark()).append("变更为")
                    .append(deviceHistory.getRemark() == null || StringUtils.isBlank(deviceHistory.getRemark()) ? "无" : deviceHistory.getRemark()).append(",");
        }
        if (deviceHistory.getChangeTime() != null && (oldDevice.getChangeTime() == null || oldDevice.getChangeTime().getTime() != deviceHistory.getChangeTime().getTime())) {
            sb.append("防爆合格证、完好牌、整定牌更换日期(半年)从")
                    .append(oldDevice.getChangeTime() == null ? "无" : sdf.format(oldDevice.getChangeTime())).append("变更为")
                    .append(sdf.format(deviceHistory.getChangeTime())).append(",");
        }
        String description = sb.toString();
        int length = description.length();
        description = description.substring(0, length - 1);
        log.setDescription(description);
        log.setUserId(user.getUserId());
        log.setUsername(user.getUsername());
        log.setCreateTime(new Date());
        logMapper.insert(log);

        oldDevice.setChangeResult(deviceHistory.getChangeResult());
        oldDevice.setCheckResult(deviceHistory.getCheckResult());
        oldDevice.setChangeTime(deviceHistory.getChangeTime());
        oldDevice.setDeviceName(deviceHistory.getDeviceName());
        oldDevice.setDeviceType(deviceHistory.getDeviceType());
        oldDevice.setDeviceNo(deviceHistory.getDeviceNo());
        oldDevice.setCoalSafetyNo(deviceHistory.getCoalSafetyNo());
        oldDevice.setProducer(deviceHistory.getProducer());
        oldDevice.setUsePlace(deviceHistory.getUsePlace());
        oldDevice.setIntoWellTime(deviceHistory.getIntoWellTime());
        oldDevice.setAcceptorResult(deviceHistory.getAcceptorResult());
        oldDevice.setInstallTime(deviceHistory.getInstallTime());
        oldDevice.setInstallResult(deviceHistory.getInstallResult());
        oldDevice.setDesiccantReplaceTime(deviceHistory.getDesiccantReplaceTime());
        oldDevice.setCheckTime(deviceHistory.getCheckTime());
        oldDevice.setDeviceStatus(deviceHistory.getDeviceStatus());
        oldDevice.setDeviceUpTime(deviceHistory.getDeviceUpTime());
        oldDevice.setRemark(deviceHistory.getRemark());

        //新增字段
        oldDevice.setUseCompany(deviceHistory.getUseCompany());
        oldDevice.setUsers(deviceHistory.getUsers());
        oldDevice.setDeviceInfo(deviceHistory.getDeviceInfo());
        oldDevice.setBlastFindTime(deviceHistory.getBlastFindTime());
        oldDevice.setBlastFindPerson(deviceHistory.getBlastFindPerson());
        oldDevice.setBlastFindInfo(deviceHistory.getBlastFindInfo());
        oldDevice.setBlastInfoSeePerson(deviceHistory.getBlastInfoSeePerson());
        oldDevice.setDeviceTestTime(deviceHistory.getDeviceTestTime());
        oldDevice.setTestPerson(deviceHistory.getTestPerson());
        oldDevice.setCheckInfo(deviceHistory.getCheckInfo());
        oldDevice.setDeviceRecord(deviceHistory.getDeviceRecord());
        deviceMapper.updateById(oldDevice);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    private void deleteLog(DeviceHistory deviceHistory, User user) {
        Log log = new Log();
        log.setLogId(UUIDUtil.getUUID());
        log.setUserId(user.getUserId());
        log.setCategoryId(deviceHistory.getCategoryId());
        log.setUsername(user.getUsername());
        log.setCreateTime(new Date());
        String categoryId = deviceHistory.getCategoryId();
        Category category = categoryMapper.selectById(categoryId);
        String description = "删除了" + category.getCategoryName() + "类目的待办事项" + deviceHistory.getDeviceName() + "设备";
        log.setDescription(description);
        logMapper.insert(log);
    }
}
