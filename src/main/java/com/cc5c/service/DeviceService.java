package com.cc5c.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cc5c.common.exception.ExceptionCast;
import com.cc5c.common.request.QueryPageRequest;
import com.cc5c.common.response.CommonCode;
import com.cc5c.common.response.QueryPageResult;
import com.cc5c.common.response.QueryResult;
import com.cc5c.common.response.ResponseResult;
import com.cc5c.common.response.code.FileCode;
import com.cc5c.common.response.code.UserCode;
import com.cc5c.entity.dto.DeviceDTO;
import com.cc5c.entity.pojo.Category;
import com.cc5c.entity.pojo.Device;
import com.cc5c.entity.pojo.Log;
import com.cc5c.entity.pojo.User;
import com.cc5c.mapper.CategoryMapper;
import com.cc5c.mapper.DeviceMapper;
import com.cc5c.mapper.LogMapper;
import com.cc5c.utils.ExcelUtil;
import com.cc5c.utils.UUIDUtil;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class DeviceService {
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private LogMapper logMapper;

    public ResponseResult findPage(int pageIndex, int pageSize, QueryPageRequest queryPageRequest) {
        //设备名称  设备地点 设备检修时间  设备状态
        if (queryPageRequest == null) {
            queryPageRequest = new QueryPageRequest();
        }
        if (pageIndex <= 0) {
            pageIndex = 1;
        }
        if (pageSize <= 0) {
            pageSize = 10;
        }
        PageHelper.startPage(pageIndex, pageSize);
        QueryWrapper<Device> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(queryPageRequest.getCategoryId())) {
            wrapper.lambda().eq(Device::getCategoryId, queryPageRequest.getCategoryId());
        }
        if (StringUtils.isNotBlank(queryPageRequest.getUsePlace())) {
            wrapper.lambda().like(Device::getUsePlace, queryPageRequest.getUsePlace());
        }
        if (queryPageRequest.getDeviceStatus() != null) {
            wrapper.lambda().eq(Device::getDeviceStatus, queryPageRequest.getDeviceStatus());
        }
        if (queryPageRequest.getCheckTime() != null) {
            wrapper.lambda().eq(Device::getCheckTime, queryPageRequest.getCheckTime());
        }
        wrapper.lambda().orderByDesc(Device::getInsertTime);
        List<Device> list = deviceMapper.selectList(wrapper);
        QueryResult<Device> queryResult = new QueryResult<>(list);
        return new QueryPageResult(CommonCode.SUCCESS, queryResult);
    }

    public Device findOne(String deviceId) {
        if (StringUtils.isBlank(deviceId)) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        return deviceMapper.selectById(deviceId);
    }

    public ResponseResult add(Device device) {
        device.setDeviceId(UUIDUtil.getUUID());
        device.setInsertTime(new Date());
        deviceMapper.insert(device);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    public ResponseResult edit(Device device) {
        Device one = findOne(device.getDeviceId());
        if (StringUtils.isNotBlank(device.getDeviceId())) {
            //change_result check_result change_time
            device.setInsertTime(one.getInsertTime());
            return addLog(device, one);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    public ResponseResult delete(String id) {
        if (StringUtils.isBlank(id)) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (user == null) {
            return new ResponseResult(UserCode.UN_LOGIN);
        }
        Device one = findOne(id);
        if (one == null) {
            return ResponseResult.FAIL();
        }
        deleteLog(one, user);
        deviceMapper.deleteById(id);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    public ResponseResult importExcel(MultipartFile file, String categoryId) {
        if (StringUtils.isBlank(categoryId)) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        String filename = file.getOriginalFilename();
        String suffix = filename.split("\\.")[1];
        if (!("xls".equals(suffix) || "xlsx".equals(suffix))) {
            ExceptionCast.cast(FileCode.FILE_TYPE_ERROR);
        }
        List<DeviceDTO> deviceDTOS = null;
        try {
            deviceDTOS = ExcelUtil.readExcel(file.getInputStream(), DeviceDTO.class);
        } catch (IOException e) {
            return new ResponseResult(CommonCode.FAIL);
        }
        if (deviceDTOS.size() <= 0) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        Device device = null;
        for (DeviceDTO deviceDTO : deviceDTOS) {
            device = new Device();
            deviceDTO.setDeviceId(UUIDUtil.getUUID());
            deviceDTO.setCategoryId(categoryId);
            device.setDeviceId(deviceDTO.getDeviceId());
            device.setCategoryId(deviceDTO.getCategoryId());
            device.setDeviceName(deviceDTO.getDeviceName());
            device.setDeviceType(deviceDTO.getDeviceType());
            device.setDeviceNo(deviceDTO.getDeviceNo());
            device.setCoalSafetyNo(deviceDTO.getCoalSafetyNo());
            device.setProducer(deviceDTO.getProducer());
            device.setUsePlace(deviceDTO.getUsePlace());
            device.setIntoWellTime(deviceDTO.getIntoWellTime());
            device.setAcceptorResult(deviceDTO.getAcceptorResult());
            device.setInstallTime(deviceDTO.getInstallTime());
            device.setInstallResult(deviceDTO.getInstallResult());
            device.setDesiccantReplaceTime(deviceDTO.getDesiccantReplaceTime());
            device.setCheckTime(deviceDTO.getCheckTime());
            if ("在用".equals(deviceDTO.getDeviceStatusStr())) {
                device.setDeviceStatus(Short.valueOf("0"));
            } else if ("闲置".equals(deviceDTO.getDeviceStatusStr())) {
                device.setDeviceStatus(Short.valueOf("1"));
            } else {
                device.setDeviceStatus(Short.valueOf("2"));
            }
            device.setDeviceUpTime(deviceDTO.getDeviceUpTime());
            device.setRemark(deviceDTO.getRemark());
            device.setInsertTime(new Date());
            device.setChangeResult(deviceDTO.getChangeResult());
            device.setCheckResult(deviceDTO.getCheckResult());
            device.setChangeTime(deviceDTO.getChangeTime());

            //新增字段
            device.setUseCompany(deviceDTO.getUseCompany());
            device.setUsers(deviceDTO.getUsers());
            device.setDeviceInfo(deviceDTO.getDeviceInfo());
            device.setBlastFindTime(deviceDTO.getBlastFindTime());
            device.setBlastFindPerson(deviceDTO.getBlastFindPerson());
            device.setBlastFindInfo(deviceDTO.getBlastFindInfo());
            device.setBlastInfoSeePerson(deviceDTO.getBlastInfoSeePerson());
            device.setDeviceTestTime(deviceDTO.getDeviceTestTime());
            device.setTestPerson(deviceDTO.getTestPerson());
            device.setCheckInfo(deviceDTO.getCheckInfo());
            device.setDeviceRecord(deviceDTO.getDeviceRecord());
            deviceMapper.insert(device);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    private ResponseResult addLog(Device device, Device oldDevice) {
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
        sb.append(oldCategory.getCategoryName()).append("：");
        String categoryId = device.getCategoryId();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (!oldDevice.getCategoryId().equals(device.getCategoryId())) {
            Category category = categoryMapper.selectById(categoryId);
            sb.append("设备所属类别从")
                    .append(StringUtils.isBlank(oldCategory.getCategoryName()) ? "无" : oldCategory.getCategoryName())
                    .append("变更为").append(StringUtils.isBlank(category.getCategoryName()) ? "无" : category.getCategoryName()).append(",");
        }
        if (StringUtils.isNotBlank(device.getDeviceName()) && (oldDevice.getDeviceName() == null || !oldDevice.getDeviceName().equals(device.getDeviceName()))) {
            sb.append("设备名称从")
                    .append(StringUtils.isBlank(oldDevice.getDeviceName()) ? "无" : oldDevice.getDeviceName()).append("变更为")
                    .append(device.getDeviceName()).append(",");
        }
        if (StringUtils.isNotBlank(device.getDeviceType()) && (oldDevice.getDeviceType() == null || !oldDevice.getDeviceType().equals(device.getDeviceType()))) {
            sb.append("设备型号从")
                    .append(StringUtils.isBlank(oldDevice.getDeviceType()) ? "无" : oldDevice.getDeviceType()).append("变更为")
                    .append(device.getDeviceType()).append(",");
        }
        if (StringUtils.isNotBlank(device.getDeviceNo()) && (oldDevice.getDeviceNo() == null || !oldDevice.getDeviceNo().equals(device.getDeviceNo()))) {
            sb.append("设备编号从")
                    .append(StringUtils.isBlank(oldDevice.getDeviceNo()) ? "无" : oldDevice.getDeviceNo()).append("变更为")
                    .append(device.getDeviceNo()).append(",");
        }
        if (StringUtils.isNotBlank(device.getCoalSafetyNo()) && (oldDevice.getCoalSafetyNo() == null || !oldDevice.getCoalSafetyNo().equals(device.getCoalSafetyNo()))) {
            sb.append("煤安编号从")
                    .append(StringUtils.isBlank(oldDevice.getCoalSafetyNo()) ? "无" : oldDevice.getCoalSafetyNo()).append("变更为")
                    .append(device.getCoalSafetyNo()).append(",");
        }
        if (StringUtils.isNotBlank(device.getProducer()) && (oldDevice.getProducer() == null || !oldDevice.getProducer().equals(device.getProducer()))) {
            sb.append("制造厂家从")
                    .append(StringUtils.isBlank(oldDevice.getProducer()) ? "无" : oldDevice.getProducer()).append("变更为")
                    .append(device.getProducer()).append(",");
        }
        if (StringUtils.isNotBlank(device.getUsePlace()) && (oldDevice.getUsePlace() == null || !oldDevice.getUsePlace().equals(device.getUsePlace()))) {
            sb.append("使用地点从")
                    .append(StringUtils.isBlank(oldDevice.getUsePlace()) ? "无" : oldDevice.getUsePlace()).append("变更为")
                    .append(device.getUsePlace()).append(",");
        }
        if (StringUtils.isNotBlank(device.getUseCompany()) && (oldDevice.getUseCompany() == null || !oldDevice.getUseCompany().equals(device.getUseCompany()))) {
            sb.append("使用单位从")
                    .append(oldDevice.getUseCompany() == null || StringUtils.isBlank(oldDevice.getUseCompany()) ? "无" : oldDevice.getUseCompany()).append("变更为")
                    .append(device.getUseCompany() == null || StringUtils.isBlank(device.getUseCompany()) ? "无" : device.getUseCompany()).append(",");
        }
        if (StringUtils.isNotBlank(device.getUsers()) && (oldDevice.getUsers() == null || !oldDevice.getUsers().equals(device.getUsers()))) {
            sb.append("包机人从")
                    .append(StringUtils.isBlank(oldDevice.getUsers()) ? "无" : oldDevice.getUsers()).append("变更为")
                    .append(device.getUsers()).append(",");
        }
        if (StringUtils.isNotBlank(device.getDeviceInfo()) && (oldDevice.getDeviceInfo() == null || !oldDevice.getDeviceInfo().equals(device.getDeviceInfo()))) {
            sb.append("设备资料情况从")
                    .append(StringUtils.isBlank(oldDevice.getDeviceInfo()) ? "无" : oldDevice.getDeviceInfo()).append("变更为")
                    .append(device.getDeviceInfo()).append(",");
        }
        if (device.getIntoWellTime() != null && (oldDevice.getIntoWellTime() == null || oldDevice.getIntoWellTime().getTime() != device.getIntoWellTime().getTime())) {
            sb.append("入井时间从")
                    .append(oldDevice.getIntoWellTime() == null ? "无" : sdf.format(oldDevice.getIntoWellTime())).append("变更为")
                    .append(sdf.format(device.getIntoWellTime())).append(",");
        }
        if (StringUtils.isNotBlank(device.getAcceptorResult()) && (oldDevice.getAcceptorResult() == null || !oldDevice.getAcceptorResult().equals(device.getAcceptorResult()))) {
            sb.append("入井验收人及验收结果从")
                    .append(StringUtils.isBlank(oldDevice.getAcceptorResult()) ? "无" : oldDevice.getAcceptorResult()).append("变更为")
                    .append(device.getAcceptorResult()).append(",");
        }
        if (device.getInstallTime() != null && (oldDevice.getInstallTime() == null || oldDevice.getInstallTime().getTime() != device.getInstallTime().getTime())) {
            sb.append("安装时间从")
                    .append(oldDevice.getInstallTime() == null ? "无" : sdf.format(oldDevice.getInstallTime())).append("变更为")
                    .append(sdf.format(device.getInstallTime())).append(",");
        }
        if (StringUtils.isNotBlank(device.getInstallResult()) && (oldDevice.getInstallResult() == null || !oldDevice.getInstallResult().equals(device.getInstallResult()))) {
            sb.append("安装验收人及验收结果从")
                    .append(StringUtils.isBlank(oldDevice.getInstallResult()) ? "无" : oldDevice.getInstallResult()).append("变更为")
                    .append(device.getInstallResult()).append(",");
        }
        if (device.getBlastFindTime() != null && (oldDevice.getBlastFindTime() == null || oldDevice.getBlastFindTime().getTime() != device.getBlastFindTime().getTime())) {
            sb.append("防爆检查时间从")
                    .append(oldDevice.getBlastFindTime() == null ? "无" : sdf.format(oldDevice.getBlastFindTime())).append("变更为")
                    .append(sdf.format(device.getBlastFindTime())).append(",");
        }
        if (StringUtils.isNotBlank(device.getBlastFindPerson()) && (oldDevice.getBlastFindPerson() == null || !oldDevice.getBlastFindPerson().equals(device.getBlastFindPerson()))) {
            sb.append("防爆检查人从")
                    .append(StringUtils.isBlank(oldDevice.getBlastFindPerson()) ? "无" : oldDevice.getBlastFindPerson()).append("变更为")
                    .append(device.getBlastFindPerson()).append(",");
        }
        if (StringUtils.isNotBlank(device.getBlastFindInfo()) && (oldDevice.getBlastFindInfo() == null || !oldDevice.getBlastFindInfo().equals(device.getBlastFindInfo()))) {
            sb.append("防爆检查情况从")
                    .append(StringUtils.isBlank(oldDevice.getBlastFindInfo()) ? "无" : oldDevice.getBlastFindInfo()).append("变更为")
                    .append(device.getBlastFindInfo()).append(",");
        }
        if (StringUtils.isNotBlank(device.getBlastInfoSeePerson()) && (oldDevice.getBlastInfoSeePerson() == null || !oldDevice.getBlastInfoSeePerson().equals(device.getBlastInfoSeePerson()))) {
            sb.append("防爆检查问题整改情况及验收人从")
                    .append(StringUtils.isBlank(oldDevice.getBlastInfoSeePerson()) ? "无" : oldDevice.getBlastInfoSeePerson()).append("变更为")
                    .append(device.getBlastInfoSeePerson()).append(",");
        }
        if (device.getDeviceTestTime() != null && (oldDevice.getDeviceTestTime() == null || oldDevice.getDeviceTestTime().getTime() != device.getDeviceTestTime().getTime())) {
            sb.append("设备定期试验时间从")
                    .append(oldDevice.getDeviceTestTime() == null ? "无" : sdf.format(oldDevice.getDeviceTestTime())).append("变更为")
                    .append(sdf.format(device.getDeviceTestTime())).append(",");
        }
        if (StringUtils.isNotBlank(device.getTestPerson()) && (oldDevice.getTestPerson() == null || !oldDevice.getTestPerson().equals(device.getTestPerson()))) {
            sb.append("试验人及试验结果从")
                    .append(StringUtils.isBlank(oldDevice.getTestPerson()) ? "无" : oldDevice.getTestPerson()).append("变更为")
                    .append(device.getTestPerson()).append(",");
        }
        if (device.getDesiccantReplaceTime() != null && (oldDevice.getDesiccantReplaceTime() == null || oldDevice.getDesiccantReplaceTime().getTime() != device.getDesiccantReplaceTime().getTime())) {
            sb.append("干燥剂更换时间从")
                    .append(oldDevice.getDesiccantReplaceTime() == null ? "无" : sdf.format(oldDevice.getDesiccantReplaceTime())).append("变更为")
                    .append(sdf.format(device.getDesiccantReplaceTime())).append(",");
        }
        if (device.getCheckTime() != null && (oldDevice.getCheckTime() == null || oldDevice.getCheckTime().getTime() != device.getCheckTime().getTime())) {
            sb.append("设备检修时间从")
                    .append(oldDevice.getCheckTime() == null ? "无" : sdf.format(oldDevice.getCheckTime())).append("变更为")
                    .append(sdf.format(device.getCheckTime())).append(",");
        }
        if (StringUtils.isNotBlank(device.getCheckInfo()) && (oldDevice.getCheckInfo() == null || !oldDevice.getCheckInfo().equals(device.getCheckInfo()))) {
            sb.append("设备日常检修情况从")
                    .append(StringUtils.isBlank(oldDevice.getCheckInfo()) ? "无" : oldDevice.getCheckInfo()).append("变更为")
                    .append(device.getCheckInfo()).append(",");
        }
        if (StringUtils.isNotBlank(device.getDeviceRecord()) && (oldDevice.getDeviceRecord() == null || !oldDevice.getDeviceRecord().equals(device.getDeviceRecord()))) {
            sb.append("设备事故记录从")
                    .append(StringUtils.isBlank(oldDevice.getDeviceRecord()) ? "无" : oldDevice.getDeviceRecord()).append("变更为")
                    .append(device.getDeviceRecord()).append(",");
        }
        if (!oldDevice.getDeviceStatus().equals(device.getDeviceStatus())) {
            String statusStr = "";
            if (device.getDeviceStatus() == 0) {
                statusStr = "在用";
            } else if (device.getDeviceStatus() == 1) {
                statusStr = "闲置";
            } else if (device.getDeviceStatus() == 2) {
                statusStr = "备用";
            }
            sb.append("设备状态从").append(oldDevice.getDeviceStatusStr()).append("变更为").append(statusStr).append(",");
        }
        if (device.getDeviceUpTime() != null && (oldDevice.getDeviceUpTime() == null || oldDevice.getDeviceUpTime().getTime() != device.getDeviceUpTime().getTime())) {
            sb.append("设备升井时间从")
                    .append(oldDevice.getDeviceUpTime() == null ? "无" : sdf.format(oldDevice.getDeviceUpTime())).append("变更为")
                    .append(sdf.format(device.getDeviceUpTime())).append(",");
        }
        if (StringUtils.isNotBlank(device.getRemark()) && (oldDevice.getRemark() == null || !oldDevice.getRemark().equals(device.getRemark()))) {
            sb.append("备注从")
                    .append(StringUtils.isBlank(oldDevice.getRemark()) ? "无" : oldDevice.getRemark()).append("变更为")
                    .append(device.getRemark()).append(",");
        }
        if (device.getChangeTime() != null && (oldDevice.getChangeTime() == null || oldDevice.getChangeTime().getTime() != device.getChangeTime().getTime())) {
            sb.append("防爆合格证、完好牌、整定牌更换日期(半年)从")
                    .append(oldDevice.getChangeTime() == null ? "无" : sdf.format(oldDevice.getChangeTime())).append("变更为")
                    .append(sdf.format(device.getChangeTime())).append(",");
        }
        String description = sb.toString();
        int length = description.length();
        description = description.substring(0, length - 1);
        log.setDescription(description);
        log.setUserId(user.getUserId());
        log.setUsername(user.getUsername());
        log.setCreateTime(new Date());
        logMapper.insert(log);
        deviceMapper.updateById(device);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    private void deleteLog(Device device, User user) {
        Log log = new Log();
        log.setLogId(UUIDUtil.getUUID());
        log.setUserId(user.getUserId());
        log.setCategoryId(device.getCategoryId());
        log.setUsername(user.getUsername());
        log.setCreateTime(new Date());
        String categoryId = device.getCategoryId();
        Category category = categoryMapper.selectById(categoryId);
        String description = "删除了" + category.getCategoryName() + "类目的" + device.getDeviceName() + "设备";
        log.setDescription(description);
        logMapper.insert(log);
    }
}
